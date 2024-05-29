# Form Binding

I looked far and wide and was surprised that there was no annotation based way to bind Form URL Encoded string to a Java Object. Think of this project like JAX-B or JSON-B, but for Form URL Encoding.

Wouldn't it be nice to create this form:

```
key=AGreatSuccess&anInt=42
```

From this object:

```
public class TestObject {
	private String key = "AGreatSuccess";
	private int anInt = 42;
}
```

With on line of code:

```
final String form = writer.write(new TestObject());
```

Yes. Yes it would. Oh and there's a JAX-RS integration: MessageBodyReader and MessageBodyWriter that produces/consumes `application/x-www-form-urlencoded`!


## License
All files are Licensed Apache Source License 2.0. Please consider contributing back any changes you may make, thanks!

## Usage

There are two parts, and API jar and a implementation jar. Include the API jar in your project as a compile dependency, and the implementation jar as a runtime dependency. Annotate your fields (or don't, the default behavior uses the field name and looks at any fields not marked as transient). The Java SPI system will load the implementation for you. 

Internally this project makes heavy use of Apache Bean Utils `ConvertUtils` to convert between strings and value types. You can even extend this behavior by implementing `FormBindingConverter` and packaging it as a Java SPI Service.

### Maven Coordinates


API Jar:

```
<dependency>
	<groupId>com.github.exabrial</groupId>
	<artifactId>form-binding-api</artifactId>
	<version>SEE THE RELEASES TAB FOR VERSION NUMBER</version>
	<scope>compile</scope>
</dependency>
```
Runtime Jar:

```
<dependency>
	<groupId>com.github.exabrial</groupId>
	<artifactId>form-binding</artifactId>
	<version>SEE THE RELEASES TAB FOR VERSION NUMBER</version>
	<scope>runtime</scope>
</dependency>
```

### Binding your classes

* The default behavior is to bind all of your fields that are not marked `@FormBindingTransient`
* If _any_ field is marked as a `@FormBindingParam`, only fields that are marked with `@FormBindingParam` will be serialized
* You can change the name of the parameter bound like this: `@FormBindingParam(paramName = "differentName")`

Example:

```
public class MixedAnnotations {
	@FormBindingParam(paramName = "differentName")
	private String heresAField = "Here's a field!!!";
	@FormBindingTransient
	private String ignoreMe = "Don't encode this!!";
	private String heresANonAnnotatedField = "It shouldn't be included";
}
```

### Java Object -> Form URL Encoded String


```
FormBindingWriter writer = FormBinding.getWriter();
String form = writer.write(new MixedAnnotations());
```

Will produce:

```
differentName=Here%27s+a+field%21%21%21
```

### Form URL Encoded String -> Java Object

```
String input = "differentName=ConvertingBackToJavaObject";
MixedAnnotations mixedAnnotations = reader.read(input, MixedAnnotations.class);
```

On the returned object, `heresAField` the field will be set to: `ConvertingBackToJavaObject`


### Implementing your own converter

* Bean Utils does pretty good job with basic types, but if you have nested objects, you'll want to implement your own converter.
* Simply implement that `FormBindingConverter` interface, then put a file in `META-INF/services` named `com.github.exabrial.formbinding.FormBindingConverter`
* Inside this file, put the fully qualified classname of your custom implementation
* See https://github.com/exabrial/form-binding/tree/master/form-binding-test-module for an example


### JAX-RS


#### Maven Coordinates

```
<dependency>
	<groupId>com.github.exabrial</groupId>
	<artifactId>form-binding-jaxrs</artifactId>
	<version>SEE THE RELEASES TAB FOR VERSION NUMBER</version>
	<scope>compile</scope>
</dependency>

<dependency>
	<groupId>com.github.exabrial</groupId>
	<artifactId>form-binding</artifactId>
	<version>SEE THE RELEASES TAB FOR VERSION NUMBER</version>
	<scope>runtime</scope>
</dependency>
```

#### Server

* Accept forms easily without a ton of silly `@FormParam` annotations!

```
@ApplicationPath("/api/1.0")
public class RestApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { RestResource.class, FormBindingMessageBodyReader.class }));
	}
}
```

```
@ApplicationScoped
@Path("/testObject")
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
public class RestResource {
	@POST
	public void testObjectForm(TestObject testObject) {
		System.out.println(testObject);
	}
}
```

#### Client

* Post an http form to a URL without creating an intermediary MultiValuedMap or JAX-RS `javax.ws.rs.core.Form` class:

```
public void postTestObject(TestObject testObject) {
	Client client = ClientBuilder.newClient();
	client.register(FormBindingMessageBodyWriter.class);
	client.target("http://example.com").path("testObject").request().post(Entity.entity(testObject, MediaType.APPLICATION_FORM_URLENCODED));
	client.close();
}
```

* Read an `application/x-www-form-urlencoded` response back from a REST service!

```
public TestObject getTestObject() {
	Client client = ClientBuilder.newClient();
	client.register(FormBindingMessageBodyReader.class);
	TestObject testObject = client.target("http://example.com").path("testObject").request().accept(MediaType.APPLICATION_FORM_URLENCODED).get(TestObject.class);
	client.close();
	return testObject;
}
```

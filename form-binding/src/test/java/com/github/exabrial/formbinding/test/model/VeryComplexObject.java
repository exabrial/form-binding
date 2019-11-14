package com.github.exabrial.formbinding.test.model;

import java.math.BigDecimal;

public class VeryComplexObject {
	public int testInt;
	public Integer wrapperLong;
	public BigDecimal bigDecimal;
	public double testDouble;
	public String stringParam;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bigDecimal == null) ? 0 : bigDecimal.hashCode());
		result = prime * result + ((stringParam == null) ? 0 : stringParam.hashCode());
		long temp;
		temp = Double.doubleToLongBits(testDouble);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + testInt;
		result = prime * result + ((wrapperLong == null) ? 0 : wrapperLong.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VeryComplexObject other = (VeryComplexObject) obj;
		if (bigDecimal == null) {
			if (other.bigDecimal != null) {
				return false;
			}
		} else if (!bigDecimal.equals(other.bigDecimal)) {
			return false;
		}
		if (stringParam == null) {
			if (other.stringParam != null) {
				return false;
			}
		} else if (!stringParam.equals(other.stringParam)) {
			return false;
		}
		if (Double.doubleToLongBits(testDouble) != Double.doubleToLongBits(other.testDouble)) {
			return false;
		}
		if (testInt != other.testInt) {
			return false;
		}
		if (wrapperLong == null) {
			if (other.wrapperLong != null) {
				return false;
			}
		} else if (!wrapperLong.equals(other.wrapperLong)) {
			return false;
		}
		return true;
	}
}

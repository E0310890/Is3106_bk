package com.is3106.common;

import java.math.BigDecimal;

public class ValidatorHelper {

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	public static boolean isNullOrEmpty(String obj) {
		return obj == null || obj.isEmpty() || obj.toString().equalsIgnoreCase("");
	}
	
	public static boolean isNullOrEmpty(Integer obj) {
		return obj == null || obj != 0;
	}
	


	@SuppressWarnings("unchecked")
	public static <T> T validateObj(T obj, Class<?> classType) {
		if (obj != null)
			return obj;
		T emp = null;
		if(classType == Integer.class || classType == int.class)
			emp = (T) classType.cast(0);
		if(classType == String.class)
			emp = (T) classType.cast("");
		if(classType == BigDecimal.class)
			emp = (T) new BigDecimal(0);
		return emp;
	}
}

package com.is3106.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mapping.MappingException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.is3106.exception.JsonMappingException;

public class MapperHelper {
	
	public static <T> T jsonToObj(String json, Class<T>  objClass) throws JsonMappingException{
		ObjectMapper mapper = new ObjectMapper();
		try {
			//global jackson config
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			mapper.setSerializationInclusion(Include.NON_NULL);
			//disable unknown fields, can be done on class level
			//by adding -> @JsonIgnoreProperties(ignoreUnknown = true)
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			//
			T object = (T) mapper.readValue(json, objClass);
			return object;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage());
		}
	}
	
	public static String ObjToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage());
		}
	}
	
	public static <T, V> V mergeObjects(T first, V second) {
		if (second == null)
			return null;

		Class<?> clazz2 = second.getClass();

		PropertyDescriptor[] pd2 = BeanUtils.getPropertyDescriptors(clazz2);
		Set<String> getter2Set = Arrays.asList(pd2).stream().map(pd -> pd.getReadMethod().getName())
				.collect(Collectors.toSet());
		getter2Set.remove("getClass");
		try {
			for (PropertyDescriptor pd1 : BeanUtils.getPropertyDescriptors(first.getClass())) {
				Method getter1 = pd1.getReadMethod();
				String getter1Name = getter1.getName();
				Object value1;
				value1 = getter1.invoke(first);
				if (getter2Set.contains(getter1Name) && value1 != null) {
					Method getter2 = BeanUtils.findMethod(clazz2, getter1Name);
					Class<?> getter2ReturnType = getter2.getReturnType();
					Object value2 = getter2.invoke(second);

					String setter2Name = "s" + getter1Name.substring(1);
					Method setter2 = BeanUtils.findMethod(clazz2, setter2Name, getter2ReturnType);
					if (value2 == null || !value2.equals(value1))
						setter2.invoke(second, value1);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new MappingException("error mapping of object fields: @MapperHelper.mergeObjects(..)");
		}
		return second;
	}
	
	

}

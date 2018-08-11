/**
 * 
 */
package com.krista.spring.boot.utils;

/**
 * 枚举基接口
 * @author 王东鸿
 * @Copyright (c) 2016, frontpay.cn
 * @date 2016年12月7日 下午7:37:56 
 */
public interface BaseEnum<T> {
	T getCode();
	String getName();
	
	public static <C extends BaseEnum<?>> C parse(Class<C> enumType, Object code) {
		C[] enums = enumType.getEnumConstants();
		for (C t : enums) {
			if(t.getCode().equals(code))
				return t;
		}
		
		throw new IllegalArgumentException("没有枚举" + enumType.getCanonicalName() + "." + code);
	}
}
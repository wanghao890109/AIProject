package com.hao.common.utils

import java.lang.reflect.*
import kotlin.jvm.Throws


object GenericsUtils {

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * @param clazz The class to introspect
     * @return the first generic declaration, or `Object.class` if cannot be determined
     */
    fun getSuperClassGenericType(clazz: Class<*>): Class<*> {
        return getSuperClassGenericType(clazz, 0)
    }

    /**
     * 获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    @Throws(IndexOutOfBoundsException::class)
    fun getSuperClassGenericType(clazz: Class<*>, index: Int): Class<*> {
        val genType = clazz.genericSuperclass as? ParameterizedType ?: return Any::class.java
        val params =
            genType.actualTypeArguments
        if (index >= params.size || index < 0) {
            return Any::class.java
        }
        return if (params[index] !is Class<*>) {
            Any::class.java
        } else params[index] as Class<*>
    }

    fun getInterfaceGenericType(clazz: Class<*>): Class<*> {
        return getInterfaceGenericType(clazz, 0)
    }

    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    fun getInterfaceGenericType(clazz: Class<*>, index: Int): Class<*> {
        val types: kotlin.Array<Type> = clazz.genericInterfaces
        val parameterizedType = types[index] as? ParameterizedType?: return Any::class.java
        val params = parameterizedType.actualTypeArguments
        if (index >= params.size || index < 0) {
            return Any::class.java
        }
        return if (params[index] !is Class<*>) {
            Any::class.java
        } else params[index] as Class<*>
    }
}
package com.cz.library.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by cz on 16/3/7.
 */
public class ReflectUtils {

    private static final String TAG = "ReflectUtils";

    public static void inviteMethod(Object object, String methodName, Class<?>[] paramClass, Object... args) {
        if (null == object) return;
        Class<?> clazz = object.getClass();
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramClass);
            method.setAccessible(true);
            Object rtnValue = method.invoke(object, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象取值
     *
     * @param object
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Object getValue(Object object, Class<?> clazz, String fieldName) {
        Object value = null;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            value = field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 设置对象取值
     *
     * @param object
     * @param fieldName
     * @param value
     */
    public static void setValue(Object object, String fieldName, Object value) {
        if (null != object) {
            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

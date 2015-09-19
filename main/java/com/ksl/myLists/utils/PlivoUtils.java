package com.ksl.myLists.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class PlivoUtils {

    public static String getFields(Object obj) throws IllegalAccessException {
        StringBuffer buffer = new StringBuffer();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
          if (!Modifier.isStatic(f.getModifiers())) {
            f.setAccessible(true);
            Object value = f.get(obj);
            buffer.append(f.getName());
            buffer.append("=");
            buffer.append("" + value);
            buffer.append("\n");
          }
        }
        return buffer.toString();
    }
    
}

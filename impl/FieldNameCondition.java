package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 根据字段名进行条件判断的条件实现类。
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class FieldNameCondition<T, V> implements Condition<T> {
    @Getter
    @Setter
    private String fieldName;
    @Getter
    @Setter
    private Condition<V> innerCondition;
    private static final Map<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();

    public FieldNameCondition(String fieldName, Condition<V> innerCondition) {
        this.fieldName = fieldName;
        this.innerCondition = innerCondition;
    }

    @Override
    public boolean evaluate(T obj) {
        if (obj == null) {
            return false;
        }

        try {
            Field field = getField(obj.getClass(), fieldName);
            Object value = field.get(obj);

            // 编译时就保证了是 V 类型
            return innerCondition.evaluate((V) value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to evaluate field: " + fieldName, e);
        }
    }

    private Field getField(Class<?> clazz, String fieldName) {
        String key = clazz.getName() + "#" + fieldName;
        return FIELD_CACHE.computeIfAbsent(key, k -> {
            for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
                try {
                    Field f = current.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    return f;
                } catch (NoSuchFieldException ignored) {
                }
            }
            throw new IllegalArgumentException("Field not found: " + fieldName);
        });
    }
}

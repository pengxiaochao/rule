package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.function.Function;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 类字段支持
 *
 * @author p_x_c
 */
@JsonTypeName("field")
@NoArgsConstructor
public class FieldCondition<T, R> implements Condition<T> {
    @Getter
    @Setter
    private Function<T, R> getter;
    @Getter
    @Setter
    private Condition<R> condition;

    public FieldCondition(Function<T, R> getter, Condition<R> condition) {
        this.getter = getter;
        this.condition = condition;
    }

    @Override
    public boolean evaluate(T object) {
        R fieldValue = getter.apply(object);
        return condition.evaluate(fieldValue);
    }
}

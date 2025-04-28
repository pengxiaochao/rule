package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.NoArgsConstructor;

/**
 * 判断是否不为null的条件
 *
 * @author p_x_c
 */
@JsonTypeName("isnotnull")
@NoArgsConstructor
public class IsNotNullCondition<T> implements Condition<T> {
    @Override
    public boolean evaluate(T value) {
        return value != null;
    }
}

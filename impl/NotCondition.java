package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 否
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class NotCondition<T> implements Condition<T> {
    @Getter
    @Setter
    private Condition<T> condition;

    public NotCondition(Condition<T> condition) {
        this.condition = condition;
    }

    @Override
    public boolean evaluate(T value) {
        return !condition.evaluate(value);
    }
}

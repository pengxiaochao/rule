package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BETWEEN 条件
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class BetweenCondition<T extends Comparable<T>> implements Condition<T> {
    @Getter
    @Setter
    private T min;
    @Getter
    @Setter
    private T max;

    public BetweenCondition(T min, T max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean evaluate(T value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }
}

package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * > 条件
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class GreaterThanCondition<T extends Comparable<T>> implements Condition<T> {
    @Getter
    @Setter
    private T threshold;

    /**
     * 构造函数
     *
     * @param threshold 要对比的阈值
     */
    public GreaterThanCondition(T threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean evaluate(T value) {
        return value.compareTo(threshold) > 0;
    }
}

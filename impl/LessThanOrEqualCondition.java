package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 判断值是否小于等于指定阈值的条件
 *
 * @author p_x_c
 */
@JsonTypeName("lessthanorequal")
@NoArgsConstructor
public class LessThanOrEqualCondition<T extends Comparable<T>> implements Condition<T> {
    @Getter
    @Setter
    private T threshold;

    public LessThanOrEqualCondition(T threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean evaluate(T value) {
        return value.compareTo(threshold) <= 0;
    }
}

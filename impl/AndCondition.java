package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * AND 组合
 * @author p_x_c
 */
@NoArgsConstructor
public class AndCondition<T> implements Condition<T> {
    @Getter
    @Setter
    private List<Condition<T>> conditions;

    public AndCondition(List<Condition<T>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean evaluate(T value) {
        return conditions.stream().allMatch(c -> c.evaluate(value));
    }
}

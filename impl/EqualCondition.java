package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 相等 ==
 * @author p_x_c
 */
@NoArgsConstructor
public class EqualCondition<T> implements Condition<T> {
    @Getter
    @Setter
    private T target;

    public EqualCondition(T target) {
        this.target = target;
    }

    @Override
    public boolean evaluate(T value) {
        return Objects.equals(value, target);
    }
}


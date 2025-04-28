package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 判断是否不等于指定值的条件
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class NotEqualCondition<T> implements Condition<T> {
    @Getter
    @Setter
    private T target;

    public NotEqualCondition(T target) {
        this.target = target;
    }

    @Override
    public boolean evaluate(T value) {
        return !Objects.equals(value, target);
    }
}

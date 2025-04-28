package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 判断字符串是否不以指定前缀开头的条件
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class NotStartsWithCondition implements Condition<String> {
    @Getter
    @Setter
    private String prefix;

    public NotStartsWithCondition(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean evaluate(String value) {
        return value == null || !value.startsWith(prefix);
    }
}

package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 以某个字符串开头
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class StartsWithCondition implements Condition<String> {
    @Getter
    @Setter
    private String prefix;

    public StartsWithCondition(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean evaluate(String value) {
        return value != null && value.startsWith(prefix);
    }
}

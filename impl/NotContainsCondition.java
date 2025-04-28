package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 判断字符串是否不包含指定关键字的条件
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class NotContainsCondition implements Condition<String> {
    @Getter
    @Setter
    private String keyword;

    public NotContainsCondition(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean evaluate(String value) {
        return value == null || !value.contains(keyword);
    }
}

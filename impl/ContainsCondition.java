package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 包含 contains
 *
 * @author p_x_c
 */
@NoArgsConstructor
public class ContainsCondition implements Condition<String> {
    @Getter
    @Setter
    private String keyword;

    public ContainsCondition(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean evaluate(String value) {
        return value != null && value.contains(keyword);
    }
}

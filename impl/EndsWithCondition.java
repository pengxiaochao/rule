package com.au92.common.util.rule.impl;

import com.au92.common.util.rule.Condition;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 判断字符串是否以指定后缀结尾的条件
 *
 * @author p_x_c
 */
@JsonTypeName("endwith")
@NoArgsConstructor
public class EndsWithCondition implements Condition<String> {
    @Getter
    @Setter
    private String suffix;

    public EndsWithCondition(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean evaluate(String value) {
        return value != null && value.endsWith(suffix);
    }
}

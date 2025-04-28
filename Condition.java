package com.au92.common.util.rule;

import com.au92.common.util.rule.impl.AndCondition;
import com.au92.common.util.rule.impl.BetweenCondition;
import com.au92.common.util.rule.impl.ContainsCondition;
import com.au92.common.util.rule.impl.EndsWithCondition;
import com.au92.common.util.rule.impl.EqualCondition;
import com.au92.common.util.rule.impl.FieldCondition;
import com.au92.common.util.rule.impl.FieldNameCondition;
import com.au92.common.util.rule.impl.GreaterThanCondition;
import com.au92.common.util.rule.impl.GreaterThanOrEqualCondition;
import com.au92.common.util.rule.impl.IsNotNullCondition;
import com.au92.common.util.rule.impl.IsNullCondition;
import com.au92.common.util.rule.impl.LessThanCondition;
import com.au92.common.util.rule.impl.LessThanOrEqualCondition;
import com.au92.common.util.rule.impl.NotCondition;
import com.au92.common.util.rule.impl.NotContainsCondition;
import com.au92.common.util.rule.impl.NotEndsWithCondition;
import com.au92.common.util.rule.impl.NotEqualCondition;
import com.au92.common.util.rule.impl.NotStartsWithCondition;
import com.au92.common.util.rule.impl.OrCondition;
import com.au92.common.util.rule.impl.StartsWithCondition;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 规则引擎接口
 * <p>
 * 使用方法见{@link ConditionTest}
 *
 * @author p_x_c
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AndCondition.class, name = "and"),
        @JsonSubTypes.Type(value = BetweenCondition.class, name = "between"),
        @JsonSubTypes.Type(value = ContainsCondition.class, name = "contains"),
        @JsonSubTypes.Type(value = EndsWithCondition.class, name = "endwith"),
        @JsonSubTypes.Type(value = EqualCondition.class, name = "equal"),
        @JsonSubTypes.Type(value = FieldCondition.class, name = "field"),
        @JsonSubTypes.Type(value = FieldNameCondition.class, name = "fieldName"),
        @JsonSubTypes.Type(value = GreaterThanCondition.class, name = "greaterthanorequal"),
        @JsonSubTypes.Type(value = GreaterThanOrEqualCondition.class, name = "greaterThan"),
        @JsonSubTypes.Type(value = IsNotNullCondition.class, name = "isnotnull"),
        @JsonSubTypes.Type(value = IsNullCondition.class, name = "isnull"),
        @JsonSubTypes.Type(value = LessThanCondition.class, name = "lessthan"),
        @JsonSubTypes.Type(value = LessThanOrEqualCondition.class, name = "lessthanorequal"),
        @JsonSubTypes.Type(value = NotCondition.class, name = "not"),
        @JsonSubTypes.Type(value = NotContainsCondition.class, name = "notcontains"),
        @JsonSubTypes.Type(value = NotEndsWithCondition.class, name = "notendwith"),
        @JsonSubTypes.Type(value = NotEqualCondition.class, name = "notequal"),
        @JsonSubTypes.Type(value = NotStartsWithCondition.class, name = "notstartswith"),
        @JsonSubTypes.Type(value = OrCondition.class, name = "or"),
        @JsonSubTypes.Type(value = StartsWithCondition.class, name = "startswith"),
})
public interface Condition<T> {

    /**
     * 规则计算
     *
     * @param value
     * @return
     */
    boolean evaluate(T value);
}

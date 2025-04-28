package com.au92.common.util.rule;

import com.au92.common.util.json.JsonUtils;
import com.au92.common.util.rule.impl.AndCondition;
import com.au92.common.util.rule.impl.ContainsCondition;
import com.au92.common.util.rule.impl.EndsWithCondition;
import com.au92.common.util.rule.impl.EqualCondition;
import com.au92.common.util.rule.impl.FieldCondition;
import com.au92.common.util.rule.impl.GreaterThanCondition;
import com.au92.common.util.rule.impl.IsNotNullCondition;
import com.au92.common.util.rule.impl.NotCondition;
import com.au92.common.util.rule.impl.StartsWithCondition;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.val;

/**
 * 使用
 *
 * @author p_x_c
 */

public class ConditionTest {
    public static void main(String[] args) throws Exception {
        Condition<String> condition = new AndCondition<>(List.of(
                new StartsWithCondition("Hello"),
                new ContainsCondition("world"),
                new NotCondition<>(new ContainsCondition("bad"))
        ));

        boolean result = condition.evaluate("Hello world!");
        System.out.println("Result1: " + result); // true

        User user = new User("张三", 25, "active", 85.0);
        Condition<User> condition2 = new AndCondition<>(List.of(
                new FieldCondition<User, Integer>(User::getAge, new GreaterThanCondition<Integer>(18)),
                new FieldCondition<User, String>(User::getName, new ContainsCondition("张")),
                new FieldCondition<>(User::getStatus, new EqualCondition<String>("active")),
                new FieldCondition<>(User::getScore, new GreaterThanCondition<>(80.0))
        ));

        boolean result2 = condition2.evaluate(user);
        System.out.println("Result2: " + result2); // true

        // (age > 18) AND ((name contains 张 OR name contains 李)) AND (NOT(status == banned)) AND (name.endWith(三))
        Condition<User> condition3 = RuleBuilder.<User>start()
                                                .and()
                                                .field("age", new GreaterThanCondition<Integer>(18))
                                                .or()
                                                .field("name", new ContainsCondition("张"))
                                                .field("name", new ContainsCondition("李"))
                                                .not()
                                                .field("status", new EqualCondition<>("banned"))
                                                .and()
                                                .field("name", new EndsWithCondition("三"))
                                                .and()
                                                .field("name", new IsNotNullCondition<>())
                                                .build();

        val json = JsonUtils.toJSONString(condition3);
        System.out.println(json);

        Condition<User> condition4 = JsonUtils.toObject(json, Condition.class, User.class);

        boolean result3 = condition3.evaluate(user);
        System.out.println("Result3: " + result3); // true

        System.out.println("Result4: " + new StartsWithCondition("Hello").evaluate("Hello world!")); // true

    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
        private String status;
        private double score;
    }
}

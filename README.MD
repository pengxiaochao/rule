# Java Rule Engine

## 概述

这是一个轻量级、可嵌入的 Java 规则引擎库，旨在提供一种灵活且易于使用的方式来定义和评估业务规则。它允许将复杂的业务逻辑从核心应用程序代码中分离出来，使得规则更易于管理、修改和理解。

该引擎支持通过流畅的 Builder API 或通过 JSON 配置来定义规则，并提供了丰富的内置条件类型，同时也易于扩展以支持自定义条件。

## 特点

*   **流畅的 Builder API**: 使用 `RuleBuilder` 可以以声明式的方式轻松构建复杂的规则逻辑。
*   **逻辑组合**: 支持 `AND`, `OR`, `NOT` 逻辑操作符的嵌套组合。
*   **丰富的条件**: 内置多种常用条件，包括：
    *   比较 (`>`, `>=`, `<`, `<=`, `==`, `!=`, `BETWEEN`)
    *   字符串操作 (`startsWith`, `endsWith`, `contains`, 以及它们的否定形式)
    *   空值检查 (`isNull`, `isNotNull`)
    *   字段条件 (`field` - 基于字段名或 Getter 函数)
*   **类型安全**: 利用 Java 泛型确保类型安全。
*   **JSON 支持**: 规则可以序列化为 JSON 格式或从 JSON 反序列化，便于规则的存储、传输和动态加载。
*   **可扩展性**: 可以通过实现 `Condition` 接口轻松添加自定义条件。
*   **性能**: `FieldNameCondition` 使用缓存来提高反射访问字段的性能。

## 优势

*   **解耦**: 将易变的业务规则与稳定的应用程序代码分离。
*   **灵活性**: 无需修改和重新部署应用程序代码即可更改或添加规则（特别是使用 JSON 配置时）。
*   **可读性**: `RuleBuilder` API 提高了复杂规则的可读性和可维护性。
*   **动态性**: 支持从外部源（如数据库、配置文件）动态加载规则定义。
*   **可重用性**: 条件可以被复用在不同的规则组合中。

## 示例代码

```java

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

```

序列化之后的json内容

```json
{
    "type": "and",
    "conditions": [
        {
            "type": "and",
            "conditions": [
                {
                    "type": "fieldName",
                    "fieldName": "age",
                    "innerCondition": {
                        "type": "greaterthanorequal",
                        "threshold": 18
                    }
                }
            ]
        },
        {
            "type": "or",
            "conditions": [
                {
                    "type": "fieldName",
                    "fieldName": "name",
                    "innerCondition": {
                        "type": "contains",
                        "keyword": "张"
                    }
                },
                {
                    "type": "fieldName",
                    "fieldName": "name",
                    "innerCondition": {
                        "type": "contains",
                        "keyword": "李"
                    }
                }
            ]
        },
        {
            "type": "not",
            "condition": {
                "type": "fieldName",
                "fieldName": "status",
                "innerCondition": {
                    "type": "equal",
                    "target": "banned"
                }
            }
        },
        {
            "type": "and",
            "conditions": [
                {
                    "type": "fieldName",
                    "fieldName": "name",
                    "innerCondition": {
                        "type": "endwith",
                        "suffix": "三"
                    }
                }
            ]
        }
    ]
}
```

## 可用条件

引擎内置了以下条件实现（位于 `com.au92.common.util.rule.impl` 包下）：

*   `AndCondition`: 逻辑与
*   `OrCondition`: 逻辑或
*   `NotCondition`: 逻辑非
*   `EqualCondition`: 等于 (`==`)
*   `NotEqualCondition`: 不等于 (`!=`)
*   `GreaterThanCondition`: 大于 (`>`)
*   `GreaterThanOrEqualCondition`: 大于等于 (`>=`)
*   `LessThanCondition`: 小于 (`<`)
*   `LessThanOrEqualCondition`: 小于等于 (`<=`)
*   `BetweenCondition`: 介于两者之间 (包含边界)
*   `StartsWithCondition`: 字符串以指定前缀开头
*   `EndsWithCondition`: 字符串以指定后缀结尾
*   `ContainsCondition`: 字符串包含指定子串
*   `NotStartsWithCondition`: 字符串不以指定前缀开头
*   `NotEndsWithCondition`: 字符串不以指定后缀结尾
*   `NotContainsCondition`: 字符串不包含指定子串
*   `IsNullCondition`: 值为 `null`
*   `IsNotNullCondition`: 值不为 `null`
*   `FieldNameCondition`: 对指定名称的字段值应用内部条件
*   `FieldCondition` (已废弃): 对通过 Getter 函数获取的字段值应用内部条件 (不支持 JSON 序列化)

## 扩展

要创建自定义条件，只需实现 `com.au92.common.util.rule.Condition<T>` 接口，并实现 `evaluate(T value)` 方法。

如果希望自定义条件支持 JSON 序列化/反序列化，需要：

1.  在 `Condition` 接口的 `@JsonSubTypes` 注解中添加 `@JsonSubTypes.Type(value = YourCondition.class, name = "your_condition_name")`。
2.  确保自定义条件类有无参构造函数（或使用 Lombok 的 `@NoArgsConstructor`），并且需要序列化的字段有 Getter/Setter（或使用 Lombok 的 `@Getter`/`@Setter`）。


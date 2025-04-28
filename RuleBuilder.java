package com.au92.common.util.rule;

import com.au92.common.util.rule.impl.AndCondition;
import com.au92.common.util.rule.impl.FieldCondition;
import com.au92.common.util.rule.impl.FieldNameCondition;
import com.au92.common.util.rule.impl.NotCondition;
import com.au92.common.util.rule.impl.OrCondition;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

/**
 * 规则引擎构造器，用于动态构建复杂的规则逻辑。 支持 AND、OR、NOT 等逻辑操作符的嵌套组合。
 *
 * <p>使用示例：
 * <pre>
 * Condition<MyObject> condition = RuleBuilder.start()
 *     .field(MyObject::getField1, new LessThanCondition<>(10))
 *     .and()
 *         .field(MyObject::getField2, new StartsWithCondition("prefix"))
 *         .field(MyObject::getField3, new BetweenCondition<>(5, 15))
 *     .build();
 * </pre>
 *
 * @author p_x_c
 */
public class RuleBuilder<T> {
    /**
     * 用于存储条件的栈，每个栈帧表示一个逻辑块中的条件列表
     */
    private final Deque<List<Condition<T>>> stack = new ArrayDeque<>();
    /**
     * 用于存储逻辑操作符的栈，例如 AND、OR、NOT
     */
    private final Deque<String> logicStack = new ArrayDeque<>();

    /**
     * 构造方法，初始化条件栈。
     */
    public RuleBuilder() {
        stack.push(new ArrayList<>());
    }

    /**
     * 创建并返回一个新的规则构造器实例。
     *
     * @param <T> 规则适用的对象类型
     * @return 新的规则构造器实例
     */
    public static <T> RuleBuilder<T> start() {
        return new RuleBuilder<>();
    }

    /**
     * 开始一个 AND 逻辑块。
     *
     * @return 当前规则构造器实例
     */
    public RuleBuilder<T> and() {
        end();
        stack.push(new ArrayList<>());
        logicStack.push("AND");
        return this;
    }

    /**
     * 构建最终的规则条件。
     *
     * @return 构建的规则条件
     * @throws IllegalStateException 如果逻辑块未正确结束或栈状态不平衡
     */
    public Condition<T> build() {
        end();
        if (stack.size() != 1 || !logicStack.isEmpty()) {
            throw new IllegalStateException("Unbalanced rule builder. Check and()/or()/not()/end() calls.");
        }
        List<Condition<T>> finalConditions = stack.pop();
        return finalConditions.size() == 1 ? finalConditions.getFirst() : new AndCondition<>(finalConditions);
    }

    /**
     * 添加一个字段条件到当前逻辑块。
     *
     * @param <R>       字段的类型
     * @param getter    获取字段值的函数
     * @param condition 字段值需要满足的条件
     * @return 当前规则构造器实例
     * @deprecated 不支持反序列化, 使用{@link #field(String, Condition)}替代
     */
    @Deprecated
    @SuppressWarnings("all")
    public <R> RuleBuilder<T> field(Function<T, R> getter, Condition<R> condition) {
        stack.peek()
             .add(new FieldCondition<>(getter, condition));
        return this;
    }

    /**
     * 添加一个字段条件到当前逻辑块。
     *
     * @param <R>       字段的类型
     * @param name      字段名
     * @param condition 字段值需要满足的条件
     * @return 当前规则构造器实例
     */
    @SuppressWarnings("all")
    public <R> RuleBuilder<T> field(String name, Condition<R> condition) {
        stack.peek()
             .add(new FieldNameCondition<T, R>(name, condition));
        return this;
    }

    /**
     * 开始一个 NOT 逻辑块。
     *
     * @return 当前规则构造器实例
     */
    public RuleBuilder<T> not() {
        end();
        stack.push(new ArrayList<>());
        logicStack.push("NOT");
        return this;
    }

    /**
     * 开始一个 OR 逻辑块。
     *
     * @return 当前规则构造器实例
     */
    public RuleBuilder<T> or() {
        end();
        stack.push(new ArrayList<>());
        logicStack.push("OR");
        return this;
    }

    /**
     * 结束当前逻辑块，并将其组合为一个条件。
     *
     * @return 当前规则构造器实例
     * @throws IllegalStateException 如果 NOT 块中包含的条件数量不为 1，或遇到未完成的逻辑类型
     */
    @SuppressWarnings("all")
    private RuleBuilder<T> end() {
        if (stack.peek() == null || logicStack.peek() == null) {
            return this;
        }
        List<Condition<T>> current = stack.pop();
        String logic = logicStack.pop();
        Condition<T> combined = switch (logic) {
            case "AND" -> new AndCondition<>(current);
            case "OR" -> new OrCondition<>(current);
            case "NOT" -> {
                if (current.size() != 1) {
                    throw new IllegalStateException("NOT block must contain exactly one condition");
                }
                yield new NotCondition<>(current.get(0));
            }
            default -> throw new IllegalStateException("Unknown logic type: " + logic);
        };

        assert stack.peek() != null;
        stack.peek()
             .add(combined);
        return this;
    }

}

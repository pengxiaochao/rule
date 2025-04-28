package com.au92.common.util.json;

import com.au92.common.util.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.Maps;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * JSON工具类
 *
 * @author p_x_c
 */
@UtilityClass
public class JsonUtils {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 忽略在json字符串中存在，但是在java对象中不存在对应属性的情况
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略空Bean转json的错误
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 允许不带引号的字段名称
        MAPPER.configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature(), true);
        // 允许单引号
        MAPPER.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
        // allow int startWith 0
        MAPPER.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature(), true);
        // 允许字符串存在转义字符：\r \n \t
        MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        // 排除空值字段
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 使用驼峰式
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        // 使用bean名称
        JsonMapper.builder()
                .enable(MapperFeature.USE_STD_BEAN_NAMING);
        // 所有日期格式都统一为固定格式
        MAPPER.setDateFormat(new SimpleDateFormat(CommonConstant.DATETIME_FORMAT));
        MAPPER.setTimeZone(TimeZone.getTimeZone(CommonConstant.TIME_ZONE_GMT8));
    }

    /**
     * 对象转换为json字符串
     *
     * @param o 要转换的对象
     */
    public static String toJSONString(Object o) {
        return toJSONString(o, false);
    }

    /**
     * 对象转换为json字符串
     *
     * @param o      要转换的对象
     * @param format 是否格式化json
     */
    @SneakyThrows
    public static String toJSONString(Object o, boolean format) {
        if (o == null) {
            return "";
        }
        if (o instanceof Number) {
            return o.toString();
        }
        if (o instanceof String) {
            return (String) o;
        }
        if (format) {
            return MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(o);
        }
        return MAPPER.writeValueAsString(o);
    }

    /**
     * 字符串转换为指定对象
     *
     * @param json json字符串
     * @param cls  目标对象
     */
    @SneakyThrows
    public static <T> T toObject(String json, Class<T> cls) {
        if (StringUtils.isBlank(json) || cls == null) {
            return null;
        }
        return MAPPER.readValue(json, cls);
    }

    /**
     * 字符串转换为指定对象，并增加泛型转义 例如：List<Integer> test = toObject(jsonStr, List.class, Integer.class);
     *
     * @param json             json字符串
     * @param parametrized     目标对象
     * @param parameterClasses 泛型对象
     */
    @SneakyThrows
    public static <T> T toObject(String json, Class<?> parametrized, Class<?>... parameterClasses) {
        if (StringUtils.isBlank(json) || parametrized == null) {
            return null;
        }
        JavaType javaType = MAPPER.getTypeFactory()
                .constructParametricType(parametrized, parameterClasses);
        return MAPPER.readValue(json, javaType);
    }

    /**
     * 字符串转换为指定对象
     *
     * @param json          json字符串
     * @param typeReference 目标对象类型
     */
    @SneakyThrows
    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json) || typeReference == null) {
            return null;
        }
        return MAPPER.readValue(json, typeReference);
    }

    /**
     * 字符串转换为JsonNode对象
     *
     * @param json json字符串
     */
    @SneakyThrows
    public static JsonNode parse(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return MAPPER.readTree(json);
    }

    /**
     * 对象转换为map对象
     *
     * @param o 要转换的对象
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMap(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return toObject((String) o, Map.class);
        }
        return MAPPER.convertValue(o, Map.class);
    }

    /**
     * json字符串转换为list对象
     *
     * @param json json字符串
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return MAPPER.readValue(json, List.class);
    }

    /**
     * json字符串转换为list对象，并指定元素类型
     *
     * @param json json字符串
     * @param cls  list的元素类型
     */
    @SneakyThrows
    public static <T> List<T> toList(String json, Class<T> cls) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        JavaType javaType = MAPPER.getTypeFactory()
                .constructParametricType(List.class, cls);
        return MAPPER.readValue(json, javaType);
    }

    public static void main(String[] args) {
        Map<String, Integer> map = Maps.newHashMap();
        map.put("a", 1);
        System.out.println(JsonUtils.toJSONString(map));
        System.out.println(JsonUtils.<String, Integer>toMap(JsonUtils.toJSONString(map)));
    }
}

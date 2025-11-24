package com.aiteacher.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类
 *
 * @author AI Teacher Team
 */
public class Constants {

    /**
     * 省份映射
     */
    private static final Map<String, String> PROVINCE_MAP = new HashMap<>();

    /**
     * 年级映射
     */
    private static final Map<String, String> GRADE_MAP = new HashMap<>();

    static {
        // 省份
        PROVINCE_MAP.put("11", "北京市");
        PROVINCE_MAP.put("12", "天津市");
        PROVINCE_MAP.put("13", "河北省");
        PROVINCE_MAP.put("14", "山西省");
        PROVINCE_MAP.put("15", "内蒙古自治区");
        PROVINCE_MAP.put("21", "辽宁省");
        PROVINCE_MAP.put("22", "吉林省");
        PROVINCE_MAP.put("23", "黑龙江省");
        PROVINCE_MAP.put("31", "上海市");
        PROVINCE_MAP.put("32", "江苏省");
        PROVINCE_MAP.put("33", "浙江省");
        PROVINCE_MAP.put("34", "安徽省");
        PROVINCE_MAP.put("35", "福建省");
        PROVINCE_MAP.put("36", "江西省");
        PROVINCE_MAP.put("37", "山东省");
        PROVINCE_MAP.put("41", "河南省");
        PROVINCE_MAP.put("42", "湖北省");
        PROVINCE_MAP.put("43", "湖南省");
        PROVINCE_MAP.put("44", "广东省");
        PROVINCE_MAP.put("45", "广西壮族自治区");
        PROVINCE_MAP.put("46", "海南省");
        PROVINCE_MAP.put("50", "重庆市");
        PROVINCE_MAP.put("51", "四川省");
        PROVINCE_MAP.put("52", "贵州省");
        PROVINCE_MAP.put("53", "云南省");
        PROVINCE_MAP.put("54", "西藏自治区");
        PROVINCE_MAP.put("61", "陕西省");
        PROVINCE_MAP.put("62", "甘肃省");
        PROVINCE_MAP.put("63", "青海省");
        PROVINCE_MAP.put("64", "宁夏回族自治区");
        PROVINCE_MAP.put("65", "新疆维吾尔自治区");

        // 年级
        GRADE_MAP.put("primary_1", "小学一年级");
        GRADE_MAP.put("primary_2", "小学二年级");
        GRADE_MAP.put("primary_3", "小学三年级");
        GRADE_MAP.put("primary_4", "小学四年级");
        GRADE_MAP.put("primary_5", "小学五年级");
        GRADE_MAP.put("primary_6", "小学六年级");
        GRADE_MAP.put("middle_1", "初中一年级");
        GRADE_MAP.put("middle_2", "初中二年级");
        GRADE_MAP.put("middle_3", "初中三年级");
        GRADE_MAP.put("high_1", "高中一年级");
        GRADE_MAP.put("high_2", "高中二年级");
        GRADE_MAP.put("high_3", "高中三年级");
    }

    public static String getProvinceName(String code) {
        return PROVINCE_MAP.getOrDefault(code, code);
    }

    public static String getGradeName(String code) {
        return GRADE_MAP.getOrDefault(code, code);
    }
}


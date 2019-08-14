package com.foodie.product.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {


    public static boolean isNumeric(String str){
        return  Pattern.matches("/^[1-9]\\d*$", str);
    }
    public static boolean isPhone(String str){
        String REGEX_MOBILE_SIMPLE = "[1][35678]\\d{9}";
        //把正则表达式的规则编译成模板
        Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
        //把需要匹配的字符给模板匹配，获得匹配器
        Matcher matcher = pattern.matcher(str);
        // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
        return  matcher.find();
    }


}

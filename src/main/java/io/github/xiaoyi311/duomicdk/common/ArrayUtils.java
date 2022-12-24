package io.github.xiaoyi311.duomicdk.common;

import java.util.List;

/**
 * 数组工具
 */
public class ArrayUtils {
    /**
     * 列表转字符串
     *
     * @param spiltChar 分割字符
     * @param list      列表
     * @return          字符串
     */
    public static String ListToString(String spiltChar, List<?> list){
        StringBuilder result = new StringBuilder();
        for (Object com: list) {
            result.append(com.toString());
            if (list.indexOf(com.toString()) != list.size() -1) {
                result.append(spiltChar);
            }
        }
        return result.toString();
    }
}

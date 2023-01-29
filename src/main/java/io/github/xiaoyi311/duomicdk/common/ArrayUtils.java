package io.github.xiaoyi311.duomicdk.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组工具
 */
public class ArrayUtils {
    /**
     * 一页所拥有的元素
     */
    public static int pageObjCount = 10;

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

    /**
     * 获取页面总数目
     *
     * @param list 列表
     * @return 页面数目
     */
    public static <T> int GetPageCount(List<T> list){
        return (int)Math.ceil((double)list.size() / pageObjCount);
    }

    /**
     * 获取指定页面内容
     *
     * @param list 列表
     * @param page 第几页
     * @return 元素列表，页面不存在返回 NULL
     */
    public static <T> List<T> GetListPage(List<T> list, int page){
        if(page > GetPageCount(list)) return null;
        if(page <= 0) return null;

        List<T> result = new ArrayList<>();
        for (int i = (page - 1) * pageObjCount; i < list.size(); i++) {
            if (result.size() == pageObjCount) return result;
            result.add(list.get(i));
        }

        return result;
    }
}

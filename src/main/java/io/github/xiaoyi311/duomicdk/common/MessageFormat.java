package io.github.xiaoyi311.duomicdk.common;

import org.bukkit.ChatColor;

/**
 * 信息格式化
 */
public class MessageFormat {
    /**
     * 前缀
     */
    public static String prefix = "";

    /**
     * 信息获取
     *
     * @param msg 原信息
     * @return    转换后信息
     */
    public static String getMessage(String msg){
        return ChatColor.translateAlternateColorCodes(
                '&',
                prefix + msg
        );
    }

    /**
     * 信息获取
     *
     * @param msg 原信息
     * @return    转换后信息
     */
    public static String getMessageDefault(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}

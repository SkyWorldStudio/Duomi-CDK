package io.github.xiaoyi311.duomicdk.common;

/**
 * 简写时间转化器
 */
public class TimeTransfer {
    /**
     * 转换简写时间为时间
     *
     * @param timeString 简写时间
     * @return           时间(毫秒)
     */
    public static Long getTime(String timeString){
        try{
            if(timeString.endsWith("m")) return Long.parseLong(timeString.replace("m", "")) * 60000;
            if(timeString.endsWith("h")) return Long.parseLong(timeString.replace("h", "")) * 3600000;
            if(timeString.endsWith("d")) return Long.parseLong(timeString.replace("d", "")) * 86400000;
        }catch (Exception e){
            return null;
        }
        return null;
    }
}

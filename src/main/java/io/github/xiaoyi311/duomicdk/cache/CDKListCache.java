package io.github.xiaoyi311.duomicdk.cache;

import io.github.xiaoyi311.duomicdk.DuomiCDK;
import io.github.xiaoyi311.duomicdk.common.ArrayUtils;
import io.github.xiaoyi311.duomicdk.enity.CDK;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CDK 列表缓存
 */
public class CDKListCache {
    /**
     * CDK 列表
     */
    public static List<String> cdk = new ArrayList<>();

    /**
     * 缓存 CDK 列表
     */
    public static List<CDK> cdkCache = new ArrayList<>();

    /**
     * 更新缓存
     */
    public static void update(){
        cdk = new ArrayList<>();
        ResultSet rs = DuomiCDK.INSTANCE.sqlManager.doQueryCommand(
                DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                        "SELECT cdk FROM Cdk_list;"
                )
        );

        while (true){
            try {
                if (!rs.next()) break;
                cdk.add(rs.getString("cdk"));
            } catch (SQLException e) {
                DuomiCDK.INSTANCE.logger.severe("遍历数据库数据错误: " + e.getMessage());
            }
        }

        DuomiCDK.INSTANCE.logger.info("载入了 " + cdk.size() + " 个 cdk 数据");
    }

    /**
     * 所有数据推送到数据库
     */
    public static void push() {
        for (CDK cdk: cdkCache) {
            PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                    """
                    INSERT INTO Cdk_list (
                    cdk, fromTime, toTime, reward, canUse
                    ) VALUES (
                    ?, ?, ?, ?, ?
                    );
                    """
            );

            try {
                ps.setString(1, cdk.cdk);
                ps.setLong(2, new Date().getTime() / 1000L);
                if (cdk.time == null){
                    ps.setNull(3, Types.LONGVARCHAR);
                }else{
                    ps.setLong(3, (new Date().getTime() / 1000L) + cdk.time);
                }
                ps.setString(4, cdk.reward);
                ps.setInt(5, cdk.canUse);
                DuomiCDK.INSTANCE.sqlManager.doCommand(ps);
                CDKListCache.cdk.add(cdk.cdk);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        clear();
    }

    /**
     * 清除缓存
     */
    public static void clear() {
        cdkCache.clear();
    }

    /**
     * 导出缓存的 CDK
     *
     * @param filename 导出文件名
     */
    public static void makeFile(String filename) {
        try {
            //Gson gson = new Gson();
            StringBuilder cdkData = new StringBuilder();
            for (CDK cdk : cdkCache) {
                cdkData.append(cdk.cdk).append("\n");
            }
            Files.write(
                    Paths.get(DuomiCDK.INSTANCE.getDataFolder().getAbsolutePath(), filename),
                    //gson.toJson(cdkCache).getBytes()
                    cdkData.toString().getBytes()
            );
        } catch (IOException e) {
            DuomiCDK.INSTANCE.logger.warning("写入 CDK 失败, 请检查目录权限!");
        }
    }

    /**
     * 获取指定 CDK
     *
     * @param cdk CDK
     * @return    CDK
     */
    public static CDK get(String cdk) {
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "SELECT * FROM Cdk_list WHERE (cdk=?);"
        );

        try {
            ps.setString(1, cdk);
            ResultSet rs = DuomiCDK.INSTANCE.sqlManager.doQueryCommand(ps);
            while (rs.next()){
                return new CDK(
                        rs.getString("cdk"),
                        rs.getString("reward"),
                        rs.getLong("toTime"),
                        rs.getInt("canUse"),
                        rs.getString("player")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 使用 CDK
     *
     * @param cdk CDK
     */
    public static void use(CDK cdk) {
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "UPDATE Cdk_list SET canUse=? WHERE (cdk=?);"
        );
        PreparedStatement ps2 = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "UPDATE Cdk_list SET player=? WHERE (cdk=?);"
        );

        try {
            ps.setInt(1, cdk.canUse - 1);
            ps.setString(2, cdk.cdk);
            ps2.setString(1,  ArrayUtils.ListToString(",", cdk.player));
            ps2.setString(2, cdk.cdk);
            DuomiCDK.INSTANCE.sqlManager.doCommand(ps);
            DuomiCDK.INSTANCE.sqlManager.doCommand(ps2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个 CDK
     *
     * @param cdk    CDK
     */
    public static void delCDK(String cdk){
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "DELETE FROM Cdk_list WHERE cdk=?;"
        );

        try {
            ps.setString(1, cdk);
            DuomiCDK.INSTANCE.sqlManager.doCommand(ps);
            CDKListCache.cdk.remove(cdk);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

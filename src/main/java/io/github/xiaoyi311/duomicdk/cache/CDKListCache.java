package io.github.xiaoyi311.duomicdk.cache;

import io.github.xiaoyi311.duomicdk.DuomiCDK;
import io.github.xiaoyi311.duomicdk.common.ArrayUtils;
import io.github.xiaoyi311.duomicdk.enity.CDK;
import io.github.xiaoyi311.duomicdk.enity.Reward;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * CDK 列表缓存
 */
public class CDKListCache {
    /**
     * CDK 列表
     */
    public static Map<String, List<String>> cdk = new HashMap<>();

    /**
     * 缓存 CDK 列表
     */
    public static List<CDK> cdkCache = new ArrayList<>();

    /**
     * 更新缓存
     */
    public static void update(){
        cdk = new HashMap<>();

        try {
            for (String rewardName: RewardListCache.reward) {
                Reward reward = RewardListCache.get(rewardName);
                assert reward != null;

                PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                        "SELECT cdk FROM Cdk_list WHERE reward=?;"
                );
                ps.setString(1, reward.name);

                ResultSet rs = DuomiCDK.INSTANCE.sqlManager.doQueryCommand(ps);

                List<String> cdks = new ArrayList<>();
                while (true){
                    if (!rs.next()) break;
                    cdks.add(rs.getString("cdk"));
                }
                cdk.put(rewardName, cdks);

                RewardListCache.updateReward(reward);
            }

        } catch (SQLException e) {
            DuomiCDK.INSTANCE.logger.severe("遍历数据库数据错误: " + e.getMessage());
        }

        DuomiCDK.INSTANCE.logger.info("载入了 " + getCDKList().size() + " 个 cdk 数据");
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
                List<String> cdks = CDKListCache.cdk.get(cdk.reward);
                if (cdks == null) cdks = new ArrayList<>();
                cdks.add(cdk.cdk);
                CDKListCache.cdk.put(cdk.reward, cdks);
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
            StringBuilder cdkData = new StringBuilder();
            for (CDK cdk : cdkCache) {
                cdkData.append(cdk.cdk).append("\n");
            }
            Files.write(
                    Paths.get(DuomiCDK.INSTANCE.getDataFolder().getAbsolutePath(), filename),
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
                "SELECT * FROM Cdk_list WHERE cdk=?;"
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
                "UPDATE Cdk_list SET canUse=? WHERE cdk=?;"
        );
        PreparedStatement ps2 = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "UPDATE Cdk_list SET player=? WHERE cdk=?;"
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
     * @param cdk CDK
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

    /**
     * 删除所有为指定奖励的 CDK
     *
     * @param reward 指定奖励名
     */
    public static void delCDKFromReward(String reward){
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "DELETE FROM Cdk_list WHERE reward=?;"
        );

        try {
            ps.setString(1, reward);
            DuomiCDK.INSTANCE.sqlManager.doCommand(ps);

            cdk.remove(reward);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有已到期或用完的 CDK
     */
    public static void autoDelCDK(){
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "DELETE FROM Cdk_list WHERE canUse=0 OR toTime>=?;"
        );

        try {
            ps.setLong(1, new Date().getTime() / 1000L);
            DuomiCDK.INSTANCE.sqlManager.doCommand(ps);

            update();//由于再次处理太多余，所以直接重新读取
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 CDK 列表
     *
     * @return CDK 列表
     */
    public static List<String> getCDKList() {
        List<String> list = new ArrayList<>();
        for (List<String> cdkList : cdk.values()){
            list.addAll(cdkList);
        }
        return list;
    }
}

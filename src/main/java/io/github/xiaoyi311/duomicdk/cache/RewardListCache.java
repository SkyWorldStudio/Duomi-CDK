package io.github.xiaoyi311.duomicdk.cache;

import io.github.xiaoyi311.duomicdk.DuomiCDK;
import io.github.xiaoyi311.duomicdk.common.ArrayUtils;
import io.github.xiaoyi311.duomicdk.enity.Reward;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 奖励列表缓存
 */
public class RewardListCache {
    /**
     * 奖励列表
     */
    public static List<String> reward = new ArrayList<>();

    /**
     * 更新缓存
     */
    public static void update(){
        reward = new ArrayList<>();
        ResultSet rs = DuomiCDK.INSTANCE.sqlManager.doQueryCommand(
                DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                        "SELECT name FROM Cdk_rewards;"
                )
        );

        while (true){
            try {
                if (!rs.next()) break;
                reward.add(rs.getString("name"));
            } catch (SQLException e) {
                DuomiCDK.INSTANCE.logger.severe("遍历数据库数据错误: " + e.getMessage());
            }
        }

        DuomiCDK.INSTANCE.logger.info("载入了 " + reward.size() + " 个奖励数据");
    }

    /**
     * 获取指定奖励
     *
     * @param name 奖励名
     */
    public static Reward get(String name) {
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "SELECT * FROM Cdk_rewards WHERE name=?;"
        );

        try {
            ps.setString(1, name);
            ResultSet rs = DuomiCDK.INSTANCE.sqlManager.doQueryCommand(ps);
            while(rs.next()){
                return new Reward(
                        rs.getString("name"),
                        rs.getString("commands"),
                        rs.getString("tip")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新建一个奖励
     *
     * @param name 奖励名称
     */
    public static void addReward(String name, String tip) {
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                """
                INSERT INTO Cdk_rewards (
                name, tip
                ) VALUES (
                ?, ?
                );
                """
        );

        try {
            ps.setString(1, name);
            ps.setString(2, tip);
            DuomiCDK.INSTANCE.sqlManager.doCommand(ps);
            reward.add(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个奖励
     *
     * @param name 奖励名称
     */
    public static void delReward(String name) {
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "DELETE FROM Cdk_rewards WHERE name=?;"
        );

        try {
            ps.setString(1, name);
            DuomiCDK.INSTANCE.sqlManager.doCommand(ps);
            reward.remove(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新奖励数据
     *
     * @param reward  奖励
     */
    public static void updateReward(Reward reward) {
        PreparedStatement ps = DuomiCDK.INSTANCE.sqlManager.getSQLRun(
                "UPDATE Cdk_rewards SET commands=? WHERE name=?;"
        );

        try {
            ps.setString(1, ArrayUtils.ListToString(",", reward.commands));
            ps.setString(2, reward.name);
            DuomiCDK.INSTANCE.sqlManager.doCommand(ps);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

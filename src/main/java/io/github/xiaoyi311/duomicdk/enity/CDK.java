package io.github.xiaoyi311.duomicdk.enity;

import io.github.xiaoyi311.duomicdk.cache.RewardListCache;
import io.github.xiaoyi311.duomicdk.common.MessageFormat;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * CDK 实体
 */
public class CDK {
    /**
     * CDK
     */
    public String cdk;

    /**
     * 奖励
     */
    public String reward;

    /**
     * 到期时间
     */
    public Long time;

    /**
     * 剩余使用次数
     */
    public Integer canUse;

    /**
     * 使用过的玩家
     */
    public List<String> player;

    /**
     * 构建 CDK
     *
     * @param cdk    CDK
     * @param reward 奖励
     * @param time   有效时间
     * @param canUse 剩余使用次数
     */
    public CDK(String cdk, String reward, Long time, Integer canUse, String player) {
        this.cdk = cdk;
        this.reward = reward;
        this.time = time;
        this.canUse = canUse;
        this.player = player == null ? new ArrayList<>() : Arrays.stream(player.split(",")).toList();
    }

    /**
     * 使用 CDK
     *
     * @param sender 使用者
     */
    public void run(CommandSender sender) {
        if(time != 0 && new Date().getTime() / 1000L > time){
            sender.sendMessage(MessageFormat.getMessage("&c该 CDK 已过期!"));
            return;
        }

        Reward relReward = RewardListCache.get(reward);
        if(relReward == null){
            sender.sendMessage(MessageFormat.getMessage("&c无法找到对应的奖励!"));
            return;
        }

        player.add(sender.getName());
        relReward.run(sender);
    }
}

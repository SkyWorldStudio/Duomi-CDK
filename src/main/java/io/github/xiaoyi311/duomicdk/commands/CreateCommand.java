package io.github.xiaoyi311.duomicdk.commands;

import io.github.xiaoyi311.duomicdk.DuomiCDK;
import io.github.xiaoyi311.duomicdk.cache.CDKListCache;
import io.github.xiaoyi311.duomicdk.cache.RewardListCache;
import io.github.xiaoyi311.duomicdk.common.MessageFormat;
import io.github.xiaoyi311.duomicdk.common.TimeTransfer;
import io.github.xiaoyi311.duomicdk.enity.CDK;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

/**
 * 创建 CDK 子命令
 */
public class CreateCommand {
    /**
     * 使用子命令时
     *
     * @param sender 使用玩家
     * @param args   参数
     */
    public static void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("duomiCDK.cdkManage")){
            sender.sendMessage(MessageFormat.getMessage("&c你没有权限这样做!"));
            return;
        }

        if(args.length == 1){
            sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
            return;
        }

        switch (args[1]) {
            case "long" -> {
                if (args.length < 4) {
                    sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
                    break;
                }
                if (!RewardListCache.reward.contains(args[3])) {
                    sender.sendMessage(MessageFormat.getMessage("&c未找到该奖励! 请先新建"));
                    break;
                }
                if (args.length > 4 && TimeTransfer.getTime(args[4]) == null) {
                    sender.sendMessage(MessageFormat.getMessage("&c时间格式错误!"));
                    break;
                }
                sender.sendMessage(MessageFormat.getMessage("&a开始创建 CDK, 如果数量较多可能需要等待一段时间"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                                CDKListCache.cdkCache.add(new CDK(
                                        UUID.randomUUID().toString(),
                                        args[3],
                                        args.length > 4 && !Objects.equals(args[4], "inf") ? TimeTransfer.getTime(args[4]) : null,
                                        1,
                                        null
                                ));
                            }

                            CDKListCache.makeFile(args.length > 5 ? args[5] : "out.json");
                            CDKListCache.push();
                            sender.sendMessage(MessageFormat.getMessage("&a创建 CDK 成功, 数据已导出到文件, 默认为 out.json"));
                        } catch (Exception e) {
                            sender.sendMessage(MessageFormat.getMessage("&c创建 CDK 失败: ") + e.getMessage());
                        }
                    }
                }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            }
            case "medium" -> {
                sender.sendMessage(MessageFormat.getMessage("&c还没有开发啦qwq"));
            }
            case "short" -> {
                sender.sendMessage(MessageFormat.getMessage("&c还没有开发啦qwq"));
            }
            case "custom" -> {
                if (args.length < 4) {
                    sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
                    break;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            CDKListCache.cdkCache.add(new CDK(
                                    args[2],
                                    args[3],
                                    args.length > 4 && !Objects.equals(args[4], "inf") ? TimeTransfer.getTime(args[4]) : null,
                                    1,
                                    null
                            ));

                            CDKListCache.push();
                            CDKListCache.clear();
                            sender.sendMessage(MessageFormat.getMessage("&a创建 CDK 成功"));
                        } catch (Exception e) {
                            sender.sendMessage(MessageFormat.getMessage("&c创建 CDK 失败: ") + e.getMessage());
                        }
                    }
                }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            }
            default -> sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
        }
    }
}

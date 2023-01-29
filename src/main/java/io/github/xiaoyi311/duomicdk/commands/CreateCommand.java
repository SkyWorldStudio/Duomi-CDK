package io.github.xiaoyi311.duomicdk.commands;

import io.github.xiaoyi311.duomicdk.DuomiCDK;
import io.github.xiaoyi311.duomicdk.cache.CDKListCache;
import io.github.xiaoyi311.duomicdk.cache.RewardListCache;
import io.github.xiaoyi311.duomicdk.common.MessageFormat;
import io.github.xiaoyi311.duomicdk.common.TimeTransfer;
import io.github.xiaoyi311.duomicdk.common.UUIDMaker;
import io.github.xiaoyi311.duomicdk.enity.CDK;
import io.github.xiaoyi311.duomicdk.enity.Reward;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

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

        if (args.length < 4) {
            sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
            return;
        }

        if (!RewardListCache.reward.contains(args[3])) {
            sender.sendMessage(MessageFormat.getMessage("&c未找到该奖励! 请先新建"));
            return;
        }

        if (args.length > 4 && !Objects.equals(args[4], "inf") && TimeTransfer.getTime(args[4]) == null) {
            sender.sendMessage(MessageFormat.getMessage("&c时间格式错误!"));
            return;
        }

        if (args.length > 5 && !Pattern.compile("[0-9]*").matcher(args[5]).matches()) {
            sender.sendMessage(MessageFormat.getMessage("&c使用次数不是数字!"));
            return;
        }

        switch (args[1]) {
            case "long" -> {
                sender.sendMessage(MessageFormat.getMessage("&a开始创建 CDK, 如果数量较多可能需要等待一段时间"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                                String cdk = UUID.randomUUID().toString();
                                CDKListCache.cdkCache.add(new CDK(
                                        cdk,
                                        args[3],
                                        args.length > 4 && !Objects.equals(args[4], "inf") ? TimeTransfer.getTime(args[4]) : null,
                                        args.length > 5 ? Integer.parseInt(args[5]) : 1,
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
                sender.sendMessage(MessageFormat.getMessage("&a开始创建 CDK, 如果数量较多可能需要等待一段时间"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                                String cdk = UUIDMaker.Medium();
                                CDKListCache.cdkCache.add(new CDK(
                                        cdk,
                                        args[3],
                                        args.length > 4 && !Objects.equals(args[4], "inf") ? TimeTransfer.getTime(args[4]) : null,
                                        args.length > 5 ? Integer.parseInt(args[5]) : 1,
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
            case "short" -> {
                sender.sendMessage(MessageFormat.getMessage("&a开始创建 CDK, 如果数量较多可能需要等待一段时间"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                                String cdk = UUIDMaker.Short();
                                CDKListCache.cdkCache.add(new CDK(
                                        cdk,
                                        args[3],
                                        args.length > 4 && !Objects.equals(args[4], "inf") ? TimeTransfer.getTime(args[4]) : null,
                                        args.length > 5 ? Integer.parseInt(args[5]) : 1,
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
            case "custom" -> {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            CDKListCache.cdkCache.add(new CDK(
                                    args[2],
                                    args[3],
                                    args.length > 4 && !Objects.equals(args[4], "inf") ? TimeTransfer.getTime(args[4]) : null,
                                    args.length > 5 ? Integer.parseInt(args[5]) : 1,
                                    null
                            ));

                            CDKListCache.push();
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

    /**
     * 命令补全
     *
     * @param sender 发送者
     * @param args   参数
     */
    public static List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 2){
            result.add("long");
            result.add("custom");
        }
        if (args.length == 3) {
            if ("long".equals(args[1])) result.add("<请输入要生成 CDK 的数量>");
            if ("custom".equals(args[1])) result.add("<请输入自定义 CDK>");
        }
        if (args.length == 4) result.addAll(RewardListCache.reward);
        if (args.length == 5) result.add("[请输入到期时间, 1m/1h/1d, 不限时间为 inf]");
        if (args.length == 6 && !"custom".equals(args[1])) result.add("[请输入导出文件名]");
        return result;
    }
}

package io.github.xiaoyi311.duomicdk.commands;

import io.github.xiaoyi311.duomicdk.DuomiCDK;
import io.github.xiaoyi311.duomicdk.cache.CDKListCache;
import io.github.xiaoyi311.duomicdk.cache.RewardListCache;
import io.github.xiaoyi311.duomicdk.common.ArrayUtils;
import io.github.xiaoyi311.duomicdk.common.MessageFormat;
import io.github.xiaoyi311.duomicdk.enity.Reward;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * 奖励命令
 */
public class RewardCommand {
    /**
     * 使用子命令时
     *
     * @param sender 使用玩家
     * @param args   参数
     */
    public static void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("duomiCDK.rewardManage")){
            sender.sendMessage(MessageFormat.getMessage("&c你没有权限这样做!"));
            return;
        }

        if(args.length == 1){
            sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
            return;
        }

        switch (args[1]) {
            case "create" -> {
                if (args.length < 3) {
                    sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
                    return;
                }
                if (RewardListCache.reward.contains(args[2])) {
                    sender.sendMessage(MessageFormat.getMessage("&c该奖励已存在!"));
                    return;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        RewardListCache.addReward(args[2], "&6领取成功!");
                        sender.sendMessage(MessageFormat.getMessage("&a奖励新建成功!"));
                    }
                }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            }
            case "add" -> {
                if (args.length < 4) {
                    sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
                    return;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Reward reward = RewardListCache.get(args[2]);
                        if (reward == null) {
                            sender.sendMessage(MessageFormat.getMessage("&c未找到该奖励! 请先新建"));
                            return;
                        }
                        reward.addCommand(ArrayUtils.ListToString(" ", Arrays.stream(args).toList().subList(3, args.length)));

                        RewardListCache.updateReward(reward);
                        sender.sendMessage(MessageFormat.getMessage("&a命令新建成功!"));
                    }
                }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            }
            case "list" -> new BukkitRunnable() {
                @Override
                public void run() {
                    StringBuilder result;
                    if (args.length == 3) {
                        if (!RewardListCache.reward.contains(args[2])) {
                            sender.sendMessage(MessageFormat.getMessage("&c该奖励不存在! 请先新建"));
                            return;
                        }

                        Integer index = 0;
                        result = new StringBuilder("&b奖励 " + args[2] + " 命令列表：\n");
                        Reward relRew = RewardListCache.get(args[2]);
                        result.append("&b领取提示: ").append(Objects.requireNonNull(relRew).tip).append("\n");
                        result.append("&b命令信息: ").append("\n");
                        for (String command : relRew.commands) {
                            result.append("&a").append(index).append("> ").append(command).append("\n");
                            index++;
                        }
                    } else {
                        int page = 1;
                        if (args.length == 4 && Objects.equals(args[2], "?")){
                            try{
                                page = Integer.parseInt(args[3]);
                            }catch (Exception e){
                                sender.sendMessage(MessageFormat.getMessage("&c请输入正确的页面"));
                                return;
                            }
                        }

                        List<String> list = RewardListCache.reward;
                        Collections.sort(list);
                        int pageCount = ArrayUtils.GetPageCount(list);
                        result = new StringBuilder("&b=--= 奖励列表 (")
                                .append(page)
                                .append("/")
                                .append(pageCount)
                                .append(") =--=\n");

                        list = ArrayUtils.GetListPage(list, page);
                        if (list != null){
                            for (String rew : list) {
                                Reward relRew = RewardListCache.get(rew);
                                if (relRew == null) return;
                                result.append("&a> ")
                                        .append(relRew.name).append(": ")
                                        .append(relRew.commands.size())
                                        .append(" 条命令").append("\n");
                            }

                            result.append("&b=--= 奖励列表 (")
                                    .append(page)
                                    .append("/")
                                    .append(pageCount)
                                    .append(") =--=");
                        }else{
                            sender.sendMessage(MessageFormat.getMessage(pageCount == 0 ? "&c你好像还没有任何奖励" : "&c该页面不存在"));
                            return;
                        }

                        /*
                        result = new StringBuilder("&b当前奖励列表：\n");
                        for (String rew : RewardListCache.reward) {
                            Reward relRew = RewardListCache.get(rew);
                            if (relRew == null) return;
                            result.append("&a> ")
                                    .append(relRew.name).append(": ")
                                    .append(relRew.commands.size())
                                    .append(" 条命令").append("\n");
                        }
                        */
                    }
                    sender.sendMessage(MessageFormat.getMessageDefault(result.toString()));
                }
            }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            case "remove" -> {
                if (args.length < 3) {
                    sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
                    break;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Reward rew = RewardListCache.get(args[2]);
                        if (rew == null) {
                            sender.sendMessage(MessageFormat.getMessage("&c未找到该奖励! 请先新建"));
                            return;
                        }

                        if (args.length == 4) {
                            try {
                                rew.commands.remove(Integer.parseInt(args[3]));
                            } catch (Exception e) {
                                sender.sendMessage(MessageFormat.getMessage("&c请输入正确的命令序号, 可使用 reward list [name] 查看"));
                                return;
                            }
                            RewardListCache.updateReward(rew);
                            sender.sendMessage(MessageFormat.getMessage("&a删除奖励命令成功!"));
                        } else {
                            RewardListCache.delReward(rew.name);
                            sender.sendMessage(MessageFormat.getMessage("&a删除奖励成功!"));
                        }
                    }
                }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            }
            case "clear" -> {
                if (args.length < 3) {
                    sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
                    break;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Reward rew = RewardListCache.get(args[2]);
                        if (rew == null) {
                            sender.sendMessage(MessageFormat.getMessage("&c未找到该奖励! 请先新建"));
                            return;
                        }

                        CDKListCache.delCDKFromReward(rew.name);
                        sender.sendMessage(MessageFormat.getMessage("&a正在删除所有含该奖励的CDK, 如果数量多可能慢一些"));
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
            result.add("create");
            result.add("add");
            result.add("remove");
            result.add("list");
        }
        if(args.length == 3 || (args.length == 4 && "remove".equals(args[1]))) result.addAll(RewardListCache.reward);
        if(args.length == 4 && "add".equals(args[1])) result.add("<请输入要添加的命令,开头加[op]以OP权限运行>");
        return result;
    }
}

package io.github.xiaoyi311.duomicdk;

import io.github.xiaoyi311.duomicdk.cache.CDKListCache;
import io.github.xiaoyi311.duomicdk.commands.CreateCommand;
import io.github.xiaoyi311.duomicdk.commands.HelpCommand;
import io.github.xiaoyi311.duomicdk.commands.RewardCommand;
import io.github.xiaoyi311.duomicdk.common.ArrayUtils;
import io.github.xiaoyi311.duomicdk.common.MessageFormat;
import io.github.xiaoyi311.duomicdk.enity.CDK;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CommandListener implements TabExecutor {
    /**
     * 收到命令时
     *
     * @param sender  实用玩家
     * @param command 命令
     * @param label   使用的简称
     * @param args    命令参数
     * @return        是否正确
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
            return true;
        }

        switch (args[0]) {
            case "help" -> HelpCommand.onCommand(sender, args);
            case "create" -> CreateCommand.onCommand(sender, args);
            case "reward" -> RewardCommand.onCommand(sender, args);
            case "reload" -> {
                if (!sender.hasPermission("duomiCDK.reload")) {
                    sender.sendMessage(MessageFormat.getMessage("&c你没有权限这样做!"));
                    break;
                }
                DuomiCDK.INSTANCE.reloadConfig();
                DuomiCDK.INSTANCE.sqlManager.shutdown();
                DuomiCDK.INSTANCE.INIT_SQL();
                sender.sendMessage(MessageFormat.getMessage("&a配置与数据重载完成!"));
            }
            case "get" -> {
                if (!sender.hasPermission("duomiCDK.useCDK")) {
                    sender.sendMessage(MessageFormat.getMessage("&c你没有权限这样做!"));
                    break;
                }
                if (args.length < 2) {
                    sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
                    break;
                }

                //Bukkit API 不可异步
                CDK cdk = CDKListCache.get(args[1]);

                if (cdk == null) {
                    sender.sendMessage(MessageFormat.getMessage("&cCDK 错误! 请检查输入的 CDK"));
                    break;
                }

                if (cdk.canUse < 1) {
                    sender.sendMessage(MessageFormat.getMessage("&c该 CDK 已经不能再使用了!"));
                    break;
                }

                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageFormat.getMessage("&cCDK 只能由玩家获取!"));
                    break;
                }

                cdk.run(sender);
                CDKListCache.use(cdk);
            }
            case "remove" -> {
                if (!sender.hasPermission("duomiCDK.cdkManage")) {
                    sender.sendMessage(MessageFormat.getMessage("&c你没有权限这样做!"));
                    break;
                }
                if (args.length < 2) {
                    sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
                    break;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        CDK Cdk = CDKListCache.get(args[1]);
                        if (Cdk == null) {
                            sender.sendMessage(MessageFormat.getMessage("&c未找到该 CDK! 请先新建"));
                            return;
                        }

                        CDKListCache.delCDK(Cdk.cdk);
                        sender.sendMessage(MessageFormat.getMessage("&aCDK 删除成功!"));
                    }
                }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            }
            case "autoremove" -> {
                if (!sender.hasPermission("duomiCDK.cdkManage")) {
                    sender.sendMessage(MessageFormat.getMessage("&c你没有权限这样做!"));
                    break;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        sender.sendMessage(MessageFormat.getMessage("&a开始清理 CDK, 请不要在此期间执行其他 CDK 命令，清理完成后会提示"));
                        CDKListCache.autoDelCDK();
                        sender.sendMessage(MessageFormat.getMessage("&aCDK 清理成功!"));
                    }
                }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            }
            case "list" -> {
                if (!sender.hasPermission("duomiCDK.cdkManage")) {
                    sender.sendMessage(MessageFormat.getMessage("&c你没有权限这样做!"));
                    break;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        StringBuilder result;
                        if (args.length == 2) {
                            CDK CDk = CDKListCache.get(args[1]);
                            if (CDk == null) {
                                sender.sendMessage(MessageFormat.getMessage("&c该 CDK 不存在! 请先新建"));
                                return;
                            }

                            result = new StringBuilder("&bCDK " + args[1] + " 信息：&e\n");
                            result.append("&bCDK: ").append(CDk.cdk).append("\n");
                            result.append("&b到期时间: ").append(CDk.time == 0L ? "永久" : new Date(CDk.time * 1000L)).append("\n");
                            result.append("&b奖励名称: ").append(CDk.reward).append("\n");
                            result.append("&b剩余次数: ").append(CDk.canUse).append("\n");
                            result.append("&b领取玩家: ").append(ArrayUtils.ListToString(", ", CDk.player));
                        } else {
                            int page = 1;
                            if (args.length == 3 && Objects.equals(args[1], "?")){
                                try{
                                    page = Integer.parseInt(args[2]);
                                }catch (Exception e){
                                    sender.sendMessage(MessageFormat.getMessage("&c请输入正确的页面"));
                                    return;
                                }
                            }

                            List<String> list = CDKListCache.getCDKList();
                            Collections.sort(list);
                            int pageCount = ArrayUtils.GetPageCount(list);
                            result = new StringBuilder("&b=--= CDK 列表 (")
                                    .append(page)
                                    .append("/")
                                    .append(pageCount)
                                    .append(") =--=\n");

                            list = ArrayUtils.GetListPage(list, page);
                            if (list != null){
                                for (String CDK : list){
                                    CDK relCDK = CDKListCache.get(CDK);
                                    if (relCDK == null) break;
                                    result.append("&a> ")
                                            .append(relCDK.cdk).append(": ")
                                            .append(relCDK.reward).append(" 奖励").append("\n");
                                }

                                result.append("&b=--= CDK 列表 (")
                                        .append(page)
                                        .append("/")
                                        .append(pageCount)
                                        .append(") =--=");
                            }else{
                                sender.sendMessage(MessageFormat.getMessage(pageCount == 0 ? "&c你好像还没有任何 CDK" : "&c该页面不存在"));
                                return;
                            }
                        }
                        sender.sendMessage(MessageFormat.getMessageDefault(result.toString()));
                    }
                }.runTaskAsynchronously(DuomiCDK.INSTANCE);
            }
            case "info" -> DuomiCDK.INSTANCE.INFO(sender);
            default -> sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
        }

        return true;
    }

    /**
     * 补全命令
     *
     * @param sender  发送者
     * @param command 命令
     * @param alias   命令简称
     * @param args    参数
     * @return        可能性
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (!(sender instanceof Player)) {
            return null;
        }

        if(args.length == 1){
            if(sender.hasPermission("duomicdk.cdkManage")){
                result.add("create");
                result.add("remove");
                result.add("list");
                result.add("info");
            }
            if(sender.hasPermission("duomicdk.rewardManage")){
                result.add("reward");
            }
            if(sender.hasPermission("duomicdk.useCDK")){
                result.add("get");
            }
            if(sender.hasPermission("duomicdk.reload")){
                result.add("reload");
            }
            result.add("help");
        }else{
            switch (args[0]){
                case "help" -> {
                    result.add("create");
                    result.add("reward");
                }
                case "get" -> result.add("<请输入您的 CDK>");
                case "list" -> result.add("[请输入指定 CDK]");
                case "remove" -> result.add("<请输入指定 CDK>");
                case "create" -> result.addAll(CreateCommand.onTabComplete(sender, args));
                case "reward" -> result.addAll(RewardCommand.onTabComplete(sender, args));
            }
        }
        return result;
    }
}

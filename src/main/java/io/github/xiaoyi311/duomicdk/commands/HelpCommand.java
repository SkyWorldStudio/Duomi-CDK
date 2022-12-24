package io.github.xiaoyi311.duomicdk.commands;

import io.github.xiaoyi311.duomicdk.common.MessageFormat;
import org.bukkit.command.CommandSender;

/**
 * 帮助命令
 */
public class HelpCommand {
    /**
     * 使用子命令时
     *
     * @param sender 使用玩家
     * @param args   参数
     */
    public static void onCommand(CommandSender sender, String[] args) {
        if (args.length == 1){
            sender.sendMessage(MessageFormat.getMessageDefault("""
                &b=======-------    &3Duomi-CDK 帮助列表    &b-------=======
                &6/cdk help <create/reward>  -->  查看(指定命令)帮助列表
                &6/cdk create <long/medium/short/custom>  -->  创建一个 CDK
                &6/cdk reward <create/add/remove/list>  -->  创建一个奖励
                &6/cdk remove <name>  -->  删除一个 CDK
                &6/cdk get <cdk>  -->  领取一个 CDK
                &6/cdk list [cdk]  -->  查看(指定) CDK (的信息)列表
                &6/cdk info  -->  查看插件信息
                &6/cdk reload  -->  重载配置与数据
                &b=======-------    &3Duomi-CDK 帮助列表    &b-------=======
                """));
            return;
        }

        switch (args[1]) {
            case "create" -> sender.sendMessage(MessageFormat.getMessageDefault("""
                    &b=======-------    &3Duomi-CDK 帮助列表    &b-------=======
                    &6create long <count> <reward> [1m/1h/1d/inf] [filename]  -->  创建长 CDK
                    &6create medium <count> <reward> [1m/1h/1d/inf] [filename]  -->  创建中 CDK
                    &6create short <count> <reward> [1m/1h/1d/inf] [filename]  -->  创建短 CDK
                    &6create custom <cdk> <reward> [1m/1h/1d/inf]  -->  创建自定义 CDK
                    &b=======-------    &3Duomi-CDK 帮助列表    &b-------=======
                    """));
            case "reward" -> sender.sendMessage(MessageFormat.getMessageDefault("""
                    &b=======-------    &3Duomi-CDK 帮助列表    &b-------=======
                    &6reward create <name>  -->  创建一个奖励
                    &6reward add <name> <command>  -->  为奖励添加一条命令
                    &6reward remove <name> [index]  -->  删除指定奖励(的一条命令)
                    &6reward list [name]  -->  查看(指定)奖励(的命令)列表
                    &b=======-------    &3Duomi-CDK 帮助列表    &b-------=======
                    """));
            default -> sender.sendMessage(MessageFormat.getMessage("&c命令参数错误! 请使用 /cdk help 查看命令帮助"));
        }
    }
}

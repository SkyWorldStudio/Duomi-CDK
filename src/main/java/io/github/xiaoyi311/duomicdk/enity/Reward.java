package io.github.xiaoyi311.duomicdk.enity;

import io.github.xiaoyi311.duomicdk.common.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 奖励实体
 */
public class Reward {
    /**
     * 奖励名称
     */
    public String name;

    /**
     * 命令
     */
    public List<String> commands;

    /**
     * 提示信息
     */
    public String tip;

    /**
     * 构建一个奖励
     *
     * @param commands 命令
     * @param name     奖励名
     */
    public Reward(String name, String commands, String tip){
        this.name = name;
        this.commands = commands == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(commands.split(",")));
        this.tip = tip;
    }

    /**
     * 添加一个归属于该奖励的命令
     *
     * @param command 命令
     */
    public void addCommand(String command){
        if(!Objects.equals(commands.get(0), "")){
            commands.add(command);
            return;
        }
        commands.set(0, command);
    }

    /**
     * 运行奖励
     *
     * @param sender 使用者
     */
    public void run(CommandSender sender) {
        for (String command: commands) {
            String relCommand = command.replace("{player}", sender.getName());
            boolean runAsOp = false;

            Player player = Bukkit.getPlayer(sender.getName());
            if(player == null) return; //你tm刚执行完指令人没了???

            if(command.startsWith("[op]") && !player.isOp()){
                runAsOp = true;
            }
            relCommand = relCommand.replace("[op]", "");

            if(runAsOp) player.setOp(true);
            player.performCommand(relCommand);
            if(runAsOp) player.setOp(false);

            player.sendMessage(MessageFormat.getMessageDefault(tip));
        }
    }
}

package io.github.xiaoyi311.duomicdk;

import io.github.xiaoyi311.duomicdk.cache.CDKListCache;
import io.github.xiaoyi311.duomicdk.cache.RewardListCache;
import io.github.xiaoyi311.duomicdk.common.ArrayUtils;
import io.github.xiaoyi311.duomicdk.common.MessageFormat;
import io.github.xiaoyi311.duomicdk.common.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Duomi CDK 主类
 */
public final class DuomiCDK extends JavaPlugin {
    /**
     * 实例
     */
    public static DuomiCDK INSTANCE;

    /**
     * 日志
     */
    public Logger logger;

    /**
     * 配置文件
     */
    public Configuration config;

    /**
     * MySQL 数据库连接
     */
    public SQLManager sqlManager;

    /**
     * 插件启动
     */
    @Override
    public void onEnable() {
        INIT();
        logger.info("========= DuomiCDK 正在启动 =========");
        logger.info("作者 -> Xiaoyi311");
        logger.info("制作团队 -> SkyWorldStudio");
        logger.info("版本 -> v1.1.0");
        logger.info("数据模式 -> " + config.getString("dataBase.type", "SQLITE"));

        //注册命令
        PluginCommand pc = Bukkit.getPluginCommand("duomicdk");
        if (pc != null) {
            pc.setExecutor(new CommandListener());
        }else{
            logger.warning("注册 DuomiCDK 命令失败! 将无法使用相关命令!");
        }

        INIT_SQL();
        logger.info("========= DuomiCDK 启动成功 =========");
    }

    /**
     * 连接数据库
     */
    public void INIT_SQL() {
        if (config.getString("dataBase.type", "SQLITE").equals("SQLITE")){
            logger.info("连接 SQLite 数据库");
            File databaseFile = new File(getDataFolder().getAbsolutePath(), config.getString("dataBase.filename", "database.db"));
            if (!databaseFile.exists()) {
                try {
                    databaseFile.createNewFile();
                } catch (IOException e) {
                    logger.info("无法新建 SQLite 数据库: " + e.getMessage());
                }
            }
            sqlManager = new SQLManager(
                    databaseFile.getAbsolutePath()
            );
        }else{
            logger.info("连接 Mysql 数据库");
            sqlManager = new SQLManager(
                    config.getString("dataBase.hostname", "127.0.0.1"),
                    config.getString("dataBase.database", "Duomi"),
                    config.getString("dataBase.username", "root"),
                    config.getString("dataBase.password", "root"),
                    config.getInt("dataBase.port", 3306)
            );
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                sqlManager.connect();

                sqlManager.doCommand(sqlManager.getSQLRun(
                        """
                        CREATE TABLE IF NOT EXISTS Cdk_list (
                            cdk TEXT NOT NULL,
                            fromTime INTEGER NOT NULL,
                            toTime INTEGER,
                            reward TEXT NOT NULL,
                            canUse INTEGER NOT NULL,
                            player TEXT
                        );
                        """
                ));

                sqlManager.doCommand(sqlManager.getSQLRun(
                        """
                        CREATE TABLE IF NOT EXISTS Cdk_rewards (
                            name TEXT NOT NULL,
                            tip TEXT NOT NULL,
                            commands TEXT
                        );
                        """
                ));

                RewardListCache.update();
                CDKListCache.update();
            }
        }.runTaskAsynchronously(DuomiCDK.INSTANCE);
    }

    /**
     * 初始化
     */
    private void INIT(){
        saveDefaultConfig();
        INSTANCE = this;
        logger = getLogger();
        config = getConfig();

        MessageFormat.prefix = "&e[&3Duomi-CDK&e] ";
        ArrayUtils.pageObjCount = config.getInt("config.onePageShow", 10);
    }

    /**
     * 插件停止
     */
    @Override
    public void onDisable() {
        logger.info("感谢使用 DuomiSDK 插件，DuomiSDK 正在关闭");
        sqlManager.shutdown();
    }

    /**
     * 查看插件信息
     */
    public void INFO(CommandSender sender){
        sender.sendMessage(MessageFormat.getMessageDefault(
                """
                      
                      &e _____     &b__   __   \s
                      &e/\\  __-.  &b/\\ "-./  \\  \s
                      &e\\ \\ \\/\\ \\&b \\ \\ \\-./\\ \\ \s
                      &e \\ \\____-&b  \\ \\_\\ \\ \\_\\\s
                      &e  \\/____/ &b  \\/_/  \\/_/\s
                      
                      &eDuomi 系列插件 &bDuomi-CDK
                      """
        ));
        sender.sendMessage(MessageFormat.getMessageDefault("&e插件版本:&b 1.1.0"));
        sender.sendMessage(MessageFormat.getMessageDefault("&e主编:&b Xiaoyi311"));
        sender.sendMessage(MessageFormat.getMessageDefault("&e代码仓库:&b https://github.com/Xiaoyi311/Duomi-CDK"));
        sender.sendMessage(MessageFormat.getMessageDefault("&e帮助文档:&b https://duomicdk.mcgame.tk"));
    }
}

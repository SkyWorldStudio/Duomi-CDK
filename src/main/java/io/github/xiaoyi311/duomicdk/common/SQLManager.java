package io.github.xiaoyi311.duomicdk.common;

import io.github.xiaoyi311.duomicdk.DuomiCDK;

import java.sql.*;
import java.util.Objects;

/**
 * MYSQL 管理类
 */
public class SQLManager {
    /**
     * 数据库类型
     */
    private String type;

    /**
     * 服务器地址
     */
    private String hostname;

    /**
     * 数据库名
     */
    private String database;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 端口
     */
    private int port;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 数据库连接
     */
    public Connection connection;

    /**
     * 初始化 MySql 连接
     *
     * @param hostname 主机名
     * @param database 数据库名
     * @param userName 用户名
     * @param password 密码
     * @param port     端口
     */
    public SQLManager(String hostname, String database, String userName, String password, int port){
        this.type = "mysql";
        this.hostname = hostname;
        this.database = database;
        this.userName = userName;
        this.password = password;
        this.port = port;
    }

    /**
     * 初始化 SQLite 连接
     *
     * @param filename 数据库文件
     */
    public SQLManager(String filename){
        this.filename = filename;
    }

    /**
     * 连接数据库
     */
    public void connect(){
        try {
            connection = Objects.equals(this.type, "mysql") ?
                    DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true", userName, password) :
                    DriverManager.getConnection("jdbc:sqlite:" + filename);
        } catch (SQLException e) {
            DuomiCDK.INSTANCE.logger.severe("连接数据库失败：" + e.getMessage());
        }
    }

    /**
     * 关闭数据库连接
     */
    public void shutdown(){
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            DuomiCDK.INSTANCE.logger.severe("断开数据库连接失败：" + e.getMessage());
        }
    }

    /**
     * 执行命令
     *
     * @param mysql  MySQL 命令
     * @param sqlite SQLite 命令
     */
    public void doCommand(PreparedStatement mysql, PreparedStatement sqlite)
    {
        try {
            if (Objects.equals(this.type, "mysql")) {
                mysql.executeUpdate();
            }else{
                sqlite.executeUpdate();
            }
        } catch (SQLException e) {
            DuomiCDK.INSTANCE.logger.severe("执行数据库操作失败：" + e.getMessage());
        }
    }

    /**
     * 执行命令
     *
     * @param command 命令
     */
    public void doCommand(PreparedStatement command)
    {
        try {
            command.executeUpdate();
        } catch (SQLException e) {
            DuomiCDK.INSTANCE.logger.severe("执行数据库操作失败：" + e.getMessage());
        }
    }

    /**
     * 执行查询命令
     *
     * @param command 命令
     */
    public ResultSet doQueryCommand(PreparedStatement command)
    {
        try {
            return command.executeQuery();
        } catch (SQLException e) {
            DuomiCDK.INSTANCE.logger.severe("执行数据库操作失败：" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取 SQL 运行器
     *
     * @return SQL 运行器
     */
    public PreparedStatement getSQLRun(String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            DuomiCDK.INSTANCE.logger.severe("预处理数据库操作失败：" + e.getMessage());
        }
        return null;
    }
}

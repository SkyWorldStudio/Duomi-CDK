---
description: 插件的基本配置，使用插件的开始
---

# 配置文件

当成功启动 Duomi-CDK 以后，Duomi-CDK 会自动生成 **config.yml** 配置文件结构如下

## database

数据库相关配置

### type

使用的数据库类型，可以为 `MYSQL SQLITE` 默认为 SQLITE

### filename

SQLITE 模式时，所使用数据库的文件名

### hostname

MYSQL 模式时，连接的地址

### port

MYSQL 模式时，连接的端口

### username

MYSQL 模式时，使用的用户名

### password

MYSQL 模式时，数据库用户的密码

### database

MYSQL 模式时，所使用的数据库名

## config

插件的主要配置

### onePageShow

在使用列表指令时，每页所显示的数据数量，默认为**每页 10 个**

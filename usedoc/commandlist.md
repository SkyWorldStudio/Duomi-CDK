---
description: 所有命令及其功能的列表
coverY: 0
---

# 命令列表

Duomi-CDK 的主指令为 `/duomicdk`，当然为了方便，你也可以使用简称 `/cdk`，效果都是一样的。

{% hint style="info" %}
为了方便用户更加快捷制作 CDK，Duomi-CDK 将一个 CDK 拆分为 CDK 与 Reward，CDK 就是主体，而 Reward 便是使用 CDK 时奖励的内容。
{% endhint %}

## 主命令

### cdk help \[create/reward]

查看指令 (或者子指令) 帮助列表

### cdk create \<long/medium/short/custom> ......

创建一个 CDK (下方有详细介绍)

### cdk reward \<create/add/remove/list> ......

创建一个奖励 (下方有详细介绍)

### cdk remove \<cdk>

权限：`duomiCDK.cdkManage`

删除一个 CDK

### cdk autoremove

权限：`duomiCDK.cdkManage`

删除已过期或用完的 CDK

{% hint style="warning" %}
由于 SQL 执行指令不会等待，所以为了避免不必要的性能损耗，在执行 autoremove 时会重载插件，在出现清理完成的提示以前，不要使用 Duomi-CDK 的指令！
{% endhint %}

### cdk get \<cdk>

权限：`duomiCDK.useCDK`

领取一个 CDK

### cdk list \[cdk]

权限：`duomiCDK.cdkManage`

查看(指定) CDK (的信息)列表

{% hint style="info" %}
如果想看第二页可以使用 /cdk list ? 2，再往后以此类推
{% endhint %}

### cdk info

查看插件信息

### cdk reload

权限：duomiCDK.reload

重载配置与数据

## cdk create 子命令

权限: `duomiCDK.cdkManage`

创建 CDK 命令

{% hint style="info" %}
为了方便用户轻松创建不同长度的 CDK，我们内置了 4 种模式

long: 36 位

medium: 32位

short: 6位

custom: 自定义
{% endhint %}

{% hint style="info" %}
限时时间中的 inf 为不限制
{% endhint %}

### create \<long/medium/short> \<count> \<reward> \[1m/1h/1d/inf] \[use] \[filename]

创建一个 CDK，从左到右参数分别为：**模式、生成个数、奖励、限时时间、可用次数、导出文件名 (带后缀)**

{% hint style="info" %}
如果不填写 filename 默认将会导出到 out.json
{% endhint %}

### create custom \<cdk> \<reward> \[1m/1h/1d/inf] \[use]

创建一个自定义 CDK，从左到右参数分别为：CDK**、奖励、限时时间、可用次数**

## cdk reward 子命令

创建奖励命令

### reward create \<name>

创建一个奖励&#x20;

### reward add \<name> \<commnad>

为奖励添加一条命令

{% hint style="info" %}
填写指令时，无需添加 "/"，默认以非 OP 身份运行，如需以 OP 身份运行，可以在开头添加 `[op],`例如:&#x20;

`/reward add <name> /me yeeeee` 这句话便是以非 OP 执行

`/reward add <name> [op]/title @a title yeeeee` 这句话便是以 OP 执行
{% endhint %}

{% hint style="info" %}
如果想要获取玩家名可以使用 `{player}` 变量，例如 `[op]/title {player} title yeeeee`
{% endhint %}

### reward remove \<name> \[index]

删除指定奖励(的一条命令)

{% hint style="info" %}
如果想要删除某一条命令，可以使用 `/reward list` 查看命令的序号，填进 \[index]
{% endhint %}

### reward list \[name]

查看(指定)奖励(的命令)列表

### reward list \[? page]

查看奖励列表的指定页面

{% hint style="info" %}
如果想看第二页可以使用 /reward list ? 2，再往后以此类推
{% endhint %}

### reward clear \<name>

清空所有与指定奖励绑定的所有 CDK

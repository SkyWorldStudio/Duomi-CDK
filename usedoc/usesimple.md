---
description: 还不会使用吗，那来看看案例吧
coverY: 0
---

# 使用案例

使用 CDK 一般分为三个步骤:

1. 创建奖励
2. 创建 CDK
3. 领取 CDK

那接下来就以这三步为基础，开始案例

## 创建奖励

使用 `/cdk reward create test` 来新建一个名为 test 的奖励

然后再使用 `/cdk reward add test [op]/give {player} minecraft:apple 1` 给予使用该 CDK 玩家一个苹果

再通知一下这个玩家 `/cdk reward add test [op]/tell {player} 送你两个苹果`

这样，我们的 **test** 奖励就有了两条命令了！

## 创建 CDK

如果想要五个随机的 CDK，那就 `/cdk create long 5 test`

或者限制这五个 CDK 只生效 5 分钟 `/cdk create long 5 test 5m`

又或者限制这五个 CDK 只能用 3 次 `/cdk create long 5 test inf 3`

运行完以后，打开插件的目录，就能看到 **out.json** (默认) 里有我们刚制作的 CDK 啦

## 领取 CDK

让玩家使用 `/cdk get <CDK>` 就可以领取啦！

如果想要查看 CDK 的使用情况可以使用 `/cdk list <CDK>`

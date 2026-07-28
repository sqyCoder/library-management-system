library-management-system
Java控制台图书管理系统

项目介绍
纯Java控制台实现，不依赖数据库。
运用单例模式、工厂方法模式、代理模式进行开发；
通过txt文件实现数据持久化，程序重启后数据不会丢失。
系统分为管理员、普通用户两种角色，代理类实现权限控制。

功能说明
管理员：图书上架、修改、下架，查询图书，统计借阅信息，清理超期图书等。
普通用户：查询图书、借阅图书、归还图书、查看个人借阅记录等。

目录结构
src
├── book
│ ├── Book.java // 图书实体类
│ ├── Library.java // 全局图书仓库（单例）
│ └── PairOfUidAndBookId.java // 借阅记录实体（用户 ID + 图书 ID）
├── constant
│ └── Constant.java // 全局常量配置
├── user
│ ├── factory
│ │ ├── AdminUserFactory.java // 管理员工厂
│ │ ├── INormalUserFactory.java// 用户工厂接口
│ │ └── NormalUserFactory.java// 普通用户工厂
│ ├── User.java // 用户抽象父类
│ ├── AdminUser.java // 管理员类
│ ├── NormalUser.java // 普通用户类
│ └── ProxyUser.java // 用户代理类，实现权限校验
├── utils
│ ├── AnalyzingBook.java // 图书持久化工具
│ ├── AnalyzingBorrowedBook.java// 借阅记录持久化工具
│ ├── FileUtils.java // 文件基础工具
│ └── PermissionException.java // 权限自定义异常
└── LibrarySystem.java // 程序启动入口

运行方法
1. IDEA导入项目
2. 运行 LibrarySystem.java
3. 在控制台选择角色进行操作

持久化说明
allbooks.txt 存储全部图书信息
borrowedbook.txt 存储用户借阅记录

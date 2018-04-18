# yuyang-fast-web
彧飏SpringBoot基础框架

基于SpringBoot和MyBatis开发的一款极轻极快Java基础服务框架

环境：
1.maven构建工具
2.mysql数据库
3.ide推荐使用IntelliJ IDEA

特点：
1.上手简单，开袋即食：
	只需在pom文件引入本项目提交的dependence即可开始使用本框架。
2.代码极简，快速实现：
	只需创建Entity类，Dao和Service通过泛型继承对应的Base类即可快速开发简单的CRUD业务。
3.动态反射，敏捷高效：
	底层依赖反射技术实现动态SQL，不需要编写xml文件的同时对代码没有入侵，
	无需使用generator工具重复生成代码，对于需求的变更提供敏捷的支持。
4.实用封装，随意扩展：
	基于rest风格实现请求返回体的基础封装，统一异常捕获机制返回有用的异常信息，
	使用起来灵活，可以根据需求集成其他插件或依赖。
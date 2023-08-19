# admin system backend

系统中已完成基础功能：用户、角色、菜单、操作日志、权限过滤、cache、session超时

> spring boot 3.1.0、jdk 17、jwt、mybatis;  
> 工程拉取下来后，需要build(mybatis-flex)生成辅助代码；  
> DTO: 页面传过来的数据; VO: 传给页面的数据,简单直接使用PO;   
> controller: 操作记录的参数添加注解RequestParam或RequestBody,用于日志参数记录;  
> ext/easy-captcha-1.6.2.jar:javax换成jakarta,支持jdk17;  
> 
## mybatis-flex

* APT生成的辅助代码在`target/generated-sources/annotations`;

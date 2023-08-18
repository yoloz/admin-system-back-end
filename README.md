# trust management system

> spring boot 3.1.0、jdk 17、jwt、mybatis;  
> 工程拉取下来后，需要build(mybatis-flex)生成辅助代码；  
> DTO: 页面传过来的数据; VO: 传给页面的数据,简单直接使用PO;   
> service: 命名IxxService;  
> controller: 操作记录的参数添加注解RequestParam或RequestBody,用于日志参数记录;  


## idea中可以使用RestfulTool测试接口

1. 安装RestfulTool插件
   点击`file/settings/Plugins`搜索`restfulltool`，安装插件即可

2. 测试接口
   调用接口`/user/getUser`获取用户列表，点击发送，提示账号未登录，所以我们要先登录系统才能调用此接口。

```json
{
  "code": 203,
  "message": "账号未登录"
}
```

3. 首先，登录系统并获取token
   调用`/user/login`接口，body如下：

```json
{
  "username": "test",
  "password": "test"
}
```

点击发送，得到数据结果，拿出token：

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjEiLCJ1c2VybmFtZSI6InRlc3QiLCJpYXQiOjE2ODY4ODMzMzgsImV4cCI6MTY4Njk2OTczOH0._YpIxW64shEqtDyH7AEz5ezTs5ql9ShgW2KAY4t_huY",
    "loginTime": 1686883338657,
    "expireTime": 86400000,
    "ipAddr": "127.0.0.1",
    "loginLocation": null,
    "user": {
      "id": 1,
      "nickname": "test",
      "username": "test",
      "password": null,
      "createTime": null
    }
  }
}
```

取出token，在之后的每个接口请求头headers中放入token，则能正常请求接口，否则jwt拦截接口。

4. 再次获取用户列表
   调用`/user/getUser`接口，并指定请求头headers：

```json
{
  "Content-Type": "application/x-www-form-urlencoded",
  "Authorization": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjEiLCJ1c2VybmFtZSI6InRlc3QiLCJpYXQiOjE2ODY4ODMzMzgsImV4cCI6MTY4Njk2OTczOH0._YpIxW64shEqtDyH7AEz5ezTs5ql9ShgW2KAY4t_huY"
}
```

点击发送，得到当前登录用户基本信息：

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "id": 1,
    "nickname": "test",
    "username": "test",
    "password": "test",
    "createTime": "2023-06-15T09:44:07.000+08:00"
  }
}
```

## mybatis-flex

* APT生成的辅助代码在`target/generated-sources/annotations`;
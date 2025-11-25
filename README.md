# 项目介绍

本项目是一个基于 Spring Boot 的影视管理后台系统，提供影视资源管理、分类管理、菜单权限管理、用户认证等功能，集成了 OSS 文件存储、视频点播 (VOD)、JWT 身份认证等服务，适合作为影视类网站的后端管理解决方案。

# 技术栈

后端框架：Spring Boot
ORM 框架：MyBatis-Plus
认证授权：JWT + Spring Security
API 文档：Knife4j(Swagger)
文件存储：阿里云 OSS
视频服务：阿里云 VOD
数据库：MySQL
缓存：Redis
构建工具：Maven

# 环境要求

JDK 11+
MySQL 8.0+
Maven 3.6+
Redis 6.0+（可选，用于权限缓存）
阿里云 OSS 账号（用于文件上传）
阿里云 VOD 账号（用于视频播放）

# 快速开始

## 1. 克隆代码

```bash
git clone https://github.com/zka-2004/YingguanCloud.git
cd yingguan-cloud
```

## 2. 配置数据库

```bash
新建MySQL数据库（编码格式：utf8mb4）
修改配置文件 zka-auth-parent/zka-service-system/src/main/resources/application-dev.yaml：
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/你的数据库名?serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=utf-8
    username: 你的数据库用户名
    password: 你的数据库密码
```

## 3. 配置阿里云服务

### 配置 OSS（文件存储）：

```bash
al:
  oss:
    endpoint: 你的OSS地域节点
    accessKeyId: 你的AccessKeyId
    accessKeySecret: 你的AccessKeySecret
    bucketName: 你的Bucket名称
    url: 你的OSS访问域名
    
```

### 配置 VOD（视频点播）：

```BASH
yaml
al:
  vod:
    regionId: 你的VOD地域
    accessKeyId: 你的AccessKeyId
    accessKeySecret: 你的AccessKeySecret    
```

## 4. 构建项目

```bash
mvn clean install -Dmaven.test.skip=true
```

## 5. 启动服务

运行 zka-auth-parent/zka-service-system 模块的主启动类，默认端口为 8080。

# 功能说明

## 1. 核心模块

用户认证：基于 JWT 的令牌认证，登录成功后生成 token，权限信息缓存至 Redis
影视管理：影视信息的增删改查、播放凭证获取
分类管理：影视分类的增删改查
菜单管理：系统菜单及权限分配管理
文件上传：基于阿里云 OSS 的图片上传功能

## 2. 主要接口示例

### 影视管理

分页查询影视：GET /admin/movie/page/{page}/{limit}
添加影视：POST /admin/movie/addMovie
获取播放凭证：GET /admin/movie/getPlayAuthById/{id}

### 分类管理

分页查询分类：GET /admin/category/page/{page}/{limit}
添加分类：POST /admin/category/add

### 文件上传

上传图片：POST /uploadImage

# 接口文档

项目集成了 Knife4j 作为 API 文档工具，启动服务后可访问：
plaintext
http://localhost:8080/doc.html
（需携带 token 访问，token 可通过登录接口获取）

# 注意事项

1.数据库表结构需自行创建（可根据实体类SysMovie、SysCategory等手动创建或使用 MyBatis-Plus 代码生成器）
2.阿里云 OSS 和 VOD 服务需要提前开通并获取 AccessKey
3.开发环境配置在application-dev.yaml，生产环境可新增application-prod.yaml配置
4.JWT 密钥在JwtHelper类中，生产环境建议修改为更复杂的密钥

# 测试

项目包含部分测试类：
TestJwt：测试 JWT 令牌生成与解析
AppServiceTest：测试分页查询功能
运行测试命令：
```bash
mvn test

#每日头条
# 1.nacos的安装和配置

## 1.1 拉取镜像
```r
docker pull nacos/nacos-server:1.2.0
```
## 1.2 启动容器
```r
docker run --env MODE=standalone --name nacos --restart=always -d -p 8848:8848 nacos/nacos-server:1.2.0
```
MODE=standalone: 设置为单机版，当前在开发阶段，设置为单机版即可

## 1.3 查看容器日志
```r
docker logs -f 容器id
```

## 1.4 访问地址
```r
http://localhost:8848/nacos/index.html
```

## 1.5 nacos中的配置文件信息
```r
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/leadnews_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    username: root
    password: root
# 设置Mapper接口所对应的XML文件位置，如果你在Mapper接口中有自定义方法，需要进行该配置
mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  # 设置别名包扫描路径，通过该属性可以给包中的类注册别名
  type-aliases-package: com.heima.model.user.pojos
```

# 2.gateway
## 2.1 gateway配置信息
```R
spring:
  cloud:
    gateway:
      globalcors:
        add-to-simple-url-handler-mapping: true
        corsConfigurations:
          '[/**]':
            allowedHeaders: "*"
            allowedOrigins: "*"
            allowedMethods:
              - GET
              - POST
              - DELETE
              - PUT
              - OPTION
      routes:
        # 平台管理
        - id: user
          uri: lb://leadnews-user
          predicates:
            - Path=/user/**
          filters:
            - StripPrefix= 1
```

# 3.前端集成

## 3.1前端项目部署思路


通过nginx来进行配置，功能如下

- 通过nginx的反向代理功能访问后台的网关资源
- 通过nginx的静态服务器功能访问前端静态页面

## 3.2配置nginx

①：解压资料文件夹中的压缩包nginx-1.18.0.zip

②：解压资料文件夹中的前端项目app-web.zip

③：配置nginx.conf文件

在nginx安装的conf目录下新建一个文件夹`leadnews.conf`,在当前文件夹中新建`heima-leadnews-app.conf`文件

heima-leadnews-app.conf配置如下：

```javascript
upstream  heima-app-gateway{
    server localhost:51601;
}

server {
	listen 8801;
	location / {
		root D:/workspace/app-web/;
		index index.html;
	}
	
	location ~/app/(.*) {
		proxy_pass http://heima-app-gateway/$1;
		proxy_set_header HOST $host;  # 不改变源请求头的值
		proxy_pass_request_body on;  #开启获取请求体
		proxy_pass_request_headers on;  #开启获取请求头
		proxy_set_header X-Real-IP $remote_addr;   # 记录真实发出请求的客户端IP
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;  #记录代理信息
	}
}
```

nginx.conf   把里面注释的内容和静态资源配置相关删除，引入heima-leadnews-app.conf文件加载

```javascript

#user  nobody;
worker_processes  1;

events {
    worker_connections  1024;
}
http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
	# 引入自定义配置文件
	include leadnews.conf/*.conf;
}
```

④ ：启动nginx

​    在nginx安装包中使用命令提示符打开，输入命令nginx启动项目

​    可查看进程，检查nginx是否启动

​	重新加载配置文件：`nginx -s reload`

⑤：打开前端项目进行测试  -- >  http://localhost:8801

​     用谷歌浏览器打开，调试移动端模式进行访问

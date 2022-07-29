## 项目介绍
本项目是个用于可快速开发的原型项目，项目中对一些开发中常用的配置和工具做了集成

## 项目基本结构
```textmate
chant:.
├─chant-common
│   ├─java
│   │   └─com_zcx_chant_crud
│   │       ├─config                                配置包
│   │       │   ├─CommonMvcConfig.java              跨域，参数转换配置等
│   │       │   ├─ControllerLogAspect.java          Controller接口出入参统一日志记录
│   │       │   ├─UnifiedExceptionHandler.java      统一异常处理
│   │       │   └─UnifiedResponseHandler.java       统一响应处理
│   │       ├─enums                                 枚举包
│   │       ├─exception                             
│   │       ├─pojo
│   │       └─utils                                 工具包
│   ├─resources
│   │   └─META-INF
│   │       └─spring.factories
│   ├─test
│   └─pom.xml
├─chant-crud
│   ├─java
│   │   ├─com_zcx_chant_crud
│   │   │   ├─annotation                            注解包，存放自定义注解
│   │   │   ├─aspect                                切面包，配置自定义切面
│   │   │   ├─config
│   │   │   ├─constant                              常量包
│   │   │   ├─cron                                  定时任务包
│   │   │   ├─exception                             自定义异常包
│   │   │   ├─listener                              监听器包
│   │   │   ├─module                                模块功能包
│   │   │   │   ├─account                           模块的某一功能(账号相关)包
│   │   │   │   │   ├─request                       
│   │   │   │   │   │   └─AccountRequest.java
│   │   │   │   │   ├─AccountController.java
│   │   │   │   │   ├─AccountEntity.java
│   │   │   │   │   ├─AccountMapper.xml
│   │   │   │   │   ├─AccountRepository.java
│   │   │   │   │   └─AccountService.java
│   │   │   │   └─message
│   │   │   └─validator                             校验处理包
│   │   └─SampleApplication.java
│   ├─resources
│   │   ├─logback.xml                               日志配置文件
│   │   └─application.yml                           SpringBoot配置文件
│   ├─test
│   └─pom.xml
├─logs                                              日志目录
│   ├─chant.log
│   └─error.log
├─.gitignore
└─pom.xml
```




**可以看到，目录结构中没有 src/main ，java(源包)下的包也没有用 . 来分割，而是用 _ 来分割
，至于为什么这样做呢？(因为我不喜欢太深的目录层级，如果你想严格按照 java 的规范呢就自己重命名吧)**



## 规范
1. 模块项目命名：项目名-模块名
2. java(源包)下的包(在此指的是源包的下一级包)命名：机构性质_公司名_项目名_模块名


## 功能新增示例
### 添加新的自定义异常
1. 在 crud 模块的 exception 包下新建异常类并继承 common 模块下的 BaseException
2. 在
# 为什么不推荐使用 kill -9 命令来终止进程
<!-- TOC -->
* [为什么不推荐使用 kill -9 命令来终止进程](#为什么不推荐使用-kill--9-命令来终止进程)
  * [1、什么是Kill 命令](#1什么是kill-命令)
    * [1.1、语法](#11语法)
    * [1.2、kill -9 与 kill -15 的区别](#12kill--9-与-kill--15-的区别)
  * [2、为什么不推荐使用`kill -9 pid`甚至禁止使用](#2为什么不推荐使用kill--9-pid甚至禁止使用)
    * [2.1、单体服务架构](#21单体服务架构)
      * [2.1.1、InnoDB](#211innodb)
      * [2.1.2、MyISAM](#212myisam)
    * [2.2 、分布式架构](#22-分布式架构)
    * [2.3、总结](#23总结)
  * [3、如何结束一个进程](#3如何结束一个进程)
    * [3.1、使用 Linux kill 命令结束进程](#31使用-linux-kill-命令结束进程)
    * [3.2、结束一个 Tomcat 服务](#32结束一个-tomcat-服务)
    * [3.3、结束一个 springboot 服务](#33结束一个-springboot-服务)
      * [3.3.1、使用 ConfigurableApplicationContext 停止服务](#331使用-configurableapplicationcontext-停止服务)
        * [3.3.1.a、为什么ConfigurableApplicationContext#close 可以关闭程序](#331a为什么configurableapplicationcontextclose-可以关闭程序)
        * [3.3.1.b、为啥子报错了](#331b为啥子报错了)
      * [3.3.2、使用 spring-boot-starter-actuator 停止服务](#332使用-spring-boot-starter-actuator-停止服务)
        * [3.3.2.a、引入依赖](#332a引入依赖)
      * [3.3.2.b 修改 application.yml 文件](#332b-修改-applicationyml-文件)
<!-- TOC -->

五一假期期间和几个同学聚餐的时候聊天吐槽公司奇葩规定的时候，一个同学说：“我上次在生产环境用了`kill -9 pid`被罚了 2000 大洋，这坑逼规定。”那么我们今天就来讨论一下为什么很多公司不推荐用`kill -9 pid`甚至禁止使用该命令。

## 1、什么是Kill 命令

Linux kill 命令用于删除执行中的程序或工作。kill 可将指定的信息送至程序。预设的信息为 SIGTERM(15)，可将指定程序终止。若仍无法终止该程序，可使用 SIGKILL(9) 信息尝试强制删除程序。程序或工作的编号可利用 ps 指令或 jobs 指令查看。[^1]

### 1.1、语法

```shell
kill [-s <信息名称或编号>][程序]　或　kill [-l <信息编号>]
```

参数信息：

- -l <信息编号> 　若不加<信息编号>选项，则 -l 参数会列出全部的信息名称。
- -s <信息名称或编号> 　指定要送出的信息。
- [程序] 可以是程序的PID或是PGID，也可以是工作编号。

常用的信号：

- 1 (HUP)：重新加载进程。
- 9 (KILL)：杀死一个进程。
- 15 (TERM)：正常停止一个进程。

举个栗子：杀掉一个 pid 为 1111 的进程可以使用`kill 1111`或`kill -15 1111`。

### 1.2、kill -9 与 kill -15 的区别

kill -9 pid ：杀死一个进程；

kill -15 pid：终止一个进程；

这么描述这两个东东的定义似乎不太好理解，那么举个栗子：你现在在码字，然后你对象说快把洗衣机里面的衣服晾一下，你回答说“好的，等我把这点写完就去”，这个场景就相当于 `kill -15 pid`的执行过程，当收到 signal 之后不是立马结束而是先处理完剩余的工作再去结束。那么`kill -9 pid`就相当于你对象叫你去晾衣服你立马就去晾衣服了，即收到 signal 之后立马马下手上的工作结束当前的工作，并且这个操作是不计后果的。

## 2、为什么不推荐使用`kill -9 pid`甚至禁止使用

举个栗子：银行的转账场景，小张通过银行卡还钱给小李，当两个账户进行加钱扣钱的时候突然断电了，那么会出现什么情况呢？这里我们要从两个不同的架构上来讨论：

### 2.1、单体服务架构

#### 2.1.1、InnoDB

对于 InnoDB 来说这种情况并没有什么损失，因为它支持事务。

#### 2.1.2、MyISAM

对于 MyISAM 来说这将是个灾难性的问题，假如给小张的账户扣了钱，现在需要给小李的账户加钱，这个时候停电了，就会造成，小张的钱被扣了，但是小李没有拿到这笔钱，这会产生很严重的后果，这在生产环境是绝对不允许的，kill -9 相当于突然断电的效果。

### 2.2 、分布式架构

在当前这个分布式架构遍地走的时代，跨服务间的操作也变得异常普遍，那么这个时候如果使用`kill -9 pid `去停止服务，那就不是你的事务能保证数据的准确性了，这个时候你可能会想到分布式事务，这个世界上没有绝对的安全系统或者架构，分布式事务也是一样，他也会存在问题，概率很小，如果一旦发生，损失有可能是无法弥补的，所以一定不能使用`kill -9 pid `去停止服务，因为你不知道他会造成什么后果。

### 2.3、总结

之所以不推荐甚至禁止使用`kill -9 pid`是因为命令执行后会存在不确定性，滥用`kill -9 pid`可能还造成比删库跑路还重大的事故，毕竟在生产环境中删库还是可以恢复的，但是由于使用`kill -9 pid`命令造成的数据错误都不知道错在哪里。

## 3、如何结束一个进程

结束一个进程应该是无后果的、无危险的，而不是不计后果的结束，应做到以下几点：

1. 停止接收请求和内部线程。
2. 判断是否有线程正在执行。
3. 等待正在执行的线程执行完毕。
4. 停止容器。

做到这几点的结束才能叫做结束进程，反之可以认为是杀死进程。那么结束进程的方式有哪些呢？

### 3.1、使用 Linux kill 命令结束进程

使用 Linux 的 kill 命令结束进程需要一个为 15 的 signal，即：`kill -15 pid`。

### 3.2、结束一个 Tomcat 服务

使用 Tomcat 的 bin 目录下提供的shutdown.sh 或 shutdown.bat。当然使用该脚本之前需要配置 Tomcat conf 文件夹里的 server.xml 文件以避免文件端口冲突。

```xml
<!-- 将 server节点中的 port 属性修改成未被使用的端口 -->
<Server port="8005" shutdown="SHUTDOWN">
  ……
</Server>
```



### 3.3、结束一个 springboot 服务

#### 3.3.1、使用 ConfigurableApplicationContext 停止服务

编写一个 common，用于注入关闭方法

```java
package cn.com.mooyea.blogs.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConfigurableApplicationContextCommon implements ApplicationContextAware {
    private  ApplicationContext  context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void shutdown() {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        configurableApplicationContext.close();
    }
}

```

再写一个 Controller 用于测试停止服务的效果

```java
package cn.com.mooyea.blogs.controller;

import cn.com.mooyea.blogs.common.ConfigurableApplicationContextCommon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Resource
    ConfigurableApplicationContextCommon common;

    @GetMapping("/start")
    public void start() {
        log.info("start........");
        log.info("start sleep........");
        // 获取当前时间
        long start = System.currentTimeMillis();
        // 模拟业务处理
        try {
            for (int i = 0; i < 10; i++) {
                log.warn("i:{}", i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("sleep end........,sleep time:{}", System.currentTimeMillis() - start);
      	log.info("end........");
    }

    @GetMapping("/shutdown")
    public void shutdown() {
        log.info("into shutdown........");
        common.shutdown();
    }
}

```

在浏览器中先请求`/test/start`再去请求`/test/shutdown`执行结果如下图所示：

![image-20230503205920045](http://cdn.mooyea.com.cn/blogs/202501211649758.png)

从图中的信息可以看出来，整个方法执行完毕了，因为 start和 end 都打印出来了。但是有两个问题需要解决：1.为什么ConfigurableApplicationContext#close 可以关闭程序。2.为啥子报错了？

##### 3.3.1.a、为什么ConfigurableApplicationContext#close 可以关闭程序

要解决这个问题首先我们要看一看源码：

![image-20230503212342644](http://cdn.mooyea.com.cn/blogs/202501211649631.png)

从==AbstractApplicationContext#close==方法中可以看出整个执行逻辑为先判断jvm 中是否有 shutdownHook，如果有那么就从 jvm 中删除 shutdownHook。那么这个 shutdownHook 是什么时候注册的呢？我想应该是在启动过程中注册的 shutdownHook，那么我们就去SpringApplication的源码中看看是不是这样的。

![image-20230503213129126](http://cdn.mooyea.com.cn/blogs/202501211649335.png)

果然在翻了源码之后发现在==SpringApplication#refreshContext== shutdownHook 被注册了，并且在进一步阅读源码后发现由于`registerShutdownHook`的默认值为 true ，那么在默认情况下只要在 spring boot 启动过程中刷新了 context 就会向 jvm 中添加 shutdownHook。

##### 3.3.1.b、为啥子报错了

这就和sleep这个方法有关了，在线程休眠期间，当调用线程的interrupt方法的时候会导致sleep抛出异常，这里很明显就是==AbstractApplicationContext#close==调用线程的interrupt方法，目的是为了让线程停止，虽然让线程停止，但线程什么时候停止还是线程自己说的算，这一点可从日志中看到 start-end 得到印证。

#### 3.3.2、使用 spring-boot-starter-actuator 停止服务

##### 3.3.2.a、引入依赖

```xml
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### 3.3.2.b 修改 application.yml 文件

在 application.yml 中增加如下配置：

```yaml
management:
  endpoints:
    web:
      exposure:
        include: shutdown
  endpoint:
    shutdown:
      enabled: true
  server:
    port: 8888
```

那么我们再测试一下，先请求一下`/test/start`,然后再 postman 中请求http://127.0.0.1:8081/actuator/shutdown，然后我们查看 postman 和 IDEA 中的信息

![image-20230503215556419](http://cdn.mooyea.com.cn/blogs/202501211650436.png)

<center>postman 中请求结果</center>

![image-20230503215949219](http://cdn.mooyea.com.cn/blogs/202501211650743.png)

<center>IDEA 中打印的日志信息</center>

从两张图可以看出来程序已经关闭了。当然我这只是演示用的代码，在实际工作中使用actuator一定要进行鉴权否则服务很可能会死的不明不白。





[^1]:https://www.runoob.com/linux/linux-comm-kill.html

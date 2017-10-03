# seckill
#### 慕课网高并发秒杀课程
###### 项目已部署到阿里云服务器  [点此访问](http://101.132.126.188:8080/seckill/list "http://101.132.126.188:8080/seckill/list")

1. [Java高并发秒杀API之业务分析与DAO层](http://www.imooc.com/learn/587)
2. [Java高并发秒杀API之Service层](http://www.imooc.com/learn/631)
3. [Java高并发秒杀API之web层](http://www.imooc.com/learn/630)
4. [Java高并发秒杀API之高并发优化](http://www.imooc.com/learn/632)

* Dao层技术：数据库的设计和实现，Mybatis的理解和使用技巧，Mybatis整合Spring高效使用
* Service层技术：业务接口设计和封装，SpringIOC配置技巧，Spring声明式事务使用
* web层技术：Restful接口设计，SpringMVC的使用技巧，前端交设计，Bootstrap和JS的使用
* 高并发优化：系统瓶颈点分析，事务、锁、网络延迟的理解，前端、CDN、缓存理解使用
* * *
### 项目开发环境
#### 开发工具：IDEA +Maven +Navicat
#### 软件架构：SpringMVC +Spring +Mybatis +jdk1.8 +Tomcat +MySQL +redis
* * *
### 项目效果图展示
* 秒杀商品列表
![](https://github.com/strugglesdy/seckill/raw/master/images/1.png)  
* 秒杀已经结束
![](https://github.com/strugglesdy/seckill/raw/master/images/2.png)  
* 秒杀尚未开启，进入倒计时
![](https://github.com/strugglesdy/seckill/raw/master/images/3.png)  
* 进入详情页后需要先输入手机号
![](https://github.com/strugglesdy/seckill/raw/master/images/4.png)  
* 在秒杀时间内，秒杀接口暴露状态，开启秒杀
![](https://github.com/strugglesdy/seckill/raw/master/images/5.png)  
* 秒杀成功
![](https://github.com/strugglesdy/seckill/raw/master/images/6.png)  
* 浏览器保存了cookies，同一手机号秒杀同一个商品时，显示重复秒杀
![](https://github.com/strugglesdy/seckill/raw/master/images/7.png)  

* * *
### 个人总结
* 在学习SSM框架时，搭建过一个简单的商品查询系统，初步认识了SSM框架。但在学习了此项目后，对SSM框架以及项目的开发流程有了更深一步的认识。
* 同时此项目还涉及前端的js交互，框架bootstrap的使用。js主要使用到了JQuery及一些插件，同时js也可以按一定的面向对象的模块机制去书写js代码。
* 在并发优化方面，事务必然会用到mysql端的锁机制，对于长期持有锁的程序来说，不得不考虑网络延迟及java的GC影响，如何尽量减少其的影响，正是并发优化的瓶颈。
因此，同时通过这个高并发秒杀项目，了解了CND，redis，对事务，行级锁，网络延迟也有了一定的认识。

* * *
##   `感谢阅读`

## 代理层 Proxy
1.1 支持JDK代理

1.2 支持Javassist代理
## 注册中心层 Registry
2.1 支持 Zookeeper注册中心

## 路由层 Router
3.1 随机路由

3.2 轮训路由 

## 序列化层 Serialize

4.1 支持JDK序列化

4.2 支持Hessian序列化

4.3 支持Kryo序列化
 
4.4 支持FastJson序列化

测试码流大小作为评判序列化的优劣

## 添加职责链 Filter

服务分组、直连调用、token鉴权

5.1 支持服务分组

5.2 支持ip直连（发现两个相同服务返回结果不同指定ip进行debug）

5.3 添加请求日志（请求时间、调用方信息、请求方法）、

## 支持SPI可插拔式组件
6.1 自定义key-value式类加载方式

6.2 实行按需加载（懒加载模式）

## 请求接收与请求处理解耦
7.1 改造服务端只在一个Handler中处理请求为**缓冲队列** + **业务线程池**处理请求

## 容错层设计
8.1 支持客户端接收并显示服务端异常

8.2 服务端接口限流

8.3 客户端支持超时重试
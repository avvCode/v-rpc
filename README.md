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

## 添加职责链 Filter

服务分组、直连调用、token鉴权

5.1 支持服务分组

5.2 支持ip直连（发现两个相同服务返回结果不同指定ip进行debug）

5.3 添加请求日志（请求时间、调用方信息、请求方法）
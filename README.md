# jrpc
本框架是阅读完dubbo, motan等相关源码与设计原理之后,自研实现的基于Netty4.x的轻量级RPC框架. 

# 技术选型
- Spring：提供Bean的依赖注入能力, 无痛接入.
- Netty：屏蔽NIO细节, 提升性能.
- Protostuff：基于 Protobuf 序列化框架，提升序列化, 反序列化效率
- ZooKeeper：提供服务注册与发现功能

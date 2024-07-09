

## RabbitMQ六种模式示例

源码: [ghdefe/rabbitmq-demo](https://github.com/ghdefe/rabbitmq-demo)

此项目分别演示六种模式: 简单模式、工作模式、发布/订阅模式、路由模式、主题模式、RPC模式

- **简单模式**: 生产者直接发送消息到队列、消费者直接消费队列、不经过交换机
  

- **工作模式**: 与简单模式一致,只是变成多个消费者消费同一个队列, 消息不会被重复消费: 如果有消息1,2, 那么消费者A消费消息1, 消费者B消费消息2. 
  

- **发布/订阅模式**: 消费者创建交换机, 创建队列, 并将队列绑定到交换机上. 生产者发送消息到交换机, 交换机将消息发送到被绑定的队列上.   
  例如: 两个微服务. 
  - 微服务A为2副本, 创建队列A, 并绑定到交换机上;
  - 微服务B为1副本, 创建队列B, 并绑定到交换机上;
  
  生产者发布2条消息1,2到交换机上, 那么:  

  - 微服务A-1副本收到消息1, 微服务A-2副本收到消息2
  - 微服务B收到消息1,2
  

- **路由模式**: 消费者创建交换机、创建队列、并通过键将队列绑定到交换机上. 生产者发送消息到交换机, 若消息键与消费者绑定键一致, 则将mq将消息发送到队列, 消费者订阅队列收到消息.
  

- **主题模式**: 与路由模式类似, 只是主题模式中, 键允许为通配符, 消息到达交换机时, 通过通配符规则转发消息到队列.
  

- **RPC模式**: 与简单模式类似, 只是RPC模式生产者发送消息后, 会等待消费者回复消息, 生产者可以接收到消费者返回的消息并处理.

### 1。 项目结构

- consumer: 消费者A
- consumer2: 消费者B, 仅用于演示发布/订阅模式
- producer: 生产者

### 2. 启动RabbitMQ实例

```shell
docker run --rm --name rabbitmq -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -e RABBITMQ_USERNAME=admin -e RABBITMQ_PASSWORD=admin    -p 5672:5672 -p 15672:15672       bitnami/rabbitmq:latest
```

### 3. 启动消费者A

消费者主要代码: `consumer/src/main/java/com/example/consumer`, 消费者启动类: `consumer/src/main/java/com/example/consumer/ConsumerApplication.java`.

测试消息均衡分配效果需要启动多个消费者实例,因此在IntelliJ Idea中右上角, 启动配置中配置`Allow multiple instances`, 就可以多次点击启动按钮启动多个消费者实例.


### 4. 启动消费者B

消费者B主要代码: `consumer2/src/main/java/com/example/consumer2`, 消费者启动类: `consumer2/src/main/java/com/example/consumer2/Consumer2Application.java`.
  
消费者B仅需要启用一个实例

### 5. 生产者发布消息

生产者配置: `producer/src/main/java/com/example/producer/service/RabbitmqProducer.java`

生产消息: 根据需要演示的模式启动对应的单元测试即可生产对应模式的消息. `producer/src/test/java/com/example/producer/ProducerApplicationTests.java`


### 效果

- 简单模式

> 消费者A
```log
2024-07-09T16:19:59.243+08:00  INFO 22032 --- [consumer1] [ntContainer#6-1] c.e.consumer.service.RabbitmqConsumer    : 简单模式, 收到消息:来自简单模式的消息: 0
2024-07-09T16:19:59.245+08:00  INFO 22032 --- [consumer1] [ntContainer#6-1] c.e.consumer.service.RabbitmqConsumer    : 简单模式, 收到消息:来自简单模式的消息: 1
2024-07-09T16:19:59.246+08:00  INFO 22032 --- [consumer1] [ntContainer#6-1] c.e.consumer.service.RabbitmqConsumer    : 简单模式, 收到消息:来自简单模式的消息: 2
2024-07-09T16:19:59.246+08:00  INFO 22032 --- [consumer1] [ntContainer#6-1] c.e.consumer.service.RabbitmqConsumer    : 简单模式, 收到消息:来自简单模式的消息: 3
2024-07-09T16:19:59.246+08:00  INFO 22032 --- [consumer1] [ntContainer#6-1] c.e.consumer.service.RabbitmqConsumer    : 简单模式, 收到消息:来自简单模式的消息: 4
```

- 工作模式
> 消费者A-1
```log
2024-07-09T16:20:59.524+08:00  INFO 22032 --- [consumer1] [ntContainer#4-1] c.e.consumer.service.RabbitmqConsumer    : 工作模式, 消费者-consumer1, 收到消息:来自工作模式的消息: 0
2024-07-09T16:20:59.528+08:00  INFO 22032 --- [consumer1] [ntContainer#4-1] c.e.consumer.service.RabbitmqConsumer    : 工作模式, 消费者-consumer1, 收到消息:来自工作模式的消息: 2
2024-07-09T16:20:59.530+08:00  INFO 22032 --- [consumer1] [ntContainer#4-1] c.e.consumer.service.RabbitmqConsumer    : 工作模式, 消费者-consumer1, 收到消息:来自工作模式的消息: 4
```

> 消费者A-2
```log
2024-07-09T16:20:59.559+08:00  INFO 20192 --- [consumer1] [ntContainer#4-1] c.e.consumer.service.RabbitmqConsumer    : 工作模式, 消费者-consumer1, 收到消息:来自工作模式的消息: 1
2024-07-09T16:20:59.563+08:00  INFO 20192 --- [consumer1] [ntContainer#4-1] c.e.consumer.service.RabbitmqConsumer    : 工作模式, 消费者-consumer1, 收到消息:来自工作模式的消息: 3
```
  
一共发送5条消息, 消费者A-1消费消息[0, 2, 4], 消费者A-2消费消息[1, 3], 可见消息被`均衡`且`不重复`消费.
  


- 发布/订阅模式
> 消费者A-1
```log
2024-07-09T16:22:28.737+08:00  INFO 22032 --- [consumer1] [ntContainer#1-1] c.e.consumer.service.RabbitmqConsumer    : 发布/订阅模式, 消费者-consumer1, 收到消息:来自广播模式的消息: 0
2024-07-09T16:22:28.740+08:00  INFO 22032 --- [consumer1] [ntContainer#1-1] c.e.consumer.service.RabbitmqConsumer    : 发布/订阅模式, 消费者-consumer1, 收到消息:来自广播模式的消息: 2
2024-07-09T16:22:28.741+08:00  INFO 22032 --- [consumer1] [ntContainer#1-1] c.e.consumer.service.RabbitmqConsumer    : 发布/订阅模式, 消费者-consumer1, 收到消息:来自广播模式的消息: 4
```

> 消费者A-2
```log
2024-07-09T16:22:28.740+08:00  INFO 20192 --- [consumer1] [ntContainer#1-1] c.e.consumer.service.RabbitmqConsumer    : 发布/订阅模式, 消费者-consumer1, 收到消息:来自广播模式的消息: 1
2024-07-09T16:22:28.741+08:00  INFO 20192 --- [consumer1] [ntContainer#1-1] c.e.consumer.service.RabbitmqConsumer    : 发布/订阅模式, 消费者-consumer1, 收到消息:来自广播模式的消息: 3
```

> 消费者B
```log
2024-07-09T16:22:28.773+08:00  INFO 16748 --- [consumer2] [ntContainer#0-1] c.e.consumer2.service.RabbitmqConsumer   : 发布/订阅模式, 消费者-consumer2, 收到消息:来自广播模式的消息: 0
2024-07-09T16:22:28.778+08:00  INFO 16748 --- [consumer2] [ntContainer#0-1] c.e.consumer2.service.RabbitmqConsumer   : 发布/订阅模式, 消费者-consumer2, 收到消息:来自广播模式的消息: 1
2024-07-09T16:22:28.780+08:00  INFO 16748 --- [consumer2] [ntContainer#0-1] c.e.consumer2.service.RabbitmqConsumer   : 发布/订阅模式, 消费者-consumer2, 收到消息:来自广播模式的消息: 2
2024-07-09T16:22:28.781+08:00  INFO 16748 --- [consumer2] [ntContainer#0-1] c.e.consumer2.service.RabbitmqConsumer   : 发布/订阅模式, 消费者-consumer2, 收到消息:来自广播模式的消息: 3
2024-07-09T16:22:28.782+08:00  INFO 16748 --- [consumer2] [ntContainer#0-1] c.e.consumer2.service.RabbitmqConsumer   : 发布/订阅模式, 消费者-consumer2, 收到消息:来自广播模式的消息: 4
```
  
  
一共发送5条消息:
- 消费者A创建队列1, 5条消息发送到队列1.
- 消费者B创建队列2, 5条消息发送到队列2.
因此:
- 消费者A-1与消费者A-2共同消费队列1, 消费者A-1消费消息[0, 2, 4], 消费者A-2消费消息[1, 3]
- 消费者B消费队列2, 消费者B消费消息[0, 1, 2, 3, 4]
  


- 路由模式
> 消费者A-1
```log
2024-07-09T16:40:32.643+08:00  INFO 4888 --- [consumer1] [ntContainer#1-1] c.e.consumer.service.RabbitmqConsumer    : 路由模式, 队列: routing-queue1, 收到消息:来自路由模式的消息: key-aaa
2024-07-09T16:40:32.643+08:00  INFO 4888 --- [consumer1] [ntContainer#2-1] c.e.consumer.service.RabbitmqConsumer    : 路由模式, 队列: routing-queue2, 收到消息:来自路由模式的消息: key-ccc
2024-07-09T16:40:32.646+08:00  INFO 4888 --- [consumer1] [ntContainer#1-1] c.e.consumer.service.RabbitmqConsumer    : 路由模式, 队列: routing-queue1, 收到消息:来自路由模式的消息: key-bbb
```
  
创建2个队列: 
- `routing-queue1`
- `routing-queue2`
  

创建3条绑定规则: 
- `key-aaa`绑定到`routing-queue1`
- `key-bbb`绑定到`routing-queue1`
- `key-ccc`绑定到`routing-queue2`
  
一共发送3条消息:
- 消息1: 键为`key-aaa`, 发送到绑定的队列`routing-queue1`
- 消息2: 键为`key-bbb`, 发送到绑定的队列`routing-queue1`
- 消息3: 键为`key-ccc`, 发送到绑定的队列`routing-queue2`
  


- 主题模式
> 消费者A-1
```log
2024-07-09T16:24:32.369+08:00  INFO 22032 --- [consumer1] [ntContainer#7-1] c.e.consumer.service.RabbitmqConsumer    : 主题模式, 队列: topic-queue3, 收到消息:来自主题模式的消息: key: aaa.111
2024-07-09T16:24:32.369+08:00  INFO 22032 --- [consumer1] [ntContainer#3-1] c.e.consumer.service.RabbitmqConsumer    : 主题模式, 队列: topic-queue1, 收到消息:来自主题模式的消息: key: aaa.111
2024-07-09T16:24:32.370+08:00  INFO 22032 --- [consumer1] [ntContainer#5-1] c.e.consumer.service.RabbitmqConsumer    : 主题模式, 队列: topic-queue2, 收到消息:来自主题模式的消息: key: aaa.111
2024-07-09T16:24:32.373+08:00  INFO 22032 --- [consumer1] [ntContainer#3-1] c.e.consumer.service.RabbitmqConsumer    : 主题模式, 队列: topic-queue1, 收到消息:来自主题模式的消息: key: aaa.222
2024-07-09T16:24:32.373+08:00  INFO 22032 --- [consumer1] [ntContainer#5-1] c.e.consumer.service.RabbitmqConsumer    : 主题模式, 队列: topic-queue2, 收到消息:来自主题模式的消息: key: aaa.222
2024-07-09T16:24:32.373+08:00  INFO 22032 --- [consumer1] [ntContainer#7-1] c.e.consumer.service.RabbitmqConsumer    : 主题模式, 队列: topic-queue3, 收到消息:来自主题模式的消息: key: aaa.222
2024-07-09T16:24:32.374+08:00  INFO 22032 --- [consumer1] [ntContainer#3-1] c.e.consumer.service.RabbitmqConsumer    : 主题模式, 队列: topic-queue1, 收到消息:来自主题模式的消息: key: aaa.xxx.111
2024-07-09T16:24:32.374+08:00  INFO 22032 --- [consumer1] [ntContainer#7-1] c.e.consumer.service.RabbitmqConsumer    : 主题模式, 队列: topic-queue3, 收到消息:来自主题模式的消息: key: aaa.xxx.111
```



创建3个队列:
- `topic-queue1`
- `topic-queue2`
- `topic-queue3`


创建3条绑定规则:
1. `#`绑定到`topic-queue1`
2. `aaa.*`绑定到`topic-queue1`
3. `aaa.#`绑定到`topic-queue2`

一共发送3条消息:
- 消息1: 键为`aaa.111`, 命中: [`规则1`, `规则2`, `规则3`]. 发送到绑定的队列[`topic-queue1`,`topic-queue2`,`topic-queue3`]
- 消息2: 键为`aaa.222`, 命中: [`规则1`, `规则2`, `规则3`]. 发送到绑定的队列[`topic-queue1`,`topic-queue2`,`topic-queue3`]
- 消息3: 键为`aaa.xxx.111`,  命中: [`规则1`, `规则3`]. 发送到绑定的队列[`topic-queue1`, `topic-queue3`]
  


- RPC模式
> 消费者A-1
```log
2024-07-09T16:24:43.012+08:00  INFO 22032 --- [consumer1] [ntContainer#0-1] c.e.consumer.service.RabbitmqConsumer    : RPC模式, 收到消息:来自RPC模式的消息
```

> 生产者
```log
2024-07-09T16:24:42.993+08:00  INFO 14384 --- [producer] [           main] .l.DirectReplyToMessageListenerContainer : SimpleConsumer [queue=amq.rabbitmq.reply-to, index=0, consumerTag=amq.ctag-YATKTxnPooF75FI1aIFwiw identity=5ad57e90] started
2024-07-09T16:24:43.025+08:00  INFO 14384 --- [producer] [           main] c.e.producer.service.RabbitmqProducer    : 生产者收到回调消息: 消费者: 收到消息:来自RPC模式的消息
```


## 附  

[参考文章](https://www.cnblogs.com/jeremylai7/p/16527309.html)

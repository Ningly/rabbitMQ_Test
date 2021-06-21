package com.atguigu.rabbitmq.dead;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    private static final String DEAD_EXCHANGE = "dead_exchange";
    private static final String NORMAL_QUEUE = "NORMAL_QUEUE";
    private static final String DEAD_QUEUE = "DEAD_QUEUE";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
//正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
//正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "lisi");
        //设置正常对了长度限制
//        params.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, params);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);


        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan",null);
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi",null);

//        DeliverCallback deliverCallback = (consumerTag, message) -> {
//            String msg = new String(message.getBody(), "UTF-8");
//            System.out.println("Consumer01 接收到消息"+msg);
//
//        };



        System.out.println("等待接收消息........... ");
        DeliverCallback deliverCallback = (consumerTag, delivery) ->
        {
            String message = new String(delivery.getBody(), "UTF-8");
            if(message.equals("info5")){
                System.out.println("Consumer01 接收到消息" + message + "并拒绝签收该消息");
//requeue 设置为 false 代表拒绝重新入队 该队列如果配置了死信交换机将发送到死信队列中
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }else {
                System.out.println("Consumer01 接收到消息"+message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        boolean autoAck = false;

        CancelCallback blockedCallback = consumerTag -> {
        };

        channel.basicConsume(NORMAL_QUEUE, autoAck, deliverCallback, blockedCallback);
    }
}

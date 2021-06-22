package com.atguigu.rabbitmq.topic;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsTopic01 {
    private static final String EXCHANGE_NAME="topic_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName="Q1";
        channel.queueDeclare(queueName,false,false,false, null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");

        DeliverCallback deliverCallback= (consumerTag, delivery)->{
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("接收队列:"+queueName+"绑定键:"+delivery.getEnvelope().getRoutingKey()+",消息:"+message);
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}

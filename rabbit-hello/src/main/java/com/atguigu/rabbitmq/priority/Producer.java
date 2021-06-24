package com.atguigu.rabbitmq.priority;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.HashMap;

public class Producer {
    public static final String QUEUE_NAME="hello_priority";
    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        HashMap<String, Object> args = new HashMap<>();
        args.put("x-max-priority",10);
        channel.queueDeclare("hello_priority", true, false, false, args);

        for (int i = 0; i < 10; i++) {
            String message="info"+i;
            if(i==5){
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }else
            {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }
    }
}

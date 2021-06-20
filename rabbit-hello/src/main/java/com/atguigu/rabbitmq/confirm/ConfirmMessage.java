package com.atguigu.rabbitmq.confirm;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        publishMessageAnyone();
        publishMessageBatch();
        publicMessageAsync();

    }

    public static void publishMessageAnyone() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();
        channel.queueDeclare(queueName, true, false, false, null);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = i + "";
            channel.basicPublish("", queueName, false, null, msg.getBytes());
            boolean b = channel.waitForConfirms();
//            if (b)
//                System.out.println("sent successful");
        }
        long end = System.currentTimeMillis();
        System.out.println("publish" + MESSAGE_COUNT + "个消息需要" + (end - begin) + "ms");
    }

    public static void publishMessageBatch() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();
        //批量确认消息大小
        int batchSize = 100;
        //未确认消息个数
        int unCheckedCount = 0;

        channel.queueDeclare(queueName, true, false, false, null);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = i + "";
            channel.basicPublish("", queueName, false, null, msg.getBytes());
            unCheckedCount++;
            if (unCheckedCount == batchSize) {
                channel.waitForConfirms();
                unCheckedCount = 0;
            }
            //为了确保还有剩余没有确认消息 再次确认
            if (unCheckedCount > 0) {
                channel.waitForConfirms();
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) + "ms");

    }

    public static void publicMessageAsync() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();

        ConcurrentSkipListMap<Long, String> publishedMap = new ConcurrentSkipListMap<>();


        ConfirmCallback confirmCallback = (deliveryTag, multiple) -> {
            if(multiple){
                //返回的是小于等于当前序列号的未确认消息 是一个 map
                ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap = publishedMap.headMap(deliveryTag,true);
                //清除该部分未确认消息
                longStringConcurrentNavigableMap.clear();
            }else {
                publishedMap.remove(deliveryTag);
            }
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = publishedMap.get(deliveryTag);
            System.out.println("发布的消息"+message+"未被确认，序列号"+deliveryTag);
        };

        channel.addConfirmListener(confirmCallback, nackCallback);
        channel.queueDeclare(queueName, true, false, false, null);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = i + "";
            publishedMap.put(channel.getNextPublishSeqNo(),msg);
//            System.out.println("channel.getNextPublishSeqNo()::: "+channel.getNextPublishSeqNo());
            channel.basicPublish("", queueName, false, null, msg.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量异步确认消息,耗时" + (end - begin) + "ms");
    }
}

package com.atguigu.springbootrabbitmq.consumer;

//@Slf4j
//@Component
//public class receiveDelayedQueue {
//
//    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
//
//    @RabbitListener(queues = DELAYED_QUEUE_NAME)
//    public void receiveDelayedQueue(Message message) {
//        String msg = new String(message.getBody());
//        log.info("当前时间：{},收到延时队列的消息：{}", new Date().toString(), msg);
//    }
//}

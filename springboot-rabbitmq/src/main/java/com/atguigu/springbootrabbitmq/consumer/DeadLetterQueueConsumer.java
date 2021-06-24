package com.atguigu.springbootrabbitmq.consumer;

//@Slf4j
//@Component
//public class DeadLetterQueueConsumer {
//    @RabbitListener(queues = "QD")
//    public void receiveD(Message message, Channel channel) throws IOException
//    {
//        String msg = new String(message.getBody());
//        log.info("当前时间：{},收到死信队列信息{}", new Date().toString(), msg);
//    }
//}
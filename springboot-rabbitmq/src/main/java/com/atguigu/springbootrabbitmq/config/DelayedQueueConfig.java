package com.atguigu.springbootrabbitmq.config;

//@Component
//public class DelayedQueueConfig {
//    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
//    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
//    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";
//
//    @Bean
//    public Queue delayedQueue(){
//        return new Queue(DELAYED_QUEUE_NAME,true);
//    }
//    @Bean
//    public CustomExchange delayedExchange(){
//        Map<String, Object> args = new HashMap<>();
//        //自定义交换机的类型
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,args);
//    }
//
//    @Bean
//    public Binding bindingDelayedQueue(@Qualifier("delayedQueue") Queue queue, @Qualifier("delayedExchange")CustomExchange delayedExchange) {
//        return BindingBuilder.bind(queue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
//    }
//}

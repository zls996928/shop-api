package com.fh.shop.api.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String MAILEXCHANGE = "mailExchange";
    public static final String MAILQUEUE = "mailQueue";
    public static final String MAILROUTEKEY = "mail";

    public static final String ORDEREXCHANGE = "orderExchange";
    public static final String ORDERQUEUE = "orderQueue";
    public static final String ORDERROUTEKEY = "order";

    @Bean
    public DirectExchange mailExchange(){
        return new DirectExchange(MAILEXCHANGE,true,false);
    }

    @Bean
    public Queue mailQueue(){
        return new Queue(MAILQUEUE,true);

    }

    @Bean
    public Binding mailBinding(){
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAILROUTEKEY);
    }

    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(ORDEREXCHANGE,true,false);
    }
    @Bean
    public Queue orderQueue(){
        return new Queue(ORDERQUEUE,true);
    }
    @Bean
    public Binding orderBinding(){
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDERROUTEKEY);
    }
}

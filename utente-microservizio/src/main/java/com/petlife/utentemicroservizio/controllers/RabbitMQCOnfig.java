package com.petlife.utentemicroservizio.controllers;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.Bindings;

@Configuration
public class RabbitMQCOnfig {

    public static final String QUEUE_A = "codaAnimali";
    public static final String QUEUE_B = "codaUtenti";
    public static final String EXCHANGE = "exchange";
    public static final String ROUTINGKEY_A = "AroutingKey";
    public static final String ROUTINGKEY_B = "BroutingKey";

    @Bean
    public Queue queue_A(){
        return new Queue(QUEUE_A,false);
    }

    @Bean
    public Queue queue_B(){
        return new Queue(QUEUE_B,false);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding_A(Queue queue_A, TopicExchange exchange){
        return BindingBuilder.bind(queue_A).to(exchange).with(ROUTINGKEY_A);
    }

    @Bean
    public Binding binding_B(Queue queue_B, TopicExchange exchange){
        return BindingBuilder.bind(queue_B).to(exchange).with(ROUTINGKEY_B);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }


}

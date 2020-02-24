package com.zslin.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通过Rabbit进行修改操作的工具类
 */
@Component
public class RabbitUpdateTools {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void updateData(String beanName, String methodName, Object ... params) {
        try {
            RabbitUpdateDto dto = new RabbitUpdateDto(beanName, methodName, params);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

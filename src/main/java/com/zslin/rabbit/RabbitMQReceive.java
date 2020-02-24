package com.zslin.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE) //监听的队列名称 TestDirectQueue
@Slf4j
public class RabbitMQReceive implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /*@Autowired
    private IUserDao userDao;*/

    @RabbitHandler
    public void process(Map testMessage) {
        String res = "DirectReceiver消费者收到消息  : " + testMessage.toString();
//        System.out.println(res);
        log.info(res);
    }

    /*@RabbitHandler
    public void addUser(User user) {
        userDao.save(user);
        log.info("添加用户信息： "+ user.toString());
    }*/

    /** 相对通用的处理方法 */
    @RabbitHandler
    public void handlerUpdate(RabbitUpdateDto dto) {
        log.info("处理数据对象 {} ", dto.toString());
        try {
            List<Object> params = dto.getParams();
            Object obj = getApplicationContext().getBean(dto.getBeanName());
            Method method ;
            boolean hasParams = false;
            if(params==null || params.size()<=0) {
                method = obj.getClass().getMethod(dto.getMethodName());
            } else {
                hasParams = true;
                Class [] paramClz = new Class[params.size()];
                int index = 0;
                for(Object o : params) {
                    paramClz[index++] = o.getClass();
                }
                method = obj.getClass().getMethod(dto.getMethodName(), paramClz);
            }
            if(hasParams) {
                method.invoke(obj, params.toArray());
            } else {
                method.invoke(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

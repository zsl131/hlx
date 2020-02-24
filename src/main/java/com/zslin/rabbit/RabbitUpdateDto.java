package com.zslin.rabbit;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过Rabbit进行修改操作的DTO对象
 */
@Data
public class RabbitUpdateDto implements Serializable {

    private String beanName;

    private String methodName;

    private List<Object> params;

    public RabbitUpdateDto(String beanName, String methodName, List<Object> params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }

    public RabbitUpdateDto(String beanName, String methodName, Object ... params) {
        this.beanName = beanName;
        this.methodName = methodName;
        List<Object> list = new ArrayList<>();
        for(int i=0;i<params.length;i++) {
            list.add(params[i]);
        }
        this.params = list;
    }
}

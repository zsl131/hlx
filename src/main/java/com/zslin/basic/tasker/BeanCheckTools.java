package com.zslin.basic.tasker;

import com.zslin.basic.exception.BusinessException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 检测任务接口是否存在的工具类
 */
@Component
public class BeanCheckTools {

    @Autowired
    private BeanFactory factory;

    public boolean checkMethod(String beanName, String methodName, Object ...params) {
//        System.out.println("--------->params::"+params);
//        System.out.println("-------------->length:"+params.length);
//        System.out.println("-------------->length:"+params[0]);
        try {
            Object obj = factory.getBean(beanName);

            List<Class> paramList = new ArrayList<>();
            for(int i=0;i<params.length;i++) {
                if(params[i]!=null) {
                    paramList.add(params[i].getClass());
                }
            }
            Class [] paramType = new Class[paramList.size()];
            paramList.toArray(paramType);
            //System.out.println(paramList.size()+"============="+paramType.length);

            boolean hasParam = (paramType.length>0);

            Method method = null;
            if(hasParam) {
                method = obj.getClass().getMethod(methodName, paramType);
            } else {
                method = obj.getClass().getMethod(methodName);
            }
            return true;
//            System.out.println("-------->"+method);
        } catch (NoSuchBeanDefinitionException e) {
            throw new BusinessException(BusinessException.Code.NO_BEAN_DEF, e.getMessage());
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
            throw new BusinessException(BusinessException.Code.NO_SUCH_METHOD, e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new BusinessException(BusinessException.Code.DEFAULT_ERR_CODE, e.getMessage());
        }
    }
}

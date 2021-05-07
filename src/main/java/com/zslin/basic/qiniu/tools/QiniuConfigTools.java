package com.zslin.basic.qiniu.tools;

import com.zslin.basic.exception.BusinessException;
import com.zslin.basic.qiniu.dao.IQiniuConfigDao;
import com.zslin.basic.qiniu.model.QiniuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 七牛配置工具类
 * Created by zsl on 2018/12/1.
 */
@Component
public class QiniuConfigTools {

    @Autowired
    private IQiniuConfigDao qiniuConfigDao;

    private static QiniuConfig instance;

    public QiniuConfig getQiniuConfig() throws BusinessException {
        if(instance==null) {
            instance = qiniuConfigDao.loadOne();
        }
        if(instance==null) {throw new BusinessException(BusinessException.Code.CONFIG_NULL, "未检测到七牛配置信息");}
        return instance;
    }

    public void setConfig(QiniuConfig config) {
        instance = config;
    }
}

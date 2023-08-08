package com.vv.framework.spring.starter.config;

import com.vv.core.client.Client;
import com.vv.core.client.ConnectionHandler;
import com.vv.core.client.RpcReference;
import com.vv.core.client.RpcReferenceWrapper;
import com.vv.framework.spring.starter.annotation.VRpcReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;

/**
 * @author vv
 * @Description 客户端自动装配
 * @date 2023/7/28-18:05
 */
public class IRpcClientAutoConfiguration implements BeanPostProcessor, ApplicationListener<ApplicationReadyEvent> {
    private static RpcReference rpcReference = null;
    private static Client client = null;
    private volatile boolean needInitClient = false;
    private volatile boolean hasInitClientConfig = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(IRpcClientAutoConfiguration.class);



    @Override
    public Object postProcessAfterInitialization (Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(VRpcReference.class)) {
                //确保只初始化一次客户端配置
                if (!hasInitClientConfig) {
                    client = new Client();
                    try {
                        rpcReference = client.initClientApplication();
                    } catch (Exception e) {
                        LOGGER.error("[IRpcClientAutoConfiguration] postProcessAfterInitialization has error ",e);
                        throw new RuntimeException(e);
                    }
                    hasInitClientConfig = true;
                }
                needInitClient = true;
                //获取指向远程服务的接口类
                VRpcReference iRpcReference = field.getAnnotation(VRpcReference.class);
                //将注解设置的字段包装成一个
                try {
                    field.setAccessible(true);
                    Object refObj;
                    RpcReferenceWrapper rpcReferenceWrapper = new RpcReferenceWrapper();
                    rpcReferenceWrapper.setAimClass(field.getType());
                    rpcReferenceWrapper.setGroup(iRpcReference.group());
                    rpcReferenceWrapper.setServiceToken(iRpcReference.serviceToken());
                    rpcReferenceWrapper.setUrl(iRpcReference.url());
                    rpcReferenceWrapper.setTimeOut(iRpcReference.timeOut());
                    //失败重试次数
                    rpcReferenceWrapper.setRetry(iRpcReference.retry());
                    rpcReferenceWrapper.setAsync(iRpcReference.async());
                    refObj = rpcReference.get(rpcReferenceWrapper);
                    field.set(bean, refObj);
                    client.doSubscribeService(field.getType());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return bean;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (needInitClient && client!=null) {
            LOGGER.info(" ================== [{}] started success ================== ",client.getClientConfig().getApplicationName());
            ConnectionHandler.setBootstrap(client.getBootstrap());
            client.doConnectServer();
            client.startClient();
        }
    }
}

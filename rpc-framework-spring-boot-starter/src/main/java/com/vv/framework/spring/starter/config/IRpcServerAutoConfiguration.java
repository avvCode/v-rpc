package com.vv.framework.spring.starter.config;

import com.vv.core.common.event.VRpcListenerLoader;
import com.vv.core.server.ApplicationShutdownHook;
import com.vv.core.server.Server;
import com.vv.core.server.ServiceWrapper;
import com.vv.framework.spring.starter.annotation.VRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author vv
 * @Description 服务端自动装配
 * @date 2023/7/28-18:05
 */
public class IRpcServerAutoConfiguration implements InitializingBean, ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(IRpcServerAutoConfiguration.class);

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Server server = null;
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(VRpcService.class);
        if (beanMap.size() == 0) {
            //说明当前应用内部不需要对外暴露服务
            return;
        }
        printBanner();
        long begin = System.currentTimeMillis();
        server = new Server();
        server.initServerConfig();
        VRpcListenerLoader iRpcListenerLoader = new VRpcListenerLoader();
        iRpcListenerLoader.init();
        for (String beanName : beanMap.keySet()) {
            Object bean = beanMap.get(beanName);
            VRpcService iRpcService = bean.getClass().getAnnotation(VRpcService.class);
            ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(bean, iRpcService.group());
            dataServiceServiceWrapper.setServiceToken(iRpcService.serviceToken());
            dataServiceServiceWrapper.setLimit(iRpcService.limit());
            server.exportService(dataServiceServiceWrapper);
            LOGGER.info(">>>>>>>>>>>>>>> [v-rpc] {} export success! >>>>>>>>>>>>>>> ",beanName);
        }
        long end = System.currentTimeMillis();
        ApplicationShutdownHook.registryShutdownHook();
        server.startApplication();
        LOGGER.info(" ================== [{}] started success in {}s ================== ",server.getServerConfig().getApplicationName(),((double)end-(double)begin)/1000);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void printBanner(){
        System.out.println();
        System.out.println("==============================================");
        System.out.println("|||---------- V-Rpc Starting Now! ----------|||");
        System.out.println("==============================================");
        System.out.println("version: 1.0.0");
        System.out.println();
    }
}

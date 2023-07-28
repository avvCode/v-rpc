package com.vv.provider.common;


import com.vv.core.common.event.VRpcListenerLoader;

import java.io.IOException;


public class ProviderDemo {

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        Server server = new Server();
        server.initServerConfig();
        VRpcListenerLoader iRpcListenerLoader = new VRpcListenerLoader();
        iRpcListenerLoader.init();
        ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(new DataServiceImpl(), "dev");
        dataServiceServiceWrapper.setServiceToken("token-a");
        dataServiceServiceWrapper.setLimit(4);
        ServiceWrapper userServiceServiceWrapper = new ServiceWrapper(new UserServiceImpl(), "dev");
        userServiceServiceWrapper.setServiceToken("token-b");
        userServiceServiceWrapper.setLimit(4);
        server.exportService(dataServiceServiceWrapper);
        server.exportService(userServiceServiceWrapper);
        ApplicationShutdownHook.registryShutdownHook();
        server.startApplication();
    }
}

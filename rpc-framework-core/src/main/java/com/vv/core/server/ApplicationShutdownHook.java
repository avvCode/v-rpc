package com.vv.core.server;

import com.vv.core.common.event.VRpcDestroyEvent;
import com.vv.core.common.event.VRpcListenerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/24-15:56
 */
public class ApplicationShutdownHook {
    public static Logger LOGGER = LoggerFactory.getLogger(ApplicationShutdownHook.class);

    /**
     * 注册一个shutdownHook的钩子，当jvm进程关闭的时候触发
     */
    public static void registryShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("[registryShutdownHook] ==== ");
                VRpcListenerLoader.sendSyncEvent(new VRpcDestroyEvent("destroy"));
                System.out.println("destroy");
            }
        }));
    }
}

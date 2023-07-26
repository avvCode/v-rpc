package com.vv.core.common.config;

import java.io.IOException;

/**
 * @author vv
 * @Description 加载配置文件
 * @date 2023/7/23-16:07
 */
public class PropertiesBootstrap {

    private volatile boolean configIsReady;

    public static final String SERVER_PORT = "v-rpc.serverPort";
    public static final String REGISTER_ADDRESS = "v-rpc.registerAddr";
    public static final String APPLICATION_NAME = "v-rpc.applicationName";
    public static final String PROXY_TYPE = "v-rpc.proxyType";

    public static final String REGISTER_TYPE = "v-rpc.registerType";
    public static final String  ROUTER_TYPE = "v-rpc.router";
    public static final String SERVER_SERIALIZE_TYPE = "v-rpc.serverSerialize";
    public static final String CLIENT_SERIALIZE_TYPE = "v-rpc.clientSerialize";

    public static ServerConfig loadServerConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadServerConfigFromLocal fail,e is {}", e);
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerPort(PropertiesLoader.getPropertiesInteger(SERVER_PORT));
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
        serverConfig.setRegisterType(PropertiesLoader.getPropertiesStr(REGISTER_TYPE));
        serverConfig.setServerSerialize(PropertiesLoader.getPropertiesStr(SERVER_SERIALIZE_TYPE));
        return serverConfig;
    }

    public static ClientConfig loadClientConfigFromLocal(){
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadClientConfigFromLocal fail,e is {}", e);
        }
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
        clientConfig.setRegisterType(PropertiesLoader.getPropertiesNotBlank(REGISTER_TYPE));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStr(PROXY_TYPE));
        clientConfig.setRouterStrategy(PropertiesLoader.getPropertiesStr(ROUTER_TYPE));
        clientConfig.setClientSerialize(PropertiesLoader.getPropertiesStr(CLIENT_SERIALIZE_TYPE));
        return clientConfig;
    }

}

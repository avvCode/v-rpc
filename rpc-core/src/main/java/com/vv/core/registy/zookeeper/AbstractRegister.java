package com.vv.core.registy.zookeeper;


import com.vv.core.common.cache.ServerCache;
import com.vv.core.registy.RegistryService;
import com.vv.core.registy.URL;
import java.util.List;
import com.vv.core.common.ClientCache;

/**
 * 抽象类，给子类可扩展
 */
public abstract class AbstractRegister implements RegistryService {


    @Override
    public void register(URL url) {
        ServerCache.PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegister(URL url) {
        ServerCache.PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
       ClientCache.SUBSCRIBE_SERVICE_LIST.add(url.getServiceName());
    }

    /**
     * 留给子类扩展
     *
     * @param url
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 留给子类扩展
     *
     * @param url
     */
    public abstract void doBeforeSubscribe(URL url);

    /**
     * 留给子类扩展
     *
     * @param serviceName
     * @return
     */
    public abstract List<String> getProviderIps(String serviceName);


    @Override
    public void doUnSubscribe(URL url) {
        ClientCache.SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }
}

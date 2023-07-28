package com.vv.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.vv.core.common.event.VRpcEvent;
import com.vv.core.common.event.VRpcListenerLoader;
import com.vv.core.common.event.VRpcNodeChangeEvent;
import com.vv.core.common.event.VRpcUpdateEvent;
import com.vv.core.common.event.data.URLChangeWrapper;
import com.vv.core.registry.RegistryService;
import com.vv.core.registry.URL;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vv.core.common.cache.ClientCache.CLIENT_CONFIG;
import static com.vv.core.common.cache.ServerCache.IS_STARTED;
import static com.vv.core.common.cache.ServerCache.SERVER_CONFIG;


public class ZookeeperRegister extends AbstractRegister implements RegistryService {


    private AbstractZookeeperClient zkClient;

    private String ROOT = "/v-rpc";

    public AbstractZookeeperClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(AbstractZookeeperClient zkClient) {
        this.zkClient = zkClient;
    }

    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" + url.getParameters().get("host") + ":";
    }

    public ZookeeperRegister() {
        String registryAddr = CLIENT_CONFIG!= null ? CLIENT_CONFIG.getRegisterAddr() : SERVER_CONFIG.getRegisterAddr();
        this.zkClient = new CuratorZookeeperClient(registryAddr);
    }

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }


    @Override
    public List<String> getProviderIps(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        return nodeDataList;
    }

    @Override
    public Map<String, String> getServiceWeightMap(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        Map<String, String> result = new HashMap<>();
        for (String ipAndHost : nodeDataList) {
            String childData = this.zkClient.getNodeData(ROOT + "/" + serviceName + "/provider/" + ipAndHost);
            result.put(ipAndHost, childData);
        }
        return result;
    }

    @Override
    public void register(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildProviderUrlStr(url);
        if (!zkClient.existNode(getProviderPath(url))) {
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        } else {
            zkClient.deleteNode(getProviderPath(url));
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        }
        super.register(url);
    }

    @Override
    public void unRegister(URL url) {
        if (!IS_STARTED) {
            return;
        }
        zkClient.deleteNode(getProviderPath(url));
        super.unRegister(url);
    }

    @Override
    public void subscribe(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildConsumerUrlStr(url);
        if (!zkClient.existNode(getConsumerPath(url))) {
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        } else {
            zkClient.deleteNode(getConsumerPath(url));
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        }
        super.subscribe(url);
    }

    @Override
    public void doAfterSubscribe(URL url) {
        //监听是否有新的服务注册
        String servicePath = url.getParameters().get("servicePath");
        String newServerNodePath = ROOT + "/" + servicePath;
        watchChildNodeData(newServerNodePath);
        String providerIpStrJson = url.getParameters().get("providerIps");
        List<String> providerIpList = JSON.parseObject(providerIpStrJson, List.class);
        for (String providerIp : providerIpList) {
            this.watchNodeDataChange(ROOT + "/" + servicePath + "/" + providerIp);
        }
    }

    /**
     * 订阅服务节点内部的数据变化
     *
     * @param newServerNodePath
     */
    public void watchNodeDataChange(String newServerNodePath) {
        zkClient.watchNodeData(newServerNodePath, new Watcher() {

            @Override
            public void process(WatchedEvent watchedEvent) {
                String path = watchedEvent.getPath();
                String nodeData = zkClient.getNodeData(path);
                nodeData = nodeData.replace(";","/");
                ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(nodeData);
                VRpcEvent iRpcEvent = new VRpcNodeChangeEvent(providerNodeInfo);
                VRpcListenerLoader.sendEvent(iRpcEvent);
                watchNodeDataChange(newServerNodePath);
            }
        });
    }

    public void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                String path = watchedEvent.getPath();
                List<String> childrenDataList = zkClient.getChildrenData(path);
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                urlChangeWrapper.setProviderUrl(childrenDataList);
                urlChangeWrapper.setServiceName(path.split("/")[2]);
                VRpcEvent iRpcEvent = new VRpcUpdateEvent(urlChangeWrapper);
                VRpcListenerLoader.sendEvent(iRpcEvent);
                //收到回调之后在注册一次监听，这样能保证一直都收到消息
                watchChildNodeData(path);
            }
        });
    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public void doUnSubscribe(URL url) {
        this.zkClient.deleteNode(getConsumerPath(url));
        super.doUnSubscribe(url);
    }



}

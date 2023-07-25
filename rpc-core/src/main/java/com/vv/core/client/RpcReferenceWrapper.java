package com.vv.core.client;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-21:38
 */
public class RpcReferenceWrapper<T> {

    private Class<T> aimClass;

    private Map<String,Object> attachments = new ConcurrentHashMap<>();
    public Class<T> getAimClass() {
        return aimClass;
    }

    public void setAimClass(Class<T> aimClass) {
        this.aimClass = aimClass;
    }

    public boolean isAsync(){
        return Boolean.valueOf(String.valueOf(attachments.get("async")));
    }

    public void setAsync(boolean async){
        this.attachments.put("async",async);
    }

    public String getUrl(){
        return String.valueOf(attachments.get("url"));
    }

    public void setUrl(String url){
        attachments.put("url",url);
    }

    public String getServiceToken(){
        return String.valueOf(attachments.get("serviceToken"));
    }

    public void setServiceToken(String serviceToken){
        attachments.put("serviceToken",serviceToken);
    }

    public String getGroup(){
        return String.valueOf(attachments.get("group"));
    }

    public void setGroup(String group){
        attachments.put("group",group);
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }
}
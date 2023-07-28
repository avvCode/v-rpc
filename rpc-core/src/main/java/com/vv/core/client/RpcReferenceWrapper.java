package com.vv.core.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-21:38
 */
public class RpcReferenceWrapper<T> {

    private Class<T> aimClass;

    private Map<String, Object> attachments = new ConcurrentHashMap<>();

    public Class<T> getAimClass() {
        return aimClass;
    }

    public void setAimClass(Class<T> aimClass) {
        this.aimClass = aimClass;
    }
    /**
     * 设置容错策略
     *
     * @param tolerant
     */
    public void setTolerant(String tolerant){
        this.attachments.put("tolerant",tolerant);
    }

    /**
     * 失败重试次数
     */
    public int getRetry(){
        if(attachments.get("retry")==null){
            return 0;
        }else {
            return (int) attachments.get("retry");
        }
    }

    public void setRetry(int retry){
        this.attachments.put("retry",retry);
    }
    public boolean isAsync() {
        Object r = attachments.get("async");
        if (r == null || r.equals(false)) {
            return false;
        }
        return Boolean.valueOf(true);
    }

    public void setAsync(boolean async) {
        this.attachments.put("async", async);
    }

    public String getUrl() {
        return String.valueOf(attachments.get("url"));
    }

    public void setUrl(String url) {
        attachments.put("url", url);
    }

    public void setTimeOut(int timeOut) {
        attachments.put("timeOut", timeOut);
    }

    public String getTimeOUt() {
        return String.valueOf(attachments.get("timeOut"));
    }

    public String getServiceToken() {
        return String.valueOf(attachments.get("serviceToken"));
    }

    public void setServiceToken(String serviceToken) {
        attachments.put("serviceToken", serviceToken);
    }

    public String getGroup() {
        return String.valueOf(attachments.get("group"));
    }

    public void setGroup(String group) {
        attachments.put("group", group);
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }
}
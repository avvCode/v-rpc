package com.vv.core.common;

import lombok.Data;

import java.io.Serializable;

import static com.vv.core.common.constant.RpcConstant.MAGIC_NUMBER;

/**
 * @author vv
 * @Description 自定义协议。可以解决采用别制定协议时出现粘包、半包现象
 * @date 2023/7/20-18:52
 */
@Data
public class RpcProtocol implements Serializable {
    /**
     * 进行序列化与反序列化 需要显示声明序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 魔法数，主要是在做服务通讯的时候定义的一个安全检测，确认当前请求的协议是否合法。
     */
    private short magicNumber = MAGIC_NUMBER;
    /**
     * 协议传输核心数据的长度。这里将长度单独拎出来设置有个好处，当服务端的接收能力有限，可以对该字段进行赋值。
     * 当读取到的网络数据包中的contentLength字段已经超过预期值的话，就不会去读取content字段。
     */
    private int contentLength;

    /**
     * 核心的传输数据，这里核心的传输数据主要是RpcInvocation
     */
    private byte[] content;

    /**
     * 编解码器时用到
     * @param content
     */
    public RpcProtocol(byte[] content) {
        this.content = content;
        this.contentLength = content.length;
    }
}

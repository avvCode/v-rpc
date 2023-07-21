package com.vv.core.server;

import com.alibaba.fastjson.JSON;
import com.vv.core.common.RpcInvocation;
import com.vv.core.common.RpcProtocol;
import com.vv.core.common.cache.ServerCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author vv
 * @Description 服务端接收到请求后的处理方式
 * @date 2023/7/20-19:09
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InvocationTargetException, IllegalAccessException {
        System.out.println(msg);
        RpcProtocol protocol = (RpcProtocol) msg;
        byte[] content = protocol.getContent();
        String json = new String(content,0,protocol.getContentLength()); //RpcInvocationJSON对象
        RpcInvocation rpcInvocation = JSON.parseObject(json, RpcInvocation.class);
        logger.info("收到客户端消息：{}",rpcInvocation);
        //请求的类名
        String serviceName = rpcInvocation.getTargetServiceName();
        //请求的方法名
        String methodName = rpcInvocation.getTargetMethod();
        //请求的参数
        Object[] args = rpcInvocation.getArgs();
        //获取请求的对象
        Object aimObject = ServerCache.PROVIDER_CLASS_MAP.get(serviceName);
        //结果
        Object result = null;
        //获取服务的方法
        Method[] methods = aimObject.getClass().getDeclaredMethods();
        //遍历对象中的方法
        for(Method method : methods){
            if(method.getName().equals(methodName)){
                System.out.println("执行方法");
               if(method.getReturnType().equals(Void.TYPE)){
                   method.invoke(aimObject, args);
               }else {
                   //找到需要执行的方法
                   result = method.invoke(aimObject, args);
               }
               break;
            }
        }
        //封装需要返回客户端的信息
        rpcInvocation.setResponse(result);
        RpcProtocol rpcProtocol = new RpcProtocol(JSON.toJSONString(rpcInvocation).getBytes());
        ctx.writeAndFlush(rpcProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
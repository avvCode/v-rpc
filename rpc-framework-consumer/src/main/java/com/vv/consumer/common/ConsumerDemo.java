package com.vv.consumer.common;



import com.vv.core.client.*;
import com.vv.interfaces.DataService;

import java.util.Scanner;



public class ConsumerDemo {

    public static void doAsyncRef() {
        RpcReferenceFuture rpcReferenceFuture = new RpcReferenceFuture<>();

    }

    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
        rpcReferenceWrapper.setAimClass(DataService.class);
        rpcReferenceWrapper.setGroup("dev");
        rpcReferenceWrapper.setServiceToken("token-a");
        rpcReferenceWrapper.setTimeOut(3000);
        //失败重试次数
        rpcReferenceWrapper.setRetry(0);
        rpcReferenceWrapper.setAsync(false);
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
        //订阅服务
        client.doSubscribeService(DataService.class);

        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        while (true){
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            for(int i=0;i<1;i++){
                String result = dataService.sendData(input);
                System.out.println(result);
            }
            System.out.println("并发结束");
        }
    }
}

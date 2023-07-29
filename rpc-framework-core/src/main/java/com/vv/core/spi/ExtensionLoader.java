package com.vv.core.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vv
 * @Description 自定义SPI加载类
 * @date 2023/7/26-18:46
 */
public class ExtensionLoader {
    public static String EXTENSION_LOADER_DIR_PREFIX = "META-INF/v-rpc/";

    //采用链表的方式让其顺序加载
    public static Map<String, LinkedHashMap<String, Class>> EXTENSION_LOADER_CLASS_CACHE = new ConcurrentHashMap<>();

    public void loadExtension(Class clazz) throws IOException,ClassNotFoundException {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null!");
        }
        String spiFilePath = EXTENSION_LOADER_DIR_PREFIX + clazz.getName();
        ClassLoader classLoader = this.getClass().getClassLoader();
        Enumeration<java.net.URL> enumeration = null;
        enumeration = classLoader.getResources(spiFilePath);
        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();
            InputStreamReader inputStreamReader = null;
            inputStreamReader = new InputStreamReader(url.openStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            LinkedHashMap<String, Class> classMap = new LinkedHashMap<>();
            while ((line = bufferedReader.readLine()) != null) {
                //如果配置中加入了#开头则表示忽略该类无需进行加载
                if (line.startsWith("#")) {
                    continue;
                }
                String[] lineArr = line.split("=");
                String implClassName = lineArr[0];
                String interfaceName = lineArr[1];
                classMap.put(implClassName, Class.forName(interfaceName));
            }
            //只会触发class文件的加载，而不会触发对象的实例化
            if(EXTENSION_LOADER_CLASS_CACHE.containsKey(clazz.getName())){
                //支持开发者自定义配置
                EXTENSION_LOADER_CLASS_CACHE.get(clazz.getName()).putAll(classMap);
            } else {
                EXTENSION_LOADER_CLASS_CACHE.put(clazz.getName(), classMap);
            }
        }
    }

}

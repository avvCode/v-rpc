package com.vv.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.vv.core.serialize.SerializeFactory;

/**
 * @author vv
 * @Description Fastjson序列化方式
 * @date 2023/7/24-19:45
 */
public class FastJsonSerializeFactory implements SerializeFactory {
    @Override
    public <T> byte[] serialize(T t) {
        String jsonStr = JSON.toJSONString(t);
        return jsonStr.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }
}

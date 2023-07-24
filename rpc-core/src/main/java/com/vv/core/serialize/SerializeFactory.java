package com.vv.core.serialize;

/**
 * @author vv
 * @Description 序列化工厂
 * @date 2023/7/24-19:34
 */
public interface SerializeFactory {
    /**
     * 序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}

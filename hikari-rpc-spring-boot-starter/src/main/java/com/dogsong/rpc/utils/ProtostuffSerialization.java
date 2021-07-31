package com.dogsong.rpc.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化工具类
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/7/29
 */
public class ProtostuffSerialization {

    private static final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static final Objenesis objenesis = new ObjenesisStd();

    public ProtostuffSerialization() {
    }


    /**
     * 序列化 (对象 -> 字节数组)
     *
     * @param obj 对象
     * @return byte[]
     * @since 2021/7/29
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        // 获得对象的类
        Class<T> cls = (Class<T>) obj.getClass();
        // 使用LinkedBuffer分配一块默认大小的buffer空间
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            // 通过对象的类构建对应的schema
            Schema<T> schema = getSchema(cls);
            // 使用给定的schema将对象序列化为一个byte数组，并返回
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }


    /**
     * 反序列化 (字节数组 -> 对象)
     *      <p>
     *          如果一个类没有参数为空的构造方法时候，那么直接调用newInstance方法试图得到一个实例对象的时候是会抛出异常的
     *          通过ObjenesisStd可以完美的避开这个问题
     *      </p>
     *
     * @param data 字节数组
     * @param cls class
     * @return T
     * @since 2021/7/29
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            // 实例化一个类对象
            T message = objenesis.newInstance(cls);
            // 通过对象的类构建对应的schema
            Schema<T> schema = getSchema(cls);
            // 使用给定的schema将byte数组和对象合并
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 缓存中获取 scheme，如果没有则创建，并缓存
     *
     * @param cls class
     * @return {@link Schema<T>}
     * @since 2021/7/29
     */
    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            // 构建schema的过程可能会比较耗时，因此希望使用过的类对应的schema能被缓存起来
            cachedSchema.put(cls, schema);
        }
        return schema;
    }

}

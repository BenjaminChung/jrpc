package util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化工具类 (基于Protostuff实现)
 * Created by benjaminchung on 2017/4/3.
 */
public class SerializationUtil {

    private static Map<Class<?>,Schema> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    /**
     * 序列化对象 (字节数组->对象)
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj){
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try{
            Schema<T> tSchema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj,tSchema,buffer);
        }catch (Exception ex){
            throw new IllegalStateException(ex.getMessage(),ex);
        }finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化对象
     * @param data
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data,Class<T> cls){
        try{
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data,message,schema);
            return message;
        }catch (Exception ex){
            throw new IllegalStateException(ex.getMessage(),ex);
        }
    }

    public static <T> Schema<T> getSchema(Class<T> cls){
        Schema<T> schema = (Schema<T>)cachedSchema.get(cls);
        if(schema == null){
            schema = RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls,schema);
        }
        return schema;
    }
}

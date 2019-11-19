package com.redhat.jdg;

import com.redhat.jdg.partial.Key;
import com.redhat.jdg.partial.KeyMarshaller;
import com.redhat.jdg.partial.Value;
import com.redhat.jdg.partial.ValueMarshaller;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;

import java.util.HashMap;
import java.util.Map;

public class PartialExample {

    public static void main(String... args) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder
                .addServer().host("127.0.0.1").port(ConfigurationProperties.DEFAULT_HOTROD_PORT)
                .marshaller(new ProtoStreamMarshaller());
        // Connect to the server
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());

        SerializationContext serializationContext =
                ProtoStreamMarshaller.getSerializationContext(cacheManager);
        serializationContext.registerProtoFiles(FileDescriptorSource.fromResources("/partial.proto"));
        serializationContext.registerMarshaller(new KeyMarshaller());
        serializationContext.registerMarshaller(new ValueMarshaller());

        // Obtain the remote cache
        RemoteCache<Key, Value> cache = cacheManager.getCache("partial");
        /// Store a value
        cache.put(new Key(1, "pk1", "descr1", true), new Value(1, "val1", "pl1"));
        cache.put(new Key(2, "pk2", "descr2", true), new Value(2, "val2", "pl2"));
        cache.put(new Key(3, "pk3", "descr3", true), new Value(3, "val3", "pl3"));
        cache.put(new Key(4, "pk4", "descr4", true), new Value(4, "val4", "pl4"));

        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        String result = cache.execute("remove-task", params);
        System.out.println("result: " + result);

        // Retrieve the value and print it out
        System.out.printf("keys = %s\n", cache.keySet());
        System.out.printf("value = %s\n", cache.values());
        // Stop the cache manager and release all resources
        cacheManager.stop();
    }

}

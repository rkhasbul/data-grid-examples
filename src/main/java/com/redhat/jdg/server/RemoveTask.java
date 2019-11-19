package com.redhat.jdg.server;

import com.redhat.jdg.partial.Key;
import com.redhat.jdg.partial.KeyMarshaller;
import com.redhat.jdg.partial.ValueMarshaller;
import org.infinispan.Cache;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.ProtobufUtil;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RemoveTask implements ServerTask<String> {

    private TaskContext taskContext;
    private SerializationContext serializationContext;

    @Override
    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
        this.serializationContext = ProtobufUtil.newSerializationContext();
    }

    @Override
    public String call() throws Exception {
        if (!taskContext.getParameters().isPresent()) {
            throw new Exception("Parameters are need to be present");
        }

        serializationContext.registerProtoFiles(FileDescriptorSource.fromResources(
                getClass().getClassLoader(), "/partial.proto"));
        serializationContext.registerMarshaller(new KeyMarshaller());
        serializationContext.registerMarshaller(new ValueMarshaller());

        long id = (Long) taskContext.getParameters().get().get("id");
        Key partial1 = new Key(id, null, null, false);
        Key partial2 = new Key(id+1, null, null, false);

        Cache<?, ?> cache = taskContext.getCacheManager().getCache("partial");

        TreeSet<Key> keys = new TreeSet<>(Comparator.comparingLong(Key::getId));
        keys.addAll(cache.keySet().stream().map(k -> {
            try {
                return (Key) ProtobufUtil.fromWrappedByteArray(serializationContext, (byte[]) k);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet()));

        Set<Key> toRemove = keys.subSet(partial1, true, partial2, false);
        toRemove.forEach(k -> {
            try {
                byte[] key = ProtobufUtil.toWrappedByteArray(serializationContext, k);
                System.out.printf("removing value: %s by key: %s%n", cache.remove(key), k);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return "done";
    }

    @Override
    public String getName() {
        return "remove-task";
    }
}

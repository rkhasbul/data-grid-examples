package com.redhat.jdg.partial;

import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class KeyMarshaller implements MessageMarshaller<Key> {

    @Override
    public Key readFrom(ProtoStreamReader reader) throws IOException {
        long id = reader.readLong("id");
        String name = reader.readString("name");
        String description = reader.readString("description");
        boolean available = reader.readBoolean("available");
        return new Key(id, name, description, available);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Key key) throws IOException {
        writer.writeLong("id", key.getId());
        writer.writeString("name", key.getName());
        writer.writeString("description", key.getDescription());
        writer.writeBoolean("available", key.isAvailable());
    }

    @Override
    public Class<? extends Key> getJavaClass() {
        return Key.class;
    }

    @Override
    public String getTypeName() {
        return "partial.Key";
    }
}

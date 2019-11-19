package com.redhat.jdg.partial;

import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class ValueMarshaller implements MessageMarshaller<Value> {

    @Override
    public Value readFrom(ProtoStreamReader reader) throws IOException {
        long id = reader.readLong("id");
        String value = reader.readString("value");
        String payload = reader.readString("payload");
        return new Value(id, value, payload);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Value value) throws IOException {
        writer.writeLong("id", value.getId());
        writer.writeString("value", value.getValue());
        writer.writeString("payload", value.getPayload());
    }

    @Override
    public Class<? extends Value> getJavaClass() {
        return Value.class;
    }

    @Override
    public String getTypeName() {
        return "partial.Value";
    }
}

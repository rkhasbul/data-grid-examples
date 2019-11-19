package com.redhat.jdg.partial;

import java.io.Serializable;

public class Value implements Serializable {

    private long id;
    private String value;
    private String payload;

    public Value(long id, String value, String payload) {
        this.id = id;
        this.value = value;
        this.payload = payload;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }

}

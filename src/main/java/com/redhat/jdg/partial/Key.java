package com.redhat.jdg.partial;

import java.io.Serializable;
import java.util.Objects;

public final class Key implements Serializable {

    private final long id;
    private final String name;
    private final String description;
    private final boolean available;

    public Key(long id, String name, String description, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return id == key.id &&
                available == key.available &&
                name.equals(key.name) &&
                description.equals(key.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available);
    }

    @Override
    public String toString() {
        return "Key{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }

}

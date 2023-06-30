package ru.itmo.web.hw4.model;

import java.util.Objects;

public class User {
    private final long id;
    private final String handle;
    private final String name;

    public User(long id, String handle, String name) {
        this.id = id;
        this.handle = handle;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return (id == user.id) && (handle.equals(user.handle)) && (name.equals(user.name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, handle, name);
    }
}

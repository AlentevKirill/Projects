package ru.itmo.wp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Tag {
    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 60)
    @Pattern(regexp = "#[a-z\\s]+", message = "Tag name must contain lower latin letters")
    private String name;

    public Tag() {
    }

    public Tag(@NotNull String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

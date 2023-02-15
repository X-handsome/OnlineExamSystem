package com.xue.zxks.models;

import java.util.Objects;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Administrator extends User {

    private String email;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (
            o == null || Hibernate.getClass(this) != Hibernate.getClass(o)
        ) return false;
        Administrator that = (Administrator) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }
}

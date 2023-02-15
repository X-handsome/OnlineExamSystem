package com.xue.zxks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
public class Teacher extends User {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long universityId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long academyId;

    private String email;
    private String number;
    private boolean audit = false;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    @ToString.Exclude
    private Set<Examination> examinations;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Set<Group> groups;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    @ToString.Exclude
    private Set<Paper> papers;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    @ToString.Exclude
    private Set<Question> questions;

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
        Teacher teacher = (Teacher) o;
        return getId() != null && Objects.equals(getId(), teacher.getId());
    }
}

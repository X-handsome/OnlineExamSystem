package com.xue.zxks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
public class Student extends User {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long universityId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long academyId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long majorId;

    private Integer enrollYear;
    private String number;

    @ManyToMany(mappedBy = "students")
    @ToString.Exclude
    @JsonIgnore
    private Set<Group> groups;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    @ToString.Exclude
    private Set<ExaminationExtend> examinations;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    @ToString.Exclude
    private Set<Grade> grades;

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
        Student student = (Student) o;
        return getId() != null && Objects.equals(getId(), student.getId());
    }
}

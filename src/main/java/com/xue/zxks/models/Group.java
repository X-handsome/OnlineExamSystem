package com.xue.zxks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "t_group")
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Group {

    @Id
    @GenericGenerator(
        name = "snowflake",
        strategy = "com.zcy.zxks.utils.SnowflakeServer",
        parameters = {
            @Parameter(name = "workerId", value = "1"),
            @Parameter(name = "datacenterId", value = "1"),
            @Parameter(name = "sequence", value = "96644898"),
        }
    )
    @GeneratedValue(generator = "snowflake")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    private boolean available = true;

    @ManyToOne
    @JsonIgnore
    private Teacher teacher;

    @ManyToMany
    @ToString.Exclude
    @JsonIgnoreProperties(value = { "groups" })
    @JoinTable(
        name = "t_group_student_list",
        joinColumns = @JoinColumn(name = "group_list_id"),
        inverseJoinColumns = @JoinColumn(name = "student_list_id")
    )
    private Set<Student> students;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    @ToString.Exclude
    private Set<Examination> examinations;

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
        Group group = (Group) o;
        return id != null && Objects.equals(id, group.id);
    }
}

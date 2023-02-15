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
@Entity
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Examination {

    @Id
    @GenericGenerator(
        name = "snowflake",
        strategy = "com.zcy.zxks.utils.SnowflakeServer",
        parameters = {
            @Parameter(name = "workerId", value = "1"),
            @Parameter(name = "datacenterId", value = "1"),
            @Parameter(name = "sequence", value = "526744"),
        }
    )
    @GeneratedValue(generator = "snowflake")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JsonIgnore
    private Teacher teacher;

    private boolean uncoiling;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "examinations", "teacher" })
    private Paper paper;

    @ManyToOne
    @JsonIgnoreProperties(value = { "examinations", "teacher" })
    private Group group;

    private boolean available = true;
    private int duration;
    private boolean visible;

    @OneToMany(mappedBy = "examination")
    @ToString.Exclude
    @JsonIgnoreProperties(value = { "examination", "student" })
    private Set<ExaminationExtend> extend;

    @OneToMany(mappedBy = "examination")
    @ToString.Exclude
    @JsonIgnoreProperties(value = { "examination" })
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
        Examination that = (Examination) o;
        return id != null && Objects.equals(id, that.id);
    }
}

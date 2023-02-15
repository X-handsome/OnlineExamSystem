package com.xue.zxks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Grade {

    @Id
    @GenericGenerator(
        name = "snowflake",
        strategy = "com.zcy.zxks.utils.SnowflakeServer",
        parameters = {
            @Parameter(name = "workerId", value = "1"),
            @Parameter(name = "datacenterId", value = "1"),
            @Parameter(name = "sequence", value = "2436454"),
        }
    )
    @GeneratedValue(generator = "snowflake")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grades" })
    private Student student;

    private int score;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    @ManyToOne
    @JsonIgnore
    private Examination examination;

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
        Grade grade = (Grade) o;
        return id != null && Objects.equals(id, grade.id);
    }
}

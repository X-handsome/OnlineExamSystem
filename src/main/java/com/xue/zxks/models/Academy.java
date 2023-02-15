package com.xue.zxks.models;

import javax.persistence.Entity;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Academy extends Organization {}

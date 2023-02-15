package com.xue.zxks.records;

public record QuestionRecord(
    String title,
    String type,
    String answer,
    boolean openness
) {}

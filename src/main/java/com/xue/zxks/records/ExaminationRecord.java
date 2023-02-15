package com.xue.zxks.records;

import java.time.LocalDateTime;

public record ExaminationRecord(
    String title,
    LocalDateTime beginTime,
    LocalDateTime endTime,
    boolean uncoiling,
    Long paperId,
    Long groupId,
    int duration,
    boolean visible
) {}

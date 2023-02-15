package com.xue.zxks.records;

import java.util.List;

public record PaperRecord(
    String title,
    boolean openness,
    List<PaperExtendRecord> paperExtendRecordList,
    int single,
    int multiple,
    int judgment
) {}

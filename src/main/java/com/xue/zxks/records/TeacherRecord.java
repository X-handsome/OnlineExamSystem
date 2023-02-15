package com.xue.zxks.records;

public record TeacherRecord(
    String email,
    String password,
    String number,
    Long universityId,
    Long academyId,
    String name,
    String captcha
) {}

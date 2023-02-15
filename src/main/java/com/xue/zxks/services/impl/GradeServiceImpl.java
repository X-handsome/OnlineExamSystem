package com.xue.zxks.services.impl;

import com.xue.zxks.models.Examination;
import com.xue.zxks.models.Grade;
import com.xue.zxks.models.Group;
import com.xue.zxks.models.Paper;
import com.xue.zxks.models.PaperExtend;
import com.xue.zxks.models.Student;
import com.xue.zxks.repositories.GradeRepository;
import com.xue.zxks.services.ExaminationExtendService;
import com.xue.zxks.services.GradeService;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GradeServiceImpl implements GradeService {

    @Resource
    private GradeRepository repository;

    @Resource
    private ExaminationExtendService examinationExtendService;

    @Override
    @Async
    public void calculate(Examination examination) {
        Group group = examination.getGroup();
        Set<Student> students = group.getStudents();
        students.forEach(student -> calculateByStudent(student, examination));
    }

    @Async
    void calculateByStudent(Student student, Examination examination) {
        log.info("计算成绩 - id: {}", student.getId());
        Paper paper = examination.getPaper();
        Set<PaperExtend> paperExtends = paper.getExtend();
        AtomicInteger score = new AtomicInteger();
        examinationExtendService
            .getByStudentAndExamination(student, examination)
            .ifPresent(examinationExtends -> {
                examinationExtends.forEach(examinationExtend ->
                    paperExtends
                        .stream()
                        .filter(paperExtend ->
                            paperExtend.getNumber() ==
                            examinationExtend.getNumber()
                        )
                        .forEach(paperExtend -> {
                            if (
                                paperExtend
                                    .getQuestion()
                                    .getAnswer()
                                    .equals(examinationExtend.getAnswer())
                            ) {
                                score.addAndGet(paperExtend.getScore());
                            }
                        })
                );
            });
        Grade grade = new Grade();
        grade
            .setStudent(student)
            .setScore(score.get())
            .setExamination(examination);
        repository.save(grade);
    }
}

package me.seokju.secondjavafinal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seokju.secondjavafinal.entity.timetable.TimeTable;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.lecture.LectureTime;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.entity.timetable.TimeTableStatus;
import me.seokju.secondjavafinal.repository.CartRepository;
import me.seokju.secondjavafinal.repository.LectureRepository;
import me.seokju.secondjavafinal.repository.MemberRepository;
import me.seokju.secondjavafinal.repository.TimeTableRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static me.seokju.secondjavafinal.entity.member.MemberRole.PROFESSOR;
import static me.seokju.secondjavafinal.entity.member.MemberRole.STUDENT;

@Slf4j
@RequiredArgsConstructor
@Profile("dev")
@Component
public class DataInit {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final CartRepository cartRepository;
    private final TimeTableRepository timeTableRepository;

    @PostConstruct
    void init() {

        // 회원 데이터 구성은 최소 10개 이상 구성
        List<Member> studentList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String email = "student" + i + "@test.com";
            String name = "student" + i;
            String classNumber = "studentClassNumber" + i;
            Member member = new Member(email, "password", name, STUDENT, classNumber);
            studentList.add(member);
        }
        memberRepository.saveAll(studentList);

        // 교수 5명
        List<Member> professorList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String email = "professor" + i + "@test.com";
            String name = "professor" + i;
            String classNumber = "professorClassNumber" + i;
            Member member = new Member(email, "password", name, PROFESSOR, classNumber);
            professorList.add(member);
        }
        memberRepository.saveAll(professorList);

        // 강의 당 정원은 5명, 최대 학점은 3점으로 한정하며 데이터 구성은 최소 10개 이상 구성
        List<Lecture> lectureList = new ArrayList<>();
        Lecture lecture1 = new Lecture("강의1", new LectureTime(Calendar.MONDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)), 5, 3, 3);
        lecture1.assignProfessor(professorList.get(0));

        Lecture lecture2 = new Lecture("강의2", new LectureTime(Calendar.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)), 5, 3, 3);
        lecture2.assignProfessor(professorList.get(1));

        Lecture lecture3 = new Lecture("강의3", new LectureTime(Calendar.TUESDAY, LocalTime.of(16, 0), LocalTime.of(18, 0)), 5, 2, 3);
        lecture3.assignProfessor(professorList.get(2));

        Lecture lecture4 = new Lecture("강의4", new LectureTime(Calendar.TUESDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)), 5, 1, 3);
        lecture4.assignProfessor(professorList.get(3));

        Lecture lecture5 = new Lecture("강의5", new LectureTime(Calendar.WEDNESDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)), 5, 5, 3);
        lecture5.assignProfessor(professorList.get(4));

        Lecture lecture6 = new Lecture("강의6", new LectureTime(Calendar.WEDNESDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)), 5, 0, 3);
        lecture6.assignProfessor(professorList.get(0));

        Lecture lecture7 = new Lecture("강의7", new LectureTime(Calendar.THURSDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)), 5, 0, 3);
        lecture7.assignProfessor(professorList.get(1));

        Lecture lecture8 = new Lecture("강의8", new LectureTime(Calendar.THURSDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)), 5, 0, 3);
        lecture8.assignProfessor(professorList.get(2));

        Lecture lecture9 = new Lecture("강의9", new LectureTime(Calendar.FRIDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)), 5, 0, 3);
        lecture9.assignProfessor(professorList.get(3));

        Lecture lecture10 = new Lecture("강의10", new LectureTime(Calendar.FRIDAY, LocalTime.of(12, 0), LocalTime.of(14, 0)), 5, 5, 3);
        lecture10.assignProfessor(professorList.get(4));

        Lecture lecture11 = new Lecture("월요일 겹치는 강의", new LectureTime(Calendar.MONDAY, LocalTime.of(13, 0), LocalTime.of(14, 0)), 5, 0, 3);
        lecture11.assignProfessor(professorList.get(4));

        Lecture lecture12 = new Lecture("월요일 겹치는 강의2", new LectureTime(Calendar.MONDAY, LocalTime.of(10, 0), LocalTime.of(14, 0)), 5, 0, 3);
        lecture12.assignProfessor(professorList.get(4));

        Lecture lecture13 = new Lecture("월요일 안 겹치는 강의", new LectureTime(Calendar.MONDAY, LocalTime.of(18, 0), LocalTime.of(20, 0)), 5, 0, 3);
        lecture13.assignProfessor(professorList.get(4));

        lectureList.add(lecture1);
        lectureList.add(lecture2);
        lectureList.add(lecture3);
        lectureList.add(lecture4);
        lectureList.add(lecture5);
        lectureList.add(lecture6);
        lectureList.add(lecture7);
        lectureList.add(lecture8);
        lectureList.add(lecture9);
        lectureList.add(lecture10);
        lectureList.add(lecture11);
        lectureList.add(lecture12);
        lectureList.add(lecture13);

        lectureRepository.saveAll(lectureList);


        TimeTable student1timeTable1 = new TimeTable(studentList.get(0), lecture1, lecture1.getCredit(), TimeTableStatus.DONE);
        TimeTable student1timeTable2 = new TimeTable(studentList.get(0), lecture2, lecture2.getCredit(), TimeTableStatus.DONE);
        TimeTable student1timeTable3 = new TimeTable(studentList.get(0), lecture3, lecture3.getCredit(), TimeTableStatus.DONE);

        timeTableRepository.save(student1timeTable1);
        timeTableRepository.save(student1timeTable2);
        timeTableRepository.save(student1timeTable3);

        TimeTable student2TimeTable1 = new TimeTable(studentList.get(1), lecture1, lecture1.getCredit(), TimeTableStatus.DONE);
        TimeTable student3TimeTable1 = new TimeTable(studentList.get(2), lecture1, lecture1.getCredit(), TimeTableStatus.DONE);

        timeTableRepository.save(student2TimeTable1);
        timeTableRepository.save(student3TimeTable1);
    }
}

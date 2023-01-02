package me.seokju.secondjavafinal.repository;

import me.seokju.secondjavafinal.entity.timetable.TimeTable;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    Optional<TimeTable> findByMemberAndLecture(Member member, Lecture lecture);

    List<TimeTable> findAllByMember(Member member);

    List<TimeTable> findAllByLecture(Lecture lecture);

    List<TimeTable> findAllByLectureIn(List<Lecture> lectureList);

    boolean existsByMemberAndLecture(Member member, Lecture lecture);
}

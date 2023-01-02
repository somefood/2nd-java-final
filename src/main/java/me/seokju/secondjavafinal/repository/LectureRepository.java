package me.seokju.secondjavafinal.repository;

import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findAllByProfessor(Member professor);

    List<Lecture> findAllByIdIn(List<Long> lectureIdList);
}

package me.seokju.secondjavafinal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seokju.secondjavafinal.dto.lecture.LectureResponseDto;
import me.seokju.secondjavafinal.entity.timetable.TimeTable;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.entity.member.MemberRole;
import me.seokju.secondjavafinal.repository.LectureRepository;
import me.seokju.secondjavafinal.repository.MemberRepository;
import me.seokju.secondjavafinal.repository.TimeTableRepository;
import me.seokju.secondjavafinal.service.ProfessorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    //     자신의 강의 신청 현황
    @GetMapping("/{professorId}")
    public List<LectureResponseDto> getCurrentApplyList(@PathVariable Long professorId) {
        return professorService.getCurrentApplyList(professorId);
    }
}

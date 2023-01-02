package me.seokju.secondjavafinal.service;

import me.seokju.secondjavafinal.dto.lecture.LectureResponseDto;
import me.seokju.secondjavafinal.dto.member.MemberResponseDto;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.entity.member.MemberRole;
import me.seokju.secondjavafinal.entity.timetable.TimeTable;
import me.seokju.secondjavafinal.repository.LectureRepository;
import me.seokju.secondjavafinal.repository.MemberRepository;
import me.seokju.secondjavafinal.repository.TimeTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProfessorService {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final TimeTableRepository timeTableRepository;

    public ProfessorService(MemberRepository memberRepository, LectureRepository lectureRepository, TimeTableRepository timeTableRepository) {
        this.memberRepository = memberRepository;
        this.lectureRepository = lectureRepository;
        this.timeTableRepository = timeTableRepository;
    }

    public List<LectureResponseDto> getCurrentApplyList(Long professorId) {

        Member findMember = memberRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        if (findMember.getRole() != MemberRole.PROFESSOR) {
            throw new RuntimeException("교수가 아닙니다.");
        }

        List<Lecture> professorLectureList = lectureRepository.findAllByProfessor(findMember);

        // 자신의 수업에서 학생들 뽑기

        return professorLectureList.stream()
                .map(lecture -> {
                    List<TimeTable> timeTableList = timeTableRepository.findAllByLecture(lecture);
                    List<MemberResponseDto> memberList = new ArrayList<>();
                    for (TimeTable timeTable : timeTableList) {
                        Member member = timeTable.getMember();
                        memberList.add(new MemberResponseDto(member.getId(), member.getName(), member.getClassNumber()));
                    }
                    return new LectureResponseDto(lecture.getId(), lecture.getName(), lecture.getLimitPeople(), lecture.getCurrentPeople(), memberList);
                })
                .collect(Collectors.toList());
    }
}

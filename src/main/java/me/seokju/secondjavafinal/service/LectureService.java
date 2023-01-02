package me.seokju.secondjavafinal.service;

import lombok.extern.slf4j.Slf4j;
import me.seokju.secondjavafinal.dto.lecture.LectureResponseDto;
import me.seokju.secondjavafinal.entity.timetable.TimeTable;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.entity.timetable.TimeTableStatus;
import me.seokju.secondjavafinal.repository.LectureRepository;
import me.seokju.secondjavafinal.repository.MemberRepository;
import me.seokju.secondjavafinal.repository.TimeTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class LectureService {

    private static final int MAX_CREDIT = 21;
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final TimeTableRepository timeTableRepository;

    public LectureService(MemberRepository memberRepository, LectureRepository lectureRepository, TimeTableRepository timeTableRepository) {
        this.memberRepository = memberRepository;
        this.lectureRepository = lectureRepository;
        this.timeTableRepository = timeTableRepository;
    }

    public List<LectureResponseDto> getLectureList() {

        List<Lecture> lectureList = lectureRepository.findAll();

        return lectureList.stream()
                .map(l -> new LectureResponseDto(l.getId(), l.getName(), l.getLimitPeople(), l.getCurrentPeople()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LectureResponseDto> getMyLectureList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        List<TimeTable> timeTableList = timeTableRepository.findAllByMember(member);
        return timeTableList.stream()
                .map(timeTable -> {
                    Lecture lecture = timeTable.getLecture();
                    return new LectureResponseDto(lecture.getId(), lecture.getName(), lecture.getLimitPeople(), lecture.getCurrentPeople(), timeTable.getStatus());
                })
                .collect(Collectors.toList());
    }

    // 본 수강 신청
    // 회원은 최대 21학점까지 강의를 신청할 수 있습니다.
    // 강의 신청 기간에 정원이내의 인원은 선착순으로 신청이 가능합니다.
    // 신청한 강의는 요일/시간이 겹치도록 신청할 수 없습니다. (장바구니로 기 신청된 강의가 있을 경우 모두 포함)
    public Long applyLecture(Long memberId, Long lectureId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 수업입니다."));

        // 이미 존재하는 수업은 빼기
        if (timeTableRepository.existsByMemberAndLecture(member, findLecture)) {
            throw new RuntimeException("이미 신청한 수업입니다.");
        }

        // 최대 학점 체크
        List<TimeTable> timeTableList = timeTableRepository.findAllByMember(member);
        int currentTotalCredit = timeTableList.stream().mapToInt(TimeTable::getCredit).sum();

        if (findLecture.getCredit() + currentTotalCredit > MAX_CREDIT) {
            throw new RuntimeException("초과 학점" + MAX_CREDIT + "를 넘길 수 없습니다. 현재: " + currentTotalCredit);
        }

        // 신청한 강의는 요일/시간이 겹치도록 신청할 수 없습니다. (장바구니로 기 신청된 강의가 있을 경우 모두 포함)
        List<Long> lectureLongList = timeTableList.stream()
                .mapToLong(t -> t.getLecture().getId())
                .boxed()
                .collect(Collectors.toList());
        List<Lecture> lectureList = lectureRepository.findAllByIdIn(lectureLongList);

        boolean isOverlapped = checkOverlapLecture(findLecture, lectureList);

        if(isOverlapped) {
            throw new RuntimeException("수업이 겹칩니다.");
        }

        // 강의 신청 기간에 정원이내의 인원은 선착순으로 신청이 가능합니다.
        int currentPeople = findLecture.getCurrentPeople();
        int limitPeople = findLecture.getLimitPeople();
        if (currentPeople >= limitPeople) {
            log.info("정원 초과 (" + currentPeople + "/" + limitPeople + ") 찜으로 추가");
            TimeTable timeTable = new TimeTable(member, findLecture, findLecture.getCredit(), TimeTableStatus.BOOKMARK);
            timeTableRepository.save(timeTable);
            return timeTable.getId();
        }

        TimeTable timeTable = new TimeTable(member, findLecture, findLecture.getCredit(), TimeTableStatus.DONE);
        timeTableRepository.save(timeTable);
        return timeTable.getId();
    }

    public void reApplyLecture(Long memberId, Long lectureId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 수업입니다."));

        TimeTable findTimeTable = timeTableRepository.findByMemberAndLecture(findMember, findLecture)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 시간표입니다."));

        int currentPeople = findLecture.getCurrentPeople();
        int limitPeople = findLecture.getLimitPeople();
        if (currentPeople >= limitPeople) {
            log.info("정원 초과 (" + currentPeople + "/" + limitPeople + ") 찜으로 추가");
            throw new RuntimeException("정원 초과, 그대로 진행");
        }
        findTimeTable.changeStatusToApply(findLecture);
    }

    public void deleteLecture(Long memberId, Long lectureId) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 수업입니다."));

        // 이미 존재하는 수업은 빼기
        if (!timeTableRepository.existsByMemberAndLecture(findMember, findLecture)) {
            throw new RuntimeException("신청한 적 없는 수업입니다.");
        }

        TimeTable findTimeTable = timeTableRepository.findByMemberAndLecture(findMember, findLecture)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 신청입니다."));


        findLecture.decreaseCurrentPeople();
        timeTableRepository.delete(findTimeTable);
    }

    public static boolean checkOverlapLecture(Lecture findLecture, List<Lecture> lectureList) {
        return lectureList.stream()
                // 같은 요일것들만 갖고와서
                .filter(lecture -> lecture.getLectureTime().getDayOfWeek() == findLecture.getLectureTime().getDayOfWeek())
                // 하나라도 매치하는게 있으면 true
                .anyMatch(l -> {
                    LocalTime wishLectureStartTime = findLecture.getLectureTime().getStartTime();
                    LocalTime wishLectureEndTime = findLecture.getLectureTime().getEndTime();
                    LocalTime lectureStartTime = l.getLectureTime().getStartTime();
                    LocalTime lectureEndTime = l.getLectureTime().getEndTime();
                    return (wishLectureStartTime.plusSeconds(1).isAfter(lectureStartTime) && wishLectureStartTime.isBefore(lectureEndTime))
                            || (wishLectureEndTime.isAfter(lectureStartTime) && wishLectureEndTime.minusSeconds(1).isBefore(lectureEndTime));
                });
    }
}

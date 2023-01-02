package me.seokju.secondjavafinal.controller;

import me.seokju.secondjavafinal.dto.lecture.LectureRequestDto;
import me.seokju.secondjavafinal.dto.lecture.LectureResponseDto;
import me.seokju.secondjavafinal.service.LectureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    // 전체 수업 리스트 보기
    @GetMapping
    public ResponseEntity<List<LectureResponseDto>> getLectureList() {
        checkIfLectureAvailableOrElseThrow();
        List<LectureResponseDto> result = lectureService.getLectureList();
        return ResponseEntity.ok(result);
    }

    // 내가 신청한 수업들 보기
    @GetMapping("/me")
    public ResponseEntity<List<LectureResponseDto>> getMyLectureList(@RequestBody LectureRequestDto requestDto) {
        checkIfLectureAvailableOrElseThrow();
        Long memberId = requestDto.getMemberId();
        List<LectureResponseDto> result = lectureService.getMyLectureList(memberId);
        return ResponseEntity.ok(result);
    }

    // 본 수강 신청
    // 회원은 최대 21학점까지 강의를 신청할 수 있습니다.
    // 강의 신청 기간에 정원이내의 인원은 선착순으로 신청이 가능합니다.
    // 신청한 강의는 요일/시간이 겹치도록 신청할 수 없습니다. (장바구니로 기 신청된 강의가 있을 경우 모두 포함)
    @PostMapping
    public ResponseEntity<Long> applyLecture(@RequestBody LectureRequestDto requestDto) {
        checkIfLectureAvailableOrElseThrow();
        Long memberId = requestDto.getMemberId();
        Long lectureId = requestDto.getLectureId();

        Long result = lectureService.applyLecture(memberId, lectureId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/re-apply")
    public ResponseEntity<Void> reApplyLecture(@RequestBody LectureRequestDto requestDto) {
        checkIfLectureAvailableOrElseThrow();
        Long memberId = requestDto.getMemberId();
        Long lectureId = requestDto.getLectureId();

        lectureService.reApplyLecture(memberId, lectureId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public void deleteLecture(@RequestBody LectureRequestDto requestDto) {
        checkIfLectureAvailableOrElseThrow();
        Long memberId = requestDto.getMemberId();
        Long lectureId = requestDto.getLectureId();
        lectureService.deleteLecture(memberId, lectureId);
    }

    // 강의 신청 가능 기간은 2023년 1월 11일 오후 2시부터 오후 6시까지로 설정합니다.
    private void checkIfLectureAvailableOrElseThrow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 11, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 11, 18, 0, 0);
        if (now.isAfter(startDate) && now.isBefore(endDate)) {
            return;
        }
        throw new RuntimeException("강의신청 기능은 2023년 1월 9일 오후 2시부터 1월 10일 오후 18시에만 사용가능합니다.");
    }
}

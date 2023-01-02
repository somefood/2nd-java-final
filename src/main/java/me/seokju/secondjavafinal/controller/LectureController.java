package me.seokju.secondjavafinal.controller;

import me.seokju.secondjavafinal.dto.lecture.LectureRequestDto;
import me.seokju.secondjavafinal.dto.lecture.LectureResponseDto;
import me.seokju.secondjavafinal.service.LectureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<LectureResponseDto> result = lectureService.getLectureList();
        return ResponseEntity.ok(result);
    }

    // 내가 신청한 수업들 보기
    @GetMapping("/me")
    public ResponseEntity<List<LectureResponseDto>> getMyLectureList(@RequestBody LectureRequestDto requestDto) {
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
        Long memberId = requestDto.getMemberId();
        Long lectureId = requestDto.getLectureId();

        Long result = lectureService.applyLecture(memberId, lectureId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/re-apply")
    public ResponseEntity<Void> reApplyLecture(@RequestBody LectureRequestDto requestDto) {
        Long memberId = requestDto.getMemberId();
        Long lectureId = requestDto.getLectureId();

        lectureService.reApplyLecture(memberId, lectureId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public void deleteLecture(@RequestBody LectureRequestDto requestDto) {
        Long memberId = requestDto.getMemberId();
        Long lectureId = requestDto.getLectureId();
        lectureService.deleteLecture(memberId, lectureId);
    }
}

package me.seokju.secondjavafinal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seokju.secondjavafinal.dto.cart.CartRequestDto;
import me.seokju.secondjavafinal.dto.cart.CartResponseDto;
import me.seokju.secondjavafinal.dto.lecture.LectureRequestDto;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.cart.Cart;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.repository.CartRepository;
import me.seokju.secondjavafinal.repository.LectureRepository;
import me.seokju.secondjavafinal.repository.MemberRepository;
import me.seokju.secondjavafinal.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    // 내 장바구니 조회
    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getMyCart(@RequestBody CartRequestDto cartRequestDto) {
        checkIfCartAvailableOrElseThrow();
        Long memberId = cartRequestDto.getMemberId();
        List<CartResponseDto> result = cartService.getMyCart(memberId);
        return ResponseEntity.ok(result);
    }

    // 장바구니 기능을 활용하여 강의를 사전 신청할 수 있습니다.
    // 사전 신청자가 강의의 정원을 넘지 않은 경우 강의 신청이 자동으로 이루어집니다.
    // 사전 신청자가 강의의 정원을 넘은 경우 강의 신청이 이루어지지 않습니다.
    @PostMapping
    public void applyAdvanceInCart(@RequestBody CartRequestDto cartRequestDto) {
        checkIfCartAvailableOrElseThrow();
        Long memberId = cartRequestDto.getMemberId();
        Long lectureId = cartRequestDto.getLectureId();
        cartService.applyAdvanceInCart(memberId, lectureId);
    }

    @PostMapping("/re-apply")
    public ResponseEntity<Void> reApplyLecture(@RequestBody LectureRequestDto requestDto) {
        checkIfCartAvailableOrElseThrow();
        Long memberId = requestDto.getMemberId();
        Long lectureId = requestDto.getLectureId();

        cartService.reApplyLecture(memberId, lectureId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public void cancelCart(@RequestBody CartRequestDto cartRequestDto) {
        checkIfCartAvailableOrElseThrow();
        Long memberId = cartRequestDto.getMemberId();
        Long lectureId = cartRequestDto.getLectureId();
        cartService.cancelCart(memberId, lectureId);
    }

    // 강의 장바구니 기간은 2023년 1월 9일 오후 2시부터 1월 10일 오후 6시까지
    private void checkIfCartAvailableOrElseThrow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 9, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 10, 18, 0, 0);
        if (now.isAfter(startDate) && now.isBefore(endDate)) {
            return;
        }
        throw new RuntimeException("장바구니 기능은 2023년 1월 9일 오후 2시부터 1월 10일 오후 18시에만 사용가능합니다.");
    }
}

package me.seokju.secondjavafinal.service;

import me.seokju.secondjavafinal.dto.cart.CartResponseDto;
import me.seokju.secondjavafinal.entity.cart.Cart;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.entity.timetable.TimeTable;
import me.seokju.secondjavafinal.repository.CartRepository;
import me.seokju.secondjavafinal.repository.LectureRepository;
import me.seokju.secondjavafinal.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CartService {

    private static final int MAX_CREDIT = 21;
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final CartRepository cartRepository;

    public CartService(MemberRepository memberRepository, LectureRepository lectureRepository, CartRepository cartRepository) {
        this.memberRepository = memberRepository;
        this.lectureRepository = lectureRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional(readOnly = true)
    public List<CartResponseDto> getMyCart(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        List<Cart> cartList = cartRepository.findAllByMember(findMember);
        return cartList.stream()
                .map(c -> new CartResponseDto(c.getId(), c.getLecture().getId(), c.getLecture().getName(), c.getStatus(), c.getCredit()))
                .collect(Collectors.toList());
    }

    public void applyAdvanceInCart(Long memberId, Long lectureId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        
        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

        // 이미 신청한 과목인지 체크
        if (cartRepository.existsByMemberAndLecture(findMember, findLecture)) {
            throw new RuntimeException("이미 신청한 강의입니다.");
        }

        // 본인 장바구니 다 꺼내서 수업 시간 겹치는지 확인
        List<Cart> cartListByMember = cartRepository.findAllByMember(findMember);

        List<Long> lectureLongList = cartListByMember.stream()
                .mapToLong(c -> c.getLecture().getId())
                .boxed()
                .collect(Collectors.toList());
        List<Lecture> lectureList = lectureRepository.findAllByIdIn(lectureLongList);

        boolean isOverlapped = LectureService.checkOverlapLecture(findLecture, lectureList);

        if(isOverlapped) {
            throw new RuntimeException("수업이 겹칩니다.");
        }

        // 최대 학점 체크
        List<Cart> cartList = cartRepository.findAllByMember(findMember);
        int currentTotalCredit = cartList.stream().mapToInt(Cart::getCredit).sum();

        if (findLecture.getCredit() + currentTotalCredit > MAX_CREDIT) {
            throw new RuntimeException("초과 학점" + MAX_CREDIT + "를 넘길 수 없습니다. 현재: " + currentTotalCredit);
        }

        // 정원 초과면 status BOOKMARK로 저장
        if (findLecture.getCurrentPeople() >= findLecture.getLimitPeople()) {
            Cart cart = Cart.addCartToBookmark(findMember, findLecture);
            cartRepository.save(cart);
            return;
        }
        
        Cart cart = Cart.addCartToApplyDone(findMember, findLecture);
        cartRepository.save(cart);
    }

    public void reApplyLecture(Long memberId, Long lectureId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 수업입니다."));

        Cart findCart = cartRepository.findByMemberAndLecture(findMember, findLecture)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 장바구니입니다."));

        int currentPeople = findLecture.getCurrentPeople();
        int limitPeople = findLecture.getLimitPeople();
        if (currentPeople >= limitPeople) {
            throw new RuntimeException("정원 초과, 그대로 진행");
        }
        findCart.changeStatusToApply(findLecture);
    }


    public void cancelCart(Long memberId, Long lectureId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Lecture findLecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 수업입니다."));

        // 이미 존재하는 수업은 빼기
        if (!cartRepository.existsByMemberAndLecture(findMember, findLecture)) {
            throw new RuntimeException("신청한 적 없는 수업입니다.");
        }

        Cart findCart = cartRepository.findByMemberAndLecture(findMember, findLecture)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 신청입니다."));


        findLecture.decreaseCurrentPeople();
        cartRepository.delete(findCart);
    }
}

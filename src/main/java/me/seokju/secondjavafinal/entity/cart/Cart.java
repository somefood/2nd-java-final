package me.seokju.secondjavafinal.entity.cart;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.seokju.secondjavafinal.entity.BaseEntity;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.member.Member;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lecture lecture;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    private int credit;

    public Cart(Member member, Lecture lecture, CartStatus status, int credit) {
        this.member = member;
        this.lecture = lecture;
        this.status = status;
        this.credit = credit;
    }

    public static Cart addCartToApplyDone(Member member, Lecture lecture) {
        lecture.increaseCurrentPeople();
        return new Cart(member, lecture, CartStatus.APPLY_DONE, lecture.getCredit());
    }

    public static Cart addCartToBookmark(Member member, Lecture lecture) {
        return new Cart(member, lecture, CartStatus.BOOKMARK, lecture.getCredit());
    }

    public void changeStatusToApply(Lecture lecture) {
        this.status = CartStatus.APPLY_DONE;

        lecture.increaseCurrentPeople();
    }
}

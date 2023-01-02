package me.seokju.secondjavafinal.repository;

import me.seokju.secondjavafinal.entity.cart.CartStatus;
import me.seokju.secondjavafinal.entity.lecture.Lecture;
import me.seokju.secondjavafinal.entity.cart.Cart;
import me.seokju.secondjavafinal.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsByMemberAndLecture(Member member, Lecture lecture);

    List<Cart> findAllByMember(Member member);

    List<Cart> findAllByLecture(Lecture lecture);

    List<Cart> findAllByStatus(CartStatus status);

    Optional<Cart> findByMemberAndLecture(Member member, Lecture lecture);
}

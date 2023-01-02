package me.seokju.secondjavafinal.repository;

import me.seokju.secondjavafinal.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

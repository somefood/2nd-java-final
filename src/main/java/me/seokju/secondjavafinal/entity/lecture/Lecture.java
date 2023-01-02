package me.seokju.secondjavafinal.entity.lecture;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.seokju.secondjavafinal.entity.BaseEntity;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.entity.member.MemberRole;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private LectureTime lectureTime;

    private int limitPeople;

    private int currentPeople;

    private int credit;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member professor;

    public Lecture(String name, LectureTime lectureTime, int limitPeople, int currentPeople, int credit) {
        this.name = name;
        this.lectureTime = lectureTime;
        this.limitPeople = limitPeople;
        this.currentPeople = currentPeople;
        this.credit = credit;
    }

    public Lecture(String name, LectureTime lectureTime, int limitPeople, int currentPeople, int credit, Member professor) {
        this(name, lectureTime, limitPeople, currentPeople, credit);
        this.professor = professor;
    }

    public void assignProfessor(Member member) {
        if (member.getRole() != MemberRole.PROFESSOR) {
            throw new RuntimeException("교수만 배정 가능!");
        }
        this.professor = member;
    }

    public void increaseCurrentPeople() {
        this.currentPeople++;
    }

    public void decreaseCurrentPeople() {
        this.currentPeople--;
    }
}

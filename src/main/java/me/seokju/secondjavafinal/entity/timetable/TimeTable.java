package me.seokju.secondjavafinal.entity.timetable;

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
public class TimeTable extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lecture lecture;

    private int credit;

    @Enumerated(EnumType.STRING)
    private TimeTableStatus status;

    public void setMember(Member member) {
        this.member = member;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public void changeStatusToApply(Lecture lecture) {
        this.status = TimeTableStatus.DONE;

        lecture.increaseCurrentPeople();
    }

    public TimeTable(Member member, Lecture lecture, int credit, TimeTableStatus status) {
        this.member = member;
        this.lecture = lecture;
        this.credit = credit;
        this.status = status;

        lecture.increaseCurrentPeople();
    }
}


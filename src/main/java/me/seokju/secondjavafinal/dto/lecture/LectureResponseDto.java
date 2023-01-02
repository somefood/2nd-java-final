package me.seokju.secondjavafinal.dto.lecture;

import lombok.Getter;
import me.seokju.secondjavafinal.dto.member.MemberResponseDto;
import me.seokju.secondjavafinal.entity.member.Member;
import me.seokju.secondjavafinal.entity.timetable.TimeTableStatus;

import java.util.List;

@Getter
public class LectureResponseDto {

    private Long id;

    private String name;

    private int limitPeople;

    private int currentPeople;

    private TimeTableStatus status;

    private List<MemberResponseDto> memberList;

    public LectureResponseDto(Long id, String name, int limitPeople, int currentPeople) {
        this.id = id;
        this.name = name;
        this.limitPeople = limitPeople;
        this.currentPeople = currentPeople;
    }

    public LectureResponseDto(Long id, String name, int limitPeople, int currentPeople, List<MemberResponseDto> memberList) {
        this.id = id;
        this.name = name;
        this.limitPeople = limitPeople;
        this.currentPeople = currentPeople;
        this.memberList = memberList;
    }

    public LectureResponseDto(Long id, String name, int limitPeople, int currentPeople, TimeTableStatus status) {
        this.id = id;
        this.name = name;
        this.limitPeople = limitPeople;
        this.currentPeople = currentPeople;
        this.status = status;
    }
}

package me.seokju.secondjavafinal.dto.member;

import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long studentId;

    private String studentName;

    private String studentClassNumber;

    public MemberResponseDto(Long studentId, String studentName, String studentClassNumber) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentClassNumber = studentClassNumber;
    }
}

package me.seokju.secondjavafinal.entity.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.seokju.secondjavafinal.entity.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String classNumber;

    public Member(String email, String password, String name, MemberRole role, String classNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.classNumber = classNumber;
    }
}

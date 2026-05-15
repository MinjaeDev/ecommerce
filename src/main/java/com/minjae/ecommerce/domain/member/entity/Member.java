package com.minjae.ecommerce.domain.member.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import com.minjae.ecommerce.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "public_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false, length = 36)
    private String publicId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Column(name = "point", nullable = false)
    private Integer point = 0;

    public void addPoint(int amount) {
        this.point += amount;
    }

    @Builder
    public Member(String email, String password, String name, String phone) {
        this.publicId = UuidCreator.getTimeOrderedEpoch().toString(); // uuid 추가
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = MemberRole.USER;
        this.status = MemberStatus.ACTIVE;
    }

    // 비즈니스 메서드
    public void updateProfile(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void withdraw() {
        this.status = MemberStatus.WITHDRAWN;
    }

    public void suspend() {
        this.status = MemberStatus.SUSPENDED;
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

}

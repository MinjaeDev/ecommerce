package com.minjae.ecommerce.api.member.response;

import com.minjae.ecommerce.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long memberId;
    private final String email;
    private final String name;
    private final String phone;
    private final String role;
    private final String status;

    public MemberResponse(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.phone = member.getPhone();
        this.role = member.getRole().name();
        this.status = member.getStatus().name();
    }
}

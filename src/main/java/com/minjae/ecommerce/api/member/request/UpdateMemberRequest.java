package com.minjae.ecommerce.api.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateMemberRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    private String phone;
}

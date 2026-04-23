package com.minjae.ecommerce.api.member;

import com.minjae.ecommerce.api.member.request.UpdateMemberRequest;
import com.minjae.ecommerce.api.member.response.MemberResponse;
import com.minjae.ecommerce.domain.member.service.MemberService;
import com.minjae.ecommerce.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "회원 정보 관리")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyInfo(@AuthenticationPrincipal Long memberId) {
        return ResponseEntity.ok(ApiResponse.ok(memberService.getMyInfo(memberId)));
    }

    @Operation(summary = "내 정보 수정")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMyInfo(@AuthenticationPrincipal Long memberId,
            @Valid @RequestBody UpdateMemberRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(memberService.updateMyInfo(memberId, request)));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> withdraw(@AuthenticationPrincipal Long memberId) {
        memberService.withdraw(memberId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}

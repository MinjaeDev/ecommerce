package com.minjae.ecommerce.domain.member.service;

import com.minjae.ecommerce.api.member.request.LoginRequest;
import com.minjae.ecommerce.api.member.request.SignupRequest;
import com.minjae.ecommerce.api.member.request.UpdateMemberRequest;
import com.minjae.ecommerce.api.member.response.MemberResponse;
import com.minjae.ecommerce.api.member.response.TokenResponse;
import com.minjae.ecommerce.domain.member.entity.Member;
import com.minjae.ecommerce.domain.member.repository.MemberRepository;
import com.minjae.ecommerce.domain.order.repository.OrderRepository;
import com.minjae.ecommerce.global.exception.BusinessException;
import com.minjae.ecommerce.global.exception.ErrorCode;
import com.minjae.ecommerce.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberResponse signup(SignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .build();

        return new MemberResponse(memberRepository.save(member));
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        if (!member.isActive()) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        //publicId를 JWT에 저장
        String accessToken = jwtTokenProvider.createAccessToken(
                member.getPublicId(), member.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(
                member.getPublicId(), member.getRole().name());

        return new TokenResponse(accessToken, refreshToken);
    }

    public MemberResponse getMyInfo(String publicId) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberResponse(member);
    }

    @Transactional
    public MemberResponse updateMyInfo(String publicId, UpdateMemberRequest request) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateProfile(request.getName(), request.getPhone());
        return new MemberResponse(member);
    }

    @Transactional
    public void withdraw(String publicId) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.withdraw();
    }

    // 내부용 - publicId로 memberId 조회
    public Member findByPublicId(String publicId) {
        return memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }
}

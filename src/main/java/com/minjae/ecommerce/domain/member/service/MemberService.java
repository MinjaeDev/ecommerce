package com.minjae.ecommerce.domain.member.service;

import com.minjae.ecommerce.api.member.request.LoginRequest;
import com.minjae.ecommerce.api.member.request.SignupRequest;
import com.minjae.ecommerce.api.member.request.UpdateMemberRequest;
import com.minjae.ecommerce.api.member.response.MemberResponse;
import com.minjae.ecommerce.api.member.response.TokenResponse;
import com.minjae.ecommerce.domain.member.entity.Member;
import com.minjae.ecommerce.domain.member.repository.MemberRepository;
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

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getMemberId(), member.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(
                member.getMemberId(), member.getRole().name());

        return new TokenResponse(accessToken, refreshToken);
    }

    public MemberResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberResponse(member);
    }

    @Transactional
    public MemberResponse updateMyInfo(Long memberId, UpdateMemberRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateProfile(request.getName(), request.getPhone());
        return new MemberResponse(member);
    }

    @Transactional
    public void withdraw(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.withdraw();
    }
}

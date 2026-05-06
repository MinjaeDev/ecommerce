package com.minjae.ecommerce.domain.member.service;

import com.minjae.ecommerce.api.member.request.LoginRequest;
import com.minjae.ecommerce.api.member.request.SignupRequest;
import com.minjae.ecommerce.domain.member.repository.MemberRepository;
import com.minjae.ecommerce.global.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 단위테스트")
public class MemberServiceTest {
}

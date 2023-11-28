package com.gdscswu_server.server.global.util.filter;

import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.dto.LoginResponseDto;
import com.gdscswu_server.server.domain.model.ContextUser;
import com.gdscswu_server.server.global.error.exception.ApiException;
import com.gdscswu_server.server.global.util.JwtUtil;
import com.gdscswu_server.server.global.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    /*
    - Refresh token 존재 -> 토큰 재발급, filtering 종료
    - Refresh token 미존재 -> 유저 정보 저장, filtering 재개
    */

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ApiException, ServletException, IOException, ExpiredJwtException {
        final String LOGIN_API_URL = "/api/v1/member/login/oauth/google";
        final String TOKEN_REFRESH_API_URL = "/api/v1/member/token";

        // uri 검사하여 jwt 검증 필터 통과 여부 결정
        String uri = request.getRequestURI();
        if (uri.equals(LOGIN_API_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        // refresh token을 헤더에 가지고 있는 경우 검증 후 token 재발급
        String refresh_token = jwtUtil.decodeHeader(false, request);

        if (request.getRequestURI().equals(TOKEN_REFRESH_API_URL) && refresh_token != null) {
            Member member = jwtUtil.validateToken(refresh_token);
            LoginResponseDto dto = jwtUtil.generateTokens(member);

            responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, dto);

            return;
        }

        // access token 검증
        String access_token = jwtUtil.decodeHeader(true, request);
        Member member = jwtUtil.validateToken(access_token);

        saveAuthentication(member);
        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(Member member) {
        ContextUser contextUser = new ContextUser(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                contextUser,
                contextUser.getPassword(),
                authoritiesMapper.mapAuthorities(contextUser.getAuthorities()));

        // context 초기화 후 인증 정보 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}

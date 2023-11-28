package com.gdscswu_server.server.global.util.filter;

import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.error.MemberErrorCode;
import com.gdscswu_server.server.global.error.GlobalErrorCode;
import com.gdscswu_server.server.global.error.exception.ApiException;
import com.gdscswu_server.server.global.util.GoogleOAuthUtil;
import com.gdscswu_server.server.global.util.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final MemberRepository memberRepository;
    private final GoogleOAuthUtil googleOAuthUtil;
    private final ResponseUtil responseUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ApiException {
        // uri 검사
        final String GOOGLE_OAUTH_API_URL = "/api/v1/member/login/oauth/google";
        if (!request.getRequestURI().equals(GOOGLE_OAUTH_API_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        Member requestMember;

        // Google OAuth
        try {
            requestMember = googleOAuthUtil.authenticate(getIdToken(request));
        } catch (GeneralSecurityException e) {
            throw new ApiException(GlobalErrorCode.INVALID_ACCESS);
        }

        // 회원 조회 및 저장
        Member member = memberRepository.findByGoogleEmail(requestMember.getGoogleEmail())
                .orElseGet(() -> memberRepository.save(requestMember));

        // 응답 전송
        responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, member);
    }

    public String getIdToken(HttpServletRequest request) throws AuthenticationServiceException {
        String idToken = request.getHeader("id-token");

        if (idToken == null)
            throw new ApiException(MemberErrorCode.ID_TOKEN_REQUIRED);

        return idToken;
    }
}

package com.gdscswu_server.server.global.util;

import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.domain.member.dto.LoginResponseDto;
import com.gdscswu_server.server.domain.model.TokenClaimVo;
import com.gdscswu_server.server.global.error.GlobalErrorCode;
import com.gdscswu_server.server.global.error.exception.ApiException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private final MemberRepository memberRepository;

    @Value("${JWT_ISSUER}")
    private String ISSUER;
    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;

    public LoginResponseDto generateTokens(Member member) {
        TokenClaimVo vo = new TokenClaimVo(member.getId(), member.getEmail());

        // 토큰 발급
        String access_token = generateToken(true, vo);
        String refresh_token = generateToken(false, vo);

        return new LoginResponseDto(member.getId(), access_token, refresh_token);
    }

    // Generate Token
    public String generateToken(boolean isAccessToken, TokenClaimVo vo) {
        // Payloads 생성
        Map<String, Object> payloads = new LinkedHashMap<>();
        payloads.put("member_id", vo.getId());
        payloads.put("email", vo.getEmail());

        // Expiration time (access: 1 h / refresh: 7 d)
        Date now = new Date();
        Duration duration = isAccessToken ? Duration.ofHours(1) : Duration.ofDays(7);
        Date expiration = new Date(now.getTime() + duration.toMillis());

        // Subject
        String subject = isAccessToken ? "access" : "refresh";

        // Build
        return builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(payloads)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(subject)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes()))
                .compact();
    }

    // Validate token
    public Member validateToken(String token) throws ApiException {
        // 검증 및 payload 추출
        Map<String, Object> payloads = parser()
                .setSigningKey(JWT_SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();

        // member 조회
        return memberRepository.findById(((Number) payloads.get("member_id")).longValue())
                .orElseThrow(() -> new ApiException(GlobalErrorCode.INVALID_TOKEN));
    }
    
    // Decode request
    public String decodeHeader(boolean isAccessToken, HttpServletRequest request) {
        final String ACCESS_HEADER = "Authorization";
        final String REFRESH_HEADER = "Authorization-refresh";
        final String header = isAccessToken ? ACCESS_HEADER : REFRESH_HEADER;

        String header_value = request.getHeader(header);
        if (isAccessToken && header_value == null)
            throw new ApiException(GlobalErrorCode.ACCESS_TOKEN_REQUIRED);
        else if (header_value == null)
            return null;

        return decodeBearer(header_value);
    }

    // Decode Bearer
    public String decodeBearer(String bearer_token) throws ApiException {
        try {
            final String BEARER = "Bearer ";
            return Arrays.stream(bearer_token.split(BEARER)).toList().get(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ApiException(GlobalErrorCode.INVALID_TOKEN);
        }
    }
}

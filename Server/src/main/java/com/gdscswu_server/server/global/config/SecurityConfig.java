package com.gdscswu_server.server.global.config;

import com.gdscswu_server.server.domain.member.domain.MemberRepository;
import com.gdscswu_server.server.global.error.handler.ExceptionHandlerFilter;
import com.gdscswu_server.server.global.util.GoogleOAuthUtil;
import com.gdscswu_server.server.global.util.JwtUtil;
import com.gdscswu_server.server.global.util.ResponseUtil;
import com.gdscswu_server.server.global.util.filter.AuthenticationFilter;
import com.gdscswu_server.server.global.util.filter.JwtAuthenticationProcessingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;
    private final GoogleOAuthUtil googleOAuthUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(httpRequests -> httpRequests
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // Preflight 요청은 허용 , 사전 인증이라는 요청 방식은 허용하겠다
                        .requestMatchers(new AntPathRequestMatcher("/")
                                , new AntPathRequestMatcher("/css/**")
                                , new AntPathRequestMatcher("/images/**")
                                , new AntPathRequestMatcher("/h2-console/**")
                                , new AntPathRequestMatcher("/profile")
                        ).permitAll()
                        .requestMatchers("/api/v1/user/login/**").permitAll()
                        .requestMatchers("/api/v1/member/test").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtAuthenticationProcessingFilter(), LogoutFilter.class)
                .addFilterBefore(authenticationFilter(), JwtAuthenticationProcessingFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(responseUtil), AuthenticationFilter.class)
        ;

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtUtil, responseUtil);
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(memberRepository, googleOAuthUtil, responseUtil, jwtUtil);
    }
}
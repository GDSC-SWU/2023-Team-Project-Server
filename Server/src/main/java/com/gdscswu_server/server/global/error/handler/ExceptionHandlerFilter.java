package com.gdscswu_server.server.global.error.handler;

import com.gdscswu_server.server.global.error.GlobalErrorCode;
import com.gdscswu_server.server.global.error.exception.ApiException;
import com.gdscswu_server.server.global.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ResponseUtil responseUtil;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            responseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, GlobalErrorCode.EXPIRED_JWT.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            responseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, GlobalErrorCode.INVALID_TOKEN.getMessage());
        } catch (ApiException e) {
            responseUtil.setResponse(response, e.getErrorCode().getHttpStatus().value(), e.getMessage());
        } catch (AuthenticationServiceException e) {
            responseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            responseUtil.setResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }
}

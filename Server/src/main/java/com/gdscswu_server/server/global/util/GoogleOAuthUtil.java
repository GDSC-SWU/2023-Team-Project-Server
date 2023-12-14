package com.gdscswu_server.server.global.util;

import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.domain.member.error.MemberErrorCode;
import com.gdscswu_server.server.global.error.exception.ApiException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@Slf4j
public class GoogleOAuthUtil {
    @Value("${GOOGLE_OAUTH_CLIENT_ID}")
    private String CLIENT_ID;

    public Member authenticate(String idToken) throws GeneralSecurityException, IOException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);

        if (googleIdToken == null)
            throw new ApiException(MemberErrorCode.INVALID_ID_TOKEN);

        Payload payload = googleIdToken.getPayload();

        // Get profile information from payload
        String userId = payload.getSubject();
        String email = payload.getEmail();
        String pictureUrl = (String) payload.get("picture");

        return new Member(email, userId, pictureUrl);
    }
}

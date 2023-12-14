package com.gdscswu_server.server.global.util;

import com.gdscswu_server.server.domain.member.domain.Member;
import com.gdscswu_server.server.global.error.GlobalErrorCode;
import com.gdscswu_server.server.global.error.exception.ApiException;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProfileImageUtil {
    @Value("${GCP_PROJECT_ID}")
    String PROJECT_ID;

    @Value("${GCP_BUCKET_NAME}")
    String BUCKET_NAME;

    @Transactional
    public void uploadProfileImage(MultipartFile file, Member member) throws IOException {
        if (member.getProfileImagePath() != null) {
            // 기존 이미지 삭제
            deleteGCSFile(member.getProfileImagePath());
        }

        if (file == null) {
            member.updateProfileImagePath(null);
            return;
        }

        String newPath = uploadGCSFile(file);
        member.updateProfileImagePath(newPath);
    }

    public String uploadGCSFile(MultipartFile file) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(PROJECT_ID).build().getService();
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = "profile-images/" + fileName;
        String ext = file.getContentType();

        BlobId blobId = BlobId.of(BUCKET_NAME, filePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(ext)
                .build();
        byte[] content = file.getBytes();
        storage.createFrom(blobInfo, new ByteArrayInputStream(content));

        log.info("[Google Cloud Storage] Uploaded successfully. :: " + filePath);

        return filePath;
    }

    public void deleteGCSFile(String filePath) throws ApiException {
        Storage storage = StorageOptions.newBuilder().setProjectId(PROJECT_ID).build().getService();
        Blob blob = storage.get(BUCKET_NAME, filePath);
        if (blob == null) {
            log.error("[Google Cloud Storage] File not found. :: " + filePath);
            throw new ApiException(GlobalErrorCode.GCS_FILE_NOT_FOUND);
        }

        Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());

        storage.delete(BUCKET_NAME, filePath, precondition);

        log.info("[Google Cloud Storage] Deleted successfully. :: " + filePath);
    }
}

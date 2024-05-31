package com.be.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class UploadFile {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) {
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            if (!uploadDirFile.mkdirs()) {
                return "Failed to create upload directory";
            }
        }
        try {
            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return file.getOriginalFilename();
        } catch (Exception e) {
            return "Failed to save file";
        }
    }
}

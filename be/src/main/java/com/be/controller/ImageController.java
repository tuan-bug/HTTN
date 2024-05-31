package com.be.controller;

import com.be.utils.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    @Value("${file.upload-dir}")
    private String filePath;

    private final UploadFile uploadFile;

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            // Lay duong dan anh
            Path imgPath = Paths.get(filePath, fileName);

            // Ensure file exists
            if (!Files.exists(imgPath)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            byte[] bytes;
            // Chuyen ve byte de tra ra ket qua
            try (InputStream in = Files.newInputStream(imgPath)) {
                bytes = StreamUtils.copyToByteArray(in);
            }

            // Determine content type
            HttpHeaders headers = new HttpHeaders();
            String mimeType = Files.probeContentType(imgPath);
            headers.setContentType(MediaType.parseMediaType(mimeType != null ? mimeType : MediaType.APPLICATION_OCTET_STREAM_VALUE));

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes;
            String fileName = uploadFile.uploadFile(file);
            Path imgPath = Paths.get(filePath, fileName);

            try (InputStream in = Files.newInputStream(imgPath)) {
                bytes = StreamUtils.copyToByteArray(in);
            }

            HttpHeaders headers = new HttpHeaders();
            String mimeType = Files.probeContentType(imgPath);
            headers.setContentType(MediaType.parseMediaType(mimeType != null ? mimeType : MediaType.APPLICATION_OCTET_STREAM_VALUE));

            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}

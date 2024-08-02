package org.sesac.wagekeeper.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.image.service.ImageService;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.springframework.http.HttpHeaders;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;
    private final String uploadDir = System.getProperty("java.io.tmpdir");

    @PostMapping("/upload/{workspaceId}")
    public ResponseEntity<SuccessResponse<?>> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable Long workspaceId) throws IOException {
        String imageUrl = imageService.saveImage(workspaceId, file);
        return SuccessResponse.ok(imageUrl);
    }


    //이미지 파일 띄우는 용
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {
        File imgFile =  new File(uploadDir, filename);

        if (!imgFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        BufferedImage img = ImageIO.read(imgFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(imageBytes);
    }


}

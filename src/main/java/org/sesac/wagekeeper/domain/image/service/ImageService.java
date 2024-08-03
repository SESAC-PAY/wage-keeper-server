package org.sesac.wagekeeper.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.image.entity.Image;
import org.sesac.wagekeeper.domain.image.repository.ImageRepository;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.repository.UserRepository;
import org.sesac.wagekeeper.domain.workspace.entity.Workspace;
import org.sesac.wagekeeper.domain.workspace.repository.WorkspaceRepository;
import org.sesac.wagekeeper.global.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.sesac.wagekeeper.global.error.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;
    private final WorkspaceRepository workspaceRepository;
    private final String uploadDir = System.getProperty("java.io.tmpdir");

    public String saveImage(Long workspaceId, MultipartFile file) throws IOException {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(()
                -> new RuntimeException("No workspace id " + workspaceId));

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new RuntimeException("File is not an image");
        }

        File outputfile = new File(uploadDir, file.getOriginalFilename());
        ImageIO.write(originalImage, "png", outputfile);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/image/")
                .path(file.getOriginalFilename())
                .toUriString();

        imageRepository.save(Image.builder()
                .imageUrl(imageUrl)
                .workspace(workspace)
                .build());

        return imageUrl;

    }


}

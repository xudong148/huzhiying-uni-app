package com.huzhiying.server.service;

import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.persistence.PersistenceEntities.MediaFileEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 本地对象存储抽象。
 */
@Service
@Transactional
public class FileStorageService {

    private final PlatformRepository platformRepository;
    private final Path rootPath;

    public FileStorageService(PlatformRepository platformRepository,
                              @Value("${hzy.storage.local-path:data/uploads}") String localPath) {
        this.platformRepository = platformRepository;
        this.rootPath = Paths.get(localPath).toAbsolutePath().normalize();
    }

    public SupportDtos.MediaFilePayload upload(MultipartFile file, String bizType, String bizId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        try {
            Files.createDirectories(rootPath);
            String originalName = file.getOriginalFilename() == null ? "file.bin" : file.getOriginalFilename();
            String extension = "";
            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = originalName.substring(dotIndex);
            }
            String storedName = UUID.randomUUID().toString().replace("-", "") + extension;
            Path target = rootPath.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            MediaFileEntity entity = new MediaFileEntity();
            entity.id = platformRepository.nextLongId("MediaFileEntity", "id", 9000L);
            entity.bizType = bizType == null || bizType.isBlank() ? "general" : bizType;
            entity.bizId = bizId;
            entity.originalName = originalName;
            entity.contentType = file.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : file.getContentType();
            entity.sizeBytes = file.getSize();
            entity.storagePath = target.toString();
            entity.accessUrl = "/api/files/" + entity.id + "/content";
            entity.createdAt = LocalDateTime.now();

            return toPayload(platformRepository.saveMediaFile(entity));
        } catch (IOException exception) {
            throw new IllegalStateException("文件保存失败", exception);
        }
    }

    @Transactional(readOnly = true)
    public SupportDtos.MediaFilePayload getFile(Long fileId) {
        return toPayload(findFileEntity(fileId));
    }

    @Transactional(readOnly = true)
    public Resource loadFileResource(Long fileId) {
        MediaFileEntity entity = findFileEntity(fileId);
        try {
            return new ByteArrayResource(Files.readAllBytes(Path.of(entity.storagePath)));
        } catch (IOException exception) {
            throw new IllegalStateException("读取文件失败", exception);
        }
    }

    @Transactional(readOnly = true)
    public MediaType mediaType(Long fileId) {
        MediaFileEntity entity = findFileEntity(fileId);
        try {
            return MediaType.parseMediaType(entity.contentType);
        } catch (Exception ignored) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @Transactional(readOnly = true)
    public SupportDtos.MediaFilePayload toPayload(MediaFileEntity entity) {
        return new SupportDtos.MediaFilePayload(
                entity.id,
                entity.bizType,
                entity.bizId,
                entity.originalName,
                entity.accessUrl,
                entity.contentType,
                entity.sizeBytes,
                entity.createdAt
        );
    }

    @Transactional(readOnly = true)
    public MediaFileEntity findFileEntity(Long fileId) {
        return platformRepository.findMediaFile(fileId).orElseThrow();
    }
}

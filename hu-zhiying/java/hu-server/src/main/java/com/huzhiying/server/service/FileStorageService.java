package com.huzhiying.server.service;

import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.persistence.PersistenceEntities.MediaFileEntity;
import com.huzhiying.server.repository.PlatformRepository;
import com.huzhiying.server.service.storage.StorageProvider;
import com.huzhiying.server.service.storage.StoredFileDescriptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件存储门面，业务侧始终经由 provider 访问底层存储。
 */
@Service
@Transactional
public class FileStorageService {

    private final PlatformRepository platformRepository;
    private final List<StorageProvider> storageProviders;
    private final String storageProvider;

    public FileStorageService(PlatformRepository platformRepository,
                              List<StorageProvider> storageProviders,
                              @Value("${hzy.storage.provider:local}") String storageProvider) {
        this.platformRepository = platformRepository;
        this.storageProviders = storageProviders;
        this.storageProvider = storageProvider;
    }

    public SupportDtos.MediaFilePayload upload(MultipartFile file, String bizType, String bizId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        try {
            StoredFileDescriptor storedFile = activeProvider().store(file);

            MediaFileEntity entity = new MediaFileEntity();
            entity.id = platformRepository.nextLongId("MediaFileEntity", "id", 9000L);
            entity.bizType = bizType == null || bizType.isBlank() ? "general" : bizType;
            entity.bizId = bizId;
            entity.originalName = storedFile.originalName();
            entity.contentType = storedFile.contentType();
            entity.sizeBytes = storedFile.size();
            entity.storagePath = storedFile.storageKey();
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
            return activeProvider().load(entity.storagePath);
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

    private StorageProvider activeProvider() {
        return storageProviders.stream()
                .filter(provider -> provider.supports(storageProvider))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("未找到可用的文件存储提供器"));
    }
}

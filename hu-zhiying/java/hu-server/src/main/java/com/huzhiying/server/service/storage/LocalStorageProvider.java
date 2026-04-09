package com.huzhiying.server.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class LocalStorageProvider implements StorageProvider {

    private static final String LOCAL_PREFIX = "local:";

    private final Path rootPath;

    public LocalStorageProvider(@Value("${hzy.storage.local-path:data/uploads}") String localPath) {
        this.rootPath = Paths.get(localPath).toAbsolutePath().normalize();
    }

    @Override
    public String provider() {
        return "local";
    }

    @Override
    public StoredFileDescriptor store(MultipartFile file) throws IOException {
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
        String contentType = file.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : file.getContentType();
        return new StoredFileDescriptor(LOCAL_PREFIX + storedName, originalName, contentType, file.getSize());
    }

    @Override
    public Resource load(String storageKey) throws IOException {
        Path target = resolve(storageKey);
        return new ByteArrayResource(Files.readAllBytes(target));
    }

    private Path resolve(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            throw new IllegalStateException("storage key is empty");
        }
        if (storageKey.startsWith(LOCAL_PREFIX)) {
            return rootPath.resolve(storageKey.substring(LOCAL_PREFIX.length())).normalize();
        }
        Path rawPath = Paths.get(storageKey);
        if (rawPath.isAbsolute()) {
            return rawPath.normalize();
        }
        return rootPath.resolve(storageKey).normalize();
    }
}

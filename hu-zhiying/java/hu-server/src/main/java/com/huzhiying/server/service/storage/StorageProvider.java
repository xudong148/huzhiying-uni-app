package com.huzhiying.server.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageProvider {

    String provider();

    default boolean supports(String provider) {
        return provider().equalsIgnoreCase(provider == null ? "" : provider.trim());
    }

    StoredFileDescriptor store(MultipartFile file) throws IOException;

    Resource load(String storageKey) throws IOException;
}

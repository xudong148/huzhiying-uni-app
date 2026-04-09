package com.huzhiying.server.service.storage;

public record StoredFileDescriptor(
        String storageKey,
        String originalName,
        String contentType,
        long size
) {
}

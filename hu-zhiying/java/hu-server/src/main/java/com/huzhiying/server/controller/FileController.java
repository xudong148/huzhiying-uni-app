package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "file", description = "文件上传与下载接口")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/api/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件")
    public ApiResponse<?> upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "bizType", required = false) String bizType,
                                 @RequestParam(value = "bizId", required = false) String bizId) {
        return ApiResponse.success(fileStorageService.upload(file, bizType, bizId));
    }

    @GetMapping("/api/files/{id}")
    @Operation(summary = "查询文件元数据")
    public ApiResponse<?> file(@PathVariable("id") Long id) {
        return ApiResponse.success(fileStorageService.getFile(id));
    }

    @GetMapping("/api/files/{id}/content")
    @Operation(summary = "下载文件内容")
    public ResponseEntity<Resource> fileContent(@PathVariable("id") Long id) {
        var payload = fileStorageService.getFile(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.inline().filename(payload.originalName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(fileStorageService.mediaType(id))
                .body(fileStorageService.loadFileResource(id));
    }
}

package com.huzhiying.server.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 兼容旧文档入口，统一跳转到 Knife4j 的 doc.html 页面。
 */
@RestController
public class OpenApiPortalController {

    @GetMapping("/swagger-ui/index.html")
    public ResponseEntity<Void> redirectToKnife4j() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/doc.html");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}

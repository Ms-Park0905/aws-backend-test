package com.example.awsbackendtest.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/health")
    fun getHealthApi(): ResponseEntity<String> {
        return ResponseEntity
            .ok("서버 정상 작동중입니다.")
    }
}
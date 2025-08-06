package com.example.awsbackendtest.controller

import com.example.awsbackendtest.entity.Board
import com.example.awsbackendtest.repository.BoardRepository
import io.awspring.cloud.s3.ObjectMetadata
import io.awspring.cloud.s3.S3Operations
import io.awspring.cloud.s3.S3Resource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.Instant


@RestController
class TestController(
    private val boardRepository: BoardRepository,
    private val s3Operations: S3Operations
) {

    @GetMapping("/health")
    fun getHealthApi(): ResponseEntity<String> {
        return ResponseEntity
            .ok("서버 정상 작동중입니다.")
    }

    @GetMapping("/board")
    fun getBoard(): ResponseEntity<Board?> {
        return ResponseEntity.ok(boardRepository.findByIdOrNull(1))
    }

    @PostMapping("/uploads")
    fun uploadFiles(
        @RequestPart("image") imageFile: MultipartFile,
    ): ResponseEntity<String> {
        val originalFilename = imageFile.originalFilename
        val fileExtension = originalFilename!!.substring(originalFilename.lastIndexOf("."))
        val filename = Instant.now().epochSecond.toString() + fileExtension

        val imageUrl: String?
        try {
            imageFile.inputStream.use { inputStream ->
                val s3Resource: S3Resource = s3Operations.upload(
                    "minseok-server-upload-files", filename, inputStream,
                    ObjectMetadata.builder().contentType(imageFile.contentType).build()
                )
                imageUrl = s3Resource.url.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return ResponseEntity.ok("이미지 업로드 : ${imageUrl}")
    }

}
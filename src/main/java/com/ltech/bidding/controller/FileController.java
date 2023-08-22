package com.ltech.bidding.controller;

import com.ltech.bidding.model.FileInfo;
import com.ltech.bidding.service.dto.file.FileResourceDto;
import com.ltech.bidding.repository.FileInfoRepository;
import com.ltech.bidding.service.FileStorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileStorageService storageService;
    private final FileInfoRepository fileInfoRepository;

    public FileController(@Qualifier("ImageStorageService") FileStorageService storageService,
                          FileInfoRepository fileInfoRepository) {
        this.storageService = storageService;
        this.fileInfoRepository = fileInfoRepository;
    }

    @PostMapping("/upload/{contentKeyId}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String contentKeyId) {
        String message = "";
        try {
            String id = storageService.save(file,contentKeyId);

            message = id;
            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id) {
        FileResourceDto file = storageService.load(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"").body(file.getResource());
    }

    @GetMapping("/all/{contentKey}")
    public ResponseEntity<?> getFiles(@PathVariable String contentKey) {
        List<FileInfo> fileInfos = fileInfoRepository.findByContentKeyIdOrderByPriorityAsc(contentKey);

        return ResponseEntity.ok().body(fileInfos);
    }
}

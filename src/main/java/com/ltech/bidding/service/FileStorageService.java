package com.ltech.bidding.service;

import com.ltech.bidding.service.dto.file.FileResourceDto;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    String save(MultipartFile file);
    String save(MultipartFile file, String contentKeyId);

    FileResourceDto load(String filename);

    void deleteAll();

    Stream<Path> loadAll();
}

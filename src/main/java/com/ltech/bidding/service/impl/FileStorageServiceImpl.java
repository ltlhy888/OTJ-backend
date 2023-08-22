package com.ltech.bidding.service.impl;

import com.ltech.bidding.model.FileInfo;
import com.ltech.bidding.service.dto.file.FileResourceDto;
import com.ltech.bidding.model.enumeration.FileProvider;
import com.ltech.bidding.repository.FileInfoRepository;
import com.ltech.bidding.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Service("FileStorageService")
public class FileStorageServiceImpl implements FileStorageService {
    private final Path root = Paths.get("./uploads");
    private final FileInfoRepository fileInfoRepository;
    @Value("${application.uri}")
    private String uri;

    @Override
    public String save(MultipartFile file) {
        try {
            String id = UUID.randomUUID().toString();

            Files.copy(file.getInputStream(), root.resolve(id));

            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(file.getOriginalFilename());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setSize(file.getSize());
            fileInfo.setPriority(0);
            fileInfo.setId(id);
            fileInfo.setProvider(FileProvider.LOCAL);
            fileInfo.setUrl(uri + "/file/" + id);

            fileInfoRepository.save(fileInfo);

            return id;
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public String save(MultipartFile file, String contentKeyId) {
        try {
            String id = UUID.randomUUID().toString();

            Files.copy(file.getInputStream(), root.resolve(id));

            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(file.getOriginalFilename());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setSize(file.getSize());
            fileInfo.setId(id);
            fileInfo.setPriority(0);
            fileInfo.setContentKeyId(contentKeyId);
            fileInfo.setProvider(FileProvider.LOCAL);
            fileInfo.setUrl(uri + "/file/" + id);

            fileInfoRepository.save(fileInfo);

            return id;
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FileResourceDto load(String id) {
        try {
            FileInfo fileInfo = fileInfoRepository.getReferenceById(id);

            Path file = root.resolve(id);

            Resource resource = new UrlResource(file.toUri());

            FileResourceDto fileResourceDto = new FileResourceDto();
            fileResourceDto.setFileName(fileInfo.getName());
            fileResourceDto.setResource(resource);
            fileResourceDto.setFileType(fileInfo.getFileType());

            if (resource.exists() || resource.isReadable()) {
                return fileResourceDto;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

}

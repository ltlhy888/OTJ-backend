package com.ltech.bidding.service.dto.file;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class FileResourceDto {
    private Resource resource;
    private String fileName;
    private String fileType;
}

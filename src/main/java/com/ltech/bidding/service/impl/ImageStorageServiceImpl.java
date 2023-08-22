package com.ltech.bidding.service.impl;

import com.ltech.bidding.repository.FileInfoRepository;
import com.ltech.bidding.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Transactional
@Service("ImageStorageService")
public class ImageStorageServiceImpl extends FileStorageServiceImpl {
    public ImageStorageServiceImpl(FileInfoRepository fileInfoRepository) {
        super(fileInfoRepository);
    }

    @Override
    public String save(MultipartFile file) {

        if(allowedMimeTypes().contains(file.getContentType().toLowerCase())) {
            return super.save(file);

        }

        throw new RuntimeException("File type not allowed " +file.getContentType());
    }

    @Override
    public String save(MultipartFile file, String contentKeyId) {

        if(allowedMimeTypes().contains(file.getContentType().toLowerCase())) {
            return super.save(file, contentKeyId);

        }

        throw new RuntimeException("File type not allowed " +file.getContentType());
    }

    private ArrayList<String> allowedMimeTypes() {
        ArrayList<String> mimeTypes = new ArrayList<String>();
        mimeTypes.add("image/jpeg");
        mimeTypes.add("image/png");
        mimeTypes.add("image/webp");
        mimeTypes.add("image/gif");

        return mimeTypes;
    }
}

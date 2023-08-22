package com.ltech.bidding.repository;

import com.ltech.bidding.model.Content;
import com.ltech.bidding.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, String>, BaseContentRepository<FileInfo>{
    List<FileInfo> findByContentKeyIdOrderByPriorityAsc(String id);
}

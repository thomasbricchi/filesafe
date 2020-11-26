package com.thomas.filesafe.service;

import com.thomas.filesafe.dto.AttachmentDTO;
import com.thomas.filesafe.dto.FileContentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AttachmentsService {

    Long save(MultipartFile uploadingFile);

    List<AttachmentDTO> findAll();

    Optional<FileContentDTO> getContent(Long id);

    void deleteAll();
}

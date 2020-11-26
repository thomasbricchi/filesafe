package com.thomas.filesafe.service.impl;


import com.thomas.filesafe.domain.Attachment;
import com.thomas.filesafe.dto.AttachmentDTO;
import com.thomas.filesafe.dto.FileContentDTO;
import com.thomas.filesafe.repository.AttachmentsRepository;
import com.thomas.filesafe.service.AttachmentsService;
import com.thomas.filesafe.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AttachmentsServiceImpl implements AttachmentsService {

    private StorageService storageService;
    private AttachmentsRepository attachmentsRepository;

    public AttachmentsServiceImpl(StorageService storageService, AttachmentsRepository attachmentsRepository) {
        this.storageService = storageService;
        this.attachmentsRepository = attachmentsRepository;
    }


    @Override
    @Transactional
    public Long save(MultipartFile uploadingFile) {

        String path = storageService.uploadFile(uploadingFile);

        Attachment attachmentToSave = createAttachmentsToSave(uploadingFile.getOriginalFilename(), path);

        return attachmentsRepository.save(attachmentToSave).getId();
    }

    @Override
    public List<AttachmentDTO> findAll() {
        return attachmentsRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private AttachmentDTO toDto(Attachment attachment) {
        final AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setId(attachment.getId());
        attachmentDTO.setFileName(attachment.getFileName());
        attachmentDTO.setPath(attachment.getPath());
        return attachmentDTO;
    }

    @Override
    public Optional<FileContentDTO> getContent(Long id) {

        return attachmentsRepository.findById(id).map(a -> {
            final byte[] fileContent = storageService.getFileContent(a.getPath());
            return new FileContentDTO(a.getFileName(), fileContent, storageService.getMimeTypeFromName(a.getFileName()));
        });
    }

    @Override
    public void deleteAll() {
        attachmentsRepository.deleteAll();
    }

    private Attachment createAttachmentsToSave(String originalFilename, String path) {
        final Attachment attachment = new Attachment();
        attachment.setFileName(originalFilename);
        attachment.setPath(path);
        return attachment;

    }


}

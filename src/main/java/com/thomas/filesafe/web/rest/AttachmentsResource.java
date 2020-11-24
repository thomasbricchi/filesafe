package com.thomas.filesafe.web.rest;

import com.thomas.filesafe.dto.AttachmentDTO;
import com.thomas.filesafe.dto.FileContentDTO;
import com.thomas.filesafe.service.AttachmentsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/attachments")
public class AttachmentsResource {

    private static final String ENTITY_NAME = "Attachments";


    private final AttachmentsService attachmentsService;

    public AttachmentsResource(AttachmentsService attachmentsService) {
        this.attachmentsService = attachmentsService;
    }


    @PostMapping("/attachments")
    public ResponseEntity<Void> createAttachment(
        @RequestParam("uploadingFile") MultipartFile uploadingFile) throws URISyntaxException {

        Long result = attachmentsService.save(uploadingFile);

        return ResponseEntity.created(new URI("/api/attachments/" + result))
            .build();
    }

    @GetMapping("/attachments")
    public ResponseEntity<List<AttachmentDTO>> getAllAttachments() {
        List<AttachmentDTO> attachments = attachmentsService.findAll();
        return ResponseEntity.ok().body(attachments);
    }

    @GetMapping("/attachments/{id}/content")
    public ResponseEntity<byte[]> getAttachmentContent(@PathVariable Long id) {
        Optional<FileContentDTO> fileContentDTO = attachmentsService.getContent(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileContentDTO
            .map(FileContentDTO::getFileName)
            .orElse(""));

        headers.add(HttpHeaders.CONTENT_TYPE, fileContentDTO
            .map(FileContentDTO::getMimeType)
            .orElse(""));

        headers.add(HttpHeaders.CONTENT_LENGTH, fileContentDTO.map(FileContentDTO::getContent)
            .map(bytes -> bytes.length)
            .map(Object::toString)
            .orElse(""));

        return fileContentDTO.map(response ->
            ResponseEntity.ok().headers(headers).body(response.getContent())
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}

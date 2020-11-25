package com.thomas.filesafe.web.rest;

import com.thomas.filesafe.dto.AttachmentDTO;
import com.thomas.filesafe.dto.FileContentDTO;
import com.thomas.filesafe.service.AttachmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/attachments")
public class AttachmentsResource {

    private final AttachmentsService attachmentsService;

    public AttachmentsResource(AttachmentsService attachmentsService) {
        this.attachmentsService = attachmentsService;
    }


    @PostMapping("")
    public ResponseEntity<Void> createAttachment(
        @RequestParam("uploadingFile") MultipartFile uploadingFile) throws URISyntaxException {
        log.info("--- START createAttachment");
        Long result = attachmentsService.save(uploadingFile);
        log.info("--END createAttachment");
        return ResponseEntity.created(new URI("/api/attachments/" + result))
            .build();
    }

    @GetMapping("")
    public ResponseEntity<List<AttachmentDTO>> getAllAttachments() {
        log.info("---START getAllAttachments---");
        List<AttachmentDTO> attachments = attachmentsService.findAll();
        log.info("---END getAllAttachments---");
        return ResponseEntity.ok().body(attachments);
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<byte[]> getAttachmentContent(@PathVariable Long id) {
        log.info("---START getAttachmentContent---");
        Optional<FileContentDTO> fileContentDTO = attachmentsService.getContent(id);
        log.info("---END getAttachmentContent---");
        return fileContentDTO.map(fileContent -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileContent.getFileName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, fileContent.getMimeType());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileContent.getContent().length));
            log.info("---WITH " + fileContent.logToString());
            return ResponseEntity.ok().headers(headers).body(fileContent.getContent());
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }


}

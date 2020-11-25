package com.thomas.filesafe.service.impl;

import com.thomas.filesafe.exception.GetFileContentException;
import com.thomas.filesafe.exception.UploadFileException;
import com.thomas.filesafe.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

    private final String basePath;

    public FileSystemStorageService(@Value("${attachments.base-path}") String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        final String path = getPath();
        String relativePath = path.substring(0, path.lastIndexOf('/') + 1);
        String fileName = path.substring(path.lastIndexOf('/') + 1);

        try {
            Path folder = Paths.get(relativePath);
            if (!folder.toFile().exists()) {
                Files.createDirectories(folder);
            }
            Files.copy(file.getInputStream(), folder.resolve(fileName));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UploadFileException(e.getMessage());
        }

        return path;
    }


    @Override
    public byte[] getFileContent(String path) {

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            Files.copy(new File(path).toPath(), byteArrayOutputStream);
        } catch (IOException e) {
            throw new GetFileContentException(e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }


    public String getPath() {
        return Paths.get(basePath, UUID.randomUUID().toString()).toFile().getPath();
    }
}

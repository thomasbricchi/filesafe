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
        String fileName = UUID.randomUUID().toString();

        try {
            Path folder = Paths.get(basePath);
            createFolderWhenNotExists(folder);
            saveFileIntoFolder(file, folder.resolve(fileName));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UploadFileException(e.getMessage());
        }

        return getPath(fileName);
    }

    private void saveFileIntoFolder(MultipartFile file, Path path) throws IOException {
        Files.copy(file.getInputStream(), path);
    }

    private void createFolderWhenNotExists(Path folder) throws IOException {
        if (folderNotExists(folder)) {
            Files.createDirectories(folder);
        }
    }

    private boolean folderNotExists(Path folder) {
        return !folder.toFile().exists();
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


    public String getPath(String fileName) {
        return Paths.get(basePath, fileName).toFile().getPath();
    }
}

package com.thomas.filesafe.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Optional;

/**
 * Takes care to store files
 * <p>
 * 2019/02/20
 *
 * @author Sirius
 */
public interface StorageService {

    String uploadFile(MultipartFile file);

    byte[] getFileContent(String path);

    default String getMimeTypeFromName(String name) {
        return Optional.ofNullable(URLConnection.guessContentTypeFromName(name)).orElse("application/octet-stream");
    }


}

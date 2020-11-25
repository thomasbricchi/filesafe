package com.thomas.filesafe.dto;

import lombok.Data;

@Data
public class FileContentDTO {

    private final String fileName;

    private final byte[] content;

    private final String mimeType;

    public String logToString() {
        return "FileContentDTO{" +
            "fileName='" + fileName + '\'' +
            ", content=" + content.length +
            ", mimeType='" + mimeType + '\'' +
            '}';
    }
}

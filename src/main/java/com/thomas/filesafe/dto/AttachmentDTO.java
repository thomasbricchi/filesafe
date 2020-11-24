package com.thomas.filesafe.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class AttachmentDTO {
    private Long id;

    @NotNull
    private String fileName;

    @NotNull
    private String path;
}

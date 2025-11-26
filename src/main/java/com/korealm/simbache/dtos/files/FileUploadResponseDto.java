package com.korealm.simbache.dtos.files;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponseDto {
    private String filename;
    private String url;
    private String contentType;
    private long size;
    private String message;
}
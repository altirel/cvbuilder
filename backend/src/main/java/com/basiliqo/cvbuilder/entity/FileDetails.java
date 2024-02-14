package com.basiliqo.cvbuilder.entity;

import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.enums.FileFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDetails {

    private String filename;

    private String filepath;

    private String bucket;

    @Field("format")
    @JsonProperty("format")
    private FileFormat fileFormat;

    public static FileDetails create(String filename, FileSaveResponse fileSaveResponse, FileFormat fileFormat) {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFilename(filename);
        fileDetails.setBucket(fileSaveResponse.bucket());
        fileDetails.setFilepath(fileSaveResponse.filepath());
        fileDetails.setFileFormat(fileFormat);
        return fileDetails;
    }
}

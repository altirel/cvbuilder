package com.basiliqo.cvbuilder.mapper;

import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import io.minio.ObjectWriteResponse;

/**
 * File data structures mapper.
 */
public interface FileDataMapper {

    String NAME = "cvb_FileDataMapper";

    FileSaveResponse toFileSaveResponse(ObjectWriteResponse response);
}

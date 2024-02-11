package com.basiliqo.cvbuilder.mapper.impl;

import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.mapper.FileDataMapper;
import io.minio.ObjectWriteResponse;
import org.springframework.stereotype.Component;

@Component(FileDataMapper.NAME)
public class FileDataMapperImpl implements FileDataMapper {

    @Override
    public FileSaveResponse toFileSaveResponse(ObjectWriteResponse response) {
        return new FileSaveResponse(response.bucket(), response.object());
    }
}

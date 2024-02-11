package com.basiliqo.cvbuilder.mapper;

import com.basiliqo.cvbuilder.dto.FileSaveResponse;
import com.basiliqo.cvbuilder.mapper.impl.FileDataMapperImpl;
import io.minio.ObjectWriteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FileDataMapperTest {

    private FileDataMapper fileDataMapper;

    @BeforeEach
    public void beforeEach() {
        fileDataMapper = new FileDataMapperImpl();
    }

    @Test
    public void toFileSaveResponse_shouldPass() {
        // Preparations
        ObjectWriteResponse objectWriteResponse =
                new ObjectWriteResponse(null, "templates", "US", "/resumes/resume.pdf", null, null);

        // Call
        FileSaveResponse actual = fileDataMapper.toFileSaveResponse(objectWriteResponse);

        // Assertions
        assertEquals(objectWriteResponse.bucket(), actual.bucket());
        assertEquals(objectWriteResponse.object(), actual.filepath());
    }
}
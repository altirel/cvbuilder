package com.basiliqo.cvbuilder.enums;

public enum FileFormat {
    PDF("pdf"),
    DOCX("docx");

    private final String id;

    FileFormat(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

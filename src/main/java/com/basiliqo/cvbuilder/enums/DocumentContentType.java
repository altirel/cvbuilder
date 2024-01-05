package com.basiliqo.cvbuilder.enums;


import javax.annotation.Nullable;

public enum DocumentContentType {
    RESUME("Resume"),
    COVER_LETTER("Cover letter");

    private final String id;

    DocumentContentType(String id) {
        this.id = id;
    }

    @Nullable
    public static DocumentContentType fromId(String id) {
        for (DocumentContentType documentContentType : values()) {
            if (documentContentType.id.equals(id)) {
                return documentContentType;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }
}

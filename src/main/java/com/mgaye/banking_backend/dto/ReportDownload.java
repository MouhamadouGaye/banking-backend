package com.mgaye.banking_backend.dto;

import org.springframework.core.io.Resource;

public class ReportDownload {
    private final Resource resource;
    private final String contentType;
    private final String filename;

    public ReportDownload(Resource resource, String contentType, String filename) {
        this.resource = resource;
        this.contentType = contentType;
        this.filename = filename;
    }

    // Getters
    public Resource resource() {
        return resource;
    }

    public String contentType() {
        return contentType;
    }

    public String filename() {
        return filename;
    }

    // -----------``

}
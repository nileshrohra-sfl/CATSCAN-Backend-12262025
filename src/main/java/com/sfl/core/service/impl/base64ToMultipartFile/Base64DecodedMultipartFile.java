package com.sfl.core.service.impl.base64ToMultipartFile;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64DecodedMultipartFile implements MultipartFile {

    private final byte[] content;
    private final String header;

    public Base64DecodedMultipartFile(byte[] content, String header) {
        this.content = content;
        this.header = header.split(";")[0];
    }

    @Override
    public String getName() {
        return System.currentTimeMillis() + "_" + Math.random();
    }

    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + "_" + Math.random() + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        java.nio.file.Files.write(dest.toPath(), content);
    }
}

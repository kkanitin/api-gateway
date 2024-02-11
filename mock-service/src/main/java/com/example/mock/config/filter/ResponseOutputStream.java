package com.example.mock.config.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResponseOutputStream extends ServletOutputStream {

    protected ServletOutputStream stream;
    protected ByteArrayOutputStream cache;

    public ResponseOutputStream(ServletOutputStream stream, ByteArrayOutputStream cache) {
        super();
        this.stream = stream;
        this.cache = cache;
    }

    @Override
    public void write(int i) throws IOException {
        if (stream == null) {
            throw new IOException("ServletOutputStream stream null, unable to write");
        } else if (cache == null) {
            throw new IOException("ByteArrayOutputStream cache null, unable to write");
        }
        stream.write(i);
        cache.write(i);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}

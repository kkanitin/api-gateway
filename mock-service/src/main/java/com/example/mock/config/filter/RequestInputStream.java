package com.example.mock.config.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RequestInputStream extends ServletInputStream {

    protected ByteArrayInputStream byteArrayInputStream;

    public RequestInputStream(byte[] bytes) {
        this.byteArrayInputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}

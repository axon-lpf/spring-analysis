package com.axon.springframework.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 根据指定文件路径去读取一些配置信息
 */
public class FileSystemResource implements Resource {

    private final File file;

    private final String path;

    public FileSystemResource(File file) {
        this.file = file;
        this.path = this.getPath();
    }

    public FileSystemResource(String path) {
        this.file = new File(path);
        this.path = path;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }


    public final String getPath() {
        return this.path;
    }
}

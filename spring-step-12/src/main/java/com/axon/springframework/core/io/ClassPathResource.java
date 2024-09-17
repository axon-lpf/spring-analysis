package com.axon.springframework.core.io;

import com.axon.springframework.utils.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 默认的类加载器
 */
public class ClassPathResource implements Resource {

    /**
     * 加载路径
     */
    private final String path;

    /**
     * 类加载器
     */
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }


    @Override
    public InputStream getInputStream() throws IOException {
        InputStream resourceAsStream = classLoader.getResourceAsStream(path);
        if (resourceAsStream == null) {
            throw new FileNotFoundException(this.path + " cannot be  opened because is does not exist");
        }
        return resourceAsStream;
    }
}

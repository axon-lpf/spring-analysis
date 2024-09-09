package com.bugstack.springframework.core.io;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 默认的资源加载器实现
 */
public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
        if (location.startsWith(CLASS_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASS_URL_PREFIX.length()));
        } else {
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                return new FileSystemResource(location);
            }
        }
    }
}

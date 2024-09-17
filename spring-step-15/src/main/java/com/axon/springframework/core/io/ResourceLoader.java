package com.axon.springframework.core.io;

/**
 *  包装加载器
 */
public interface ResourceLoader {

    String CLASS_URL_PREFIX="classpath:";

    Resource getResource(String location);
}

package com.axon.springframework.beans.factory;

/**
 *  释放的接口
 */
public interface DisposableBean {

    void  destroy() throws Exception;
}

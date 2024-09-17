package com.axon.springframework.beans.factory.context;

import com.axon.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {

    void  setApplicationContext(ApplicationContext applicationContext);
}

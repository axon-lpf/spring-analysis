package com.bugstack.springframework.beans.factory.context;

import com.bugstack.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {

    void  setApplicationContext(ApplicationContext applicationContext);
}

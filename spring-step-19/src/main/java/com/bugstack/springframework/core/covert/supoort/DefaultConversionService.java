package com.bugstack.springframework.core.covert.supoort;

import com.bugstack.springframework.core.covert.converter.ConverterRegistry;

public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
    }
}

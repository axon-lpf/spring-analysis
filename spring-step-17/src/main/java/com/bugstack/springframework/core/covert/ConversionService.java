package com.bugstack.springframework.core.covert;

import com.sun.istack.internal.Nullable;

public interface ConversionService {

    /**
     * Return {@code true} if objects of {@code sourceType} can be converted to the {@code targetType}.
     */
    boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);

    /**
     * Convert the given {@code source} to the specified {@code targetType}.
     */
    <T> T convert(Object source, Class<T> targetType);
}

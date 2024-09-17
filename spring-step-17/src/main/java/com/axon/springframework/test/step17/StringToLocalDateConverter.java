package com.axon.springframework.test.step17;

import com.axon.springframework.core.covert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 时间转换
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter;

    public StringToLocalDateConverter(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, formatter);
    }
}

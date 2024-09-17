package com.axon.springframework.beans.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * 装载属性集合
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValues = new ArrayList<>();

    public void addPropertyValue(PropertyValue value) {
        this.propertyValues.add(value);
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValues.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName) {


        for (PropertyValue f : this.propertyValues) {
            if (f.getName().equals(propertyName)) {
                return f;
            }
        }
        return null;
    }
}

package com.axon.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 这个接口的主要作用是提供一种机制，方便地从注解中提取各种属性值。这在处理复杂注解时非常有用，特别是涉及动态注解解析、元注解或注解继承等高级场景。
 * <p>
 * 方法解释
 * <p>
 * 1.	Class<?> getAnnotationType():
 * •	作用：返回注解的类型（Class 对象），即我们想要提取的注解类。
 * •	使用场景：当需要处理某种特定类型的注解时，可以通过此方法确认注解的类型。
 * 2.	Object getAnnotatedElement():
 * •	作用：返回应用了注解的元素。这个元素可以是类、方法、字段、构造函数等。典型的返回类型是 Class<?>、Method、Field 等。
 * •	使用场景：在解析注解时，了解这个注解是在哪个元素上应用的，比如注解是在类上、方法上还是字段上。
 * 3.	S getSource():
 * •	作用：返回注解的来源（source）。这里 S 是一个泛型，表示数据源的类型，这个数据源可能是注解本身，也可能是元注解（注解上的注解）。
 * •	使用场景：当需要从注解源获取更多信息时，可以使用该方法。比如提取注解本身的元数据，或者获取用于处理注解的上下文对象。
 * 4.	Object getAttributeValue(Method attributeMethod):
 * •	作用：传入注解属性对应的方法，返回该属性的方法值。该方法用于从注解中提取指定的属性值。
 * •	使用场景：当我们想要获取注解中的某个特定属性值时，可以调用该方法。例如，获取一个注解中 value 属性的值。
 * <p>
 * 使用场景
 * <p>
 * •	注解解析：在框架或工具类中，可能需要对某个类或方法上的注解进行解析，比如 Spring 的注解处理机制。这个接口为框架提供了标准的方式来提取注解的属性值，而不必手动处理反射和注解解析逻辑。
 * •	元注解处理：在处理元注解（注解上的注解）时，可以通过该接口从注解中提取信息，甚至递归解析。
 * •	定制注解处理逻辑：当你开发一个框架或工具，需要动态地解析注解并基于注解做一些定制逻辑时，这个接口可以帮你抽象出注解属性提取的逻辑，减少重复代码。
 *
 * @param <S>
 */
public interface AnnotationAttributeExtractor<S> {


    Class<? extends Annotation> getAnnotationType();


    Object getAnnotatedElement();


    S getSource();


    Object getAttributeValue(Method attributeMethod);

}

package com.bugstack.springframework.core.annotation;

import java.lang.annotation.*;

/**
 * @AliasFor 是一个元注解（meta-annotation），在框架中，尤其是 Spring 框架中，常用于为注解的属性创建别名（即两个属性可以互相替代）。该注解主要用于注解之间的属性映射，或者同一注解内部的属性别名功能。通过 @AliasFor，可以让注解的多个属性互相指代，从而提供更灵活的使用方式。
 *
 * 主要作用
 *
 * 	1.	属性别名：它允许为注解的一个属性指定另一个属性作为别名，这样两个属性可以互换使用。
 * 	2.	元注解属性映射：可以将一个注解的属性映射到另一个注解中，这种情况下，它不仅仅处理同一个注解的内部属性，还可以处理不同注解之间的属性映射。
 * 	3.	简化注解使用：在注解中提供多个同义的属性（如 value 和 attribute），方便使用者不必在注解中多次重复类似的配置。
 *
 * 注解参数解释
 *
 * 	•	@AliasFor("attribute") 和 @AliasFor("value")：这两个注解参数表明 value 和 attribute 是互为别名的属性。这意味着在使用注解时，可以选择设置 value 或者 attribute，它们的值会被互相映射。
 * 	•	Class<? extends Annotation> annotation() default Annotation.class：这个参数指定了某个注解类型（默认为 Annotation.class），用于跨注解属性映射。当指定了某个注解类型时，value 或 attribute 的值会映射到该注解中的相应属性。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AliasFor {

    @AliasFor("attribute")
    String value() default "";


    @AliasFor("value")
    String attribute() default "";


    Class<? extends Annotation> annotation() default Annotation.class;
}

package com.axon.springframework.step14;

import com.axon.springframework.beans.factory.config.BeanPostProcessor;
import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.step14.bean.IUserService;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *  本章主要添加了通过注解注入属性的信息
 *  核心代码块
 *  1.1>添加自定义注解 Autowired、Qualifier、Value
 *  1.2>添加AutowiredAnnotationBeanPostProcessor 接口， 并实现对应的方法
 *      public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
 *
     *     private ConfigurableListableBeanFactory beanFactory;
     *
     *     @Override
     *     public void setBeanFactory(BeanFactory beanFactory) {
     *         this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
     *     }
 *     }
 *          // 这里是创建代理的方法
 *         Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);
 *         //TODO 设置属性的方法
 *         PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName);
 *
 * 1.3>AutowiredAnnotationBeanPostProcessor 设置属性的核心方法
 *       @Override
 *     public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
 *         // 1. 处理注解 @Value
 *         Class<?> clazz = bean.getClass();
 *         clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
 *
 *         Field[] declaredFields = clazz.getDeclaredFields();
 *
 *         for (Field field : declaredFields) {
 *              //TODO 获取对应的注解Value
 *             Value valueAnnotation = field.getAnnotation(Value.class);
 *             if (null != valueAnnotation) {
 *                 String value = valueAnnotation.value();
 *                 value = beanFactory.resolveEmbeddedValue(value);
 *                 BeanUtil.setFieldValue(bean, field.getName(), value);
 *             }
 *         }
 *
 *         // 2.TODO  处理注解 @Autowired
 *         for (Field field : declaredFields) {
 *             Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
 *             if (null != autowiredAnnotation) {
 *                 Class<?> fieldType = field.getType();
 *                 String dependentBeanName = null;
 *                 Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
 *                 Object dependentBean = null;
 *                 if (null != qualifierAnnotation) {
 *                     dependentBeanName = qualifierAnnotation.value();
 *                     dependentBean = beanFactory.getBean(dependentBeanName, fieldType);
 *                 } else {
 *                     dependentBean = beanFactory.getBean(fieldType);
 *                 }
 *                 BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
 *             }
 *         }
 *
 *         return pvs;
 *     }
 *1.5>AbstractAutowireCapableBeanFactory 中createBean中修改添加以下方法
 *          @Override
 *     protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
 *
 *         // 判断是否返回代理 Bean 对象
 *         Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
 *         if (null != bean) {
 *             return bean;
 *         }
 *         // 创建bean实例
 *         bean = createBeanInstance(beanName, beanDefinition, args);
 *
 *         // TODO 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值， 这里 通过注解去填充属性
 *         applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
 *
 *         // 设置属性填充
 *         applyPropertyValues(beanName, bean, beanDefinition);
 *
 *         //执行初始化方法 ,初始化前， 初始化后
 *         bean = initializeBean(beanName, bean, beanDefinition);
 *
 *         //注册实现DisposableBean 的接口
 *         registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
 *         if (beanDefinition.isSingleton()) {
 *             //如果是单例则创建
 *             registerSingletonBean(beanName, bean);
 *         }
 *         return bean;
 *     }
 *     TODO applyBeanPostProcessorsBeforeApplyingPropertyValues 的方法调用，调用的则是AutowiredAnnotationBeanPostProcessor 中的postProcessPropertyValues 方法
 *
 *       protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
 *         for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
 *             if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
 *                 PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
 *                 if (null != pvs) {
 *                     for (PropertyValue propertyValue : pvs.getPropertyValues()) {
 *                          //TODO 将属性添加到  beanDefinition的PropertyValue 中去， 方便后续解析赋值
 *                         beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
 *                     }
 *                 }
 *             }
 *         }
 *     }
 *
 */
public class ApiTest {


    @Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_scan.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

    @Test
    public void test_property() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_property.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService);
    }

    @Test
    public void test_beanPost() {

        BeanPostProcessor beanPostProcessor = new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) {
                return null;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                return null;
            }
        };

        List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();
        beanPostProcessors.add(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
        beanPostProcessors.remove(beanPostProcessor);

        System.out.println(beanPostProcessors.size());
    }
}

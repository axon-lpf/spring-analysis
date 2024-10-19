package com.axon.springframewrok.test.step17;

import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;


/**
 *  本章主要是添加了对数据类型的转换操作
 *  核心代码块：
 *  1.1>定义ConvertFactoryBean工厂处理
 *      public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {
 *
         private Set<?> converters;

         public void setConverters(Set<?> converters) {
             this.converters = converters;
        }

        //TODO 核心代码块
         @Override
         public void afterPropertiesSet() {
             this.conversionService = new DefaultConversionService();
             registerConverters(converters, conversionService);

         }


    //TODO 核心代码，初始化时，完成对convert的工厂注册

     private void registerConverters(Set<?> converters, ConverterRegistry registry) {
         if (converters != null) {
             for (Object converter : converters) {
                     if (converter instanceof GenericConverter) {

                     registry.addConverter((GenericConverter) converter);
             } else if (converter instanceof Converter<?, ?>) {
                     registry.addConverter((Converter<?, ?>) converter);
             } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
             } else {
                    throw new RuntimeException("类型注册转换异常");
             }
     }
    }
         }
    }

 1.2>Spring.xml 中加入以下代码
         <bean id="conversionService" class="com.axon.springframework.beans.factory.context.support.ConversionServiceFactoryBean">
         <property name="converters" ref="converters"></property>
         </bean>
 *
 *
 1.3> AbstractApplicationContext 中加入对
         protected void setConversionService(ConfigurableListableBeanFactory beanFactory) {
             // 设置类型转换器
             if (beanFactory.containsBean("conversionService")) {
                 Object conversionService = null;
             try {
                conversionService = beanFactory.getBean("conversionService");
             } catch (InstantiationException e) {
                e.printStackTrace();
             } catch (IllegalAccessException e) {
                e.printStackTrace();
             }
             if (conversionService instanceof ConversionService) {
                //TODO 核心，去设置 conversionService
                 beanFactory.setConversionService((ConversionService) conversionService);
             }
             }

         }

 1.4> refresh() 加入对转换器的注册， 注册到容器中去u

         @Override
         public void refresh() {

                 //1. 创建beanFactory,并加载BeanDefinition
                 refreshBeanFactory();

                 //2.获取beanFactory
                 ConfigurableListableBeanFactory beanFactory = getBeanFactory();

                 //3.添加ApplicationContextAwareProcessor 类，让继承自ApplicationContextAware接口的bean对象都能感知所属的ApplicationContext , 注意感知对象名称、感知加载器、感知beanFactory
                 beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

                 //4. 在bean对象实例化之前，执行BeanFactoryPostProcessor操作,这里去修改BeanDefinition中的值
                 invokeBeanFactoryPostProcessor(beanFactory);

                 //5. BeanPostProcessor需要在bean对象实例化之前注册，对象的后置处理器
                 registerBeanPostProcessor(beanFactory);

                 //6.初始化事件发布者
                 initApplicationEventMulticaster();

                 //7.注册监听事件
                 registerListeners();

                 //8.注册设置类型转换器的bean，实例化转换器bean，这里需要前置，把类型转换器注册到容器中， 然后才能对其他普通对象的属性才能转换，因为转换的时候需要从容器中获取对应的转换器，如果没有
                 //前置，普通对象属性在转换时，则是获取不到的转换器，则无法转换。
                // TODO 核心代码
                 setConversionService(beanFactory);

                 //9.提前实例化单例Bean对象
                 beanFactory.preInstantiateSingletons();

                 //10.发布容器刷新完成事件
                 finishRefresh();

         }

 1.5> 设置属性的位置，加入对转换的操作
         @Override
         protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
             PropertyValues propertyValues = beanDefinition.getPropertyValues();
             PropertyValue[] valuesPropertyValues = propertyValues.getPropertyValues();
             for (PropertyValue propertyValue : valuesPropertyValues) {
                 String name = propertyValue.getName();
                 Object value = propertyValue.getValue();
                 if (value instanceof BeanReference) {
                     BeanReference beanReference = (BeanReference) value;
                     value = getBean(beanReference.getBeanName());
                 } else {
                     //TODO  这里是对普通类型的转换.判断是否需要转换，如果需要，则进行转换
                     Class<?> sourceType = value.getClass();
                     Class<?> targetType = (Class<?>) TypeUtil.getFieldType(bean.getClass(), name);
                     ConversionService conversionService = getConversionService();
                 if (conversionService != null) {
                     if (conversionService.canConvert(sourceType, targetType)) {
                         value = conversionService.convert(value, targetType);
                     }
                 }
             }
               //属性填充,一个巨坑。 由于 CGLIB 创建的是代理类，可以通过代理类的父类（即原始类）来获取字段。使用 getSuperclass() 方法获取原始类，并从中获取字段进行操作。
             Class<?> aClass = ClassUtils.isCglibProxyClass(bean.getClass()) ? bean.getClass().getSuperclass() : bean.getClass();
             Field declaredField = aClass.getDeclaredField(name);
             declaredField.setAccessible(true);
             declaredField.set(bean, value);
            //反射设置属性填充
            BeanUtil.setFieldValue(bean, name, value);
            }
         }

 1.6> 创建ConvertFactoryBean, 注入到容器中去 此处对应的
         <!--容器中设置转换工厂-->  这里返回的是一个集合
         <bean id="converters" class="com.axon.springframewrok.test.step17.ConvertersFactoryBean">

         public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

            //TODO 核心代码， 返回一个集合
             @Override
             public Set<?> getObject() throws Exception {
                 HashSet<Object> converters = new HashSet<>();
                 StringToLocalDateConverter stringToLocalDateConverter = new StringToLocalDateConverter("yyyy-MM-dd");
                 converters.add(stringToLocalDateConverter);
                //TODO  将多个转换器添加进去
                 return converters;
             }

             @Override
             public Class<?> getObjectType() {
              return null;
             }

             @Override
             public boolean isSingleton() {
                 return true;
             }
         }


 */

public class ApiTest {

    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        System.out.println("测试结果：" + husband.getMarriageDate() + " name" + husband.getWifiName());
    }
}

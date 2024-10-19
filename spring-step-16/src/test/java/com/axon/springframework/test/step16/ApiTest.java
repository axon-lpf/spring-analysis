package com.axon.springframework.test.step16;

import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.test.step16.bean.Husband;
import com.axon.springframework.test.step16.bean.IAService;
import com.axon.springframework.test.step16.bean.IBService;
import com.axon.springframework.test.step16.bean.Wife;
import org.testng.annotations.Test;

/**
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring   可以推荐阅读该作者的相关文档
 *
 * @description 单元测试
 * @date 2022/3/16
 *
 * 本章主要为了解决循环依赖， 添加了三级缓存的处理
 * 核心代码块：
 * 1.1> DefaultSingletonBeanRegistry 获取Bean时，添加三级缓存的处理
 *          @Override
 *     public Object getSingleton(String name) {
 *         //TODO 先去一级缓存中去找
 *         Object singletonObject = singletonObjects.get(name);
 *         //TODO 找不到再去二级缓存中去找
 *         if (null == singletonObject) {
 *             singletonObject = earlySingletonObjects.get(name);
 *             if (null == singletonObject) {
 *                 //TODO 判断三级缓存中是否有对象，如果有，则这个对象就是代理对象，因为只有代理对象才会存储到三级缓存中。
 *                 ObjectFactory<?> singletonFactory = singletonFactories.get(name);
 *                 if (singletonFactory != null) {
 *                     singletonObject = singletonFactory.getObject();
 *                     //TODO 获取三级缓存中真是代理对象，存储到二级缓存中
 *                     earlySingletonObjects.put(name, singletonObject);
 *                     //TODO 移除三级缓存
 *                     singletonFactories.remove(name);
 *                 }
 *             }
 *         }

 *         return singletonObject;
 *     }
 *
 * 1.2>AbstractAutowireCapableBeanFactory 类中在创建Bean时，将创建的bean代理对象，放入到三级缓存中去
 *     @Override
 *     protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
 *
 *         Object bean = createBeanInstance(beanName, beanDefinition, args);
 *         if (beanDefinition.isSingleton()) {
 *             Object finalBean = bean;
 *             //TODO 创建一个代理对象，添加到三级缓存中
 *             addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
 *         }
 *
 *         // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
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
 *
 *1.3 >AbstractAutowireCapableBeanFactory 中的 getEarlyBeanReference  方法处理
 *       protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
 *         Object exposedObject = bean;
 *         for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
 *             if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
 *                  //TODO 核心代码， 如果被标记了切面， 则通过CGLib代理创建一个对象，返回并存入三级缓存中去。
 *                 exposedObject = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, beanName);
 *                 if (null == exposedObject) {
 *                     return exposedObject;
 *                 }
 *             }
 *         }
 *         return exposedObject;
 *     }
 *
 * 1.4>AbstractAutowireCapableBeanFactory 中 addSingletonFactory 的方法处理
 *          protected void addSingletonFactory(String name, ObjectFactory<?> singletonFactory) {
     *         //TODO 核心代码： 如果这个bean代理对象不存在三级缓存中，则添加到三级缓存中， 并移除二级缓存中对象（可能是不存在）
     *         if (!this.singletonFactories.containsKey(name)) {
     *             this.singletonFactories.put(name, singletonFactory);
     *             this.earlySingletonObjects.remove(name);
 *         }
 *     }
 *
 *
 */
public class ApiTest {

    @Test
    public void test_circular() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);
        System.out.println("老公的媳妇：" + husband.queryWife());
        System.out.println("媳妇的老公：" + wife.queryHusband());
    }

    /**
     * A依赖B , B依赖A的问题解决。 通过三级缓存
     *
     * 1.先实例化后A， 再创建一个A的代理对象放入的三级缓存中, 通过CGlib创建的。
     * 2.去填充A的属性值，发现有依赖B
     * 3.实例化B, 再次创建一个B的代理对象放入三级缓存中
     * 4.继续填充B的属性值， 发现又依赖A。
     * 5.此时从三级缓存中，取出A的代理对象赋值给B.并将A添加到二级缓存中，移除A的三级缓存， 将B 添加到一级缓存中
     * 6.继续去填充A对象属性值， 从一级缓存获取到B的对象，赋值给A , 然后添加到将A添加到一级缓存中， 移除二级缓存中的值。
     *
     */
    @Test
    public void test_ABService() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IAService iaService = applicationContext.getBean("aServiceImpl", IAService.class);
        iaService.queryBService();

        IBService ibService = applicationContext.getBean("bServiceImpl", IBService.class);
        ibService.queryAService();


    }


}
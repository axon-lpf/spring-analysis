package com.axon.springframework.step15;

import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;


/**
 *  本章主要添加了对代理对象的赋值操作
 *  核心代码块：
 *  1.1>对创建代理对象进行迁移， 迁移到后置处理器中去了
 *          @Override
 *     protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
 *
 *         // 判断是否参与了aop切面拦截，则需要代理创建。  TODO 核心这块代码删除，创建代理对象进行迁移， 迁移到后置处理器中去了
 * //       Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
 *         if (null != bean) {
 *             return bean;
 *    //     }
 *         //TODO 创建bean实例, 先去创建bean实例， 对依赖的属性进行赋值， 赋值之后再去创建代理对象。
         *Object bean=createBeanInstance(beanName,beanDefinition,args);
         *
         *         // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
         *applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName,bean,beanDefinition);
         *
         *         // 设置属性填充
         *applyPropertyValues(beanName,bean,beanDefinition);
         *
         *         //执行初始化方法 ,初始化前， 初始化后   TODO 核心在这一块， 后置处理器
         *bean=initializeBean(beanName,bean,beanDefinition);
         *
         *         //注册实现DisposableBean 的接口
         *registerDisposableBeanIfNecessary(beanName,bean,beanDefinition);
         *if(beanDefinition.isSingleton()){
         *             //如果是单例则创建
         *registerSingletonBean(beanName,bean);
         *}
         *return bean;
         *}
 *
 * 1.2> 初始化中的核心代码
 *              private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
 *
 *         //设置感知对象
 *         if (bean instanceof Aware) {
 *             if (bean instanceof BeanFactoryAware) {
 *                 ((BeanFactoryAware) bean).setBeanFactory(this);
 *             }
 *             if (bean instanceof BeanClassLoaderAware) {
 *                 ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
 *             }
 *             if (bean instanceof BeanNameAware) {
 *                 ((BeanNameAware) bean).setBeanName(beanName);
 *             }
 *         }
 *         // 初始化前处理
 *         Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
 *         // 执行初始化方法
 *         invokeInitMethods(beanName, wrappedBean, beanDefinition);
 *         // 初始化后处理  TODO 核心代码
 *         wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 *         return wrappedBean;
 *     }
 *
 *1.3> 后置处理器的处理
 *          @Override
 *     public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
 *         Object result = existingBean;
 *         List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
 *         for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
 *             // TODO  后置处理器的处理， 包装了一层代理对象出来
 *             Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
 *             if (null == current) {
 *                 return result;
 *             }
 *             result = current;
 *         }
 *         return result;
 *     }
 *
 * 1.4>
 *      public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
 *      //TODO 核心代码 包装代理对象，创建对应刀切面
 *     @Override
 *     public Object postProcessAfterInitialization(Object bean, String beanName) {
 *         //这里很坑，需要注意一下，确保产生的代理类继承了这几个类
 *         // private boolean isInfrastructureClass(Class<?> beanClass) {
 *         //        return Advice.class.isAssignableFrom(beanClass)
 *         //                || Pointcut.class.isAssignableFrom(beanClass)
 *         //                || Advisor.class.isAssignableFrom(beanClass);
 *         //
 *         //    }
 *         // 否则会发生递归，到账栈溢出
 *         if (isInfrastructureClass(bean.getClass())) {
 *             return bean;
 *         }
 *
 *         Collection<AspectJExpressionPointcutAdvisor> advisors =
 *                 beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
 *
 *         for (AspectJExpressionPointcutAdvisor advisor : advisors) {
 *
 *             ClassFilter classFilter = advisor.getPointcut().getClassFilter();
 *             if (!classFilter.matches(bean.getClass())) {   //根据表达式规则去匹配， 匹配不到跳过， 匹配到进入下面的环节
 *                 continue;
 *             }
 *
 *             AdvisedSupport advisedSupport = new AdvisedSupport();
 *             TargetSource targetSource = null;
 *
 *             try {
 *                 targetSource = new TargetSource(bean);    //包装bean，包装成目标源
 *             } catch (Exception e) {
 *                 e.printStackTrace();
 *             }
 *
 *             advisedSupport.setTargetSource(targetSource);  // 目标源
 *             advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher()); // 匹配规则
 *             advisedSupport.setInterceptor((MethodInterceptor) advisor.getAdvice()); //代理方法
 *             advisedSupport.setProxyTargetClass(false); // 设置是用jdk代理还是cglib代理
 *             return new ProxyFactory(advisedSupport).getProxy();
 *
 *         }
 *         return bean;
 *     }
 *
 *      }
 *
 */
public class ApiTest {


    @Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }
}

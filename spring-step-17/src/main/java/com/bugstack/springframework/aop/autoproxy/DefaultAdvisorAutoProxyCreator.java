package com.bugstack.springframework.aop.autoproxy;

import com.bugstack.springframework.aop.*;
import com.bugstack.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.bugstack.springframework.aop.framework.ProxyFactory;
import com.bugstack.springframework.beans.factory.BeanFactory;
import com.bugstack.springframework.beans.factory.BeanFactoryAware;
import com.bugstack.springframework.beans.factory.PropertyValues;
import com.bugstack.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {


    private DefaultListableBeanFactory beanFactory;

    private Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // 如果缓存不存在，则创建代理对象
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    /**
     * 创建代理对象
     *
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        //添加到当前的缓存中
        earlyProxyReferences.add(beanName);
        //创建代理对象
        return wrapIfNecessary(bean, beanName);
    }


    private Object wrapIfNecessary(Object bean, String beanName) {
        //这里很坑，需要注意一下，确保产生的代理类继承了这几个类
        // private boolean isInfrastructureClass(Class<?> beanClass) {
        //        return Advice.class.isAssignableFrom(beanClass)
        //                || Pointcut.class.isAssignableFrom(beanClass)
        //                || Advisor.class.isAssignableFrom(beanClass);
        //
        //    }
        // 否则会发生递归，到账栈溢出
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        Collection<AspectJExpressionPointcutAdvisor> advisors =
                beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {

            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(bean.getClass())) {   //根据表达式规则去匹配， 匹配不到跳过， 匹配到进入下面的环节
                continue;
            }

            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = null;

            try {
                targetSource = new TargetSource(bean);    //包装bean，包装成目标源
            } catch (Exception e) {
                e.printStackTrace();
            }

            advisedSupport.setTargetSource(targetSource);  // 目标源
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher()); // 匹配规则
            advisedSupport.setInterceptor((MethodInterceptor) advisor.getAdvice()); //代理方法
            //advisedSupport.setProxyTargetClass(false); // 设置是用jdk代理还是cglib代理
            advisedSupport.setProxyTargetClass(true);  // 这里不知道为啥又设置成cglib了，jdk代理会报错，反射赋值不成功。
            return new ProxyFactory(advisedSupport).getProxy();

        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        return pvs;
    }

    /**
     * 1.	isAssignableFrom：
     * •	这个方法是 Java Class 类中的一个方法，它的作用是检查当前类是否是参数类的父类或者相同类。
     * •	举例：Parent.class.isAssignableFrom(Child.class) 如果 Child 是 Parent 的子类，那么返回 true，否则返回 false。
     * •	在这个上下文中，代码用于检查 beanClass 是否是 Advice、Pointcut 或 Advisor 的子类或者它们本身。
     * 2.	判断逻辑：
     * •	代码通过 isAssignableFrom 方法检查 beanClass 是否为以下三种接口或其子类：
     * •	Advice：代表一个增强（Advice），是 AOP 中的通知行为（例如 Before Advice 或 After Advice）。
     * •	Pointcut：代表一个切点（Pointcut），它定义了在哪些方法上应用 AOP 增强逻辑。
     * •	Advisor：代表一个顾问（Advisor），它将 Advice 和 Pointcut 结合起来，形成可以在特定切点上应用的增强。
     * <p>
     * 作用：
     * <p>
     * 这段代码用来判断 beanClass 是否是 AOP 基础设施的一部分，Spring 在内部会做出不同的处理：
     * <p>
     * •	基础设施类（如 Advice、Pointcut、Advisor）不应该被 AOP 代理，因为这些类本身就是用于定义和管理 AOP 代理的。如果对它们进行代理，会导致递归或混淆代理逻辑。
     * •	因此，Spring 会对这些基础设施类跳过某些处理步骤，确保框架的运行逻辑清晰。
     * <p>
     * 这段代码用于检查某个类是否是 AOP 的基础类，以确保框架不会对这些基础设施类进行不必要的代理操作，避免混淆和递归错误。
     *
     * @param beanClass
     * @return
     */
    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass)
                || Pointcut.class.isAssignableFrom(beanClass)
                || Advisor.class.isAssignableFrom(beanClass);

    }
}

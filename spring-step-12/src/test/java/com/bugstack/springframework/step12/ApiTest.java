package com.bugstack.springframework.step12;

import com.bugstack.springframework.aop.AdvisedSupport;
import com.bugstack.springframework.aop.ClassFilter;
import com.bugstack.springframework.aop.TargetSource;
import com.bugstack.springframework.aop.adapter.MethodBeforeAdviceInterceptor;
import com.bugstack.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.bugstack.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.bugstack.springframework.aop.framework.ProxyFactory;
import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ApiTest {


    /*private AdvisedSupport advisedSupport;

    @BeforeTest
    public void init() {

        IUserService userService = new UserServiceImpl();
        advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.bugstack.springframework.step12.UserServiceImpl.*(..))"));
    }

    @Test
    public void test_proxyFactory() {

        advisedSupport.setProxyTargetClass(false); // false/true，JDK动态代理、CGlib动态代理
        IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();

        System.out.println("测试结果：" + proxy.queryUserInfo());
    }

    @Test
    public void test_beforeAdvice() {
        UserServiceBeforeAdvice userServiceBeforeAdvice = new UserServiceBeforeAdvice();
        MethodBeforeAdviceInterceptor methodBeforeAdviceInterceptor = new MethodBeforeAdviceInterceptor(userServiceBeforeAdvice);
        advisedSupport.setInterceptor(methodBeforeAdviceInterceptor);

        IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
        System.out.println("测试结果：" + proxy.queryUserInfo());

    }


    @Test
    public void test_advisor() {
        // 目标对象
        IUserService userService = new UserServiceImpl();

        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("execution(* com.bugstack.springframework.step12.UserServiceImpl.*(..))");
        advisor.setAdvice(new MethodBeforeAdviceInterceptor(new UserServiceBeforeAdvice()));

        ClassFilter classFilter = advisor.getPointcut().getClassFilter();
        if (classFilter.matches(userService.getClass())) {
            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = new TargetSource(userService);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(true); // false/true，JDK动态代理、CGlib动态代理

            IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
            System.out.println("测试结果：" + proxy.queryUserInfo());
        }
    }*/

    @Test
    public void test_aop() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = classPathXmlApplicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果" + userService.queryUserInfo());

    }
}

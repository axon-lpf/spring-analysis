package com.axon.springframework.step11;

import com.axon.springframework.aop.AdvisedSupport;
import com.axon.springframework.aop.TargetSource;
import com.axon.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.axon.springframework.aop.framework.Cglib2AopProxy;
import com.axon.springframework.aop.framework.JdkDynamicAopProxy;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ApiTest {


    @Test
    public void test_aop() throws NoSuchFieldException, NoSuchMethodException {

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.axon.springframework.step11.UserServiceImpl.*(..))");
        Class<UserServiceImpl> userServiceClass = UserServiceImpl.class;
        Method queryUserInfo = userServiceClass.getMethod("queryUserInfo");
        System.out.println(pointcut.matches(userServiceClass));
        System.out.println(pointcut.matches(queryUserInfo, userServiceClass));
    }

    @Test
    public void test_dynamic() throws NoSuchFieldException, IllegalAccessException {

        IUserService userService = new UserServiceImpl();

        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.axon.springframework.step11.UserServiceImpl.*(..))"));


        IUserService userServiceProxy = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();

        System.out.println("测试结果" + userServiceProxy.queryUserInfo());

         userServiceProxy = (IUserService) new Cglib2AopProxy(advisedSupport).getProxy();

        System.out.println("测试结果" + userServiceProxy.queryUserInfo());
    }

    @Test
    public void test_proxy_class() {
        IUserService userService = (IUserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IUserService.class}, (proxy, method, args) -> "你被代理了！");
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
    }
}

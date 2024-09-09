package com.bugstack.springframework.test;

import com.bugstack.springframework.beans.factory.config.BeanDefinition;
import com.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.bugstack.springframework.test.bean.IUserService;
import com.bugstack.springframework.test.bean.impl.UserServiceImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TestFactory {


    @Test
    public void test_BeanFactory() {
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

        defaultListableBeanFactory.registerBeanDefinition("userService", new BeanDefinition(UserServiceImpl.class));

        IUserService userService = (IUserService) defaultListableBeanFactory.getBean("userService");

        userService.queryUserInfo();

        // 再次获取调用
        userService = (IUserService) defaultListableBeanFactory.getBean("userService", "王八蛋");
        userService.queryUserInfo();
    }


    /**
     * 测试生成实例的方法
     */
    @Test
    public void test_newInstance() {

        try {
            UserServiceImpl userService = UserServiceImpl.class.newInstance();
            System.out.println(userService);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试验证有构造函数是方法
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void test_Constructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<UserServiceImpl> userServiceClass = UserServiceImpl.class;
        Constructor<UserServiceImpl> declaredConstructor =
                userServiceClass.getDeclaredConstructor(String.class);

        UserServiceImpl userService = declaredConstructor.newInstance("阿信");
        System.out.println(userService);
    }


    /**
     * 测试验证获取构造函数
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void test_parameterTypes() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<UserServiceImpl> userServiceClass = UserServiceImpl.class;
        //获取所有的构造函数
        Constructor<?>[] declaredConstructors = userServiceClass.getDeclaredConstructors();
        Constructor<?> declaredConstructor = declaredConstructors[1];

        Constructor<UserServiceImpl> declaredConstructor1 = userServiceClass.getDeclaredConstructor(declaredConstructor.getParameterTypes());
        UserServiceImpl userService = declaredConstructor1.newInstance("阿信");
        userService.queryUserInfo();

    }

    /**
     *  cglib的测试
     */
    @Test
    public void test_cglib() {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class);
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });

        UserServiceImpl o =(UserServiceImpl) enhancer.create(new Class[]{String.class}, new Object[]{"阿信"});
        o.queryUserInfo();
        System.out.println(o);

    }
}

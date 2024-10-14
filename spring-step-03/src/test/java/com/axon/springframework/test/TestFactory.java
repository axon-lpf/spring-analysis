package com.axon.springframework.test;

import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.axon.springframework.test.bean.IUserService;
import com.axon.springframework.test.bean.impl.UserServiceImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *  本章节主要添加了cglib的代理处理， 根据beanDefinition中存储的信息，解析出来，然后通过cglib创建对应的实例对象，存储 bean中去。
 *
 *  创建有两种方式：
 *  1.1>第一种
 *      bean = beanDefinition.getBeanClass().newInstance();
 *  1.2>第二种
 *       @Override
 *     protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) {
 *         Constructor constructorToUse = null;
 *         Class<?> beanClass = beanDefinition.getBeanClass();
 *         //TODO 解析构造函数
 *         Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
 *         for (Constructor ctor : declaredConstructors) {
 *             if (null != args && ctor.getParameterTypes().length == args.length) {
 *                 constructorToUse = ctor;
 *                 break;
 *             }
 *         }
 *         //TODO 核心的创建流程
 *         return instantiationStrategy.instantiate(beanDefinition,beanName,constructorToUse,args);
 *     }
 *     // TODO 这里使用的cjlib的创建方式
 *      @Override
 *     public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
 *         Enhancer enhancer = new Enhancer();
 *         enhancer.setSuperclass(beanDefinition.getBeanClass());
 *         enhancer.setCallback(new NoOp() {
 *             @Override
 *             public int hashCode() {
 *                 return super.hashCode();
 *             }
 *         });
 *         if (null == ctor) {
 *             return enhancer.create();
 *         }
 *
 *         return enhancer.create(ctor.getParameterTypes(), args);
 *     }
 *
 *     //TODO 这里又是另一种方式， 代码中未使用
 *      @Override
 *     public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) {
 *             Class<?> beanClass = beanDefinition.getBeanClass();
 *             if (null != args) {
 *                 return beanClass.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
 *             } else {
 *                 return beanClass.getDeclaredConstructor().newInstance();
 *             }
 *     }
 *
 *
 */
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

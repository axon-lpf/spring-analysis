package com.axon.springframework.step12;

import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

/**
 *  本章节主要将Aop整合到Spring中去
 *  核心代码块
 *  1.1> AbstractAutowireCapableBeanFactory 修改createBean方法
 *          @Override
 *     protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
 *
 *         //TODO 核心代码 判断是否返回代理 Bean 对象， 这里去创建代理对象
 *         Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
 *         if (null != bean) {
 *             return bean;
 *         }
 *         // 创建bean实例
 *         bean = createBeanInstance(beanName, beanDefinition, args);
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
 *       protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
 *         // 1.TODO  尝试在实例化之前应用后置处理器 ，在前置处理器中，加入对代理对象的处理
 *         Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
 *         // 2. 如果实例化前后置处理器生成了一个对象（通常是代理对象）
 *         if (null != bean) {
 *             // 3. 直接调用初始化后置处理器，不再进行默认的实例化流程
 *             bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 *         }
 *
 *         return bean; // 返回代理对象或空
 *     }
     *  //TODO 获取对应的InstantiationAwareBeanPostProcessor 的实例对象， 对其进行创建代理的操作
 *     protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
 *         for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
 *             if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
 *                 Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
 *                 if (null != result) {
 *                     return result;
 *                 }
 *             }
 *         }
 *         return null;
 *     }
 *
 *     //TODO  真正的创建代理的地方
 *     @Override
 *     public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
 *
 *         //这里很坑，需要注意一下，确保产生的代理类继承了这几个类
 *         // private boolean isInfrastructureClass(Class<?> beanClass) {
 *         //        return Advice.class.isAssignableFrom(beanClass)
 *         //                || Pointcut.class.isAssignableFrom(beanClass)
 *         //                || Advisor.class.isAssignableFrom(beanClass);
 *         //
 *         //    }
 *         // TODO 否则会发生递归，到账栈溢出, 这里是核心
 *         if (isInfrastructureClass(beanClass)) {
 *             return null;
 *         }
 *
 *         Collection<AspectJExpressionPointcutAdvisor> advisors =
 *                 beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
 *
 *          //TODO  这里会获取拦截的镀锌
 *         for (AspectJExpressionPointcutAdvisor advisor : advisors) {
 *
 *             ClassFilter classFilter = advisor.getPointcut().getClassFilter();
 *             if (!classFilter.matches(beanClass)) {
 *             //TODO 如果没有命中， 则跳出， 如StudentService 没有被配置execution(* com.axon.springframework.step12.IStudentService.*(..)),所以会跳出， 返回null，走正常创建bean的流程
 *                 continue;
 *             }
 *
 *             AdvisedSupport advisedSupport = new AdvisedSupport();
 *             TargetSource targetSource = null;
 *
 *             try {
 *                 targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
 *             } catch (Exception e) {
 *                 e.printStackTrace();
 *             }
 *
 *             advisedSupport.setTargetSource(targetSource);
 *             advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
 *             advisedSupport.setInterceptor((MethodInterceptor) advisor.getAdvice());
 *             advisedSupport.setProxyTargetClass(false);
 *             return new ProxyFactory(advisedSupport).getProxy();
 *
 *         }
 *         return null;
 *     }
 *
 *  1.2>使用
 *       ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
 *         IUserService userService = classPathXmlApplicationContext.getBean("userService", IUserService.class);
 *         System.out.println("测试结果" + userService.queryUserInfo());
 */
public class ApiTest {


    /*private AdvisedSupport advisedSupport;

    @BeforeTest
    public void init() {

        IUserService userService = new UserServiceImpl();
        advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.axon.springframework.step12.UserServiceImpl.*(..))"));
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
        advisor.setExpression("execution(* com.axon.springframework.step12.UserServiceImpl.*(..))");
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


        IStudentService studentService = classPathXmlApplicationContext.getBean("studentService", IStudentService.class);

        studentService.testQueryStudent();

    }
}

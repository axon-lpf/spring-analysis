package com.axon.springframework.step11;

import com.axon.springframework.aop.AdvisedSupport;
import com.axon.springframework.aop.TargetSource;
import com.axon.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.axon.springframework.aop.framework.Cglib2AopProxy;
import com.axon.springframework.aop.framework.JdkDynamicAopProxy;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 *  本章主要写基于JDK 和CGlib 的实现AOP切面
 *  核心代码块块逻辑
 *  1.1>添加Pointcut, ClassFilter, MethodMatcher 对应的接口
 *      在 Spring AOP（面向切面编程）中，Pointcut、ClassFilter 和 MethodMatcher 是用来定义切面逻辑的重要组件。它们的作用如下：
         * Pointcut：
         *
         * Pointcut 是一种表达式，用于定义哪些连接点（即可以应用通知的地方）应该被拦截。通常，Pointcut 会指定某个类的特定方法，或者一组符合某个条件的方法。
         * 通过定义切点，你可以控制哪些方法会被拦截并应用相应的通知（例如，前置通知、后置通知等）。
         * ClassFilter：
         *
         * ClassFilter 是用于过滤类的接口。它允许你定义哪些类应该被考虑作为连接点。具体来说，它通常用于限制哪些类的所有方法都可以被 Pointcut 拦截。
         * ClassFilter 可以帮助你提高性能，避免对不必要的类进行切面处理。
         * MethodMatcher：
         *
         * MethodMatcher 是用于过滤方法的接口。它定义了一个 matches 方法，用于检查特定方法是否符合切点的条件。
         * 这个接口允许你根据方法的名称、参数类型、返回类型等来判断该方法是否应该被拦截。
         * 总结来说，Pointcut 用于定义何时应用通知，ClassFilter 过滤适用的类，而 MethodMatcher 则在类内进一步过滤具体的方法。这三个组件一起工作，以实现灵活的切面编程。
 *
 * 1.2>添加AspectJExpressionPointcut 表达式 ，实现Pointcut, ClassFilter, MethodMatcher 对应的接口的方法
 * 1.3> MethodInvocation 的使用
 *      MethodInvocation 接口是 Spring AOP 中的重要组成部分，主要用于表示对方法的调用。它的功能和作用包括：
 *
         * 封装方法调用：
         *
         * MethodInvocation 封装了一个具体方法的调用信息，包括目标对象、方法、方法参数等。
         * 提供方法执行的上下文：
         *
         * 它提供了执行方法所需的上下文信息，使得在执行切面逻辑时可以访问这些信息。
         * 执行方法：
         *
         * MethodInvocation 接口通常提供一个 proceed() 方法，该方法用于实际执行被拦截的方法。这允许切面在执行目标方法之前或之后添加自定义逻辑。
         * 访问目标对象和方法信息：
         *
         * 通过 MethodInvocation，你可以获取目标对象的引用、方法的名称、参数类型等信息，这对于实现更复杂的切面逻辑非常有用。
         * 与切面结合使用：
         *
         * MethodInvocation 常与切面（如环绕通知）一起使用，以便在方法执行前后添加特定的行为（例如，日志记录、性能监控、事务管理等）。
         * 总结来说，MethodInvocation 接口为 AOP 提供了一个灵活的方式来处理方法调用，使得开发者可以在保持代码清晰的同时，添加跨越多个模块的关注点。
 *
 * 1.4>InvocationHandler的使用
 *      InvocationHandler 是 Java 动态代理机制中的一个接口，其功能和作用主要包括：
 *
         * 拦截方法调用：
         * InvocationHandler 用于定义代理对象的方法调用逻辑。当代理对象的方法被调用时，会被转发到 InvocationHandler 的 invoke 方法中。
         * 实现动态代理：
         *
         * 通过 InvocationHandler，你可以在运行时为接口生成代理实现，从而在不改变目标类的情况下增强其功能（如添加日志、事务处理等）。
         * 提供上下文信息：
         *
         * invoke 方法的参数包含了被调用的方法、目标对象及其参数，这使得你可以根据需要处理方法调用。
         * 灵活性：
         *
         * 允许在代理中实现通用的处理逻辑，可以根据具体的业务需求动态调整方法调用的行为。
         * 总结来说，InvocationHandler 使得动态代理的实现成为可能，提供了灵活的方法拦截机制，广泛应用于面向切面编程和设计模式中。
 *
 * 1.5>定义AopProxy接口，实现创建代理类
 *      public interface AopProxy {
 *          Object getProxy();
 *      }
 *
 * 1.6> CGlib的动态代理类的实现
 *
 *      //TODO 核心实现类Cglib2AopProxy
 *      public class Cglib2AopProxy implements AopProxy {
 *
 *     private final AdvisedSupport advisedSupport;
 *
 *     public Cglib2AopProxy(AdvisedSupport advisedSupport) {
 *         this.advisedSupport = advisedSupport;
 *     }
 *     //TODO 核心方法
 *     @Override
 *     public Object getProxy() {
 *
 *         Enhancer enhancer = new Enhancer();
 *         Class<?> aClass = advisedSupport.getTargetSource().getTarget().getClass();
 *         aClass = ClassUtils.isCglibProxyClass(aClass) ? aClass.getSuperclass() : aClass;
 *         enhancer.setSuperclass(aClass);
 *         enhancer.setInterfaces(advisedSupport.getTargetSource().getTargetClass());
 *         enhancer.setCallback(new DynamicAdvisedInterceptor(advisedSupport));
 *         return enhancer.create();
 *     }
 *
 * 1.7>java中JDK的动态代理 JdkDynamicAopProxy， 接口使用JDK动态代理
 *      //TODO 核心的动态代理
 *      public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
 *
     *     private final AdvisedSupport advisedSupport;
     *
     *     public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
     *         this.advisedSupport = advisedSupport;
     *     }
 *
 *      //TODO  核心方法
 *
 *         @Override
     *     public Object getProxy() {
     *         return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), advisedSupport.getTargetSource().getTargetClass(), this);
     *     }
 *
 *     }
 *
 * 1.8> 使用方式
 *
 *        IUserService userService = new UserServiceImpl();
 *        //设置
 *         AdvisedSupport advisedSupport = new AdvisedSupport();
 *         advisedSupport.setTargetSource(new TargetSource(userService));
 *         advisedSupport.setInterceptor(new UserServiceInterceptor());
 *         //扫描对应的类
 *         advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.axon.springframework.step11.UserServiceImpl.*(..))"));
 *
 *          //JDK 动态代理
 *         IUserService userServiceProxy = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
 *
 *         System.out.println("测试结果" + userServiceProxy.queryUserInfo());
 *
 *
 * CGlib 和JDK动态代理的类的区别是什么
 *  JDK 动态代理和 CGLIB 动态代理的区别主要体现在以下几个方面：
 *
         * 代理方式：
         *
         * JDK 动态代理：只能代理实现了接口的类，生成的代理类必须实现指定的接口。
         * CGLIB 动态代理：通过继承目标类生成代理类，能够代理没有接口的类。
         * 性能：
         *
         * JDK 动态代理：性能相对较低，因为每次调用都需要通过反射。
         * CGLIB 动态代理：由于是生成字节码文件，性能较高，但对于复杂的类结构可能会消耗更多内存。
         * 使用场景：
         *
         * JDK 动态代理：适用于接口驱动的设计，特别是在 AOP 中应用广泛。
         * CGLIB 动态代理：适用于需要对具体类进行代理的场景，尤其是没有接口或需要代理非接口方法的情况。
         * 继承与委托：
         *
         * JDK 动态代理：通过接口进行委托。
         * CGLIB 动态代理：通过继承目标类进行代理，因此不能对 final 类和 final 方法进行代理。
         * 总结来说，选择哪种代理方式主要取决于你的需求是否需要代理接口，或是针对具体的类进行动态代理。
 *
 *

         * CGLIB  能代理接口吗？
             * CGLIB 动态代理主要是通过生成目标类的子类来实现代理，因此它并不依赖于接口。虽然 CGLIB 可以代理实现接口的类，但其主要设计目的是为了能够代理没有实现接口的类。
         *
         * 总结：
         *
         * CGLIB 既可以代理实现接口的类，也可以代理没有接口的类。
         * 但是，由于 CGLIB 是通过继承实现代理的，所以它不能代理 final 类和 final 方法。
         * 因此，如果你的目标类是 final 的，使用 CGLIB 进行代理就会失败。这时可以考虑使用 JDK 动态代理。
 *
 *
 *
 */
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

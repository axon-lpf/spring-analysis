package com.axon.springframework.step10;

import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

/**
 * 在 Spring 中，容器事件（ApplicationEvent）机制允许应用程序的各个部分通过发布和监听事件进行异步通信。Spring 提供了一个事件驱动的编程模型，开发者可以通过监听和发布事件来实现模块之间的解耦，增强系统的可扩展性和灵活性。
 * <p>
 * 容器事件的使用场景
 * <p>
 * 1.	组件解耦和模块间通信：
 * •	场景：当你有多个模块需要相互通信，但又不希望它们直接依赖时，可以通过事件发布和监听实现松耦合。一个组件可以发布事件，多个监听器可以响应事件，彼此之间没有直接依赖。
 * •	示例：用户注册成功后，你可以发布一个 UserRegisteredEvent，各个模块监听该事件来执行额外的逻辑（如发送欢迎邮件、初始化用户设置等）。
 * 2.	异步处理：
 * •	场景：某些业务场景需要异步处理，但不希望阻塞主流程。你可以发布事件并让事件的监听器异步处理任务。
 * •	示例：用户注册后，发送激活邮件的操作不需要立即完成，可以将其作为一个事件处理异步执行。
 * 3.	系统初始化和关闭：
 * •	场景：在系统启动和关闭时，某些组件需要执行特定的初始化或清理操作。
 * •	示例：当 Spring 容器启动时，ContextRefreshedEvent 会被发布，监听这个事件的组件可以在此时进行资源加载、数据预热等操作。在容器关闭时，监听 ContextClosedEvent 来做资源释放或持久化操作。
 * 4.	动态扩展功能：
 * •	场景：你可以在应用中增加动态的扩展模块，使用事件系统实现模块化的功能。
 * •	示例：一个电商系统的支付模块可以发布 PaymentSuccessEvent，而物流模块监听该事件，触发发货流程。通过事件的发布和监听，可以轻松扩展功能而不需要修改已有代码。
 * 5.	状态变更通知：
 * •	场景：当某些状态发生变化时，其他组件需要响应。通过事件通知机制，某个组件的状态变更可以被其他相关组件感知并执行相应操作。
 * •	示例：购物车状态变化时发布 CartUpdatedEvent，其他模块如优惠券系统可以监听该事件，并重新计算可用优惠券。
 * 6.	用户行为分析：
 * •	场景：你可以通过发布用户行为相关的事件，来实现用户行为的监控和分析。
 * •	示例：当用户进行某些重要操作（如登录、下单等）时，发布 UserActionEvent，分析模块监听该事件来收集用户行为数据，并进行分析和决策。
 *
 *
 *  本章节， 主要添加了事件监听的功能
 *  核心步骤
 *
 *  1.1> 添加一个事件的广播器 ApplicationEventMulticaster
 *      public interface ApplicationEventMulticaster {
 *          //TODO 添加监听
 *          void addApplicationListener(ApplicationListener<?> listener);
            //TODO 移除监听事件
            *void removeApplicationListener(ApplicationListener<?> listener);
            //TODO 广播事件， 推送时间消息
        *   void multicastEvent(ApplicationEvent event);
        *
        *}
 *
 * 1.2> 定义一个事件监听器接口
 *      public interface  ApplicationListener<E extends ApplicationEvent> extends EventListener {
 *
 *          void  onApplicationEvent(E event);
 *      }
 *
 * 1.2>AbstractApplicationContext 中 refresh()
 *      @Override
 *     public void refresh() {
 *
 *         //1. 创建beanFactory,并加载BeanDefinition
 *         refreshBeanFactory();
 *
 *         //2.获取beanFactory
 *         ConfigurableListableBeanFactory beanFactory = getBeanFactory();
 *
 *         //3.添加ApplicationContextAwareProcessor 类，让继承自ApplicationContextAware接口的bean对象都能感知所属的ApplicationContext
 *         beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
 *
 *         //4. 在bean对象实例化之前，执行BeanFactoryPostProcessor操作
 *         invokeBeanFactoryPostProcessor(beanFactory);
 *
 *         //5. BeanPostProcessor需要子啊将bean对象实例化之前注册
 *         registerBeanPostProcessor(beanFactory);
 *
 *         //TODO 6.初始化事件发布者
 *         initApplicationEventMulticaster();
 *
 *         //TODO 7.注册监听事件
 *         registerListeners();
 *
 *         //8.提前实例化单例bean对象
 *         beanFactory.preInstantiateSingletons();
 *
 *         //TODO 9.发布容器刷新完成事件
 *         finishRefresh();
 *
 *     }
 *
 * 1.3 初始化事件发布者的代码
 *          private void initApplicationEventMulticaster() {
     *         ConfigurableListableBeanFactory beanFactory = getBeanFactory();
 *              // TODO  SimpleApplicationEventMulticaster 自定义的事件广播器， 实现了ApplicationEventMulticaster 接口。
 *              //TODO beanFactory 是容器对象， 将容器对象传入进去
     *         applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
 *              //TODO  往容器中注册事件bean
     *         beanFactory.registerSingletonBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
 *
 *          }

 1.4 监听事件的核心方法
        private void registerListeners() {
                Collection<ApplicationListener> values = getBeansOfType(ApplicationListener.class).values();
                values.forEach(f -> {
                applicationEventMulticaster.addApplicationListener(f);
                });
        }
 *
 1.5> 完成对事件的调用核心方法

        //TODO 对应refresh中的第9步
        private void finishRefresh() {
            publishEvent(new ContextRefreshedEvent(this));
        }

         @Override
         public void publishEvent(ApplicationEvent event) {
                applicationEventMulticaster.multicastEvent(event);
         }

 1.6> 事件的使用， 自定义事件， 将事件发布到容器中

         public class CustomEventListener implements ApplicationListener<CustomEvent> {

             @Override
             public void onApplicationEvent(CustomEvent event) {
                 System.out.println("收到：" + event.getSource() + "消息;时间：" + new Date());
                 System.out.println("消息：" + event.getId() + ":" + event.getMessage());
             }
         }

         public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
             @Override
             public void onApplicationEvent(ContextRefreshedEvent event) {
                    System.out.println("刷新事件：" + this.getClass().getName());
             }
         }

         public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
             @Override
             public void onApplicationEvent(ContextClosedEvent event) {
                System.out.println("关闭事件：" + this.getClass().getName());
             }
         }

1.7> xml 中添加以下代码块
         <beans>
         <bean class="ContextClosedEventListener"></bean>
         <bean class="ContextRefreshedEventListener"></bean>
         <bean class="CustomEventListener"></bean>
         </beans>
 *
 *
 *
 *
 */
public class ApiTest {

    @Test
    public void test_event() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 100000, "发布自定义事件"));
        applicationContext.registerShutdownHook();
    }

}

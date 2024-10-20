package com.axon.springframework.test;

import com.alibaba.fastjson.JSON;
import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.test.dao.IAccountInfo;
import org.testng.annotations.Test;

import java.util.List;


public class ApiTest {


    /**
     *  总体的核心流程：
     *
     * 1.扫描注入MapperScannerConfigurer ,它又依赖sqlSessionFactory
     * 2.提前去修改Dao的BeanDefition 中的属性值，设置beanClass 为MapperFactoryBean
     * 3.  AccountInfo accountInfo = iAccountInfo.queryUserInfoById("黑狗"); 调用时，通过实际的代理对象去操作，则使用MapperFactoryBean
     *
     *
     * 核心步骤
     * 1.1>添加MapperFactoryBean
     *      public class MapperFactoryBean<T> implements FactoryBean<T> {
     *          private Class<T> mapperInterface;
     *          private SqlSessionFactory sqlSessionFactory;
     *
     *           //TODO 核心步骤， 创建代理对象，去执行sql的步骤
             *     @Override
             *     public T getObject() throws Exception {
             *         InvocationHandler handler = (proxy, method, args) -> {
             *             // 排除 Object 方法；toString、hashCode
             *             if (Object.class.equals(method.getDeclaringClass())) {
             *                 return method.invoke(this, args);
             *             }
             *             try {
             *                 System.out.println("你被代理了，执行SQL操作！" + method.getName());
             *                 return sqlSessionFactory.openSession().selectOne(mapperInterface.getName() + "." + method.getName(), args[0]);
             *             } catch (Exception e) {
             *                 e.printStackTrace();
             *             }
             *             return method.getReturnType().newInstance();
             *         };
             *         return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{mapperInterface}, handler);
             *     }
     *
     *      }
     *
     * 1.2>初始化SqlSessionFactoryBean , 将dataSource注入进去
     *
     *      public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean
     *      {
     *      // TODO 核心代码块
         *     @Override
         *     public void afterPropertiesSet() {
         *
         *         DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
         *         Resource resource = defaultResourceLoader.getResource(this.resource);
         *         try (InputStream inputStream = resource.getInputStream()) {
         *             this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
         *         } catch (Exception e) {
         *             e.printStackTrace();
         *         }
     *     }
     *
     *      }
     *
     * 1.3> 添加
     *      public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor{
     *
     *              @Override
     *     public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
     *         try {
     *             Set<Class<?>> classes = ClassScanner.scanPackage(basePackage); // 扫描包
     *             for (Class<?> clazz : classes) {
     *                 // Bean 对象定义
     *                 BeanDefinition beanDefinition = new BeanDefinition(clazz);
     *                 PropertyValues propertyValues = new PropertyValues();
     *                 TODO 核心代码块，
     *                 propertyValues.addPropertyValue(new PropertyValue("mapperInterface", clazz));   // 注入实际的接口类型。方便MapperFactoryBean 去创建代理对象
     *                 propertyValues.addPropertyValue(new PropertyValue("sqlSessionFactory", sqlSessionFactory));  //注入数据库连接工厂， 方便MapperFactoryBean去操作数据库
     *                 beanDefinition.setPropertyValues(propertyValues);
     *                 //根据指定的Dao接口设置实际的BeanClass类型，最终在创建bean的时候，实例化的是MapperFactoryBean，
     *                 //通MapperFactoryBean 的getObject 获取一个代理对象，然后执行数据库的访问方法。
     *                 //TODO 核心代码
     *                 beanDefinition.setBeanClass(MapperFactoryBean.class);
     *                 // Bean 对象注册
     *                 registry.registerBeanDefinition(clazz.getSimpleName(), beanDefinition);
     *             }
     *         } catch (Exception e) {
     *             e.printStackTrace();
     *         }
     *     }
     *      }
     *
     *1.4 spring.xml中加入以下信息， 在构建容器的时候注入进去。
     *
     *          <!--   注入数据库连接池-->
     *     <bean id="sqlSessionFactory" class="com.axon.springframework.mybatis.SqlSessionFactoryBean">
     *         <property name="resource" value="classpath:mybatis-config-datasource.xml"/>
     *     </bean>
     *
     *     <!--     提前注入扫描器-->
     *     <bean class="com.axon.springframework.mybatis.MapperScannerConfigurer">
     *         <!-- 注入sqlSessionFactory -->
     *         <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
     *         <!-- 给出需要扫描Dao接口包 -->
     *         <property name="basePackage" value="com.axon.springframework.test.dao"/>
     *     </bean>
     *
     */
     *
    @Test
    public void test_queryUserInfoById() {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IAccountInfo iAccountInfo = classPathXmlApplicationContext.getBean("IAccountInfo", IAccountInfo.class);
        AccountInfo accountInfo = iAccountInfo.queryUserInfoById("黑狗"); // 这里是通过代理对象去操作数据库的
        System.out.println(JSON.toJSONString(accountInfo));

        List<AccountInfo> accountInfos = iAccountInfo.queryUserList();
        System.out.println(JSON.toJSONString(accountInfo));
    }
}

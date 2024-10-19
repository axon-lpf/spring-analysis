package com.axon.springframework.step18;

import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 *  本章主要整合了Jdbc的相关功能
 *  核心底代码：
 *  1.1>添加JdbcTemplate 继承了JdbcAccess， JdbcAccess中有dataSource的注入
 *      public class JdbcTemplate extends JdbcAccess implements JdbcOperations
 *      }
 *
 *      //TODO  JdbcAccess implements InitializingBean 实现了初始化的代码
 *      public abstract class JdbcAccess implements InitializingBean {
 *
 *
     *     private DataSource dataSource;
     *
     *     public DataSource getDataSource() {
     *         return dataSource;
     *     }
     *
     *     public void setDataSource(DataSource dataSource) {
     *         this.dataSource = dataSource;
     *     }
     *
     *     protected DataSource obtainDataSource() {
     *         DataSource dataSource = getDataSource();
     *         Assert.state(dataSource != null, "No DataSource set");
     *         return dataSource;
     *     }
     *
 *         //TODO 核心代码，初始化数据源
     *     @Override
     *     public void afterPropertiesSet() {
     *         //TODO  通过注入的方式
 *          if (getDataSource() == null) {
     *             throw new RuntimeException("数据库配置未初始化");
     *         }
     *     }
 *
 * }
 *
 * 1.2> 加入相关的xml文件配置，通过xml解析，注入相关的配置
 *       <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
 *         <property name="driverClass" value="com.mysql.cj.jdbc.Driver"/>
 *         <property name="jdbcUrl" value="jdbc:mysql://192.168.2.104:3306/my_test_db?useSSL=false"></property>
 *         <property name="username" value="root"></property>
 *         <property name="password" value="123456"></property>
 *     </bean>
 *
 *     <bean id="jdbcTemplate"
 *           class="JdbcTemplate">
 *         <property name="dataSource" ref="dataSource"/>
 *     </bean>
 *
 */
public class ApiTest {


    private JdbcTemplate jdbcTemplate;

    @BeforeTest
    public void before() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        jdbcTemplate = classPathXmlApplicationContext.getBean("jdbcTemplate", JdbcTemplate.class);
    }


    @Test
    public void execute(){
        jdbcTemplate.execute("insert into account (name, balance) values ('184172133','1000') ");
    }


    @Test
    public void queryForListTest() {
        List<Map<String, Object>> allResult = jdbcTemplate.queryForList("select * from account");
        for (Map<String, Object> objectMap : allResult) {
            System.out.println("测试结果：" + objectMap);
        }
    }




}

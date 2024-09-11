package com.bugstack.springframework.test.bean;

import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

/**
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 *
 * @description 单元测试
 * @date 2022/3/16
 */
public class ApiTest {

    @Test
    public void test_circular() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);
        System.out.println("老公的媳妇：" + husband.queryWife());
        System.out.println("媳妇的老公：" + wife.queryHusband());
    }

    /**
     * A依赖B , B依赖A的问题解决。 通过三级缓存
     *
     * 1.先实例化后A， 再创建一个A的代理对象放入的三级缓存中
     * 2.去填充A的属性值，发现有依赖B
     * 3.实例化B, 再次创建一个B的代理对象放入三级缓存中
     * 4.继续填充B的属性值， 发现又依赖A。
     * 5.此时从三级缓存中，取出A的代理对象赋值给B.并将A添加到二级缓存中，移除A的三级缓存， 将B 添加到一级缓存中
     * 6.继续去填充A对象属性值， 从一级缓存获取到B的对象，赋值给A , 然后添加到将A添加到一级缓存中， 移除二级缓存中的值。
     *
     */
    @Test
    public void test_ABService() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IAService iaService = applicationContext.getBean("aServiceImpl", IAService.class);
        iaService.queryBService();

        IBService ibService = applicationContext.getBean("bServiceImpl", IBService.class);
        ibService.queryAService();


    }


}
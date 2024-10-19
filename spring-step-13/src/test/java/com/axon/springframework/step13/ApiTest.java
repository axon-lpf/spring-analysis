package com.axon.springframework.step13;

import com.axon.springframework.beans.factory.config.BeanPostProcessor;
import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.step13.bean.IUserService;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *  本章主要添加主动扫描和注册bean 对象。
 *  核心代码块:
 *  1.1>添加注解
 *      @Target(ElementType.TYPE)
     * @Retention(RetentionPolicy.RUNTIME)
     * @Documented
     * public @interface Component {
     *
     *     String value() default "";
     *
     * }
 *
 * 1.2>修改XmlBeanDefinitionReader 中解析的核心代码
 *          protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
 *         SAXReader reader = new SAXReader();
 *         Document document = reader.read(inputStream);
 *         Element root = document.getRootElement();
 *
 *         //TODO  解析 context:component-scan 标签，扫描包中的类并提取相关信息，用于组装 BeanDefinition
 *         Element componentScan = root.element("component-scan");
 *         if (null != componentScan) {
 *              //TODO 核心代码块1
 *             String scanPath = componentScan.attributeValue("base-package");
 *             if (StrUtil.isEmpty(scanPath)) {
 *                 throw new RuntimeException("The value of base-package attribute can not be empty or null");
 *             }
 *             //TODO 核心代码2
 *             scanPackages(scanPath);
 *         }
 *
 *         List<Element> beanList = root.elements("bean");
 *         for (Element bean : beanList) {
 *
 *             String id = bean.attributeValue("id");
 *             String name = bean.attributeValue("name");
 *             String className = bean.attributeValue("class");
 *             String initMethod = bean.attributeValue("init-method");
 *             String destroyMethodName = bean.attributeValue("destroy-method");
 *             String beanScope = bean.attributeValue("scope");
 *
 *             // 获取 Class，方便获取类中的名称
 *             Class<?> clazz = Class.forName(className);
 *             // 优先级 id > name
 *             String beanName = StrUtil.isNotEmpty(id) ? id : name;
 *             if (StrUtil.isEmpty(beanName)) {
 *                 beanName = StrUtil.lowerFirst(clazz.getSimpleName());
 *             }
 *
 *             // 定义Bean
 *             BeanDefinition beanDefinition = new BeanDefinition(clazz);
 *             beanDefinition.setInitMethodName(initMethod);
 *             beanDefinition.setDestroyMethodName(destroyMethodName);
 *
 *             if (StrUtil.isNotEmpty(beanScope)) {
 *                 beanDefinition.setScope(beanScope);
 *             }
 *
 *             List<Element> propertyList = bean.elements("property");
 *             // 读取属性并填充
 *             for (Element property : propertyList) {
 *                 // 解析标签：property
 *                 String attrName = property.attributeValue("name");
 *                 String attrValue = property.attributeValue("value");
 *                 String attrRef = property.attributeValue("ref");
 *                 // 获取属性值：引入对象、值对象
 *                 Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
 *                 // 创建属性信息
 *                 PropertyValue propertyValue = new PropertyValue(attrName, value);
 *                 beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
 *             }
 *             if (getRegistry().containsBeanDefinition(beanName)) {
 *                 throw new RuntimeException("Duplicate beanName[" + beanName + "] is not allowed");
 *             }
 *             // 注册 BeanDefinition
 *             getRegistry().registerBeanDefinition(beanName, beanDefinition);
 *         }
 *     }
 *
 *     //TODO 核心代码3
 *     private void scanPackages(String scanPath) {
 *         String[] basePackages = StrUtil.splitToArray(scanPath, ',');
 *         ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
 *         scanner.doScan(basePackages);
 *     }
 * 1.3> ClassPathBeanDefinitionScanner 中doScan代码
 *          //TODO 核心代码4
 *          public void doScan(String... basePackages) {
 *         for (String basePackage : basePackages) {
 *              //TODO 5 获取所有的BeanDefinition
 *             Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
 *
 *             for (BeanDefinition beanDefinition : candidateComponents) {
 *                 String s = resolveBeanScope(beanDefinition);
 *                 if (StrUtil.isNotEmpty(s)){
 *                     beanDefinition.setScope(s);
 *                 }
 *                 //TODO 6核心代码解析key值
 *                String key= determineBeanName(beanDefinition);
 *                //TODO 将beanDefinition 添加的缓存中去
 *                 registry.registerBeanDefinition(key,beanDefinition);
 *             }
 *         }
 *     }
 *          //TODO 7 核心代码
 *         private  String determineBeanName(BeanDefinition beanDefinition){
     *         Class<?> beanClass = beanDefinition.getBeanClass();
     *         Component component = beanClass.getAnnotation(Component.class);
     *         String value = component.value();
     *         if (StrUtil.isEmpty(value)) {
     *             value = StrUtil.lowerFirst(beanClass.getSimpleName());
     *         }
     *         return value;
 *     }
 *
         *   //TODO 8 核心代码
         *     public Set<BeanDefinition> findCandidateComponents(String basePackage) {
         *         Set<BeanDefinition> candidates = new LinkedHashSet<>();
         *         Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
         *         for (Class<?> clazz : classes) {
         *             candidates.add(new BeanDefinition(clazz));
         *         }
         *         return candidates;
         *     }
 *
 *
 *  1.4>使用
 *          @Test
 *     public void test_scan() {
 *         ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_scan.xml");
 *         IUserService userService = applicationContext.getBean("userService", IUserService.class);
 *         System.out.println("测试结果：" + userService.queryUserInfo());
 *     }
 *
 *
 *
 *
 *
 */
public class ApiTest {


    @Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_scan.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }

    @Test
    public void test_property() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_property.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService);
    }

    @Test
    public void test_beanPost() {

        BeanPostProcessor beanPostProcessor = new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) {
                return null;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                return null;
            }
        };

        List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();
        beanPostProcessors.add(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
        beanPostProcessors.remove(beanPostProcessor);

        System.out.println(beanPostProcessors.size());
    }
}

package com.axon.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.axon.springframework.beans.factory.PropertyValue;
import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.config.BeanReference;
import com.axon.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.axon.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.axon.springframework.core.io.Resource;
import com.axon.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream);
        } catch (IOException | ClassCastException | ClassNotFoundException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }

    }

    @Override
    public void loadBeanDefinitions(String location) {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }


    @Override
    public void loadBeanDefinitions(String... locations) {

        for (String location : locations) {
            ResourceLoader resourceLoader = getResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            loadBeanDefinitions(resource);
        }

    }


    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {

            if (!(childNodes.item(i) instanceof Element)) {
                continue;
            }
            if (!"bean".equals(childNodes.item(i).getNodeName())) {
                continue;
            }
            //开始解析标签
            Element bean = (Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String aClass = bean.getAttribute("class");

            Class<?> clazz = Class.forName(aClass);
            //优先级id >name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            BeanDefinition beanDefinition = new BeanDefinition(clazz);

            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {

                if (!(bean.getChildNodes().item(j) instanceof Element)) {
                    continue;
                }
                if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) {
                    continue;
                }
                Element property = (Element) bean.getChildNodes().item(j);

                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");

                //获取属性值
                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;

                //创建属性属性
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {

                throw new RuntimeException("注册BeanDefinition 异常");
            }
            //注册beanBeanDefinition

            getRegistry().registerBeanDefinition(beanName, beanDefinition);

        }
    }

}

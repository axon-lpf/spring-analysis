package com.bugstack.springframework.beans.factory.support;

import com.bugstack.springframework.beans.factory.DisposableBean;
import com.bugstack.springframework.beans.factory.config.SingletonBeanRegistry;
import com.bugstack.springframework.beans.factory.context.ObjectFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类获取和注册单列bean
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {


    //  一级缓存，普通对象，存储已经初始化完成的bean
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();


    //二级缓存，提前暴露对象，没有完全实例化的对象， 属性已经注入，但是未完成初始化的
    protected final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>();


    //三级缓存，用于存储代理对象
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>();

    private Map<String, Object> singletonMap = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();


    @Override
    public Object getSingleton(String name) {
        //先去一级缓存中去找
        Object singletonObject = singletonObjects.get(name);
        //找不到再去二级缓存中去找
        if (null == singletonObject) {
            singletonObject = earlySingletonObjects.get(name);
            if (null == singletonObject) {
                //判断三级缓存中是否有对象，如果有，则这个对象就是代理对象，因为只有代理对象才会存储到三级缓存中。
                ObjectFactory<?> singletonFactory = singletonFactories.get(name);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    //获取三级缓存中真是代理对象，存储到二级缓存中
                    earlySingletonObjects.put(name, singletonObject);
                    //移除三级缓存
                    singletonFactories.remove(name);
                }
            }
        }

        return singletonMap.get(name);
    }

    /**
     *  将完成初始化的bean添加到以及缓存中
     * @param beanName
     * @param singletonBean
     */
    @Override
    public void registerSingletonBean(String beanName, Object singletonBean) {
        //存储完成初始化的bean
        singletonObjects.put(beanName,singletonBean);
        //分别移除二级和三级缓存中bean
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    /**
     *  将代理对象添加到三级缓存
     * @param name
     * @param singletonFactory
     */
    protected void  addSingletonFactory(String name,ObjectFactory<?> singletonFactory){
        //如果这个bean代理对象不存在三级缓存中，则添加到三级缓存中， 并移除二级缓存中对象（可能是不存在）
        if (!this.singletonFactories.containsKey(name)){
            this.singletonFactories.put(name,singletonFactory);
            this.earlySingletonObjects.remove(name);
        }
    }

    /**
     * 注册销毁
     *
     * @param beanName
     * @param bean
     */
    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    /**
     * 销毁方法destroySingletons
     */

    public void destroySingletons() {

        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();

        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new RuntimeException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }

    }
}

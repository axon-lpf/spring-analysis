package com.axon.springframework.tx.transaction.interceptor;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.threadlocal.NamedThreadLocal;
import com.axon.springframework.beans.factory.BeanFactory;
import com.axon.springframework.beans.factory.BeanFactoryAware;
import com.axon.springframework.beans.factory.InitializingBean;
import com.axon.springframework.tx.transaction.PlatformTransactionManager;
import com.axon.springframework.tx.transaction.TransactionStatus;
import com.axon.springframework.utils.ClassUtils;

import java.lang.reflect.Method;


/**
 * 事务的处理
 */
public abstract class TransactionAspectSupport implements BeanFactoryAware, InitializingBean {

    private static final ThreadLocal<TransactionInfo> transactionInfoHolder =
            new NamedThreadLocal<>("Current aspect-driven transaction");

    private BeanFactory beanFactory;

    private TransactionAttributeSource transactionAttributeSource;

    private PlatformTransactionManager transactionManager;

    /**
     * 事务提交的核心逻辑
     *
     * @param method
     * @param targetClass
     * @param invocation
     * @return
     * @throws Throwable
     */
    protected Object invokeWithinTransaction(Method method, Class<?> targetClass, InvocationCallback invocation) throws Throwable {
        TransactionAttributeSource tas = getTransactionAttributeSource();
        // 查找事务注解 Transactional
        TransactionAttribute txAttr = (tas != null ? tas.getTransactionAttribute(method, targetClass) : null);

        //获取对应的事物管理器
        PlatformTransactionManager manager = determineTransactionManager();
        //获取对应执行的目标方法
        String joinPointIdentification = methodIdentification(method, targetClass);
        // 执行之前去创建一个事物
        TransactionInfo txInfo = createTransactionIfNecessary(manager, txAttr, joinPointIdentification);

        Object retVal = null;
        try {
            // 执行目标方法，即sql语句， 代理执行
            retVal = invocation.proceedWithInvocation();
        } catch (Throwable e) {
            // 遇到异常，则回滚事务
            completeTransactionAfterThrowing(txInfo, e);
            throw e;
        } finally {
            // 清除threadLocal中的value，避免内存溢出，因为与线程的声明周期一致，所以手动回收
            cleanupTransactionInfo(txInfo);
        }
        // 最后提交事物
        commitTransactionAfterReturning(txInfo);

        return retVal;
    }

    public TransactionAttributeSource getTransactionAttributeSource() {
        return transactionAttributeSource;
    }

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 当前使用DataSourceTransactionManager
     */
    protected PlatformTransactionManager determineTransactionManager() {
        return getTransactionManager();
    }

    /**
     * 获取目标方法的唯一标识
     */
    private String methodIdentification(Method method, Class<?> targetClass) {
        return ClassUtils.getQualifiedMethodName(method, targetClass);
    }

    /**
     * 创建事务的核心逻辑
     *
     * @param tm
     * @param txAttr
     * @param joinPointIdentification
     * @return
     */
    protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinPointIdentification) {
        if (txAttr != null && txAttr.getName() == null) {
            txAttr = new DelegatingTransactionAttribute(txAttr) {
                @Override
                public String getName() {
                    return joinPointIdentification;
                }
            };
        }

        TransactionStatus status = null;
        if (txAttr != null) {
            if (tm != null) {
                status = tm.getTransaction(txAttr);
            }
        }
        return prepareTransactionInfo(tm, txAttr, joinPointIdentification, status);
    }

    protected TransactionInfo prepareTransactionInfo(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinPointIdentification, TransactionStatus status) {
        TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinPointIdentification);
        if (txAttr != null) {
            txInfo.newTransactionStatus(status);
        }
        txInfo.bindToThread();
        return txInfo;
    }

    protected void completeTransactionAfterThrowing(TransactionInfo txInfo, Throwable ex) {
        if (null != txInfo && null != txInfo.getTransactionStatus()) {
            if (txInfo.transactionAttribute != null && txInfo.transactionAttribute.rollbackOn(ex)) {
                txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
            } else {
                txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
            }
        }
    }

    protected void cleanupTransactionInfo(TransactionInfo txInfo) {
        if (null != txInfo) {
            txInfo.restoreThreadLocalStatus();
        }
    }

    protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
        if (null != txInfo && null != txInfo.getTransactionStatus()) {
            txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
        }
    }

    protected interface InvocationCallback {
        Object proceedWithInvocation() throws Throwable;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {

    }

    protected final class TransactionInfo {

        private final PlatformTransactionManager transactionManager;
        private final TransactionAttribute transactionAttribute;
        private final String joinPointIdentification;
        private TransactionStatus transactionStatus;
        private TransactionInfo oldTransactionInfo;

        public TransactionInfo(PlatformTransactionManager transactionManager,
                               TransactionAttribute transactionAttribute, String joinpointIdentification) {
            this.transactionManager = transactionManager;
            this.transactionAttribute = transactionAttribute;
            this.joinPointIdentification = joinpointIdentification;
        }

        public PlatformTransactionManager getTransactionManager() {
            Assert.state(this.transactionManager != null, "No PlatformTransactionManager set");
            return transactionManager;
        }

        public String getJoinPointIdentification() {
            return joinPointIdentification;
        }

        public TransactionAttribute getTransactionAttribute() {
            return transactionAttribute;
        }

        public void newTransactionStatus(TransactionStatus status) {
            this.transactionStatus = status;
        }

        public TransactionStatus getTransactionStatus() {
            return transactionStatus;
        }

        public boolean hasTransaction() {
            return null != this.transactionStatus;
        }

        private void bindToThread() {
            this.oldTransactionInfo = transactionInfoHolder.get();
            transactionInfoHolder.set(this);
        }

        private void restoreThreadLocalStatus() {
            transactionInfoHolder.set(this.oldTransactionInfo);
        }

    }

}

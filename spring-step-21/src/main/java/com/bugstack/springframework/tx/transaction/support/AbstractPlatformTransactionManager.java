package com.bugstack.springframework.tx.transaction.support;

import com.bugstack.springframework.tx.transaction.PlatformTransactionManager;
import com.bugstack.springframework.tx.transaction.TransactionDefinition;
import com.bugstack.springframework.tx.transaction.TransactionException;
import com.bugstack.springframework.tx.transaction.TransactionStatus;

import java.io.Serializable;

/**
 * @description 抽象事务管理器平台
 * @date 2022/3/16
 * /CodeDesignTutorials
 */
public abstract class AbstractPlatformTransactionManager implements PlatformTransactionManager, Serializable {

    /**
     * 获取事务的核心逻辑
     *
     * @param definition
     * @return
     * @throws TransactionException
     */
    @Override
    public final TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        //获取一个事物
        Object transaction = doGetTransaction();
        if (null == definition) {  // 为空，则创建一个默认的事物
            definition = new DefaultTransactionDefinition();
        }
        if (definition.getTimeout() < TransactionDefinition.TIMEOUT_DEFAULT) {
            throw new TransactionException("Invalid transaction timeout " + definition.getTimeout());
        }
        // 暂定事务传播为默认的行为
        DefaultTransactionStatus status = newTransactionStatus(definition, transaction, true);
        // 开始事务
        doBegin(transaction, definition);
        return status;
    }

    protected DefaultTransactionStatus newTransactionStatus(TransactionDefinition definition, Object transaction, boolean newTransaction) {
        return new DefaultTransactionStatus(transaction, newTransaction);
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalArgumentException(
                    "Transaction is already completed - do not call or rollback more than once per transaction");
        }
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        processCommit(defStatus);
    }

    private void processCommit(DefaultTransactionStatus status) throws TransactionException {
        doCommit(status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalArgumentException(
                    "Transaction is already completed - do not call commit or rollback more than once per transaction");
        }
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        processRollback(defStatus, false);
    }

    private void processRollback(DefaultTransactionStatus status, boolean unexpected) {
        doRollback(status);
    }

    /**
     * 获取事务
     */
    protected abstract Object doGetTransaction() throws TransactionException;

    /**
     * 提交事务
     */
    protected abstract void doCommit(DefaultTransactionStatus status) throws TransactionException;

    /**
     * 事务回滚
     */
    protected abstract void doRollback(DefaultTransactionStatus status) throws TransactionException;

    /**
     * 开始事务
     */
    protected abstract void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException;

}

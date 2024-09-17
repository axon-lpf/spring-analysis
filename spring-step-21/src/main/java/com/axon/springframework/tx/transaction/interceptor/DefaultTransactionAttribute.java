package com.axon.springframework.tx.transaction.interceptor;


import com.axon.springframework.tx.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @description Spring's common transaction attribute implementation.
 * Rolls back on runtime, but not checked, exceptions by default.
 * @date 2022/3/16
 *  /CodeDesignTutorials
 *
 */
public class DefaultTransactionAttribute extends DefaultTransactionDefinition implements TransactionAttribute {

    public DefaultTransactionAttribute() {
        super();
    }

    @Override
    public boolean rollbackOn(Throwable ex) {
        return (ex instanceof RuntimeException || ex instanceof Error);
    }

    @Override
    public String toString() {
        return "DefaultTransactionAttribute{}";
    }
}

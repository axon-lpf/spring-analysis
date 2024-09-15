package com.bugstack.springframework.tx.transaction.annoation;

import com.bugstack.springframework.core.annotation.AnnotatedElementUtils;
import com.bugstack.springframework.core.annotation.AnnotationAttributes;
import com.bugstack.springframework.tx.transaction.interceptor.RollbackRuleAttribute;
import com.bugstack.springframework.tx.transaction.interceptor.RuleBasedTransactionAttribute;
import com.bugstack.springframework.tx.transaction.interceptor.TransactionAttribute;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析事务的主机诶
 */
public class SpringTransactionAnnotationParser implements TransactionAnnotationParser, Serializable {

    @Override
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement element) {
        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                element, Transactional.class, false, false);
        if (null != attributes) {
            return parseTransactionAnnotation(attributes);
        } else {
            return null;
        }
    }

    /**
     * 参照源码，简化实现
     * org.springframework.transaction.annotation.SpringTransactionAnnotationParser#parseTransactionAnnotation
     */
    protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
        RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();

        List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
        for (Class<?> rbRule : attributes.getClassArray("rollbackFor")) {
            rollbackRules.add(new RollbackRuleAttribute(rbRule));
        }

        rbta.setRollbackRules(rollbackRules);
        return rbta;
    }

}

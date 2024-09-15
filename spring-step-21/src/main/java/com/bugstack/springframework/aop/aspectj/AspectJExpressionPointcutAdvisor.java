package com.bugstack.springframework.aop.aspectj;

import com.bugstack.springframework.aop.Pointcut;
import com.bugstack.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * 该类的主要功能是将 切面pointcut、拦截方法advice 和具体的拦截表达式包装起来
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    public void setPointcut(AspectJExpressionPointcut pointcut) {
        this.pointcut = pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    //切面
    private AspectJExpressionPointcut pointcut;

    //具体的拦截方法
    private Advice advice;

    //表达式
    private String expression;


    @Override
    public Pointcut getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }
}

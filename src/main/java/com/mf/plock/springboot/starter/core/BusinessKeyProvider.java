package com.mf.plock.springboot.starter.core;

import com.mf.plock.springboot.starter.annotation.Plock;
import com.mf.plock.springboot.starter.exception.PlockExeception;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BusinessKeyProvider {

    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     *
     * @param joinPoint
     * @param plock
     * @return get the lock name
     *
     * e.g. @Plock(name="ab", keys={"#user.id", "#user.name} ) id: 1 name:c
     * the name is 1-c
     */
    public String getKeyName(JoinPoint joinPoint, Plock plock) {
        List<String> keyList = new ArrayList<>();
        Method method = getMethod(joinPoint);
        List<String> definitionKeys = getSpelDefinitionKey(plock.keys(), method, joinPoint.getArgs());
        keyList.addAll(definitionKeys);
        return StringUtils.collectionToDelimitedString(keyList,"", "-", "");

    }

    private Method getMethod(JoinPoint joinPoint){
       MethodSignature signature = (MethodSignature) joinPoint.getSignature();

      Method method = signature.getMethod();
      if (method.getDeclaringClass().isInterface()) {
          try {
              method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
          } catch (Exception e) {
              throw new PlockExeception("method can not found", e);

          }
      }
      return method;
    }

    private List<String> getSpelDefinitionKey(String [] definitionKeys, Method method, Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList<>();
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, nameDiscoverer);
        for (String definitionKey : definitionKeys) {
            if (!ObjectUtils.isEmpty(definitionKey)) {
               Object objKey =  parser.parseExpression(definitionKey).getValue(context);
               definitionKeyList.add(ObjectUtils.nullSafeToString(objKey));
            }
        }

        return definitionKeyList;
    }
}

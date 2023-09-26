package com.example.aopdemo.aspect;

import com.example.aopdemo.Account;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@Order(2)
public class MyAnalyticsAspect {

    @Around("execution(* com.example.aopdemo.service.*.getFortune(..))")
    public Object aroundGetFortune(
            ProceedingJoinPoint theProceedingJoinPoint) throws Throwable{

        String method = theProceedingJoinPoint.getSignature().toShortString();

        System.out.println("\n======>>> Executing @Around on method: " + method);

        long begin = System.currentTimeMillis();

        Object result = null;

        try {
            result = theProceedingJoinPoint.proceed();
        }
        catch (Exception exc) {

            System.out.println(exc.getMessage());

            result = "Major accident! But no worries, your private AOP helicopter is on the way";
//            throw exc;
        }

        long end = System.currentTimeMillis();

        long duration = end - begin;

        System.out.println("\n======>> Duration: " + duration / 1000.0 + " seconds");

        System.out.println(result);

        return result;
    }

    @After("execution(* com.example.aopdemo.dao.AccountDAO.findAccounts(..))")
    public void afterFinallyFindAccountsAdvice(JoinPoint theJoinPoint) {

        String method = theJoinPoint.getSignature().toShortString();

        System.out.println("\n======>>> Executing @After (finally) on method: " + method);
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.aopdemo.dao.AccountDAO.findAccounts(..))",
            throwing = "theExc"
    )
    public void afterThrowingFindAccountsAdvice(JoinPoint theJoinPoint, Throwable theExc) {

        String method = theJoinPoint.getSignature().toShortString();

        System.out.println("\n======>>> Executing @AfterThrowing on method: " + method);

        System.out.println("\n======>>> The exception is: " + theExc);

    }

    @AfterReturning(
            pointcut = "execution(* com.example.aopdemo.dao.AccountDAO.findAccounts(..))",
            returning = "result"
    )
    public void afterReturningFindAccountsAdvice(JoinPoint theJoinPoint, List<Account> result) {

        String method = theJoinPoint.getSignature().toShortString();

        System.out.println("\n======>>> Executing @AfterReturning on method: " + method);

        System.out.println("\n======>>>> result is: " + result);

        convertAccountNamesToUpperCase(result);

        System.out.println("\n======>>>> result is: " + result);
    }

    private void convertAccountNamesToUpperCase(List<Account> result) {

        for (Account tempAccount : result) {

            String theUpperName = tempAccount.getName().toUpperCase();

            tempAccount.setName(theUpperName);
        }
    }

    @Before("com.example.aopdemo.aspect.LuvAopExpressions.forDaoPackageNoGetterSetter()")
    public void performApiAnalytics(JoinPoint joinPoint) {

        System.out.println("\n====>>>> Performing API analytics");

        System.out.println("Method: " + joinPoint.getSignature());

        Object[] argues = joinPoint.getArgs();

        for (Object argu : argues) {

            System.out.println(argu);
        }
    }
}

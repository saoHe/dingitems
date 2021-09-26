//package com.ding.dingitems.util.interceptor.comaspect;
//
//import com.ding.dingitems.sysbiz.dto.ApiReturnResult;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//@Slf4j
//public class ComAspect {
//
//    /**
//     * 定义切入点：对要拦截的方法进行定义与限制，如包、类
//     * <p>
//     * 1、execution(public * *(..)) 任意的公共方法
//     * 2、execution（* set*（..）） 以set开头的所有的方法
//     * 3、execution（* com.lingyejun.annotation.LoggerApply.*（..））com.lingyejun.annotation.LoggerApply这个类里的所有的方法
//     * 4、execution（* com.lingyejun.annotation.*.*（..））com.lingyejun.annotation包下的所有的类的所有的方法
//     * 5、execution（* com.lingyejun.annotation..*.*（..））com.lingyejun.annotation包及子包下所有的类的所有的方法
//     * 6、execution(* com.lingyejun.annotation..*.*(String,?,Long)) com.lingyejun.annotation包及子包下所有的类的有三个参数，第一个参数为String类型，第二个参数为任意类型，第三个参数为Long类型的方法
//     * 7、execution(@annotation(com.lingyejun.annotation.Lingyejun))
//     */
//    @Pointcut("execution(* com.ding.dingitems..*Controller.*(..))")
//    private void cutMethod() {
//
//    }
//
//    /**
//     * 前置通知：方法执行前调用
//     */
//    @Before("cutMethod()")
//    public void begin() {
//    }
//
//    /**
//     * 后置通知： 方法执行后调用，若方法出现异常，不执行
//     */
//    @AfterReturning("cutMethod()")
//    public void afterReturning() {
//    }
//
//    /**
//     * 最终通知：无论无何都会调用，类似于：try/catch中的finally
//     */
//    @After("cutMethod()")
//    public void after() {
//    }
//
//    /**
//     * 异常通知：方法抛出异常时执行
//     */
//    @AfterThrowing("cutMethod()")
//    public void afterThrowing() {
//    }
//
//    /**
//     * 环绕通知
//     * 既可以在目标方法之前织入增强动作，也可以在执行目标方法之后织入增强动作；
//     * 可以决定目标方法在什么时候执行，如何执行，甚至可以完全阻止目标目标方法的执行；
//     * 可以改变执行目标方法的参数值，也可以改变执行目标方法之后的返回值； 当需要改变目标方法的返回值时，只能使用Around方法；
//     */
//    @Around("cutMethod()")
//    public ApiReturnResult around(ProceedingJoinPoint point) throws Throwable {
//        // 获取方法传入参数
//
//        Object r = point.proceed();
//
//        // 执行源方法
//        return ApiReturnResult.succ(r);
//    }
//
//
//
//}

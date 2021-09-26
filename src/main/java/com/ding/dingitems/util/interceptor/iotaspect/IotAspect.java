//package com.ding.dingitems.util.interceptor.iotaspect;
//
//import com.google.common.hash.Hashing;
//import com.starcharging.energy.general.api.dto.ApiReturnResult;
//import com.starcharging.energy.star.energy.base.entity.CmDict;
//import com.starcharging.energy.star.energy.base.service.CmDictService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import tk.mybatis.mapper.entity.Condition;
//import tk.mybatis.mapper.entity.Example;
//
//import java.lang.reflect.Method;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Aspect
//@Component
//@Slf4j
//public class IotAspect {
//
//
//    @Autowired
//    CmDictService cmDictService;
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
//    @Pointcut("@annotation(com.starcharging.energy.star.energy.base.interceptor.iotaspect.IotAnnotation)")
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
//    public Object around(ProceedingJoinPoint point) throws Throwable {
//        // 获取方法传入参数
//        Object[] args = point.getArgs();
//
//        Signature signature = point.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        // 获取到方法的所有参数名称的字符串数组
//        String[] parameterNames = methodSignature.getParameterNames();
//        try {
//            //3.通过你需要获取的参数名称的下标获取到对应的值
//            int tokenIndex = ArrayUtils.indexOf(parameterNames, "token");
//            int timeIndex = ArrayUtils.indexOf(parameterNames, "time");
//            if (tokenIndex >= 0 && timeIndex >= 0 && args.length == parameterNames.length && args[tokenIndex] != null && args[timeIndex] != null) {
//                String token = (String) args[tokenIndex];
//                long time = (Long) args[timeIndex];
//                if (Math.abs((System.currentTimeMillis() - time)) > 1800000) {
//                    return "time out";
//                }
//                Condition condition = new Condition(CmDict.class);
//                Example.Criteria criteria = condition.createCriteria();
//                criteria.andEqualTo("name", "emsKey");
//
//                List<CmDict> list = cmDictService.queryListByExample(condition);
//                if (list.size() <= 0 || StringUtils.isBlank(list.get(0).getValue())) {
//                    return ApiReturnResult.fail("没有设置dict中emsKey");
//                }
//
//                String tokenStr = time + "," + list.get(0).getValue();
//                String encodeToken = Hashing.sha256().hashBytes(tokenStr.getBytes(StandardCharsets.UTF_8)).toString();
//                if (!token.equals(encodeToken)) {
//                    return "error key";
//                }
//            }
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return "error iot around";
//        }
//
//        // 执行源方法
//        return point.proceed();
//    }
//
//
//    /**
//     * 获取方法中声明的注解
//     *
//     * @param joinPoint
//     * @return
//     * @throws NoSuchMethodException
//     */
//    public IotAnnotation getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
//        // 获取方法名
//        String methodName = joinPoint.getSignature().getName();
//        // 反射获取目标类
//        Class<?> targetClass = joinPoint.getTarget().getClass();
//        // 拿到方法对应的参数类型
//        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
//        // 根据类、方法、参数类型（重载）获取到方法的具体信息
//        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
//        // 拿到方法定义的注解信息
//        // 返回
//        return objMethod.getDeclaredAnnotation(IotAnnotation.class);
//    }
//}

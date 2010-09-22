package de.devsurf.injection.guice.scanner.asm.example.autobind.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

import de.devsurf.injection.guice.aop.ClassMatcher;
import de.devsurf.injection.guice.aop.Intercept;
import de.devsurf.injection.guice.aop.Interceptor;
import de.devsurf.injection.guice.aop.Invoke;
import de.devsurf.injection.guice.aop.MethodMatcher;

@Interceptor
public class InvalidMethodInterceptor{

    @Invoke
    public Object invoke(MethodInvocation invocation, Object obj) throws Throwable {
	return invocation.proceed();
    }

    @ClassMatcher
    public Matcher<? super Class<?>> getClassMatcher() {
	return Matchers.any();
    }

    @MethodMatcher
    public Matcher<? super Method> getMethodMatcher() {
	return Matchers.annotatedWith(Intercept.class);
    }

}

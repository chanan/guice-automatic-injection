/**
 * Copyright (C) 2010 Daniel Manzke <daniel.manzke@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.devsurf.injection.guice.aop;


import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;


@Interceptor
public class MethodCallingInterceptor{
    private Logger _logger = Logger.getLogger(MethodCallingInterceptor.class.getName());
    
    @Invoke
    public Object invoke(MethodInvocation invocation) throws Throwable {
	Object destination = invocation.getThis();
	StringBuilder logMessageBuilder = new StringBuilder(250);
	
	logMessageBuilder.append("Invoking Method \"");
	logMessageBuilder.append(invocation.getMethod().getName());
	logMessageBuilder.append("\" on ");
	logMessageBuilder.append(destination.getClass().getName());
	logMessageBuilder.append(" with Arguments: ");
	
	for(Object parameter : invocation.getArguments()){
	    logMessageBuilder.append(" \"");
	    logMessageBuilder.append(parameter.getClass().getSimpleName());
	    logMessageBuilder.append("\": ");
	    logMessageBuilder.append(parameter);
	}
	_logger.log(Level.SEVERE, logMessageBuilder.toString());

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

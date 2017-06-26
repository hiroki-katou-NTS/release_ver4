package nts.uk.shr.sample.intercept;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@SampleClassAround
public class SampleClassInterceptor {

	@Inject
	private Intercepting inj;
	
    @AroundInvoke
    public Object invokeAround(final InvocationContext context) throws Exception {

    	this.inj.run();
    	
        Object result = context.proceed();
        return result;
    }
}
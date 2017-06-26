package nts.uk.shr.sample.intercept;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Target({
    ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface SampleClassAround {

	
} 
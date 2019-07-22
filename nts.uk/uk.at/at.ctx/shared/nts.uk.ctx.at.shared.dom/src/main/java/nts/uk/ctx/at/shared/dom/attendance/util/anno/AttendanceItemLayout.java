package nts.uk.ctx.at.shared.dom.attendance.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface AttendanceItemLayout {
	
	int index() default -1;

	String layout();
	
	String jpPropertyName();
	
	String needCheckIDWithMethod() default "";
	
	boolean listNoIndex() default false;
	
	int listMaxLength() default -1;
	
	boolean isOptional() default false;
	
	String indexField() default "";
	
	String enumField() default "";
	
	boolean removeConflictEnum() default false;
	
	String defaultIdx() default "";
	
//	String needCheckIDWithField() default "";
	
//	boolean needCheckIDWithIndex() default false;
	
//	boolean isList() default false;
	
//	String methodForEnumValues() default "";
}

package nts.uk.shr.com.primitive;

import java.util.Arrays;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.PrimitiveValueConstraintPackage;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * CodePrimitiveValue
 * @param <S> type
 */
public class CodePrimitiveValue<S> extends StringPrimitiveValue<CodePrimitiveValue<S>> {

	/** 
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * @param rawValue raw value
	 */
	public CodePrimitiveValue(String rawValue) {
		super(rawValue == null ? "" : rawValue.trim());
	}
    
    /**
     * Returns true if this code is equal otherCode.
     * 
     * @param otherCode other code
     * @return result
     */
    public boolean equals(String otherCode) {
        return this.v().equals(otherCode.trim());
    }
    
    @Override
    protected String getRawValueToBeValidated() {
    	return this.v().trim();
    }
    
    /**
     * Pad left with character.
     * 
     * @param paddingChar padding character
     * @return padded string
     */
    protected String padLeft(String paddingChar) {
    	return pad(paddingChar, true, this.v().trim());
    }
    
    /**
     * Pad right with character.
     * 
     * @param paddingChar padding character
     * @return padded string
     */
    protected String padRight(String paddingChar) {
    	return pad(paddingChar, false, this.v().trim());
    }
    
    /**
     * Pad character to value string
     * 
     * @param paddingChar padding character
     * @param isPaddingLeft pad on the left side
     * @param value value string
     * @return padded string
     */
    private String pad(String paddingChar, boolean isPaddingLeft, String value) {
    	int maxLength = maxLength();
    	StringBuffer sb = new StringBuffer(value);
    	int length = maxLength - value.length();
    	if (length < 0) return value;
    	sb = isPaddingLeft ? sb.reverse() : sb;
    	for (int i = 0; i < length; i++) {
        	sb.append(paddingChar);
    	}
    	
    	if (isPaddingLeft) return sb.reverse().toString();
    	return sb.toString();
    }
    
    /**
     * Get max length of code primitive value.
     * 
     * @return max length of code
     */
    private int maxLength() {
    	String paramValue = Arrays.asList(this.getClass().getDeclaredAnnotations()).stream().map(a -> a.toString())
    			.filter(s -> s.contains(PrimitiveValueConstraintPackage.NAME) 
    						&& StringMaxLength.class.getSimpleName().equals(getAnnotationName(s)))
    			.map(a -> getAnnotationParamValue(a)).findFirst()
    			.orElseThrow(() -> new RuntimeException("Code primitive value must have max length."));
    	
    	return Integer.parseInt(paramValue.replaceAll("value=", ""));
    }
    
    /**
     * Get annotation name.
     * 
     * @param annotation annotation
     * @return annotation name
     */
    private String getAnnotationName(String annotation) {
    	String annotationFullName = annotation.substring(0, annotation.indexOf("("));
    	int start = annotationFullName.lastIndexOf(".") + 1;

    	return annotationFullName.substring(start);
    }
    
    /**
     * Get annotation parameter value.
     * 
     * @param annotation annotation
     * @return annotation parameter value
     */
    private String getAnnotationParamValue(String annotation) {
		return annotation.substring(annotation.indexOf("(") + 1, annotation.indexOf(")"));
	}
}

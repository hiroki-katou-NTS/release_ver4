package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * Primitive Value: Processing Name
 *
 */
@StringMaxLength(12)
public class ProcessingName extends StringPrimitiveValue<ProcessingName>  {

	/** serialVersionUID */
	private static final long serialVersionUID = -5691224003343521403L;
	
	/**
	 * Constructs.
	 * 
	 * @param rawValue raw value
	 */
	public ProcessingName(String rawValue) {
		super(rawValue);
	}

}

package nts.uk.ctx.sys.assist.dom.datarestoration;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(100)
public class ProcessingRecoverContent extends StringPrimitiveValue<ProcessingRecoverContent> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new processing content.
	 *
	 * @param rawValue the raw value
	 */
	public ProcessingRecoverContent(String rawValue) {
		super(rawValue);
	}
}

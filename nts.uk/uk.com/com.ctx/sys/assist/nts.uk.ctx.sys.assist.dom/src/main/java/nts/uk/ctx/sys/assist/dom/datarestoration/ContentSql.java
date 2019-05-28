package nts.uk.ctx.sys.assist.dom.datarestoration;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(2000)
public class ContentSql extends StringPrimitiveValue<ContentSql> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new error content.
	 *
	 * @param rawValue the raw value
	 */
	public ContentSql(String rawValue) {
		super(rawValue);
	}
}

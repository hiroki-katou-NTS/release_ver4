package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(10)
public class ErrMessageResource extends StringPrimitiveValue<ErrMessageResource> {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new optional item name.
	 *
	 * @param rawValue the raw value
	 */
	public ErrMessageResource(String rawValue) {
		super(rawValue);
	}
}

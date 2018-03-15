package nts.uk.ctx.at.record.dom.divergence.time.setting;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

// TODO: Auto-generated Javadoc
/**
 * The Class DivergenceReason.
 */
@StringMaxLength(60)
public class DivergenceReason extends StringPrimitiveValue<DivergenceReason>{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new divergence reason.
	 *
	 * @param rawValue the raw value
	 */
	public DivergenceReason(String rawValue) {
		super(rawValue);
	}

}

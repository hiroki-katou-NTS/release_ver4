package nts.uk.ctx.at.record.dom.divergence.time;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

// TODO: Auto-generated Javadoc
/**
 * The Class DivergenceTimeName.
 */
@StringMaxLength(30)
public class DivergenceTimeName extends StringPrimitiveValue<PrimitiveValue<String>> {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new divergence time name.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public DivergenceTimeName(String rawValue) {
		super(rawValue);
	}

}

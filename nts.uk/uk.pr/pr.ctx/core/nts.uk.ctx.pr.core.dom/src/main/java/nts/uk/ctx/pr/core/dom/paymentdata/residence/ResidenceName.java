package nts.uk.ctx.pr.core.dom.paymentdata.residence;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 住民税納付先名
 */
@StringMaxLength(24)
public class ResidenceName extends StringPrimitiveValue<ResidenceName> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public ResidenceName(String rawValue) {
		super(rawValue);
	}

}

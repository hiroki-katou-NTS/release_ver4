package nts.uk.ctx.pr.core.dom.paymentdata;

import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * 明細書名
 */
@StringMaxLength(20)
public class SpecificationName extends CodePrimitiveValue<SpecificationName> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public SpecificationName(String rawValue) {
		super(rawValue);
	}

}

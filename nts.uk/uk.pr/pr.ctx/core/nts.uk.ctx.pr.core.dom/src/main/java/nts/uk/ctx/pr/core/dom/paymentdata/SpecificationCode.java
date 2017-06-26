package nts.uk.ctx.pr.core.dom.paymentdata;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * 明細書コード
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(2)
public class SpecificationCode extends CodePrimitiveValue<SpecificationCode> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public SpecificationCode(String rawValue) {
		super(rawValue);
	}

}

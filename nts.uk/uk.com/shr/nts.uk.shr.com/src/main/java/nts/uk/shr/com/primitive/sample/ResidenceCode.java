package nts.uk.shr.com.primitive.sample;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * 住民税納付先コード
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(6)
public class ResidenceCode extends CodePrimitiveValue<ResidenceCode> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public ResidenceCode(String rawValue) {
		super(rawValue); 
	}
}

/**
 * 10:25:37 AM Jun 6, 2017
 */
package nts.uk.ctx.at.shared.dom.bonuspay.primitives;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * @author hungnm
 *
 */
// 勤怠項目ID
@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(36)
public class EmployeeId extends CodePrimitiveValue<EmployeeId> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new employee id
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public EmployeeId(String rawValue) {
		super(rawValue);
	}
}

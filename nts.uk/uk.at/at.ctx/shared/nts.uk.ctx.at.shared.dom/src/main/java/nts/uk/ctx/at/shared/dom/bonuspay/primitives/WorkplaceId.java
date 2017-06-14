/**
 * 10:29:00 AM Jun 6, 2017
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
@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(36)
public class WorkplaceId extends CodePrimitiveValue<WorkplaceId> {

	/**
	 * 職場ID
	 */
	private static final long serialVersionUID = 1L;

	public WorkplaceId(String rawValue) {
		super(rawValue);
	}
}

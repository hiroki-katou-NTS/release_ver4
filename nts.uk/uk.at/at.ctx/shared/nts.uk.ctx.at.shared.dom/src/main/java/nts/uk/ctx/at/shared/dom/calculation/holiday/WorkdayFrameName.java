package nts.uk.ctx.at.shared.dom.calculation.holiday;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 * @author phongtq
 *
 */
@StringMaxLength(12)
public class WorkdayFrameName extends StringPrimitiveValue<PrimitiveValue<String>> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public WorkdayFrameName(String rawValue) {
		super(rawValue);
	}
}

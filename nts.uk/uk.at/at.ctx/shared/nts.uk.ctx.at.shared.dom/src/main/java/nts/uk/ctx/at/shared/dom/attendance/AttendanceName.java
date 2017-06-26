package nts.uk.ctx.at.shared.dom.attendance;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(30)
public class AttendanceName extends StringPrimitiveValue<PrimitiveValue<String>> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public AttendanceName(String rawValue) {
		super(rawValue);
	}
}

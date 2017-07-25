package nts.uk.ctx.at.shared.dom.attendanceitem.primitives;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(30)
public class AttendanceItemName extends CodePrimitiveValue<AttendanceItemName> {

	private static final long serialVersionUID = 1L;
	
	public AttendanceItemName(String rawValue) {
		super(rawValue);
	}
}

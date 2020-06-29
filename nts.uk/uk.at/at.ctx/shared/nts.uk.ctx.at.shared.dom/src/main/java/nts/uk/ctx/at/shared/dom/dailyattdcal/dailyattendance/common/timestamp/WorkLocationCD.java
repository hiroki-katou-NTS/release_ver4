package nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.common.timestamp;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.ZeroPaddedCode;
@StringMaxLength(4)
/**
 * 勤務場所コード
 * @author hieult
 *
 */
@StringCharType(CharType.ALPHA_NUMERIC)
@ZeroPaddedCode
public class WorkLocationCD extends StringPrimitiveValue<WorkLocationCD>{

	public WorkLocationCD(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}

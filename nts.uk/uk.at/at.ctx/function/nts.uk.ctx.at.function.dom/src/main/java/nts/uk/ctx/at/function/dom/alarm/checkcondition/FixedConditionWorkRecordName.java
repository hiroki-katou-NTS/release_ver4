package nts.uk.ctx.at.function.dom.alarm.checkcondition;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * エラーアラームメッセージ
 * @author tutk
 *
 */
@StringMaxLength(100)
public class FixedConditionWorkRecordName extends StringPrimitiveValue<FixedConditionWorkRecordName> {

	private static final long serialVersionUID = 1L;
	public FixedConditionWorkRecordName(String rawValue) {
		super(rawValue);
	}
	
}

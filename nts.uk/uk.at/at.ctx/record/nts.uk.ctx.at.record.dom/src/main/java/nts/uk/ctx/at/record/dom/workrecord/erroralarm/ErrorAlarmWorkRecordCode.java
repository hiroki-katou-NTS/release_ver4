/**
 * 2:17:33 PM Jul 21, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author hungnm
 *
 */
/* 勤務実績のエラーアラームコード */
@StringMaxLength(3)
public class ErrorAlarmWorkRecordCode extends StringPrimitiveValue<ErrorAlarmWorkRecordCode> {

	private static final long serialVersionUID = 1L;

	public ErrorAlarmWorkRecordCode(String rawValue) {
		super(rawValue);
	}
}

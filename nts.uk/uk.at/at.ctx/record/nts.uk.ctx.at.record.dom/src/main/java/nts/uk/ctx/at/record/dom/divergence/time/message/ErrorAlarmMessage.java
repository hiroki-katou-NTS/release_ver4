package nts.uk.ctx.at.record.dom.divergence.time.message;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class ErrorAlarmMessage.
 */
// エラーアラームメッセージ
@StringMaxLength(100)
public class ErrorAlarmMessage extends StringPrimitiveValue<ErrorAlarmMessage> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new error alarm message.
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public ErrorAlarmMessage(String rawValue) {
		super(rawValue);
	}

}

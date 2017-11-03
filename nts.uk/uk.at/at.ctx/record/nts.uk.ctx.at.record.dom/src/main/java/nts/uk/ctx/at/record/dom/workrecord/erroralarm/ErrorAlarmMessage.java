/**
 * 10:38:11 AM Jul 21, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ColorCode;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.DisplayMessage;

/**
 * @author hungnm
 *
 */
/* メッセージ */
@Value
@EqualsAndHashCode(callSuper = false)
@Getter
public class ErrorAlarmMessage extends DomainObject {

	/* 表示メッセージ */
	private DisplayMessage displayMessage;

	/* メッセージを太字にする */
	private Boolean boldAtr;

	/* メッセージの色 */
	private ColorCode messageColor;

	/**
	 * Create ErrorAlarmMessage from Java type
	 * 
	 * @param String displayMessage, boolean boldAtr, String messageColor
	 * @return ErrorAlarmMessage
	 **/
	public static ErrorAlarmMessage createFromJavaType(String displayMessage, boolean boldAtr, String messageColor) {
		return new ErrorAlarmMessage(new DisplayMessage(displayMessage), boldAtr, new ColorCode(messageColor));
	}
}

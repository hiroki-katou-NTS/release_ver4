/**
 * 4:55:17 PM Jul 21, 2017
 */
package nts.uk.ctx.at.record.app.find.workrecord.erroralarm;

import java.math.BigDecimal;

import lombok.Value;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;

/**
 * @author hungnm
 *
 */
@Value
public class ErrorAlarmWorkRecordDto {

	/* 会社ID */
	private String companyId;

	/* コード */
	private String code;

	/* 名称 */
	private String name;

	/* システム固定とする */
	private int fixedAtr;

	/* 使用する */
	private int useAtr;

	/* 区分 */
	private int typeAtr;

	/* 表示メッセージ */
	private String displayMessage;

	/* メッセージを太字にする */
	private int boldAtr;

	/* メッセージの色 */
	private String messageColor;

	/* エラーアラームを解除できる */
	private int cancelableAtr;

	/* エラー表示項目 */
	private BigDecimal errorDisplayItem;

	public static ErrorAlarmWorkRecordDto fromDomain(ErrorAlarmWorkRecord domain) {
		return new ErrorAlarmWorkRecordDto(domain.getCompanyId(), domain.getCode().v(), domain.getName().v(),
				domain.getFixedAtr() ? 1 : 0, domain.getUseAtr() ? 1 : 0, domain.getTypeAtr().value,
				domain.getMessage().getDisplayMessage().v(), domain.getMessage().getBoldAtr() ? 1 : 0,
				domain.getMessage().getMessageColor().v(), domain.getCancelableAtr() ? 1 : 0,
				domain.getErrorDisplayItem());
	}

}

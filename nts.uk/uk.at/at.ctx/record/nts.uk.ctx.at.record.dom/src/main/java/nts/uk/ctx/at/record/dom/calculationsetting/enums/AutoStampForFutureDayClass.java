package nts.uk.ctx.at.record.dom.calculationsetting.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 * 未来日の自動打刻セット区分
 */
@AllArgsConstructor
public enum AutoStampForFutureDayClass {

	/* 自動打刻をセットしない */
	DO_NOT_SET_AUTO_STAMP(0),
	/* 自動打刻をセットする */
	SET_AUTO_STAMP(1);
	
	public final int value;
}

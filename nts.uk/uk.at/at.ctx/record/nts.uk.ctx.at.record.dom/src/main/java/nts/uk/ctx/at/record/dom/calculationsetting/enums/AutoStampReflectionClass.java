package nts.uk.ctx.at.record.dom.calculationsetting.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 * 自動打刻反映区分
 */
@AllArgsConstructor
public enum AutoStampReflectionClass {

	/* 打刻を反映しない */
	DO_NOT_STAMP_REFLECT(0),
	/* 打刻を反映する */
	STAMP_REFLECT(1);
	
	public final int value;
}

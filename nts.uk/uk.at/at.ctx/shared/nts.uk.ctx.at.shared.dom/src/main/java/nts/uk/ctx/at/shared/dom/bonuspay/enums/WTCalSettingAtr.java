/**
 * 10:23:03 AM Jun 6, 2017
 */
package nts.uk.ctx.at.shared.dom.bonuspay.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum WTCalSettingAtr {

	// 自動計算しない
	NON_CALCULATION(0, "Enum_AutoCalcAtr_NonCalculation"),
	// 自動計算する
	CALCULATION(1, "Enum_AutoCalcAtr_Calculation");

	public final int value;

	public final String nameId;
}
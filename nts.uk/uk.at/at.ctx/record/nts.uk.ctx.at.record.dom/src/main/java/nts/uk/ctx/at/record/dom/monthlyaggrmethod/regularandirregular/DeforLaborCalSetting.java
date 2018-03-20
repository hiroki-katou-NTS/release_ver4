/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.monthlyaggrmethod.regularandirregular;

import lombok.Getter;

/**
 * The Class DeforLaborCalSetting.
 */
@Getter
// 変形労働計算の設定
// TODO: Update CalcSettingOfIrregular
public class DeforLaborCalSetting {

	// 基準時間未満の残業時間を変形基準内残業とする
	private boolean isOtTransCriteria;

	public DeforLaborCalSetting(boolean isOtTransCriteria) {
		super();
		this.isOtTransCriteria = isOtTransCriteria;
	}

}

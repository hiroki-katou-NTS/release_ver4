/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.ot.autocalsetting;

import org.eclipse.persistence.internal.xr.ValueObject;

import lombok.Getter;

/**
 * The Class AutoCalSetting.
 */
// 自動計算設定
@Getter
public class AutoCalSetting extends ValueObject {

	/** The up limit ot set. */
	// 上限残業時間の設定
	private TimeLimitUpperLimitSetting upLimitORtSet;

	/** The cal atr. */
	/// 計算区分
	private AutoCalAtrOvertime calAtr;

	/**
	 * Instantiates a new auto cal setting.
	 *
	 * @param upLimitOtSet
	 *            the up limit ot set
	 * @param calAtr
	 *            the cal atr
	 */
	public AutoCalSetting(TimeLimitUpperLimitSetting upLimitOtSet, AutoCalAtrOvertime calAtr) {
		super();
		this.upLimitORtSet = upLimitOtSet;
		this.calAtr = calAtr;
	}
	
	public static AutoCalSetting defaultValue(){
		return new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.APPLYMANUALLYENTER);
	}

	
	/**
	 * 計算区分を受け取った計算区分へ変更する
	 */
	public AutoCalSetting changeCalcAtr(AutoCalAtrOvertime atr) {
		return new AutoCalSetting(this.upLimitORtSet, atr);
	}

}

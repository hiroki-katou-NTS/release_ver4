/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.ot.autocalsetting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;

/**
 * The Class AutoCalOvertimeSetting.
 */
/// 残業時間の自動計算設定
@Getter
@NoArgsConstructor
public class AutoCalOvertimeSetting extends DomainObject {

	/** The early ot time. */
	// 早出残業時間
	private AutoCalSetting earlyOtTime;

	/** The early mid ot time. */
	// 早出深夜残業時間
	private AutoCalSetting earlyMidOtTime;

	/** The normal ot time. */
	// 普通残業時間
	private AutoCalSetting normalOtTime;

	/** The normal mid ot time. */
	// 普通深夜残業時間
	private AutoCalSetting normalMidOtTime;

	/** The legal ot time. */
	// 法定内残業時間
	private AutoCalSetting legalOtTime;

	/** The legal mid ot time. */
	// 法定内深夜残業時間
	private AutoCalSetting legalMidOtTime;

	/**
	 * Instantiates a new auto cal overtime setting.
	 *
	 * @param earlyOTTime the early OT time
	 * @param earlyMidOTTime the early mid OT time
	 * @param normalOTTime the normal OT time
	 * @param normalMidOTTime the normal mid OT time
	 * @param legalOTTime the legal OT time
	 * @param legalMidOTTime the legal mid OT time
	 */
	@SuppressWarnings("rawtypes")
	public AutoCalOvertimeSetting(AutoCalOvertimeSettingGetMemento memento) {
		this.earlyOtTime = memento.getEarlyOtTime();
		this.earlyMidOtTime = memento.getEarlyMidOtTime();
		this.normalOtTime = memento.getNormalOtTime();
		this.normalMidOtTime = memento.getNormalMidOtTime();
		this.legalOtTime = memento.getLegalOtTime();
		this.legalMidOtTime = memento.getLegalMidOtTime();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(AutoCalOvertimeSettingSetMemento memento) {
		memento.setEarlyOtTime(this.earlyOtTime);
		memento.setEarlyMidOtTime(this.earlyMidOtTime);
		memento.setLegalMidOtTime(this.legalMidOtTime);
		memento.setLegalOtTime(this.legalOtTime);
		memento.setNormalMidOtTime(this.normalMidOtTime);
		memento.setNormalOtTime(this.normalOtTime);
	}

	public AutoCalOvertimeSetting(AutoCalSetting earlyOtTime, AutoCalSetting earlyMidOtTime,
			AutoCalSetting normalOtTime, AutoCalSetting normalMidOtTime, AutoCalSetting legalOtTime,
			AutoCalSetting legalMidOtTime) {
		super();
		this.earlyOtTime = earlyOtTime;
		this.earlyMidOtTime = earlyMidOtTime;
		this.normalOtTime = normalOtTime;
		this.normalMidOtTime = normalMidOtTime;
		this.legalOtTime = legalOtTime;
		this.legalMidOtTime = legalMidOtTime;
	}

	public AutoCalSetting decisionUseCalcSetting(StatutoryAtr statutoryAtr,boolean goEarlyAtr) {
		if(statutoryAtr.isStatutory()) {
			return this.legalOtTime;
		}
		else {
			if(goEarlyAtr) {
				return this.earlyOtTime;
			}
			else {
				return this.normalOtTime;
			}
		}			
	}
	
	/**
	 * 法内残業の計算区分を受け取った計算区分へ変更する
	 * @param atr
	 */
	public AutoCalOvertimeSetting changeNormalAutoCalcSetting(AutoCalAtrOvertime atr) {
		return new AutoCalOvertimeSetting(
				this.earlyOtTime,
				this.earlyMidOtTime,
				this.normalOtTime,
				this.normalMidOtTime,
				this.legalOtTime.changeCalcAtr(atr),
				this.legalMidOtTime);
	}
	
	/**
	 * 計算区分の判定処理
	 * @param statutoryAtr 法定内区分
	 * @param goEarly 早出区分
	 * @return 打刻から計算する
	 */
	public boolean decisionCalcAtr(StatutoryAtr statutoryAtr,boolean goEarly) {
		if(statutoryAtr.isStatutory()) {
			if(goEarly) {
				/*早出残業区分を参照*/
				return this.getEarlyOtTime().getCalAtr().isCalculateEmbossing();
			}
			else {
				/*普通残業計算区分を参照*/
				return this.getNormalOtTime().getCalAtr().isCalculateEmbossing();
			}
		}
		else {
			/*法定内の場合*/
			return this.getLegalOtTime().getCalAtr().isCalculateEmbossing();
		}
	}
	
}

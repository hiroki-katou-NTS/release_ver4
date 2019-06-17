/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class FixedWorkCalcSetting.
 */
@Getter
@NoArgsConstructor
// 固定勤務の計算設定
public class FixedWorkCalcSetting extends WorkTimeDomainObject implements Cloneable{

	/** The exceeded pred add vacation calc. */
	// 休暇加算時間が所定を超過した場合の計算
	private ExceededPredAddVacationCalc exceededPredAddVacationCalc;

	/** The over time calc no break. */
	// 休憩未取得時の残業計算
	private OverTimeCalcNoBreak overTimeCalcNoBreak;

	/**
	 * Instantiates a new fixed work calc setting.
	 *
	 * @param memento
	 *            the memento
	 */
	public FixedWorkCalcSetting(FixedWorkCalcSettingGetMemento memento) {
		this.exceededPredAddVacationCalc = memento.getExceededPredAddVacationCalc();
		this.overTimeCalcNoBreak = memento.getOverTimeCalcNoBreak();
	}
	
	public FixedWorkCalcSetting(ExceededPredAddVacationCalc exceededPredAddVacationCalc,
			OverTimeCalcNoBreak overTimeCalcNoBreak) {
		super();
		this.exceededPredAddVacationCalc = exceededPredAddVacationCalc;
		this.overTimeCalcNoBreak = overTimeCalcNoBreak;
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(FixedWorkCalcSettingSetMemento memento) {
		memento.setExceededPredAddVacationCalc(this.exceededPredAddVacationCalc);
		memento.setOverTimeCalcNoBreak(this.overTimeCalcNoBreak);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, Optional<FixedWorkCalcSetting> oldDomain) {
		if (oldDomain.isPresent()) {
			this.exceededPredAddVacationCalc.correctData(screenMode, oldDomain.get().getExceededPredAddVacationCalc());
			this.overTimeCalcNoBreak.correctData(screenMode, oldDomain.get().getOverTimeCalcNoBreak());
		} else {
			this.correctDefaultData(screenMode);
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		this.exceededPredAddVacationCalc.correctDefaultData(screenMode);
		this.overTimeCalcNoBreak.correctDefaultData(screenMode);
	}
	
	@Override
	public FixedWorkCalcSetting clone() {
		FixedWorkCalcSetting cloned = new FixedWorkCalcSetting();
		try {
			cloned.exceededPredAddVacationCalc = this.exceededPredAddVacationCalc.clone();
			cloned.overTimeCalcNoBreak = this.overTimeCalcNoBreak.clone();
		}
		catch (Exception e){
			throw new RuntimeException("FixedWorkSetting clone error.");
		}
		return cloned;
	}
}


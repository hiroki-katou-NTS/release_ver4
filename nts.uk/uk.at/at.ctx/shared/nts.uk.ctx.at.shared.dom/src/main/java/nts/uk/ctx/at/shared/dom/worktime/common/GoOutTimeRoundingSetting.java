/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class GoOutTimeRoundingSetting.
 */
//外出時間の丸め設定
@Getter
@NoArgsConstructor
public class GoOutTimeRoundingSetting extends WorkTimeDomainObject implements Cloneable{

	/** The rounding method. */
	// 丸め方法
	private RoundingGoOutTimeSheet roundingMethod;

	/** The rounding setting. */
	// 丸め設定
	private TimeRoundingSetting roundingSetting;

	/**
	 * Instantiates a new go out time rounding setting.
	 *
	 * @param roundingMethod
	 *            the rounding method
	 * @param roundingSetting
	 *            the rounding setting
	 */
	public GoOutTimeRoundingSetting(RoundingGoOutTimeSheet roundingMethod, TimeRoundingSetting roundingSetting) {
		super();
		this.roundingMethod = roundingMethod;
		this.roundingSetting = roundingSetting;
	}

	/**
	 * Instantiates a new go out time rounding setting.
	 *
	 * @param memento
	 *            the memento
	 */
	public GoOutTimeRoundingSetting(GoOutTimeRoundingSettingGetMemento memento) {
		this.roundingMethod = memento.getRoundingMethod();
		this.roundingSetting = memento.getRoundingSetting();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(GoOutTimeRoundingSettingSetMemento memento) {
		memento.setRoundingMethod(this.roundingMethod);
		memento.setRoundingSetting(this.roundingSetting);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, GoOutTimeRoundingSetting oldDomain) {
		// Simple mode
		if (screenMode == ScreenMode.SIMPLE) {
			this.roundingMethod = RoundingGoOutTimeSheet.REVERSE_ROUNDING_EACH_TIMEZONE;
			this.roundingSetting.setDefaultDataRoundingDown();
			return;
		}

		// Detail mode
		switch (this.roundingMethod) {
		case REVERSE_ROUNDING_EACH_TIMEZONE:
			this.roundingSetting.correctData(oldDomain.getRoundingSetting());
			break;

		case INDIVIDUAL_ROUNDING:
			// Nothing change
			break;

		default:
			throw new RuntimeException("GoOutTimeRoundingMethod not found.");
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		// Simple mode
		if (screenMode == ScreenMode.SIMPLE) {
			this.roundingMethod = RoundingGoOutTimeSheet.REVERSE_ROUNDING_EACH_TIMEZONE;
			this.roundingSetting.setDefaultDataRoundingUp();
			return;
		}

		// Detail mode
		switch (this.roundingMethod) {
		case REVERSE_ROUNDING_EACH_TIMEZONE:
			this.roundingSetting.setDefaultDataRoundingUp();
			break;

		case INDIVIDUAL_ROUNDING:
			// Nothing change
			break;

		default:
			throw new RuntimeException("GoOutTimeRoundingMethod not found.");
		}
	}
	
	@Override
	public GoOutTimeRoundingSetting clone() {
		GoOutTimeRoundingSetting cloned = new GoOutTimeRoundingSetting();
		try {
			cloned.roundingMethod = RoundingGoOutTimeSheet.valueOf(this.roundingMethod.value);
			cloned.roundingSetting = this.roundingSetting.clone();
		}
		catch (Exception e){
			throw new RuntimeException("GoOutTimeRoundingSetting clone error.");
		}
		return cloned;
	}
}

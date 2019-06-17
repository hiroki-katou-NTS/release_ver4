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
 * The Class WorkTimezoneExtraordTimeSet.
 */
// 就業時間帯の臨時時間設定
@Getter
@NoArgsConstructor
public class WorkTimezoneExtraordTimeSet extends WorkTimeDomainObject implements Cloneable{

	/** The holiday frame set. */
	// 休出枠設定
	private HolidayFramset holidayFrameSet;

	/** The time rounding set. */
	// 時間丸め設定
	private TimeRoundingSetting timeRoundingSet;

	/** The o T frame set. */
	// 残業枠設定
	private ExtraordWorkOTFrameSet oTFrameSet;

	/** The calculate method. */
	// 計算方法
	private ExtraordTimeCalculateMethod calculateMethod;

	/**
	 * Instantiates a new work timezone extraord time set.
	 *
	 * @param memento
	 *            the memento
	 */
	public WorkTimezoneExtraordTimeSet(WorkTimezoneExtraordTimeSetGetMemento memento) {
		this.holidayFrameSet = memento.getHolidayFrameSet();
		this.timeRoundingSet = memento.getTimeRoundingSet();
		this.oTFrameSet = memento.getOTFrameSet();
		this.calculateMethod = memento.getCalculateMethod();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WorkTimezoneExtraordTimeSetSetMemento memento) {
		memento.setHolidayFrameSet(this.holidayFrameSet);
		memento.setTimeRoundingSet(this.timeRoundingSet);
		memento.setOTFrameSet(this.oTFrameSet);
		memento.setCalculateMethod(this.calculateMethod);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctData(ScreenMode screenMode) {
		if (ScreenMode.SIMPLE.equals(screenMode)) {
			this.timeRoundingSet.setDefaultDataRoundingDown();
		}
	}
	
	@Override
	public WorkTimezoneExtraordTimeSet clone() {
		WorkTimezoneExtraordTimeSet cloned = new WorkTimezoneExtraordTimeSet();
		try {
			cloned.holidayFrameSet = this.holidayFrameSet.clone();
			cloned.timeRoundingSet = this.timeRoundingSet.clone();
			cloned.oTFrameSet = this.oTFrameSet.clone();
			cloned.calculateMethod = ExtraordTimeCalculateMethod.valueOf(this.calculateMethod.value);
		}
		catch (Exception e){
			throw new RuntimeException("AggregateTotalTimeSpentAtWork clone error.");
		}
		return cloned;
	}
}

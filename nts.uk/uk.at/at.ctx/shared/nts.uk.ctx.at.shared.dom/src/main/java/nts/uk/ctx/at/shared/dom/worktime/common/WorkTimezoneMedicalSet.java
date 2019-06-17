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
 * The Class WorkTimezoneMedicalSet.
 */
// 就業時間帯の医療設定
@Getter
@NoArgsConstructor
public class WorkTimezoneMedicalSet extends WorkTimeDomainObject implements Cloneable{

	/** The rounding set. */
	// 丸め設定
	private TimeRoundingSetting roundingSet;

	/** The work system atr. */
	// 勤務体系区分
	private WorkSystemAtr workSystemAtr;

	/** The application time. */
	// 申送時間
	private OneDayTime applicationTime;

	/**
	 * Instantiates a new work timezone medical set.
	 *
	 * @param memento
	 *            the memento
	 */
	public WorkTimezoneMedicalSet(WorkTimezoneMedicalSetGetMemento memento) {
		this.roundingSet = memento.getRoundingSet();
		this.workSystemAtr = memento.getWorkSystemAtr();
		this.applicationTime = memento.getApplicationTime();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WorkTimezoneMedicalSetSetMemento memento) {
		memento.setRoundingSet(this.roundingSet);
		memento.setWorkSystemAtr(this.workSystemAtr);
		memento.setApplicationTime(this.applicationTime);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctData(ScreenMode screenMode) {
		if (ScreenMode.SIMPLE.equals(screenMode)) {
			this.applicationTime = new OneDayTime(0);
			this.roundingSet.setDefaultDataRoundingDown();
		}
	}
	
	@Override
	public WorkTimezoneMedicalSet clone() {
		WorkTimezoneMedicalSet cloned = new WorkTimezoneMedicalSet();
		try {
			cloned.roundingSet = this.roundingSet.clone();
			cloned.workSystemAtr = WorkSystemAtr.valueOf(this.workSystemAtr.value);
			cloned.applicationTime = new OneDayTime(this.applicationTime.v());
		}
		catch (Exception e){
			throw new RuntimeException("AggregateTotalTimeSpentAtWork clone error.");
		}
		return cloned;
	}
}

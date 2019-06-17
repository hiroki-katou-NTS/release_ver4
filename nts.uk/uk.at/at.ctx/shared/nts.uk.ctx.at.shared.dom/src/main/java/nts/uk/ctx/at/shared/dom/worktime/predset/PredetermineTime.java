/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.predset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;

/**
 * The Class PredetermineTime.
 */
@Getter
@NoArgsConstructor
// 所定時間
public class PredetermineTime extends WorkTimeDomainObject implements Cloneable{

	/** The add time. */
	// 就業加算時間
	private BreakDownTimeDay addTime;

	/** The pred time. */
	// 所定時間
	private BreakDownTimeDay predTime;

	/**
	 * Instantiates a new predetermine time.
	 *
	 * @param addTime
	 *            the add time
	 * @param predTime
	 *            the pred time
	 */
	public PredetermineTime(BreakDownTimeDay addTime, BreakDownTimeDay predTime) {
		super();
		this.addTime = addTime;
		this.predTime = predTime;
	}

	/**
	 * Instantiates a new predetermine time.
	 *
	 * @param memento
	 *            the memento
	 */
	public PredetermineTime(PredetermineTimeGetMemento memento) {
		this.addTime = memento.getAddTime();
		this.predTime = memento.getPredTime();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(PredetermineTimeSetMemento memento) {
		memento.setAddTime(this.addTime);
		memento.setPredTime(this.predTime);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param workTimeType
	 *            the work time type
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, WorkTimeDivision workTimeType, PredetermineTime oldDomain) {
		if (screenMode == ScreenMode.SIMPLE) {
			// Simple mode
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode the screen mode
	 * @param workTimeType the work time type
	 */
	public void correctDefaultData(ScreenMode screenMode, WorkTimeDivision workTimeType) {
		if (screenMode == ScreenMode.SIMPLE) {
			// Simple mode
		} 		
	}
	
	@Override
	public PredetermineTime clone() {
		PredetermineTime cloned = new PredetermineTime();
		try {
			cloned.addTime = this.addTime.clone();
			cloned.predTime = this.predTime.clone();
		}
		catch (Exception e){
			throw new RuntimeException("PredetemineTimeSetting clone error.");
		}
		return cloned;
	}

}

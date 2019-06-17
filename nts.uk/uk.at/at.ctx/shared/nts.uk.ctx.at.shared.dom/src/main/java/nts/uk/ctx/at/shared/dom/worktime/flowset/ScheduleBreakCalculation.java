/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The Class ScheduleBreakCalculation.
 */
// 予定から休憩を計算
@Getter
@NoArgsConstructor
public class ScheduleBreakCalculation implements Cloneable{

	/** The is refer rest time. */
	// 休憩時刻がない場合はマスタから休憩時刻を参照する
	private boolean isReferRestTime;

	/** The is calc from schedule. */
	// 予定と実績の勤務が一致しな場合はマスタ参照
	private boolean isCalcFromSchedule;

	/**
	 * Instantiates a new schedule break calculation.
	 *
	 * @param isReferRestTime
	 *            the is refer rest time
	 */
	public ScheduleBreakCalculation(boolean isReferRestTime) {
		super();
		this.isReferRestTime = isReferRestTime;
	}

	/**
	 * Instantiates a new schedule break calculation.
	 *
	 * @param memento
	 *            the memento
	 */
	public ScheduleBreakCalculation(ScheduleBreakCalculationGetMemento memento) {
		this.isReferRestTime = memento.getIsReferRestTime();
		this.isCalcFromSchedule = memento.getIsCalcFromSchedule();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(ScheduleBreakCalculationSetMemento memento) {
		memento.setIsReferRestTime(this.isReferRestTime);
		memento.setIsCalcFromSchedule(this.isCalcFromSchedule);
	}

	public void setDefaultValue() {
		this.isReferRestTime = false;
		this.isCalcFromSchedule = false;
	}
	
	@Override
	public ScheduleBreakCalculation clone() {
		ScheduleBreakCalculation cloned = new ScheduleBreakCalculation();
		try {
			cloned.isReferRestTime = this.isReferRestTime ? true : false ;
			cloned.isCalcFromSchedule= this.isCalcFromSchedule ? true : false ;
		}
		catch (Exception e){
			throw new RuntimeException("ScheduleBreakCalculation clone error.");
		}
		return cloned;
	}
}

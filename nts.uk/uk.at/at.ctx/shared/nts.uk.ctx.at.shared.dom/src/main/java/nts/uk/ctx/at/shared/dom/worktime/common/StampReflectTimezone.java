/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class StampReflectTimezone.
 */
//打刻反映時間帯
@Getter
@NoArgsConstructor
@Builder
public class StampReflectTimezone extends WorkTimeDomainObject implements Cloneable{

	/** The work no. */
	// 勤務NO
	private WorkNo workNo;

	/** The classification. */
	// 出退勤区分
	private GoLeavingWorkAtr classification;

	/** The end time. */
	// 終了時刻
	private TimeWithDayAttr endTime;

	/** The start time. */
	// 開始時刻
	private TimeWithDayAttr startTime;

	/**
	 * Instantiates a new stamp reflect timezone.
	 *
	 * @param workNo the work no
	 * @param classification the classification
	 * @param endTime the end time
	 * @param startTime the start time
	 */
	public StampReflectTimezone(WorkNo workNo, GoLeavingWorkAtr classification, TimeWithDayAttr endTime,
			TimeWithDayAttr startTime) {
		super();
		this.workNo = workNo;
		this.classification = classification;
		this.endTime = endTime;
		this.startTime = startTime;
	}

	/**
	 * Instantiates a new stamp reflect timezone.
	 *
	 */
	public StampReflectTimezone(StampReflectTimezoneGetMemento memento) {
		this.workNo = memento.getWorkNo();
		this.classification = memento.getClassification();
		this.endTime = memento.getEndTime();
		this.startTime = memento.getStartTime();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(StampReflectTimezoneSetMemento memento) {
		memento.setWorkNo(this.workNo);
		memento.setClassification(this.classification);
		memento.setEndTime(this.endTime);
		memento.setStartTime(this.startTime);
	}
	
	/**
	 * Update start time.
	 *
	 * @param startTime the start time
	 */
	public void updateStartTime(TimeWithDayAttr startTime) {
		this.startTime = startTime;
	}

	/**
	 * Update end time.
	 *
	 * @param endTime the end time
	 */
	public void updateEndTime(TimeWithDayAttr endTime) {
		this.endTime = endTime;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return (this.startTime.v().equals(0) && this.endTime.v().equals(0));
	}

	/**
	 * Checks if is go work 1.
	 *
	 * @return true, if is go work 1
	 */
	public boolean isGoWork1() {
		return this.workNo.v() == 1 && GoLeavingWorkAtr.GO_WORK.equals(this.classification);
	}

	/**
	 * Checks if is go work 2.
	 *
	 * @return true, if is go work 2
	 */
	public boolean isGoWork2() {
		return this.workNo.v() == 2 && GoLeavingWorkAtr.GO_WORK.equals(this.classification);
	}

	/**
	 * Checks if is leave work 1.
	 *
	 * @return true, if is leave work 1
	 */
	public boolean isLeaveWork1() {
		return this.workNo.v() == 1 && GoLeavingWorkAtr.LEAVING_WORK.equals(this.classification);
	}

	/**
	 * Checks if is leave work 2.
	 *
	 * @return true, if is leave work 2
	 */
	public boolean isLeaveWork2() {
		return this.workNo.v() == 2 && GoLeavingWorkAtr.LEAVING_WORK.equals(this.classification);
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
	public void correctData(ScreenMode screenMode, StampReflectTimezone oldDomain) {
		if (oldDomain == null) {
			return;
		}
		if (ScreenMode.SIMPLE.equals(screenMode)) {
			//this.startTime = oldDomain.getStartTime();
			//this.endTime = oldDomain.getEndTime();
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		if (ScreenMode.SIMPLE.equals(screenMode)) {
			//this.startTime = new TimeWithDayAttr(0);
			//this.endTime = new TimeWithDayAttr(0);
		}
	}
	
	@Override
	public StampReflectTimezone clone() {
		StampReflectTimezone cloned = new StampReflectTimezone();
		try {
			cloned.workNo = new WorkNo(this.workNo.v());
			cloned.classification = GoLeavingWorkAtr.valueOf(this.classification.value);
			cloned.startTime = new TimeWithDayAttr(this.startTime.valueAsMinutes());
			cloned.endTime = new TimeWithDayAttr(this.endTime.valueAsMinutes());
		}
		catch (Exception e){
			throw new RuntimeException("StampReflectTimezone clone error.");
		}
		return cloned;
	}

}

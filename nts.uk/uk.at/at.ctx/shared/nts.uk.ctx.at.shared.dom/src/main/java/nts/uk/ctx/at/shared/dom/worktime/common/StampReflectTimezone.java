/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class StampReflectTimezone.
 */
//打刻反映時間帯
@Getter
public class StampReflectTimezone extends WorkTimeDomainObject {

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
	 * @param memento
	 *            the memento
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
		return this.workNo.v() == 1 && GoLeavingWorkAtr.GO_WORK.equals(this.classification.value);
	}

	/**
	 * Checks if is go work 2.
	 *
	 * @return true, if is go work 2
	 */
	public boolean isGoWork2() {
		return this.workNo.v() == 2 && GoLeavingWorkAtr.GO_WORK.equals(this.classification.value);
	}

	/**
	 * Checks if is leave work 1.
	 *
	 * @return true, if is leave work 1
	 */
	public boolean isLeaveWork1() {
		return this.workNo.v() == 1 && GoLeavingWorkAtr.LEAVING_WORK.equals(this.classification.value);
	}

	/**
	 * Checks if is leave work 2.
	 *
	 * @return true, if is leave work 2
	 */
	public boolean isLeaveWork2() {
		return this.workNo.v() == 2 && GoLeavingWorkAtr.LEAVING_WORK.equals(classification.value);
	}
}

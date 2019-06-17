/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class WorkTimezoneOtherSubHolTimeSet.
 */
//就業時間帯別代休時間設定
@Getter
@NoArgsConstructor
public class WorkTimezoneOtherSubHolTimeSet extends WorkTimeDomainObject implements Cloneable{

	/** The sub hol time set. */
	// 代休時間設定
	private SubHolTransferSet subHolTimeSet;

	/** The work time code. */
	// 就業時間帯コード
	private WorkTimeCode workTimeCode;

	/** The origin atr. */
	// 発生元区分
	private CompensatoryOccurrenceDivision originAtr;

	/**
	 * Instantiates a new work timezone other sub hol time set.
	 *
	 * @param memento
	 *            the memento
	 */
	public WorkTimezoneOtherSubHolTimeSet(WorkTimezoneOtherSubHolTimeSetGetMemento memento) {
		this.subHolTimeSet = memento.getSubHolTimeSet();
		this.workTimeCode = memento.getWorkTimeCode();
		this.originAtr = memento.getOriginAtr();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WorkTimezoneOtherSubHolTimeSetSetMemento memento) {
		memento.setSubHolTimeSet(this.subHolTimeSet);
		memento.setWorkTimeCode(this.workTimeCode);
		memento.setOriginAtr(this.originAtr);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, WorkTimezoneOtherSubHolTimeSet oldDomain) {
		this.subHolTimeSet.correctData(screenMode, oldDomain.getSubHolTimeSet(),this.originAtr);
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		this.subHolTimeSet.correctDefaultData(screenMode,this.originAtr);
	}
	
	@Override
	public WorkTimezoneOtherSubHolTimeSet clone() {
		WorkTimezoneOtherSubHolTimeSet cloned = new WorkTimezoneOtherSubHolTimeSet();
		try {
			cloned.subHolTimeSet = this.subHolTimeSet.clone();
			cloned.workTimeCode = new WorkTimeCode(this.workTimeCode.v());
			cloned.originAtr = CompensatoryOccurrenceDivision.valueOf(this.originAtr.value);
		}
		catch (Exception e){
			throw new RuntimeException("WorkTimezoneOtherSubHolTimeSet clone error.");
		}
		return cloned;
	}

}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class WorkTimezoneOtherSubHolTimeSet.
 */
//就業時間帯別代休時間設定
@Getter
public class WorkTimezoneOtherSubHolTimeSet extends WorkTimeDomainObject {

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
	 * @param memento the memento
	 */
	public WorkTimezoneOtherSubHolTimeSet(WorkTimezoneOtherSubHolTimeSetGetMemento memento) {
		this.subHolTimeSet = memento.getSubHolTimeSet();
		this.workTimeCode = memento.getWorkTimeCode();
		this.originAtr = memento.getOriginAtr();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkTimezoneOtherSubHolTimeSetSetMemento memento) {
		memento.setSubHolTimeSet(this.subHolTimeSet);
		memento.setWorkTimeCode(this.workTimeCode);
		memento.setOriginAtr(this.originAtr);
	}
	
	/**
	 * Restore data.
	 *
	 * @param screenMode the screen mode
	 * @param oldDomain the old domain
	 */
	public void restoreData(ScreenMode screenMode, WorkTimezoneOtherSubHolTimeSet oldDomain) {
		this.subHolTimeSet.restoreData(screenMode, oldDomain.getSubHolTimeSet());
	}
	
	/**
	 * Restore default data.
	 *
	 * @param screenMode the screen mode
	 */
	public void restoreDefaultData(ScreenMode screenMode) {
		this.subHolTimeSet.restoreDefaultData(screenMode);
	}
	
}

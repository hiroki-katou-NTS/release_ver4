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
 * The Class PrioritySetting.
 */
// 優先設定
@Getter
@NoArgsConstructor
public class PrioritySetting extends WorkTimeDomainObject implements Cloneable{

	/** The priority atr. */
	// 優先区分
	private MultiStampTimePiorityAtr priorityAtr;

	/** The stamp atr. */
	// 打刻区分
	private StampPiorityAtr stampAtr;

	/**
	 * Instantiates a new priority setting.
	 *
	 * @param memento
	 *            the memento
	 */
	public PrioritySetting(PrioritySettingGetMemento memento) {
		this.priorityAtr = memento.getPriorityAtr();
		this.stampAtr = memento.getStampAtr();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(PrioritySettingSetMemento memento) {
		memento.setPriorityAtr(this.priorityAtr);
		memento.setStampAtr(this.stampAtr);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, PrioritySetting oldDomain) {
		if (screenMode == ScreenMode.SIMPLE) {
			if (this.stampAtr == StampPiorityAtr.GOING_WORK || this.stampAtr == StampPiorityAtr.LEAVE_WORK) {
				this.priorityAtr = MultiStampTimePiorityAtr.BEFORE_PIORITY;
			}
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		if (screenMode == ScreenMode.SIMPLE) {
			if (this.stampAtr == StampPiorityAtr.GOING_WORK) {
				this.priorityAtr = MultiStampTimePiorityAtr.BEFORE_PIORITY;
			}

			if (this.stampAtr == StampPiorityAtr.LEAVE_WORK) {
				this.priorityAtr = MultiStampTimePiorityAtr.AFTER_PIORITY;
			}
		}
	}
	
	@Override
	public PrioritySetting clone() {
		PrioritySetting cloned = new PrioritySetting();
		try {
			cloned.priorityAtr = MultiStampTimePiorityAtr.valueOf(this.priorityAtr.value);
			cloned.stampAtr = StampPiorityAtr.valueOf(this.stampAtr.value);
		}
		catch (Exception e){
			throw new RuntimeException("AggregateTotalTimeSpentAtWork clone error.");
		}
		return cloned;
	}
}

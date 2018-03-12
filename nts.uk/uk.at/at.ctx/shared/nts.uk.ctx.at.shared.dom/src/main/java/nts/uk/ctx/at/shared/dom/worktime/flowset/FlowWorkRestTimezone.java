/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class FlowWorkRestTimezone.
 */
// 流動勤務の休憩時間帯
@Getter
public class FlowWorkRestTimezone extends WorkTimeDomainObject {

	/** The fix rest time. */
	// 休憩時間帯を固定にする
	private boolean fixRestTime;

	/** The fixed rest timezone. */
	// 固定休憩時間帯
	private TimezoneOfFixedRestTimeSet fixedRestTimezone;

	/** The flow rest timezone. */
	// 流動休憩時間帯
	private FlowRestTimezone flowRestTimezone;

	/**
	 * Instantiates a new flow work rest timezone.
	 *
	 * @param fixRestTime the fix rest time
	 * @param fixedRestTimezone the fixed rest timezone
	 * @param flowRestTimezone the flow rest timezone
	 */
	public FlowWorkRestTimezone(boolean fixRestTime, TimezoneOfFixedRestTimeSet fixedRestTimezone,
			FlowRestTimezone flowRestTimezone) {
		super();
		this.fixRestTime = fixRestTime;
		this.fixedRestTimezone = fixedRestTimezone;
		this.flowRestTimezone = flowRestTimezone;
	}

	/**
	 * Instantiates a new flow work rest timezone.
	 *
	 * @param memento the memento
	 */
	public FlowWorkRestTimezone(FlowWorkRestTimezoneGetMemento memento) {
		this.fixRestTime = memento.getFixRestTime();
		this.fixedRestTimezone = memento.getFixedRestTimezone();
		this.flowRestTimezone = memento.getFlowRestTimezone();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlowWorkRestTimezoneSetMemento memento) {
		memento.setFixRestTime(this.fixRestTime);
		memento.setFixedRestTimezone(this.fixedRestTimezone);
		memento.setFlowRestTimezone(this.flowRestTimezone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject#validate()
	 */
	@Override
	public void validate() {
		this.fixedRestTimezone.checkOverlap("#KMK003_20");
		super.validate();
	}

	/**
	 * Restore data.
	 *
	 * @param screenMode the screen mode
	 * @param oldDomain the old domain
	 */
	public void restoreData(ScreenMode screenMode, FlowWorkRestTimezone oldDomain) {
		if (this.fixRestTime) {
			this.flowRestTimezone = oldDomain.getFlowRestTimezone();
		} else {
			this.fixedRestTimezone = oldDomain.getFixedRestTimezone();
			this.flowRestTimezone.restoreData(screenMode, oldDomain.getFlowRestTimezone());
		}
	}
	
	/**
	 * Restore default data.
	 *
	 * @param screenMode the screen mode
	 */
	public void restoreDefaultData(ScreenMode screenMode) {
		if (this.fixRestTime) {
			this.flowRestTimezone = new FlowRestTimezone();
		} else {
			this.fixedRestTimezone = new TimezoneOfFixedRestTimeSet();
			this.flowRestTimezone.restoreDefaultData(screenMode);
		}
	}
	
	public void restoreFixRestTime(boolean fixRestTime) {
		this.fixRestTime = fixRestTime;
	}
}

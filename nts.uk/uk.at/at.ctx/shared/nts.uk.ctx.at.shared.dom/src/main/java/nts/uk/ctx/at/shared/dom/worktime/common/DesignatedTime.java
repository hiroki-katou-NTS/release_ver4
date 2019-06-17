/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class DesignatedTime.
 */
// 指定時間
@Getter
@NoArgsConstructor
public class DesignatedTime extends WorkTimeDomainObject implements Cloneable{

	/** The one day time. */
	// 1日の時間
	private OneDayTime oneDayTime;

	/** The half day time. */
	// 半日の時間
	private OneDayTime halfDayTime;

	/**
	 * Instantiates a new designated time.
	 *
	 * @param oneDayTime the one day time
	 * @param halfDayTime the half day time
	 */
	public DesignatedTime(OneDayTime oneDayTime, OneDayTime halfDayTime) {
		super();
		this.oneDayTime = oneDayTime;
		this.halfDayTime = halfDayTime;
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(DesignatedTimeSetMemento memento) {
		memento.setOneDayTime(this.oneDayTime);
		memento.setHalfDayTime(this.halfDayTime);
	}
	
	/**
	 * Instantiates a new designated time.
	 *
	 * @param memento the memento
	 */
	public DesignatedTime(DesignatedTimeGetMemento memento){
		this.oneDayTime = memento.getOneDayTime();
		this.halfDayTime = memento.getHalfDayTime();
	}

	/**
	 * Restore data.
	 *
	 * @param oldDomain the old domain
	 */
	public void restoreData(DesignatedTime oldDomain) {
		this.oneDayTime = oldDomain.getOneDayTime();
		this.halfDayTime = oldDomain.getHalfDayTime();
	}
	
	/**
	 * Restore default data.
	 */
	public void restoreDefaultData() {
		this.oneDayTime = new OneDayTime(0);
		this.halfDayTime = new OneDayTime(0);
	}

	@Override
	public DesignatedTime clone() {
		DesignatedTime cloned = new DesignatedTime();
		try {
			cloned.oneDayTime = new OneDayTime(this.oneDayTime.v());
			cloned.halfDayTime = new OneDayTime(this.halfDayTime.v());
		}
		catch (Exception e){
			throw new RuntimeException("DesignatedTime clone error.");
		}
		return cloned;
	}
}

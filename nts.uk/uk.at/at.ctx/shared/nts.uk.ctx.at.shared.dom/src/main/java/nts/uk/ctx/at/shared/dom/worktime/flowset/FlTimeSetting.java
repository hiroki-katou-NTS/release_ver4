/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;

/**
 * The Class FlTimeSetting.
 */
//流動時間設定
@Getter
public class FlTimeSetting extends DomainObject {

	/** The rounding. */
	// 丸め
	private TimeRoundingSetting rounding;

	/** The elapsed time. */
	// 経過時間
	private AttendanceTime elapsedTime;

	/**
	 * Instantiates a new fl time setting.
	 *
	 * @param memento the memento
	 */
	public FlTimeSetting(FlTimeGetMemento memento) {
		this.rounding = memento.getRouding();
		this.elapsedTime = memento.getElapsedTime();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlTimeSetMemento memento) {
		memento.setRouding(this.rounding);
		memento.setElapsedTime(this.elapsedTime);
	}
}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.difftimeset;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktime.common.InstantRounding;

/**
 * The Class EmTimezoneChangeExtent.
 */
// 就業時間帯変動可能範囲
@Getter
public class EmTimezoneChangeExtent extends DomainObject {

	/** The ahead change. */
	// 前に変動
	AttendanceTime aheadChange;

	/** The unit. */
	// 単位
	InstantRounding unit;

	/** The behind change. */
	// 後に変動
	AttendanceTime behindChange;

	/**
	 * Instantiates a new em timezone change extent.
	 *
	 * @param memento the memento
	 */
	public EmTimezoneChangeExtent(EmTimezoneChangeExtentGetMemento memento) {
		this.aheadChange = memento.getAheadChange();
		this.unit = memento.getUnit();
		this.behindChange = memento.getBehindChange();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(EmTimezoneChangeExtentSetMemento memento) {
		memento.setAheadChange(this.aheadChange);
		memento.setUnit(this.unit);
		memento.setBehindChange(this.behindChange);
	}
}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class WorkTimezoneLateNightTimeSet.
 */
// 就業時間帯の深夜時間設定
@Getter
@NoArgsConstructor
public class WorkTimezoneLateNightTimeSet extends WorkTimeDomainObject implements Cloneable{

	/** The rounding setting. */
	// 丸め設定
	private TimeRoundingSetting roundingSetting;

	/**
	 * Instantiates a new work timezone late night time set.
	 *
	 * @param roundingSetting the rounding setting
	 */
	public WorkTimezoneLateNightTimeSet(TimeRoundingSetting roundingSetting) {
		super();
		this.roundingSetting = roundingSetting;
	}
	
	/**
	 * Instantiates a new work timezone late night time set.
	 *
	 * @param memento the memento
	 */
	public WorkTimezoneLateNightTimeSet(WorkTimezoneLateNightTimeSetGetMemento memento) {
		this.roundingSetting = memento.getRoundingSetting();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkTimezoneLateNightTimeSetSetMemento memento) {
		memento.setRoundingSetting(this.roundingSetting);
	}

	@Override
	public WorkTimezoneLateNightTimeSet clone() {
		WorkTimezoneLateNightTimeSet cloned = new WorkTimezoneLateNightTimeSet();
		try {
			cloned.roundingSetting = this.roundingSetting.clone();
		}
		catch (Exception e){
			throw new RuntimeException("WorkTimezoneLateNightTimeSet clone error.");
		}
		return cloned;
	}
}

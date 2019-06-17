/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class WorkTimezoneStampSet.
 */
//就業時間帯の打刻設定
@Getter
@NoArgsConstructor
public class WorkTimezoneStampSet extends WorkTimeDomainObject implements Cloneable{

	/** The rounding sets. */
	// 丸め設定
	private List<RoundingSet> roundingSets;

	/** The priority sets. */
	// 優先設定
	private List<PrioritySetting> prioritySets;

	/**
	 * Instantiates a new work timezone stamp set.
	 *
	 * @param memento
	 *            the memento
	 */
	public WorkTimezoneStampSet(WorkTimezoneStampSetGetMemento memento) {
		this.roundingSets = memento.getRoundingSet();
		this.prioritySets = memento.getPrioritySet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WorkTimezoneStampSetSetMemento memento) {
		memento.setRoundingSet(this.roundingSets);
		memento.setPrioritySet(this.prioritySets);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, WorkTimezoneStampSet oldDomain) {
		this.prioritySets.forEach(item -> item.correctData(screenMode, oldDomain.getPrioritySets().stream()
				.filter(oldItem -> oldItem.getStampAtr().equals(item.getStampAtr())).findFirst().orElse(null)));
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		this.prioritySets.forEach(item -> item.correctDefaultData(screenMode));
	}
	
	@Override
	public WorkTimezoneStampSet clone() {
		WorkTimezoneStampSet cloned = new WorkTimezoneStampSet();
		try {
			cloned.roundingSets = this.roundingSets.stream().map(c -> c.clone()).collect(Collectors.toList());
			cloned.prioritySets = this.prioritySets.stream().map(c -> c.clone()).collect(Collectors.toList());
		}
		catch (Exception e){
			throw new RuntimeException("WorkTimezoneStampSet clone error.");
		}
		return cloned;
	}
}

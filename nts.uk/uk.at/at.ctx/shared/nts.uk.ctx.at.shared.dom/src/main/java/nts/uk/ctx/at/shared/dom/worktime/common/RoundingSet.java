/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class RoundingSet.
 */
// 丸め設定
@Getter
@NoArgsConstructor
public class RoundingSet extends WorkTimeDomainObject implements Cloneable{

	/** The rounding set. */
	// 時刻丸め
	private InstantRounding roundingSet;

	/** The section. */
	// 打優丸め区分
	private Superiority section;

	/**
	 * Instantiates a new rounding set.
	 *
	 * @param memento
	 *            the memento
	 */
	public RoundingSet(RoundingSetGetMemento memento) {
		this.roundingSet = memento.getRoundingSet();
		this.section = memento.getSection();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(RoundingSetSetMemento memento) {
		memento.setRoundingSet(this.roundingSet);
		memento.setSection(this.section);
	}

	@Override
	public RoundingSet clone() {
		RoundingSet cloned = new RoundingSet();
		try {
			cloned.roundingSet = this.roundingSet.clone();
			cloned.section = Superiority.valueOf(this.section.value);
		}
		catch (Exception e){
			throw new RuntimeException("AggregateTotalTimeSpentAtWork clone error.");
		}
		return cloned;
	}
}

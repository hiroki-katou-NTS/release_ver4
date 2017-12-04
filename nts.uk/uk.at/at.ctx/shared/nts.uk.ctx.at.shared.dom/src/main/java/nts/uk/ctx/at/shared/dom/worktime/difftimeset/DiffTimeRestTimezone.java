/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.difftimeset;

import java.util.List;

import lombok.Getter;

/**
 * The Class DiffTimeRestTimezone.
 */
// 時差勤務の休憩時間帯
@Getter
public class DiffTimeRestTimezone {
	
	/** The rest timezone. */
	// 休憩時間帯
	private List<DiffTimeDeductTimezone> restTimezone;
	
	/**
	 * Instantiates a new diff time rest timezone.
	 *
	 * @param memento the memento
	 */
	public DiffTimeRestTimezone(DiffTimeRestTimezoneGetMemento memento)
	{
		this.restTimezone = memento.getRestTimezone();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(DiffTimeRestTimezoneSetMemento memento)
	{
		memento.setRestTimezone(this.restTimezone);
	}
}

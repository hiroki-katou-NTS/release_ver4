/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workingcondition;

import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class PersonalDayOfWeek.
 */
// 個人曜日別勤務
@Getter
public class PersonalDayOfWeek extends DomainObject {

	/** The monday. */
	// 月曜日
	private Optional<SingleDaySchedule> monday;

	/** The tuesday. */
	// 火曜日
	private Optional<SingleDaySchedule> tuesday;

	/** The wednesday. */
	// 水曜日
	private Optional<SingleDaySchedule> wednesday;

	/** The thursday. */
	// 木曜日
	private Optional<SingleDaySchedule> thursday;

	/** The friday. */
	// 金曜日
	private Optional<SingleDaySchedule> friday;

	/** The saturday. */
	// 土曜日
	private Optional<SingleDaySchedule> saturday;

	/** The sunday. */
	// 日曜日
	private Optional<SingleDaySchedule> sunday;

	/**
	 * Instantiates a new personal day of week.
	 *
	 * @param memento
	 *            the memento
	 */
	public PersonalDayOfWeek(PersonalDayOfWeekGetMemento memento) {
		this.saturday = memento.getSaturday();
		this.sunday = memento.getSunday();
		this.monday = memento.getMonday();
		this.thursday = memento.getThursday();
		this.wednesday = memento.getWednesday();
		this.tuesday = memento.getTuesday();
		this.friday = memento.getFriday();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(PersonalDayOfWeekSetMemento memento) {
		memento.setSaturday(this.saturday);
		memento.setSunday(this.sunday);
		memento.setMonday(this.monday);
		memento.setThursday(this.thursday);
		memento.setWednesday(this.wednesday);
		memento.setTuesday(this.tuesday);
		memento.setFriday(this.friday);
	}

	/**
	 * Instantiates a new personal day of week.
	 *
	 * @param monday the monday
	 * @param tuesday the tuesday
	 * @param wednesday the wednesday
	 * @param thursday the thursday
	 * @param friday the friday
	 * @param saturday the saturday
	 * @param sunday the sunday
	 */
	public PersonalDayOfWeek(Optional<SingleDaySchedule> monday, Optional<SingleDaySchedule> tuesday,
			Optional<SingleDaySchedule> wednesday, Optional<SingleDaySchedule> thursday,
			Optional<SingleDaySchedule> friday, Optional<SingleDaySchedule> saturday,
			Optional<SingleDaySchedule> sunday) {
		super();
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
	}
	
	
}

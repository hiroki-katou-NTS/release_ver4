/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.workingcondition;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import nts.uk.ctx.at.shared.dom.workingcondition.PersonalDayOfWeekSetMemento;
import nts.uk.ctx.at.shared.dom.workingcondition.SingleDaySchedule;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtPersonalDayOfWeek;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtPersonalDayOfWeekPK;

/**
 * The Class JpaPersonalDayOfWeekSetMemento.
 */
public class JpaPerDayOfWeekSetMemento implements PersonalDayOfWeekSetMemento {

	/** The history id. */
	private String historyId;

	/** The entities. */
	private List<KshmtPersonalDayOfWeek> entities;
	
	/**
	 * Instantiates a new jpa personal day of week set memento.
	 *
	 * @param entities
	 *            the entities
	 */
	public JpaPerDayOfWeekSetMemento(String historyId, List<KshmtPersonalDayOfWeek> entities) {
		this.historyId = historyId;
		this.entities = entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.personallaborcondition.
	 * PersonalDayOfWeekSetMemento#setSaturday(java.util.Optional)
	 */
	@Override
	public void setSaturday(Optional<SingleDaySchedule> saturday) {
		if (saturday.isPresent()) {
			this.setDayOfWeek(saturday.get(), this.historyId, DayOfWeekAtr.SATURDAY.value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.personallaborcondition.
	 * PersonalDayOfWeekSetMemento#setSunday(java.util.Optional)
	 */
	@Override
	public void setSunday(Optional<SingleDaySchedule> sunday) {
		if (sunday.isPresent()) {
			this.setDayOfWeek(sunday.get(), this.historyId, DayOfWeekAtr.SUNDAY.value);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.personallaborcondition.
	 * PersonalDayOfWeekSetMemento#setMonday(java.util.Optional)
	 */
	@Override
	public void setMonday(Optional<SingleDaySchedule> monday) {
		if (monday.isPresent()) {
			this.setDayOfWeek(monday.get(), this.historyId, DayOfWeekAtr.MONDAY.value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.personallaborcondition.
	 * PersonalDayOfWeekSetMemento#setThursday(java.util.Optional)
	 */
	@Override
	public void setThursday(Optional<SingleDaySchedule> thursday) {
		if (thursday.isPresent()) {
			this.setDayOfWeek(thursday.get(), this.historyId, DayOfWeekAtr.THURSDAY.value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.personallaborcondition.
	 * PersonalDayOfWeekSetMemento#setWednesday(java.util.Optional)
	 */
	@Override
	public void setWednesday(Optional<SingleDaySchedule> wednesday) {
		if (wednesday.isPresent()) {
			this.setDayOfWeek(wednesday.get(), this.historyId, DayOfWeekAtr.WEDNESDAY.value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.personallaborcondition.
	 * PersonalDayOfWeekSetMemento#setTuesday(java.util.Optional)
	 */
	@Override
	public void setTuesday(Optional<SingleDaySchedule> tuesday) {
		if (tuesday.isPresent()) {
			this.setDayOfWeek(tuesday.get(), this.historyId, DayOfWeekAtr.TUESDAY.value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.personallaborcondition.
	 * PersonalDayOfWeekSetMemento#setFriday(java.util.Optional)
	 */
	@Override
	public void setFriday(Optional<SingleDaySchedule> friday) {
		if (friday.isPresent()) {
			this.setDayOfWeek(friday.get(), this.historyId, DayOfWeekAtr.FRIDAY.value);
		}
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @param dayOfWeekAtr
	 *            the day of week atr
	 * @return the kshmt per day of week
	 */
	private void setDayOfWeek(SingleDaySchedule domain, String historyId,
			int dayOfWeekAtr) {
		// Convert list entities to map for searching
		Map<KshmtPersonalDayOfWeekPK, KshmtPersonalDayOfWeek> mapEntities = this.entities.stream()
				.collect(Collectors.toMap(KshmtPersonalDayOfWeek::getKshmtPersonalDayOfWeekPK, Function.identity()));
		
		// Create primary key
		KshmtPersonalDayOfWeekPK pk = new KshmtPersonalDayOfWeekPK();
		pk.setHistoryId(historyId);
		pk.setPerWorkDayOffAtr(dayOfWeekAtr);
		
		// Get exist item with primary key or return new entity if not exist
		KshmtPersonalDayOfWeek entity = mapEntities.getOrDefault(pk, new KshmtPersonalDayOfWeek());
		
		// Save new data into entity
		domain.saveToMemento(new JpaSDayScheDayOfSetMemento(historyId, dayOfWeekAtr, entity));
		
		// Put new/updated entity into map
		mapEntities.put(pk, entity);
		
		// Put back to the entities list
		this.entities.clear();
		this.entities.addAll(mapEntities.values());
	}

}

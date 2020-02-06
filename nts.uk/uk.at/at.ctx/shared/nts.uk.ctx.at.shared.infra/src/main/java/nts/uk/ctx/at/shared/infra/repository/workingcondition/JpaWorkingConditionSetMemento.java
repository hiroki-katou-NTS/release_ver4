/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.workingcondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionSetMemento;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCond;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCondPK;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * The Class JpaWorkingConditionSetMemento.
 */
public class JpaWorkingConditionSetMemento implements WorkingConditionSetMemento {

	/** The kshmt working cond. */
	private List<KshmtWorkingCond> entities;

	/** The company id. */
	private String companyId;
	
	/** The employee id. */
	private String employeeId;

	/**
	 * Instantiates a new jpa working condition set memento.
	 *
	 * @param entities
	 *            the entity
	 */
	public JpaWorkingConditionSetMemento(List<KshmtWorkingCond> entities) {
		entities.stream().forEach(item -> {
			if (item.getKshmtWorkingCondPK() == null) {
				item.setKshmtWorkingCondPK(new KshmtWorkingCondPK());
			}
		});

		this.entities = entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionSetMemento#
	 * setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionSetMemento#
	 * setEmployeeId(java.lang.String)
	 */
	@Override
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionSetMemento#
	 * setDateHistoryItem(java.util.List)
	 */
	@Override
	public void setDateHistoryItem(List<DateHistoryItem> dateHistoryItems) {
		List<String> histIds = dateHistoryItems.stream().map(DateHistoryItem::identifier)
				.collect(Collectors.toList());
	
		Map<String, DatePeriod> mapHistoryItems = dateHistoryItems.stream()
				.collect(Collectors.toMap(DateHistoryItem::identifier, DateHistoryItem::span));

		// Remove not save entities
		this.entities.removeAll(
				this.entities.stream().filter(item -> !histIds.contains(item.getKshmtWorkingCondPK().getHistoryId()))
						.collect(Collectors.toList()));

		List<String> entityHistIds = new ArrayList<>();

		// Update into old entities
		this.entities.stream().forEach(item -> {
			KshmtWorkingCondPK kshmtWorkingCondPK = item.getKshmtWorkingCondPK();
			entityHistIds.add(kshmtWorkingCondPK.getHistoryId());
			if (mapHistoryItems.keySet().contains(kshmtWorkingCondPK.getHistoryId())) {
				item.setStrD(mapHistoryItems.get(kshmtWorkingCondPK.getHistoryId()).start());
				item.setEndD(mapHistoryItems.get(kshmtWorkingCondPK.getHistoryId()).end());
			}
		});
		
		// Add new items
		histIds.stream().filter(item -> !entityHistIds.contains(item)).forEach(item -> {
			KshmtWorkingCond kshmtWorkingCond = new KshmtWorkingCond();
			kshmtWorkingCond.setCid(this.companyId);
			KshmtWorkingCondPK kshmtWorkingCondPK = new KshmtWorkingCondPK(this.employeeId, item);
			kshmtWorkingCond.setKshmtWorkingCondPK(kshmtWorkingCondPK);
			DatePeriod datePeriod = mapHistoryItems.get(item);
			kshmtWorkingCond.setStrD(datePeriod.start());
			kshmtWorkingCond.setEndD(datePeriod.end());
			this.entities.add(kshmtWorkingCond);
		});
	}
	
}

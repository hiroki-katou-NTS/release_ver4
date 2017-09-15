/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.overtime.setting;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTCalMed;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingSetMemento;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.OvertimeNote;
import nts.uk.ctx.at.shared.infra.entity.outsideot.KshstOverTimeSet;
import nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.KshstOutsideOtBrd;
import nts.uk.ctx.at.shared.infra.entity.outsideot.overtime.KshstOverTime;
import nts.uk.ctx.at.shared.infra.entity.outsideot.overtime.KshstOverTimePK;
import nts.uk.ctx.at.shared.infra.repository.outsideot.breakdown.JpaOvertimeBRDItemSetMemento;
import nts.uk.ctx.at.shared.infra.repository.outsideot.overtime.JpaOvertimeSetMemento;

/**
 * The Class JpaOvertimeSettingSetMemento.
 */
@Getter
public class JpaOvertimeSettingSetMemento implements OutsideOTSettingSetMemento{
	
	/** The entity overtimes. */
	private List<KshstOverTime> entityOvertimes;
	
	/** The entity overtime BRD items. */
	private List<KshstOutsideOtBrd> entityOvertimeBRDItems;
	
	/** The entity. */
	private KshstOverTimeSet entity;
	
	/**
	 * Instantiates a new jpa overtime setting set memento.
	 *
	 * @param entityOvertimes the entity overtimes
	 * @param entity the entity
	 */
	public JpaOvertimeSettingSetMemento(List<KshstOverTime> entityOvertimes,
			List<KshstOutsideOtBrd> entityOvertimeBRDItems, KshstOverTimeSet entity) {
		entityOvertimes.forEach(entityItem -> {
			if (entityItem.getKshstOverTimePK() == null) {
				entityItem.setKshstOverTimePK(new KshstOverTimePK());
			}
		});
		this.entityOvertimes = entityOvertimes;
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.OvertimeSettingSetMemento#setCompanyId(
	 * nts.uk.ctx.at.shared.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		this.entity.setCid(companyId.v());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.OvertimeSettingSetMemento#setNote(nts.
	 * uk.ctx.at.shared.dom.overtime.OvertimeNote)
	 */
	@Override
	public void setNote(OvertimeNote note) {
		this.entity.setNote(note.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.overtime.setting.OvertimeSettingSetMemento#
	 * setBreakdownItems(java.util.List)
	 */
	@Override
	public void setBreakdownItems(List<OutsideOTBRDItem> breakdownItems) {
		this.entityOvertimeBRDItems = breakdownItems.stream().map(overtimeBRDItem -> {
			KshstOutsideOtBrd entityOvertimeBRDItem = new KshstOutsideOtBrd();
			overtimeBRDItem.saveToMemento(
					new JpaOvertimeBRDItemSetMemento(entityOvertimeBRDItem, this.entity.getCid()));
			return entityOvertimeBRDItem;
		}).collect(Collectors.toList());

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.overtime.OvertimeSettingSetMemento#
	 * setCalculationMethod(nts.uk.ctx.at.shared.dom.overtime.
	 * OvertimeCalculationMethod)
	 */
	@Override
	public void setCalculationMethod(OutsideOTCalMed calculationMethod) {
		this.entity.setCalculationMethod(calculationMethod.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.OvertimeSettingSetMemento#setOvertimes(
	 * java.util.List)
	 */
	@Override
	public void setOvertimes(List<Overtime> overtimes) {
		this.entityOvertimes = overtimes.stream().map(overtime -> {
			KshstOverTime entityOvertime = new KshstOverTime();
			overtime.saveToMemento(new JpaOvertimeSetMemento(entityOvertime, this.entity.getCid()));
			return entityOvertime;
		}).collect(Collectors.toList());
	}

	

}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.predset;

import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetermineTimeSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.predset.KshmtPredTimeSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.predset.KshmtPredTimeSetPK;

/**
 * The Class JpaPredetermineTimeSetMemento.
 */
public class JpaPredetermineTimeSetMemento implements PredetermineTimeSetMemento{
	
	/** The entity. */
	private  KshmtPredTimeSet entity;
	
	/**
	 * Instantiates a new jpa predetermine time set memento.
	 *
	 * @param entity the entity
	 */
	public JpaPredetermineTimeSetMemento(KshmtPredTimeSet entity) {
		super();
		if(entity.getKshmtPredTimeSetPK() == null){
			entity.setKshmtPredTimeSetPK(new KshmtPredTimeSetPK());
		}
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.predset.PredetermineTimeSetMemento#
	 * setAddTime(nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay)
	 */
	@Override
	public void setAddTime(BreakDownTimeDay addTime) {
		this.entity.setWorkAddOneDay(addTime.getOneDay().valueAsMinutes());
		this.entity.setWorkAddMorning(addTime.getMorning().valueAsMinutes());
		this.entity.setWorkAddAfternoon(addTime.getAfternoon().valueAsMinutes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.predset.PredetermineTimeSetMemento#
	 * setPredTime(nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay)
	 */
	@Override
	public void setPredTime(BreakDownTimeDay predTime) {
		this.entity.setPredOneDay(predTime.getOneDay().valueAsMinutes());
		this.entity.setPredMorning(predTime.getMorning().valueAsMinutes());
		this.entity.setPredAfternoon(predTime.getAfternoon().valueAsMinutes());
	}

}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.estimate.personal;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.shift.estimate.EstimateDetailSetting;
import nts.uk.ctx.at.schedule.dom.shift.estimate.Year;
import nts.uk.ctx.at.schedule.dom.shift.estimate.personal.PersonalEstablishmentGetMemento;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.personal.KscmtEstDaysPerSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.personal.KscmtEstPricePerSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.personal.KscmtEstTimePerSet;

/**
 * The Class JpaPersonalEstablishmentGetMemento.
 */
public class JpaPersonalEstablishmentGetMemento implements PersonalEstablishmentGetMemento{
	
	public static final int FIRST_TIME = 0;
	
	/** The estimate time Personals. */
	private List<KscmtEstTimePerSet> estimateTimePersonals;
	
	/** The estimate price Personals. */
	private List<KscmtEstPricePerSet> estimatePricePersonals;
	
	/** The estimate days Personals. */
	private List<KscmtEstDaysPerSet> estimateDaysPersonals;
	
	
	
	/**
	 * Instantiates a new jpa Personal establishment get memento.
	 *
	 * @param estimateTimePersonals the estimate time Personals
	 */
	public JpaPersonalEstablishmentGetMemento(List<KscmtEstTimePerSet> estimateTimePersonals,
			List<KscmtEstPricePerSet> estimatePricePersonals,
			List<KscmtEstDaysPerSet> estimateDaysPersonals) {
		this.estimateTimePersonals = estimateTimePersonals;
		this.estimatePricePersonals = estimatePricePersonals;
		this.estimateDaysPersonals = estimateDaysPersonals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.Personal.
	 * PersonalEstablishmentGetMemento#getTargetYear()
	 */
	@Override
	public Year getTargetYear() {
		return new Year(this.estimateTimePersonals.get(FIRST_TIME).getKscmtEstTimePerSetPK()
				.getTargetYear());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.Personal.
	 * PersonalEstablishmentGetMemento#getAdvancedSetting()
	 */
	@Override
	public EstimateDetailSetting getAdvancedSetting() {
		return new EstimateDetailSetting(new JpaPersonalEstimateDetailSettingGetMemento(
				this.estimateTimePersonals, this.estimatePricePersonals, this.estimateDaysPersonals));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.personal.
	 * PersonalEstablishmentGetMemento#getEmployeeId()
	 */
	@Override
	public String getEmployeeId() {
		return estimateTimePersonals.get(FIRST_TIME).getKscmtEstTimePerSetPK().getSid();
	}

}

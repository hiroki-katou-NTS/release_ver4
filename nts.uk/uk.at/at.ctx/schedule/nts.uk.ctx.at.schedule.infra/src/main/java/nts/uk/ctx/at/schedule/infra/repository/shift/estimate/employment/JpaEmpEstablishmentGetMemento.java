/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.estimate.employment;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.shift.estimate.EstimateDetailSetting;
import nts.uk.ctx.at.schedule.dom.shift.estimate.Year;
import nts.uk.ctx.at.schedule.dom.shift.estimate.employment.EmploymentEstablishmentGetMemento;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.employment.KscmtEstDaysEmpSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.employment.KscmtEstPriceEmpSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.employment.KscmtEstTimeEmpSet;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;

/**
 * The Class JpaEmploymentEstablishmentGetMemento.
 */
public class JpaEmpEstablishmentGetMemento implements EmploymentEstablishmentGetMemento{
	
	public static final int FIRST_TIME = 0;
	
	/** The estimate time Employments. */
	private List<KscmtEstTimeEmpSet> estimateTimeEmployments;
	
	/** The estimate price Employments. */
	private List<KscmtEstPriceEmpSet> estimatePriceEmployments;
	
	/** The estimate days Employments. */
	private List<KscmtEstDaysEmpSet> estimateDaysEmployments;
	
	
	
	/**
	 * Instantiates a new jpa Employment establishment get memento.
	 *
	 * @param estimateTimeEmployments the estimate time Employments
	 */
	public JpaEmpEstablishmentGetMemento(List<KscmtEstTimeEmpSet> estimateTimeEmployments,
			List<KscmtEstPriceEmpSet> estimatePriceEmployments,
			List<KscmtEstDaysEmpSet> estimateDaysEmployments) {
		this.estimateTimeEmployments = estimateTimeEmployments;
		this.estimatePriceEmployments = estimatePriceEmployments;
		this.estimateDaysEmployments = estimateDaysEmployments;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.Employment.
	 * EmploymentEstablishmentGetMemento#getAdvancedSetting()
	 */
	@Override
	public EstimateDetailSetting getAdvancedSetting() {
		return new EstimateDetailSetting(new JpaEmpEstDetailSetGetMemento(
				this.estimateTimeEmployments, this.estimatePriceEmployments, this.estimateDaysEmployments));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.employment.
	 * EmploymentEstablishmentGetMemento#getCompanyId()
	 */
	@Override
	public CompanyId getCompanyId() {
		return new CompanyId(
				this.estimateTimeEmployments.get(FIRST_TIME).getKscmtEstTimeEmpSetPK().getCid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.employment.
	 * EmploymentEstablishmentGetMemento#getEmploymentCode()
	 */
	@Override
	public EmploymentCode getEmploymentCode() {
		return new EmploymentCode(
				this.estimateTimeEmployments.get(FIRST_TIME).getKscmtEstTimeEmpSetPK().getEmpcd());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.employment.
	 * EmploymentEstablishmentGetMemento#getTargetYear()
	 */
	@Override
	public Year getTargetYear() {
		return new Year(this.estimateDaysEmployments.get(FIRST_TIME).getKscmtEstDaysEmpSetPK().getTargetYear());
	}

}

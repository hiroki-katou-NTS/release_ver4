/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.accidentrate;

import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.InsuBizRateItem;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsu;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsuPK;

/**
 * The Class JpaAccidentInsuranceRateSetMemento.
 */
public class JpaAccidentInsuranceHistorySetMemento implements AccidentInsuranceRateSetMemento {

	/** The type value. */
	private QismtWorkAccidentInsu typeValue;

	/**
	 * Instantiates a new jpa accident insurance rate set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaAccidentInsuranceHistorySetMemento(QismtWorkAccidentInsu typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		QismtWorkAccidentInsuPK pk = new QismtWorkAccidentInsuPK();
		pk.setHistId(historyId);
		this.typeValue.setQismtWorkAccidentInsuPK(pk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateSetMemento#setCompanyCode(nts.uk.ctx.core.dom.
	 * company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		QismtWorkAccidentInsuPK pk = this.typeValue.getQismtWorkAccidentInsuPK();
		pk.setCcd(companyCode);
		this.typeValue.setQismtWorkAccidentInsuPK(pk);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateSetMemento#setApplyRange(nts.uk.ctx.pr.core.dom.
	 * insurance.MonthRange)
	 */
	@Override
	public void setApplyRange(MonthRange applyRange) {
		this.typeValue.setStrYm(applyRange.getStartMonth().v());
		this.typeValue.setEndYm(applyRange.getEndMonth().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateSetMemento#setRateItems(java.util.Set)
	 */
	@Override
	public void setRateItems(Set<InsuBizRateItem> items) {
		// To Do
	}

}

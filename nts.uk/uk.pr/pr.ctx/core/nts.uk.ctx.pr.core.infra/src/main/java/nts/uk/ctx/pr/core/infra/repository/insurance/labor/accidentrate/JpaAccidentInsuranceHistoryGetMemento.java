/******************************************************************
 
* Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.accidentrate;

import java.util.Set;

import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.InsuBizRateItem;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsu;

/**
 * The Class JpaAccidentInsuranceRateSetMemento.
 */
public class JpaAccidentInsuranceHistoryGetMemento implements AccidentInsuranceRateGetMemento {

	/** The type value. */
	private QismtWorkAccidentInsu typeValue;

	/**
	 * Instantiates a new jpa history accident insurance rate get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaAccidentInsuranceHistoryGetMemento(QismtWorkAccidentInsu typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.typeValue.getQismtWorkAccidentInsuPK().getHistId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQismtWorkAccidentInsuPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateGetMemento#getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		return MonthRange.range(YearMonth.of(this.typeValue.getStrYm()),
				YearMonth.of(this.typeValue.getEndYm()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateGetMemento#getRateItems()
	 */
	@Override
	public Set<InsuBizRateItem> getRateItems() {
		return null;
	}
}

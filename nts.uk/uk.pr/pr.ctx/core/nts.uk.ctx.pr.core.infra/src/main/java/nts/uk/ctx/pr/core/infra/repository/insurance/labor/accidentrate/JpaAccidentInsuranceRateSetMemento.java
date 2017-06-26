/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.accidentrate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.InsuBizRateItem;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsu;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsuPK;

/**
 * The Class JpaAccidentInsuranceRateSetMemento.
 */
public class JpaAccidentInsuranceRateSetMemento implements AccidentInsuranceRateSetMemento {

	/** The type value. */
	private List<QismtWorkAccidentInsu> typeValue;

	/**
	 * Instantiates a new jpa accident insurance rate set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaAccidentInsuranceRateSetMemento(List<QismtWorkAccidentInsu> typeValue) {
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
		for (int index = BusinessTypeEnum.Biz1St.index; index <= BusinessTypeEnum.Biz10Th.index; index++) {
			QismtWorkAccidentInsu itemI = this.typeValue.get(index);
			QismtWorkAccidentInsuPK pk = new QismtWorkAccidentInsuPK();
			pk.setHistId(historyId);
			itemI.setQismtWorkAccidentInsuPK(pk);
			this.typeValue.set(index, itemI);
		}
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
		for (int index = BusinessTypeEnum.Biz1St.index; index <= BusinessTypeEnum.Biz10Th.index; index++) {
			QismtWorkAccidentInsu itemI = this.typeValue.get(index);
			QismtWorkAccidentInsuPK pk = itemI.getQismtWorkAccidentInsuPK();
			pk.setCcd(companyCode);
			itemI.setQismtWorkAccidentInsuPK(pk);
			this.typeValue.set(index, itemI);
		}
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
		for (int index = BusinessTypeEnum.Biz1St.index; index <= BusinessTypeEnum.Biz10Th.index; index++) {
			QismtWorkAccidentInsu itemI = this.typeValue.get(index);
			itemI.setStrYm(applyRange.getStartMonth().v());
			itemI.setEndYm(applyRange.getEndMonth().v());
			this.typeValue.set(index, itemI);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateSetMemento#setRateItems(java.util.Set)
	 */
	@Override
	public void setRateItems(Set<InsuBizRateItem> items) {

		// set item => data
		for (InsuBizRateItem item : items) {
			this.setDataItem(item.getInsuBizType().index, item);
		}
	}

	/**
	 * Sets the data item.
	 *
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 */
	private void setDataItem(int index, InsuBizRateItem item) {
		QismtWorkAccidentInsu itemIndex = this.typeValue.get(index);
		QismtWorkAccidentInsuPK pk = itemIndex.getQismtWorkAccidentInsuPK();

		pk.setWaInsuCd(item.getInsuBizType().value);
		itemIndex.setQismtWorkAccidentInsuPK(pk);
		itemIndex.setWaInsuRate(BigDecimal.valueOf(item.getInsuRate()));
		itemIndex.setWaInsuRound(item.getInsuRound().value);

		this.typeValue.set(index, itemIndex);
	}

}

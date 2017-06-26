/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.rule.employment.unitprice;

import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceName;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceSetMemento;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceHeader;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceHeaderPK;

/**
 * The Class JpaUnitPriceSetMemento.
 */
public class JpaUnitPriceSetMemento implements UnitPriceSetMemento {

	/** The type value. */
	private QupmtCUnitpriceHeader typeValue;

	/**
	 * Instantiates a new jpa unit price set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaUnitPriceSetMemento(QupmtCUnitpriceHeader typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceSetMemento#
	 * setCompanyCode(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		QupmtCUnitpriceHeaderPK qupmtCUnitpriceHeadPK = new QupmtCUnitpriceHeaderPK();
		qupmtCUnitpriceHeadPK.setCcd(companyCode);
		this.typeValue.setQupmtCUnitpriceHeaderPK(qupmtCUnitpriceHeadPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceSetMemento#
	 * setCode(nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode)
	 */
	@Override
	public void setCode(UnitPriceCode code) {
		QupmtCUnitpriceHeaderPK qupmtCUnitpriceHeadPK = this.typeValue.getQupmtCUnitpriceHeaderPK();
		qupmtCUnitpriceHeadPK.setCUnitpriceCd(code.v());
		this.typeValue.setQupmtCUnitpriceHeaderPK(qupmtCUnitpriceHeadPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceSetMemento#
	 * setName(nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceName)
	 */
	@Override
	public void setName(UnitPriceName name) {
		this.typeValue.setCUnitpriceName(name.v());
	}

}

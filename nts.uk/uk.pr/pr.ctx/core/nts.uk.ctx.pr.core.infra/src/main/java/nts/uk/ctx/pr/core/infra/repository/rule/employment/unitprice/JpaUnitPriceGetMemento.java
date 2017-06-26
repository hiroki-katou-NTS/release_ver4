/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.rule.employment.unitprice;

import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceGetMemento;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceName;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceHeader;

/**
 * The Class JpaUnitPriceGetMemento.
 */
public class JpaUnitPriceGetMemento implements UnitPriceGetMemento {

	/** The type value. */
	private QupmtCUnitpriceHeader typeValue;

	/**
	 * Instantiates a new jpa unit price get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaUnitPriceGetMemento(QupmtCUnitpriceHeader typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceGetMemento#
	 * getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQupmtCUnitpriceHeaderPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceGetMemento#
	 * getCode()
	 */
	@Override
	public UnitPriceCode getCode() {
		return new UnitPriceCode(this.typeValue.getQupmtCUnitpriceHeaderPK().getCUnitpriceCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceGetMemento#
	 * getName()
	 */
	@Override
	public UnitPriceName getName() {
		return new UnitPriceName(this.typeValue.getCUnitpriceName());
	}
}

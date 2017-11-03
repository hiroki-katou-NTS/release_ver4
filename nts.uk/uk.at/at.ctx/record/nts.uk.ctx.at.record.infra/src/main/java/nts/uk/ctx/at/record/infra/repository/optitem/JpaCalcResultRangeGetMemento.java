/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.optitem;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.optitem.AmountRange;
import nts.uk.ctx.at.record.dom.optitem.CalcRangeCheck;
import nts.uk.ctx.at.record.dom.optitem.CalcResultRangeGetMemento;
import nts.uk.ctx.at.record.dom.optitem.NumberRange;
import nts.uk.ctx.at.record.dom.optitem.TimeRange;
import nts.uk.ctx.at.record.infra.entity.optitem.KrcstCalcResultRange;

/**
 * The Class JpaCalcResultRangeGetMemento.
 */
public class JpaCalcResultRangeGetMemento implements CalcResultRangeGetMemento {

	/** The type value. */
	private KrcstCalcResultRange typeValue;

	/**
	 * Instantiates a new jpa calc result range get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaCalcResultRangeGetMemento(KrcstCalcResultRange typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.CalcResultRangeGetMemento#getUpperLimit(
	 * )
	 */
	@Override
	public CalcRangeCheck getUpperLimit() {
		return CalcRangeCheck.valueOf(this.typeValue.getUpperLimitAtr());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.CalcResultRangeGetMemento#getLowerLimit(
	 * )
	 */
	@Override
	public CalcRangeCheck getLowerLimit() {
		return CalcRangeCheck.valueOf(this.typeValue.getLowerLimitAtr());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.CalcResultRangeGetMemento#getNumberRange
	 * ()
	 */
	@Override
	public Optional<NumberRange> getNumberRange() {
		return Optional.of(new NumberRange(this.typeValue.getUpperNumberRange(),
				this.typeValue.getLowerNumberRange()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.CalcResultRangeGetMemento#getTimeRange()
	 */
	@Override
	public Optional<TimeRange> getTimeRange() {
		return Optional.of(new TimeRange(this.typeValue.getUpperTimeRange(),
				this.typeValue.getLowerTimeRange()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.CalcResultRangeGetMemento#getAmountRange
	 * ()
	 */
	@Override
	public Optional<AmountRange> getAmountRange() {
		return Optional.of(new AmountRange(this.typeValue.getUpperAmountRange(),
				this.typeValue.getLowerAmountRange()));
	}

}

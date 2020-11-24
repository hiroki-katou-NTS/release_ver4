/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.workrecord.monthcal;

import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.AggregateTimeSetting;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.FlexAggregateMethod;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.ShortageFlexSetting;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.FlexMonthWorkTimeAggrSetSetMemento;
import nts.uk.ctx.at.record.infra.entity.workrecord.monthcal.KrcstFlexMCalSet;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class JpaFlexMonthWorkTimeAggrSetSetMemento.
 *
 * @param <T>
 *            the generic type
 */
public class JpaFlexMonthWorkTimeAggrSetSetMemento<T extends KrcstFlexMCalSet>
		implements FlexMonthWorkTimeAggrSetSetMemento {

	/** The type value. */
	private T typeValue;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.
	 * FlexMonthWorkTimeAggrSetSetMemento#setAggrMethod(nts.uk.ctx.at.record.dom
	 * .monthlyaggrmethod.flex.FlexAggregateMethod)
	 */
	@Override
	public void setAggrMethod(FlexAggregateMethod aggrMethod) {
		this.typeValue.setAggrMethod(aggrMethod.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.
	 * FlexMonthWorkTimeAggrSetSetMemento#setInsufficSet(nts.uk.ctx.at.record.
	 * dom.monthlyaggrmethod.flex.ShortageFlexSetting)
	 */
	@Override
	public void setInsufficSet(ShortageFlexSetting insufficSet) {
		this.typeValue.setInsufficSet(insufficSet.getCarryforwardSet().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.
	 * FlexMonthWorkTimeAggrSetSetMemento#setLegalAggrSet(nts.uk.ctx.at.record.
	 * dom.monthlyaggrmethod.flex.AggregateTimeSetting)
	 */
	@Override
	public void setLegalAggrSet(AggregateTimeSetting legalAggrSet) {
		this.typeValue.setLegalAggrSet(legalAggrSet.getAggregateSet().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.
	 * FlexMonthWorkTimeAggrSetSetMemento#setIncludeOverTime(nts.uk.shr.com.
	 * enumcommon.NotUseAtr)
	 */
	@Override
	public void setIncludeOverTime(NotUseAtr includeOverTime) {
		this.typeValue.setIncludeOt(includeOverTime.value);
	}

}
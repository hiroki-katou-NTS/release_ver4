/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.pensionavgearn.limit;

import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimitSetMemento;

/**
 * The Class JpaPensionAvgEarnLimitSetMemento.
 */
public class JpaPensionAvgEarnLimitSetMemento implements HealthAvgEarnLimitSetMemento {

	/** The type value. */
	// TODO: Object -> entity class.
	private Object typeValue;

	/**
	 * Instantiates a new jpa pension avg earn limit set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaPensionAvgEarnLimitSetMemento(Object typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.
	 * HealthAvgEarnLimitSetMemento#setGrade(java.lang.Integer)
	 */
	@Override
	public void setGrade(Integer grade) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.
	 * HealthAvgEarnLimitSetMemento#setAvgEarn(java.lang.Long)
	 */
	@Override
	public void setAvgEarn(Long avgEarn) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.
	 * HealthAvgEarnLimitSetMemento#setSalLimit(java.lang.Long)
	 */
	@Override
	public void setSalLimit(Long salLimit) {
		// TODO Auto-generated method stub

	}

}

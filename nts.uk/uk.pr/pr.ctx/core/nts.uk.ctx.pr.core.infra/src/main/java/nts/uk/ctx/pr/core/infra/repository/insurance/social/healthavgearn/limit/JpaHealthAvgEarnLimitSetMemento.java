/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthavgearn.limit;

import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimitSetMemento;

/**
 * The Class JpaHealthAvgEarnLimitSetMemento.
 */
public class JpaHealthAvgEarnLimitSetMemento implements HealthAvgEarnLimitSetMemento {

	/** The type value. */
	// TODO: Object -> entity class.
	private Object typeValue;

	/**
	 * Instantiates a new jpa avg earn level master setting set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaHealthAvgEarnLimitSetMemento(Object typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * HealthAvgEarnLimitSetMemento#setGrade(java.lang.Integer)
	 */
	@Override
	public void setGrade(Integer grade) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * HealthAvgEarnLimitSetMemento#setAvgEarn(java.lang.Long)
	 */
	@Override
	public void setAvgEarn(Long avgEarn) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * HealthAvgEarnLimitSetMemento#setSalLimit(java.lang.Long)
	 */
	@Override
	public void setSalLimit(Long salLimit) {
		// TODO Auto-generated method stub

	}

}

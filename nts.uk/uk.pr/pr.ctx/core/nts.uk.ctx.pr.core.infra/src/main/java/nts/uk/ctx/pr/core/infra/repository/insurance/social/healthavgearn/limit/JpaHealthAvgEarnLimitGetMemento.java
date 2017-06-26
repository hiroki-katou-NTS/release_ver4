/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthavgearn.limit;

import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimitGetMemento;

/**
 * The Class JpaHealthAvgEarnLimitGetMemento.
 */
public class JpaHealthAvgEarnLimitGetMemento implements HealthAvgEarnLimitGetMemento {

	/** The type value. */
	// TODO: Object -> entity class.
	private Object typeValue;

	/**
	 * Instantiates a new jpa avg earn level master setting get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaHealthAvgEarnLimitGetMemento(Object typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * HealthAvgEarnLimitGetMemento#getGrade()
	 */
	@Override
	public Integer getGrade() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * HealthAvgEarnLimitGetMemento#getAvgEarn()
	 */
	@Override
	public Long getAvgEarn() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * HealthAvgEarnLimitGetMemento#getSalLimit()
	 */
	@Override
	public Long getSalLimit() {
		// TODO Auto-generated method stub
		return null;
	}

}

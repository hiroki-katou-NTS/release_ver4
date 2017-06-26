/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.pensionavgearn.limit;

import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.PensionAvgEarnLimitGetMemento;

/**
 * The Class JpaPensionAvgEarnLimitGetMemento.
 */
public class JpaPensionAvgEarnLimitGetMemento implements PensionAvgEarnLimitGetMemento {

	/** The type value. */
	// TODO: Object -> entity class.
	private Object typeValue;

	/**
	 * Instantiates a new jpa pension avg earn limit get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaPensionAvgEarnLimitGetMemento(Object typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.
	 * PensionAvgEarnLimitGetMemento#getGrade()
	 */
	@Override
	public Integer getGrade() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.
	 * PensionAvgEarnLimitGetMemento#getAvgEarn()
	 */
	@Override
	public Long getAvgEarn() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.
	 * PensionAvgEarnLimitGetMemento#getSalLimit()
	 */
	@Override
	public Long getSalLimit() {
		// TODO Auto-generated method stub
		return null;
	}

}

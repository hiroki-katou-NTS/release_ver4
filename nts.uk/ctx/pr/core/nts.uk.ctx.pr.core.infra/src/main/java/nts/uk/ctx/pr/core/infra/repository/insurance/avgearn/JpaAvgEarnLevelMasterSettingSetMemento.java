/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.avgearn;

import nts.uk.ctx.pr.core.dom.insurance.avgearn.AvgEarnLevelMasterSettingSetMemento;

/**
 * The Class JpaAvgEarnLevelMasterSettingSetMemento.
 */
public class JpaAvgEarnLevelMasterSettingSetMemento implements AvgEarnLevelMasterSettingSetMemento {

	/** The type value. */
	// TODO: Object -> entity class.
	private Object typeValue;

	/**
	 * Instantiates a new jpa avg earn level master setting set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaAvgEarnLevelMasterSettingSetMemento(Object typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * AvgEarnLevelMasterSettingSetMemento#setGrade(java.lang.Integer)
	 */
	@Override
	public void setGrade(Integer grade) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * AvgEarnLevelMasterSettingSetMemento#setAvgEarn(java.lang.Long)
	 */
	@Override
	public void setAvgEarn(Long avgEarn) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * AvgEarnLevelMasterSettingSetMemento#setSalLimit(java.lang.Long)
	 */
	@Override
	public void setSalLimit(Long salLimit) {
		// TODO Auto-generated method stub

	}

}

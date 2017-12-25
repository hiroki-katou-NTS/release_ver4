/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.CompensatoryOccurrenceDivision;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSetGetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtSubstitutionSet;

/**
 * The Class JpaWorkTimezoneOtherSubHolTimeSetGetMemento.
 */
public class JpaWorkTimezoneOtherSubHolTimeSetGetMemento
		implements WorkTimezoneOtherSubHolTimeSetGetMemento {

	/** The entity. */
	private KshmtSubstitutionSet entity;

	/**
	 * Instantiates a new jpa work timezone other sub hol time set get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaWorkTimezoneOtherSubHolTimeSetGetMemento(KshmtSubstitutionSet entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneOtherSubHolTimeSetGetMemento#getSubHolTimeSet()
	 */
	@Override
	public SubHolTransferSet getSubHolTimeSet() {
		return new SubHolTransferSet(new JpaSubHolTransferSetGetMemento(this.entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneOtherSubHolTimeSetGetMemento#getWorkTimeCode()
	 */
	@Override
	public WorkTimeCode getWorkTimeCode() {
		return new WorkTimeCode(this.entity.getKshmtSubstitutionSetPK().getWorktimeCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneOtherSubHolTimeSetGetMemento#getOriginAtr()
	 */
	@Override
	public CompensatoryOccurrenceDivision getOriginAtr() {
		return CompensatoryOccurrenceDivision
				.valueOf(this.entity.getKshmtSubstitutionSetPK().getOriginAtr());
	}

}

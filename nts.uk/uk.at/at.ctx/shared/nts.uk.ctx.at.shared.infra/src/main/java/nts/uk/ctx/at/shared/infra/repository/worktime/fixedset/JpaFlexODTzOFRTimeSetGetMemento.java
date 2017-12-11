/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.fixedset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSetGetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexOdFixRest;

/**
 * The Class JpaFlexOffdayTzOFRTimeSetGetMemento.
 */
public class JpaFlexODTzOFRTimeSetGetMemento implements TimezoneOfFixedRestTimeSetGetMemento{
	
	/** The entitys. */
	private List<KshmtFlexOdFixRest> entitys;


	/**
	 * Instantiates a new jpa flex OD tz OFR time set get memento.
	 *
	 * @param entitys the entitys
	 */
	public JpaFlexODTzOFRTimeSetGetMemento(List<KshmtFlexOdFixRest> entitys) {
		super();
		this.entitys = entitys;
	}


	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSetGetMemento#getTimezones()
	 */
	@Override
	public List<DeductionTime> getTimezones() {
		if (CollectionUtil.isEmpty(this.entitys)) {
			return new ArrayList<>();
		}
		return this.entitys.stream().map(entity -> new DeductionTime(new JpaFlexODDeductionTimeGetMemento(entity)))
				.collect(Collectors.toList());
	}

}

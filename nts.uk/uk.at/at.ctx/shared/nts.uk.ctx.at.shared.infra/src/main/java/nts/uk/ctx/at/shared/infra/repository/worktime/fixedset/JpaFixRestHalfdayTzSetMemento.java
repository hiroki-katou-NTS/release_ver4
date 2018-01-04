/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.fixedset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSetSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedHalfRestSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedHalfRestSetPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedWorkSet;

/**
 * The Class JpaFixRestHalfdayTzSetMemento.
 */
public class JpaFixRestHalfdayTzSetMemento implements FixRestTimezoneSetSetMemento {

	/** The entity sets. */
	private KshmtFixedWorkSet entity;
	
	/** The cid. */
	private String cid;
	
	/** The worktime cd. */
	private String worktimeCd;
	
	/** The type. */
	private AmPmAtr type;
	
	/** The period no. */
	private int periodNo;

	/**
	 * Instantiates a new jpa fix rest halfday tz set memento.
	 *
	 * @param entitySets
	 *            the entity sets
	 */
	public JpaFixRestHalfdayTzSetMemento(KshmtFixedWorkSet entity, String cid, String worktimeCd, AmPmAtr type) {
		super();
		this.entity = entity;
		this.cid = cid;
		this.worktimeCd = worktimeCd;
		this.type = type;
		if (CollectionUtil.isEmpty(this.entity.getKshmtFixedHalfRestSets())) {
			this.entity.setKshmtFixedHalfRestSets(new ArrayList<>());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSetSetMemento#
	 * setLstTimezone(java.util.List)
	 */
	@Override
	public void setLstTimezone(List<DeductionTime> lstTimezone) {
		
		// Get settings with other type
		List<KshmtFixedHalfRestSet> otherList = this.entity.getKshmtFixedHalfRestSets().stream()
				.filter(entity -> entity.getKshmtFixedHalfRestSetPK().getAmPmAtr() != this.type.value)
				.collect(Collectors.toList());
				
		// KSHMT_FIXED_HALF_REST_SET
		if (CollectionUtil.isEmpty(lstTimezone)) {
			this.entity.setKshmtFixedHalfRestSets(otherList);
			return;
		}
		
		// get list entity
		List<KshmtFixedHalfRestSet> lstEntity = this.entity.getKshmtFixedHalfRestSets().stream()
				.filter(entity -> entity.getKshmtFixedHalfRestSetPK().getAmPmAtr() == this.type.value)
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(lstEntity)) {
			lstEntity = new ArrayList<>();
		}
		
		List<KshmtFixedHalfRestSet> newListEntity = new ArrayList<>();
		
		periodNo = 0;
		for (DeductionTime domain : lstTimezone) {			
			periodNo++;
			KshmtFixedHalfRestSetPK pk = new KshmtFixedHalfRestSetPK(this.cid, this.worktimeCd, this.type.value, periodNo);				
			KshmtFixedHalfRestSet entity = new KshmtFixedHalfRestSet();
			entity.setKshmtFixedHalfRestSetPK(pk);
			domain.saveToMemento(new JpaFixedRestTZDeductionTimeSetMemento<KshmtFixedHalfRestSet>(entity));
			
			// add list
			newListEntity.add(entity);
		}
		
		newListEntity.addAll(otherList);
		
		this.entity.setKshmtFixedHalfRestSets(newListEntity);
	}
}

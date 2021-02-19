/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.fixedset;

import java.util.ArrayList;
import java.util.List;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixOffdayWorkTimezoneSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtWtFixHolTs;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedHolTimeSetPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtWtFix;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedWorkSetPK;

/**
 * The Class JpaFixOffdayWorkTimezoneSetMemento.
 */
public class JpaFixOffdayWorkTimezoneSetMemento implements FixOffdayWorkTimezoneSetMemento {
	
	/** The entity. */
	private KshmtWtFix entity;
	
	/** The company id. */
	private String companyId;
	
	/** The work time cd. */
	private String workTimeCd;
	
	/** The Constant EQUAL. */
	private static final Integer EQUAL = 0;
	
	/**
	 * Instantiates a new jpa fix offday work timezone set memento.
	 *
	 * @param entity the entity
	 */
	public JpaFixOffdayWorkTimezoneSetMemento(KshmtWtFix entity) {
		super();
		this.entity = entity;
		if (this.entity.getKshmtFixedWorkSetPK() == null) {
			this.entity.setKshmtFixedWorkSetPK(new KshmtFixedWorkSetPK());
		}		
		
		// initial data
		this.initialData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * FixOffdayWorkTimezoneSetMemento#setRestTimezone(nts.uk.ctx.at.shared.dom.
	 * worktime.fixedset.FixRestTimezoneSet)
	 */
	@Override
	public void setRestTimezone(FixRestTimezoneSet restTimezone) {
		restTimezone.saveToMemento(new JpaFixedOffDayRestTimeSetMemento(this.entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * FixOffdayWorkTimezoneSetMemento#setLstWorkTimezone(java.util.List)
	 */
	@Override
	public void setLstWorkTimezone(List<HDWorkTimeSheetSetting> lstWorkTimezone) {
		if (CollectionUtil.isEmpty(lstWorkTimezone) || CollectionUtil.isEmpty(this.entity.getLstKshmtFixedHolTimeSet())) {
			this.entity.setLstKshmtFixedHolTimeSet(new ArrayList<>());
		}
		List<KshmtWtFixHolTs> lstEntity = this.entity.getLstKshmtFixedHolTimeSet();
		
		List<KshmtWtFixHolTs> newListEntity = new ArrayList<>();
		
		for (HDWorkTimeSheetSetting holDayTime : lstWorkTimezone) {
			
			// get entity existed
			KshmtWtFixHolTs entity = lstEntity.stream().filter(item -> {
				KshmtFixedHolTimeSetPK pk = item.getKshmtFixedHolTimeSetPK();
						return pk.getCid().compareTo(companyId) == EQUAL
								&& pk.getWorktimeCd().compareTo(workTimeCd) == EQUAL
								&& pk.getWorktimeNo() == holDayTime.getWorkTimeNo();
					})
					.findFirst()
					.orElse(new KshmtWtFixHolTs());
			
			// save to memento
			holDayTime.saveToMemento(new JpaFixedHDWorkTimeSheetSetMemento(this.companyId, this.workTimeCd, entity));
			
			// add list
			newListEntity.add(entity);
		}
		this.entity.setLstKshmtFixedHolTimeSet(newListEntity);
		
	}
	
	/**
	 * Initial data.
	 */
	private void initialData() {
		this.companyId = this.entity.getKshmtFixedWorkSetPK().getCid();
		this.workTimeCd = this.entity.getKshmtFixedWorkSetPK().getWorktimeCd();
	}
}

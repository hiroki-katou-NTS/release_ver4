/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.flexset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexCalcSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexHalfDayWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexOffdayWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexHaRtSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexHaRtSetPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexOdRtSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexOdRtSetPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexRestSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexRestSetPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexStampReflect;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexStampReflectPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexWorkSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexWorkSetPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.common.KshmtWorktimeCommonSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.common.KshmtWorktimeCommonSetPK;
import nts.uk.ctx.at.shared.infra.repository.worktime.common.JpaFlexFlowWorkRestSettingSetMemento;
import nts.uk.ctx.at.shared.infra.repository.worktime.common.JpaFlexStampReflectTZSetMemento;
import nts.uk.ctx.at.shared.infra.repository.worktime.common.JpaWorkTimezoneCommonSetSetMemento;

/**
 * The Class JpaFlexWorkSettingSetMemento.
 */
public class JpaFlexWorkSettingSetMemento implements FlexWorkSettingSetMemento {
	
	/** The entity. */
	private KshmtFlexWorkSet entity;

	/** The entity common. */
	private KshmtWorktimeCommonSet entityCommon;
	

	/**
	 * Instantiates a new jpa flex work setting set memento.
	 *
	 * @param entity the entity
	 * @param entityCommon the entity common
	 */
	public JpaFlexWorkSettingSetMemento(KshmtFlexWorkSet entity, KshmtWorktimeCommonSet entityCommon) {
		super();
		if(entity.getKshmtFlexWorkSetPK() == null){
			entity.setKshmtFlexWorkSetPK(new KshmtFlexWorkSetPK());
		}
		if(entityCommon.getKshmtWorktimeCommonSetPK() == null){
			entityCommon.setKshmtWorktimeCommonSetPK(new KshmtWorktimeCommonSetPK());
		}
		this.entity = entity;
		this.entityCommon = entityCommon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setcompanyId(java.lang.String)
	 */
	@Override
	public void setcompanyId(String companyId) {
		this.entity.getKshmtFlexWorkSetPK().setCid(companyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setWorkTimeCode(nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode)
	 */
	@Override
	public void setWorkTimeCode(WorkTimeCode workTimeCode) {
		this.entity.getKshmtFlexWorkSetPK().setWorktimeCd(workTimeCode.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setCoreTimeSetting(nts.uk.ctx.at.shared.dom.worktime.flexset.
	 * CoreTimeSetting)
	 */
	@Override
	public void setCoreTimeSetting(CoreTimeSetting coreTimeSetting) {
		if (coreTimeSetting != null) {
			coreTimeSetting.saveToMemento(new JpaCoreTimeSettingSetMemento(this.entity));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setRestSetting(nts.uk.ctx.at.shared.dom.worktime.common.
	 * FlowWorkRestSetting)
	 */
	@Override
	public void setRestSetting(FlowWorkRestSetting restSetting) {
		if (restSetting != null) {
			this.entity.setKshmtFlexRestSet(
					new KshmtFlexRestSet(new KshmtFlexRestSetPK(this.entity.getKshmtFlexWorkSetPK().getCid(),
							this.entity.getKshmtFlexWorkSetPK().getWorktimeCd())));
			restSetting.saveToMemento(new JpaFlexFlowWorkRestSettingSetMemento(this.entity.getKshmtFlexRestSet()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setOffdayWorkTime(nts.uk.ctx.at.shared.dom.worktime.flexset.
	 * FlexOffdayWorkTime)
	 */
	@Override
	public void setOffdayWorkTime(FlexOffdayWorkTime offdayWorkTime) {
		if (offdayWorkTime != null) {
			this.entity.setKshmtFlexOdRtSet(
					new KshmtFlexOdRtSet(new KshmtFlexOdRtSetPK(this.entity.getKshmtFlexWorkSetPK().getCid(),
							this.entity.getKshmtFlexWorkSetPK().getWorktimeCd())));
			offdayWorkTime.saveToMemento(new JpaFlexODWorkTimeSetMemento(this.entity.getKshmtFlexOdRtSet()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setCommonSetting(nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneCommonSet)
	 */
	@Override
	public void setCommonSetting(WorkTimezoneCommonSet commonSetting) {
		if(commonSetting!=null){
			commonSetting.saveToMemento(new JpaWorkTimezoneCommonSetSetMemento(this.entityCommon));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setUseHalfDayShift(boolean)
	 */
	@Override
	public void setUseHalfDayShift(boolean useHalfDayShift) {
		this.entity.setUseHalfdayShift(BooleanGetAtr.getAtrByBoolean(useHalfDayShift));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setLstHalfDayWorkTimezone(java.util.List)
	 */
	@Override
	public void setLstHalfDayWorkTimezone(List<FlexHalfDayWorkTime> halfDayWorkTimezone) {
		if (CollectionUtil.isEmpty(halfDayWorkTimezone)) {
			this.entity.setKshmtFlexHaRtSets(new ArrayList<>());
		}else {
			this.entity.setKshmtFlexHaRtSets(halfDayWorkTimezone.stream().map(domain -> {
				KshmtFlexHaRtSet entity = new KshmtFlexHaRtSet(
						new KshmtFlexHaRtSetPK(this.entity.getKshmtFlexWorkSetPK().getCid(),
								this.entity.getKshmtFlexWorkSetPK().getWorktimeCd()));
				domain.saveToMemento(new JpaFlexHAWorkTimeSetMemento(entity));
				return entity;
			}).collect(Collectors.toList()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setLstStampReflectTimezone(java.util.List)
	 */
	@Override
	public void setLstStampReflectTimezone(List<StampReflectTimezone> stampReflectTimezone) {
		if (CollectionUtil.isEmpty(stampReflectTimezone)) {
			this.entity.setKshmtFlexStampReflects(new ArrayList<>());
		} else {
			this.entity.setKshmtFlexStampReflects(stampReflectTimezone.stream().map(domain -> {
				KshmtFlexStampReflect entity = new KshmtFlexStampReflect(
						new KshmtFlexStampReflectPK(this.entity.getKshmtFlexWorkSetPK().getCid(),
								this.entity.getKshmtFlexWorkSetPK().getWorktimeCd()));
				domain.saveToMemento(new JpaFlexStampReflectTZSetMemento(entity));
				return entity;
			}).collect(Collectors.toList()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingSetMemento#
	 * setCalculateSetting(nts.uk.ctx.at.shared.dom.worktime.flexset.
	 * FlexCalcSetting)
	 */
	@Override
	public void setCalculateSetting(FlexCalcSetting calculateSetting) {
		if (calculateSetting != null) {
			calculateSetting.saveToMemento(new JpaFlexCalcSettingSetMemento(this.entity));
		}
	}

}

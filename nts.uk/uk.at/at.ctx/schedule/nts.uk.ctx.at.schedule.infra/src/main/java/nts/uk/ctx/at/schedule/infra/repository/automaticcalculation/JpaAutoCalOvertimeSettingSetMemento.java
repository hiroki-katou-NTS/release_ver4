/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.automaticcalculation;

import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingSetMemento;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.schedule.infra.entity.shift.autocalsetting.KshmtAutoCalSet;

/**
 * The Class JpaAutoCalOvertimeSettingSetMemento.
 */
public class JpaAutoCalOvertimeSettingSetMemento implements AutoCalOvertimeSettingSetMemento {

	/** The entity. */
	private KshmtAutoCalSet entity;
	
	/**
	 * Instantiates a new jpa auto cal overtime setting set memento.
	 *
	 * @param entity the entity
	 */
	public JpaAutoCalOvertimeSettingSetMemento(KshmtAutoCalSet entity) {
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingSetMemento#setEarlyOtTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setEarlyOtTime(AutoCalSetting earlyOtTime) {
		this.entity.setEarlyOtTimeAtr(earlyOtTime.getCalAtr().value);
		this.entity.setEarlyOtTimeLimit(earlyOtTime.getUpLimitOtSet().value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingSetMemento#setEarlyMidOtTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setEarlyMidOtTime(AutoCalSetting earlyMidOtTime) {
		this.entity.setEarlyMidOtTimeAtr(earlyMidOtTime.getCalAtr().value);
		this.entity.setEarlyMidOtTimeLimit(earlyMidOtTime.getUpLimitOtSet().value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingSetMemento#setNormalOtTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setNormalOtTime(AutoCalSetting normalOtTime) {
		this.entity.setNormalOtTimeAtr(normalOtTime.getCalAtr().value);
		this.entity.setNormalOtTimeLimit(normalOtTime.getUpLimitOtSet().value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingSetMemento#setNormalMidOtTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setNormalMidOtTime(AutoCalSetting normalMidOtTime) {
		this.entity.setNormalMidOtTimeAtr(normalMidOtTime.getCalAtr().value);
		this.entity.setNormalMidOtTimeLimit(normalMidOtTime.getUpLimitOtSet().value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingSetMemento#setLegalOtTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setLegalOtTime(AutoCalSetting legalOtTime) {
		this.entity.setLegalOtTimeAtr(legalOtTime.getCalAtr().value);
		this.entity.setLegalOtTimeLimit(legalOtTime.getUpLimitOtSet().value);
		
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingSetMemento#setLegalMidOtTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setLegalMidOtTime(AutoCalSetting legalMidOtTime) {
		this.entity.setLegalMidOtTimeAtr(legalMidOtTime.getCalAtr().value);
		this.entity.setLegalMidOtTimeLimit(legalMidOtTime.getUpLimitOtSet().value);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Override
//	public void setEarlyOtTime(AutoCalSetting earlyOtTime) {
//		this.entity.setEarlyOtTimeAtr(earlyOtTime.getCalAtr().value);
//		this.entity.setEarlyOtTimeLimit(earlyOtTime.getUpLimitOtSet().value);
//	}
//
//	@Override
//	public void setEarlyMidOtTime(AutoCalSetting earlyMidOtTime) {
//		this.entity.setEarlyMidOtTimeAtr(earlyMidOtTime.getCalAtr().value);
//		this.entity.setEarlyMidOtTimeLimit(earlyMidOtTime.getUpLimitOtSet().value);
//	}
//
//	@Override
//	public void setNormalOtTime(AutoCalSetting normalOtTime) {
//		this.entity.setNormalOtTimeAtr(normalOtTime.getCalAtr().value);
//		this.entity.setNormalOtTimeLimit(normalOtTime.getUpLimitOtSet().value);
//	}
//
//	@Override
//	public void setNormalMidOtTime(AutoCalSetting normalMidOtTime) {
//		this.entity.setNormalMidOtTimeAtr(normalMidOtTime.getCalAtr().value);
//		this.entity.setNormalMidOtTimeLimit(normalMidOtTime.getUpLimitOtSet().value);
//	}
//
//	@Override
//	public void setLegalOtTime(AutoCalSetting legalOtTime) {
//		this.entity.setLegalOtTimeAtr(legalOtTime.getCalAtr().value);
//		this.entity.setLegalOtTimeLimit(legalOtTime.getUpLimitOtSet().value);
//	}
//
//	@Override
//	public void setLegalMidOtTime(AutoCalSetting legalMidOtTime) {
//		this.entity.setLegalMidOtTimeAtr(legalMidOtTime.getCalAtr().value);
//		this.entity.setLegalMidOtTimeLimit(legalMidOtTime.getUpLimitOtSet().value);
//	}

}

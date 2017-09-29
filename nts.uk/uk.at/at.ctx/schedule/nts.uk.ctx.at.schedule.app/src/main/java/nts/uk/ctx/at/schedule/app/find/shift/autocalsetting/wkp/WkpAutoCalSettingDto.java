/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.shift.autocalsetting.wkp;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalFlexOvertimeSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.WkpAutoCalSettingSetMemento;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;

/**
 * The Class WkpAutoCalSettingDto.
 */

/**
 * Gets the rest time.
 *
 * @return the rest time
 */

/**
 * Gets the rest time.
 *
 * @return the rest time
 */
@Getter
@Setter
public class WkpAutoCalSettingDto implements WkpAutoCalSettingSetMemento{
	
	/** The wkp id. */
	private WorkplaceId wkpId;

	/** The normal OT time. */
	// 残業時間
	private AutoCalOvertimeSettingDto normalOTTime;

	/** The flex OT time. */
	// フレックス超過時間
	private AutoCalFlexOvertimeSettingDto flexOTTime;

	/** The rest time. */
	// 休出時間
	private AutoCalRestTimeSettingDto restTime;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.ComAutoCalSettingSetMemento#setCompanyId(nts.uk.ctx.at.shared.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		// do nothing
		
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.WkpAutoCalSettingSetMemento#setWkpId(nts.uk.ctx.at.shared.dom.common.WorkplaceId)
	 */
	@Override
	public void setWkpId(WorkplaceId workplaceId) {
		this.wkpId = workplaceId;
		
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.ComAutoCalSettingSetMemento#setNormalOTTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSetting)
	 */
	@Override
	public void setNormalOTTime(AutoCalOvertimeSetting normalOTTime) {
		AutoCalOvertimeSettingDto dto = new AutoCalOvertimeSettingDto();
		normalOTTime.saveToMemento(dto);
		this.normalOTTime = dto;
		
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.ComAutoCalSettingSetMemento#setFlexOTTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalFlexOvertimeSetting)
	 */
	@Override
	public void setFlexOTTime(AutoCalFlexOvertimeSetting flexOTTime) {
		AutoCalFlexOvertimeSettingDto dto = new AutoCalFlexOvertimeSettingDto();
		flexOTTime.saveToMemento(dto);
		this.flexOTTime = dto;
		
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.ComAutoCalSettingSetMemento#setRestTime(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalRestTimeSetting)
	 */
	@Override
	public void setRestTime(AutoCalRestTimeSetting restTime) {
		AutoCalRestTimeSettingDto dto = new AutoCalRestTimeSettingDto();
		restTime.saveToMemento(dto);
		this.restTime = dto;
	}

}

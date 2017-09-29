/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.shift.autocalsetting.job;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingSetMemento;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting;

/**
 * Gets the legal mid ot time.
 *
 * @return the legal mid ot time
 */
@Getter
@Setter
public class AutoCalOvertimeSettingDto implements AutoCalOvertimeSettingSetMemento {

	/** The early ot time. */
	// 早出残業時間
	private AutoCalSettingDto earlyOtTime;

	/** The early mid ot time. */
	// 早出深夜残業時間
	private AutoCalSettingDto earlyMidOtTime;

	/** The normal ot time. */
	// 普通残業時間
	private AutoCalSettingDto normalOtTime;

	/** The normal mid ot time. */
	// 普通深夜残業時間
	private AutoCalSettingDto normalMidOtTime;

	/** The legal ot time. */
	// 法定内残業時間
	private AutoCalSettingDto legalOtTime;

	/** The legal mid ot time. */
	// 法定内深夜残業時間
	private AutoCalSettingDto legalMidOtTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingSetMemento#setEarlyOtTime(nts.uk.ctx.at.schedule.
	 * dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setEarlyOtTime(AutoCalSetting earlyOtTime) {
		this.earlyOtTime = new AutoCalSettingDto(earlyOtTime.getUpLimitOtSet().value, earlyOtTime.getCalAtr().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingSetMemento#setEarlyMidOtTime(nts.uk.ctx.at.schedule
	 * .dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setEarlyMidOtTime(AutoCalSetting earlyMidOtTime) {
		this.earlyMidOtTime = new AutoCalSettingDto(earlyMidOtTime.getUpLimitOtSet().value,
				earlyMidOtTime.getCalAtr().value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingSetMemento#setNormalOtTime(nts.uk.ctx.at.schedule.
	 * dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setNormalOtTime(AutoCalSetting normalOtTime) {
		this.normalOtTime = new AutoCalSettingDto(normalOtTime.getUpLimitOtSet().value, normalOtTime.getCalAtr().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingSetMemento#setNormalMidOtTime(nts.uk.ctx.at.
	 * schedule.dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setNormalMidOtTime(AutoCalSetting normalMidOtTime) {
		this.normalMidOtTime = new AutoCalSettingDto(normalMidOtTime.getUpLimitOtSet().value,
				normalMidOtTime.getCalAtr().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingSetMemento#setLegalOtTime(nts.uk.ctx.at.schedule.
	 * dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setLegalOtTime(AutoCalSetting legalOtTime) {
		this.legalOtTime = new AutoCalSettingDto(legalOtTime.getUpLimitOtSet().value, legalOtTime.getCalAtr().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingSetMemento#setLegalMidOtTime(nts.uk.ctx.at.schedule
	 * .dom.shift.autocalsetting.AutoCalSetting)
	 */
	@Override
	public void setLegalMidOtTime(AutoCalSetting legalMidOtTime) {
		this.legalMidOtTime = new AutoCalSettingDto(legalMidOtTime.getUpLimitOtSet().value,
				legalMidOtTime.getCalAtr().value);

	}

}

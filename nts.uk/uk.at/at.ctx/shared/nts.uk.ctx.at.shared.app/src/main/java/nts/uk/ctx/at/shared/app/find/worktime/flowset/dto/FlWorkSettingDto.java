/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.flowset.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.app.find.worktime.common.dto.FlowWorkRestSettingDto;
import nts.uk.ctx.at.shared.app.find.worktime.common.dto.WorkTimezoneCommonSetDto;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlHalfDayWtz;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlOffdayWtz;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlStampReflectTz;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlWorkDedSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlWorkSettingSetMemento;

/**
 * The Class FlowWorkSettingDto.
 */
@Getter
@Setter
public class FlWorkSettingDto implements FlWorkSettingSetMemento {

	/** The working code. */
	private String workingCode;

	/** The rest setting. */
	private FlowWorkRestSettingDto restSetting;

	/** The offday work timezone. */
	private FlOffdayWorkTzDto offdayWorkTimezone;

	/** The common setting. */
	private WorkTimezoneCommonSetDto commonSetting;

	/** The half day work timezone. */
	private FlHalfDayWorkTzDto halfDayWorkTimezone;

	/** The stamp reflect timezone. */
	private FlStampReflectTzDto stampReflectTimezone;

	/** The designated setting. */
	private Integer designatedSetting;

	/** The flow setting. */
	private FlWorkDedSettingDto flowSetting;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String cid) {
		// unnecessary.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setWorkingCode(nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode)
	 */
	@Override
	public void setWorkingCode(WorkTimeCode wtCode) {
		this.workingCode = wtCode.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setRestSetting(nts.uk.ctx.at.shared.dom.worktime.common.
	 * FlowWorkRestSetting)
	 */
	@Override
	public void setRestSetting(FlowWorkRestSetting restSet) {
		restSet.saveToMemento(this.restSetting);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setOffdayWorkTimezone(nts.uk.ctx.at.shared.dom.worktime.flowset.
	 * FlowOffdayWorkTimezone)
	 */
	@Override
	public void setOffdayWorkTimezone(FlOffdayWtz offDayWtz) {
		offDayWtz.saveToMemento(this.offdayWorkTimezone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setCommonSetting(nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneCommonSet)
	 */
	@Override
	public void setCommonSetting(WorkTimezoneCommonSet cmnSet) {
		cmnSet.saveToMemento(this.commonSetting);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setHalfDayWorkTimezone(nts.uk.ctx.at.shared.dom.worktime.flowset.
	 * FlowHalfDayWorkTimezone)
	 */
	@Override
	public void setHalfDayWorkTimezone(FlHalfDayWtz halfDayWtz) {
		halfDayWtz.saveToMemento(this.halfDayWorkTimezone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setStampReflectTimezone(nts.uk.ctx.at.shared.dom.worktime.flowset.
	 * FlowStampReflectTimezone)
	 */
	@Override
	public void setStampReflectTimezone(FlStampReflectTz stampRefTz) {
		stampRefTz.saveToMemento(this.stampReflectTimezone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setDesignatedSetting(nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * LegalOTSetting)
	 */
	@Override
	public void setDesignatedSetting(LegalOTSetting legalOtSet) {
		this.designatedSetting = legalOtSet.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingSetMemento#
	 * setFlowSetting(nts.uk.ctx.at.shared.dom.worktime.flowset.
	 * FlowWorkDedicateSetting)
	 */
	@Override
	public void setFlowSetting(FlWorkDedSetting flowSet) {
		flowSet.saveToMemento(this.flowSetting);
	}
}

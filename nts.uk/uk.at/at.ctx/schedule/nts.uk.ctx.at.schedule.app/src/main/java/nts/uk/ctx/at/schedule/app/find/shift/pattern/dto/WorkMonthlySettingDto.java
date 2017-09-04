/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.shift.pattern.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.schedule.dom.shift.pattern.WorkTypeCode;
import nts.uk.ctx.at.schedule.dom.shift.pattern.WorkingCode;
import nts.uk.ctx.at.schedule.dom.shift.pattern.monthly.MonthlyPatternCode;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySettingSetMemento;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class WorkMonthlySettingDto.
 */

@Getter
@Setter
public class WorkMonthlySettingDto implements WorkMonthlySettingSetMemento{

	/** The work monthly setting code. */
	private String workTypeCode;

	/** The working code. */
	private String workingCode;
	
	/** The work type name. */
	private String workTypeName;
	
	/** The type color. */
	// ATTENDANCE = 1 , HOLIDAY = 0
	private int typeColor;
	
	/** The working name. */
	private String workingName;
	
	/** The date. */
	private int ymdk;
	
	/** The monthly pattern code. */
	private String monthlyPatternCode;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.pattern.WorkMonthlySettingSetMemento#
	 * setCompanyId(nts.uk.ctx.at.shared.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		// No thing code
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.pattern.WorkMonthlySettingSetMemento#
	 * setWorkMonthlySettingCode(nts.uk.ctx.at.schedule.dom.shift.pattern.
	 * WorkMonthlySettingCode)
	 */
	@Override
	public void setWorkTypeCode(WorkTypeCode workTypeCode) {
		this.workTypeCode = workTypeCode.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.pattern.WorkMonthlySettingSetMemento#
	 * setSiftCode(nts.uk.ctx.at.shared.dom.worktime.SiftCode)
	 */
	@Override
	public void setWorkingCode(WorkingCode workingCode) {
		this.workingCode = workingCode.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.pattern.WorkMonthlySettingSetMemento#
	 * setDate(nts.arc.time.GeneralDate)
	 */
	@Override
	public void setYmdK(BigDecimal ymdk) {
		this.ymdk = ymdk.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.pattern.WorkMonthlySettingSetMemento#
	 * setMonthlyPatternCode(nts.uk.ctx.at.schedule.dom.shift.pattern.
	 * MonthlyPatternCode)
	 */
	@Override
	public void setMonthlyPatternCode(MonthlyPatternCode monthlyPatternCode) {
		this.monthlyPatternCode = monthlyPatternCode.v();
	}

}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.workrecord.monthcal;

import lombok.Getter;

/**
 * The Class ExcessOutsideTimeSetReg.
 */
// 割増集計方法
@Getter
public class ExcessOutsideTimeSetReg {

	/** The legal over time work. */
	// 法定内残業を含める
	private Boolean legalOverTimeWork;

	/** The legal holiday. */
	// 法定外休出を含める
	private Boolean legalHoliday;

	/** The surcharge week month. */
	// 週、月割増時間を集計する
	private Boolean surchargeWeekMonth;

	/**
	 * Instantiates a new excess outside time set reg.
	 *
	 * @param legalOverTimeWork
	 *            the legal over time work
	 * @param legalHoliday
	 *            the legal holiday
	 * @param surchargeWeekMonth
	 *            the surcharge week month
	 */
	public ExcessOutsideTimeSetReg(Boolean legalOverTimeWork, Boolean legalHoliday,
			Boolean surchargeWeekMonth) {
		super();
		this.legalOverTimeWork = legalOverTimeWork;
		this.legalHoliday = legalHoliday;
		this.surchargeWeekMonth = surchargeWeekMonth;
	}

}

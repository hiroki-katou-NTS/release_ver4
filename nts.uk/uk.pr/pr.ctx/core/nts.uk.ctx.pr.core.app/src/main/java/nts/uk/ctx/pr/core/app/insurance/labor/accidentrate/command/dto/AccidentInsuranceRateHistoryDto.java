/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.dto;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;

/**
 * The Class HistoryAccidentInsuranceRateDto.
 */
@Getter
@Setter
public class AccidentInsuranceRateHistoryDto {

	/** The history id. */
	private String historyId;

	/** The start month rage. */
	private int startMonth;

	/** The end month rage. */
	private int endMonth;

	/**
	 * To domain.
	 *
	 * @return the month range
	 */
	public MonthRange toDomain() {
		return MonthRange.range(YearMonth.of(this.startMonth), YearMonth.of(this.endMonth));
	}
}

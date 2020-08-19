/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.workrecord.monthcal.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.workrecord.monthcal.calcmethod.other.DeforLaborSettlementPeriod;

/**
 * The Class DeforLaborSettlementPeriod.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeforLaborSettlementPeriodDto {

	/** The start month. */
	private Integer startMonth;

	/** The period. */
	private Integer period;

	/** The repeat atr. */
	private Boolean repeatAtr;
	
	public DeforLaborSettlementPeriod domain() {
		
		return new DeforLaborSettlementPeriod(new Month(startMonth), new Month(period), repeatAtr);
	}

}

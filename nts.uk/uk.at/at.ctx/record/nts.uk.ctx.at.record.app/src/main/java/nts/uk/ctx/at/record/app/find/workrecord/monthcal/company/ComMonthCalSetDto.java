/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.find.workrecord.monthcal.company;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.app.find.workrecord.monthcal.common.DeforLaborSettlementPeriodDto;
import nts.uk.ctx.at.record.app.find.workrecord.monthcal.common.DeforWorkTimeAggrSetDto;
import nts.uk.ctx.at.record.app.find.workrecord.monthcal.common.ExcessOutsideTimeSetRegDto;
import nts.uk.ctx.at.record.app.find.workrecord.monthcal.common.FlexMonthWorkTimeAggrSetDto;
import nts.uk.ctx.at.record.app.find.workrecord.monthcal.common.RegularWorkTimeAggrSetDto;
import nts.uk.ctx.at.shared.dom.workrecord.monthcal.calcmethod.flex.FlexMonthWorkTimeAggrSet;
import nts.uk.ctx.at.shared.dom.workrecord.monthcal.calcmethod.other.DeforWorkTimeAggrSet;
import nts.uk.ctx.at.shared.dom.workrecord.monthcal.calcmethod.other.RegularWorkTimeAggrSet;

/**
 * The Class ComMonthCalSetDto.
 */
@Getter
@Setter
@Builder
public class ComMonthCalSetDto {

	/** The flex aggr setting. */
	private FlexMonthWorkTimeAggrSetDto flexAggrSetting;

	/** The reg aggr setting. */
	private RegularWorkTimeAggrSetDto regAggrSetting;

	/** The defor aggr setting. */
	private DeforWorkTimeAggrSetDto deforAggrSetting;
	
	public void transfer(DeforWorkTimeAggrSet domain) {
		deforAggrSetting = DeforWorkTimeAggrSetDto.builder()
				.aggregateTimeSet(ExcessOutsideTimeSetRegDto.from(domain.getAggregateTimeSet()))
				.excessOutsideTimeSet(ExcessOutsideTimeSetRegDto.from(domain.getExcessOutsideTimeSet()))
				.isOtTransCriteria(domain.getDeforLaborCalSetting().isOtTransCriteria())
				.settlementPeriod(DeforLaborSettlementPeriodDto.from(domain.getDeforLaborAccPeriod()))
				.build();
	}

	public void transfer(RegularWorkTimeAggrSet domain) {
		regAggrSetting = RegularWorkTimeAggrSetDto.builder()
				.aggregateTimeSet(ExcessOutsideTimeSetRegDto.from(domain.getAggregateTimeSet()))
				.excessOutsideTimeSet(ExcessOutsideTimeSetRegDto.from(domain.getExcessOutsideTimeSet()))
				.build();
	}
	public void transfer(FlexMonthWorkTimeAggrSet domain) {
		flexAggrSetting = FlexMonthWorkTimeAggrSetDto.builder()
				.aggrMethod(domain.getAggrMethod().value)
				.includeIllegalHdwk(domain.getFlexTimeHandle().isIncludeIllegalHdwk() ? 1 : 0)
				.includeOverTime(domain.getFlexTimeHandle().isIncludeOverTime() ? 1 : 0)
				.insufficSet(domain.getInsufficSet().getCarryforwardSet().value)
				.period(domain.getInsufficSet().getPeriod().value)
				.settlePeriod(domain.getInsufficSet().getSettlePeriod().value)
				.startMonth(domain.getInsufficSet().getStartMonth().v())
				.legalAggrSet(domain.getLegalAggrSet().getAggregateSet().value)
				.build();
	}
}

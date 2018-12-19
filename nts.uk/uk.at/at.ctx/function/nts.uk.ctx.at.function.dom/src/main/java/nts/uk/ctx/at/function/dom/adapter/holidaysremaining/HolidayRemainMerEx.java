package nts.uk.ctx.at.function.dom.adapter.holidaysremaining;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.function.dom.adapter.periodofspecialleave.SpecialHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.reserveleave.ReservedYearHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.vacation.StatusHolidayImported;
@Getter
@AllArgsConstructor
public class HolidayRemainMerEx {

	private List<AnnualLeaveUsageImported> result255;
	private List<ReservedYearHolidayImported> result258;
	private List<StatusHolidayImported> result259;
	private List<StatusOfHolidayImported> result260;
	private List<SpecialHolidayImported> result263;
}

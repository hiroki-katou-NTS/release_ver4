package nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.requestperiodchange;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.workplace.ExWorkplaceHistItemImported;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface RequestPeriodChangeService {
	/**
	 * 
	 * @param employeeId
	 * @param period
	 * @param workplaceHistory 社員の職場履歴
	 * @param worktypeHis 勤務種別履歴
	 * @param isWorkplace (isWorkplace = true :職場 , if isWorkplace = false : 勤務種別
	 * @param recreate
	 * @return
	 */
	public List<DatePeriod> getPeriodChange(String employeeId, DatePeriod period,
			List<ExWorkplaceHistItemImported> workplaceHistory, List<BusinessTypeOfEmpDto> worktypeHis, boolean isWorkplace,
			boolean recreate);
}

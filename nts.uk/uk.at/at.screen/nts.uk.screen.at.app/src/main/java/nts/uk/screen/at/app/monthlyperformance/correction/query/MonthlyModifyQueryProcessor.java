package nts.uk.screen.at.app.monthlyperformance.correction.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.finder.MonthlyRecordWorkFinder;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

@Stateless
public class MonthlyModifyQueryProcessor {
	
	@Inject
	MonthlyRecordWorkFinder monthlyRecordWorkFinder;

	public List<MonthlyModifyResult> initScreen(MonthlyMultiQuery query, List<Integer> itemIds, 
												YearMonth yearMonth, ClosureId closureId, 
												ClosureDate closureDate) {
		if(query.getEmployeeIds() == null || query.getEmployeeIds().isEmpty()){
			return new ArrayList<>();
		}
		return query.getEmployeeIds().stream().map(employee -> {
			MonthlyRecordWorkDto recordData = this.monthlyRecordWorkFinder.find(employee, yearMonth, closureId,	closureDate);
			return MonthlyModifyResult.builder()
					.items(AttendanceItemUtil.toItemValues(recordData, itemIds))
					.employeeId(recordData.getEmployeeId())
					.yearMonth(recordData.getYearMonth().v())
					.closureId(recordData.getClosureId())
					.closureDate(recordData.getClosureDate())
					.completed();
		}).collect(Collectors.toList());
	}
}

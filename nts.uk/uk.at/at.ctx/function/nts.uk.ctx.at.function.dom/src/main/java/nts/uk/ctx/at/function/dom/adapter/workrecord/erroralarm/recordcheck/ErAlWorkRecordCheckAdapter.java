package nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckTargetCondition;
import nts.arc.time.calendar.period.DatePeriod;

public interface ErAlWorkRecordCheckAdapter {

	public Map<String, Map<String, Boolean>> check(GeneralDate workingDate, Collection<String> employeeIds,
			List<String> EACheckIDs);

	public Map<String, List<RegulationInfoEmployeeResult>> filterEmployees(GeneralDate workingDate,
			Collection<String> employeeIds, List<String> EACheckIDs);

	public List<RegulationInfoEmployeeResult> filterEmployees(GeneralDate workingDate, Collection<String> employeeIds,
			AlarmCheckTargetCondition condition);

	public List<ErrorRecordImport> check(List<String> EACheckIDs, DatePeriod workingDate,
			Collection<String> employeeIds);

	public List<ErrorRecordImport> checkV2(List<String> EACheckIDs, DatePeriod workingDate,
			Collection<String> employeeIds, Map<String, Integer> mapCheckItem);

	/**
	 * 対象者をしぼり込む
	 */
	public Map<String, List<RegulationInfoEmployeeResult>> filterEmployees(DatePeriod targetPeriod,
			Collection<String> employeeIds, List<AlarmCheckTargetCondition> conditions);

}

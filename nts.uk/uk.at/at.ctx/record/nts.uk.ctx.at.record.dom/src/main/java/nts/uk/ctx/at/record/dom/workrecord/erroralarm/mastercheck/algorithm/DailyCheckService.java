package nts.uk.ctx.at.record.dom.workrecord.erroralarm.mastercheck.algorithm;

import java.util.List;

import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;

public interface DailyCheckService {
	public void extractDailyCheck(String cid, List<String> lstSid, DatePeriod dPeriod,	
			String errorDailyCheckId,
			List<String> errorDailyCheckCd, List<WorkPlaceHistImportAl> getWplByListSidAndPeriod, 
			List<StatusOfEmployeeAdapterAl> lstStatusEmp, List<ResultOfEachCondition> lstResultCondition, 
			List<AlarmListCheckInfor> lstCheckType);
}

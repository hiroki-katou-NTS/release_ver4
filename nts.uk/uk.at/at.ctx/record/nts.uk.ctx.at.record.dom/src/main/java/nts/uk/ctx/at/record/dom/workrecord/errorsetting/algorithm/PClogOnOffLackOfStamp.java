package nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;

/**
 * 打刻漏れ(PCログオンオグオフ)
 * @author nampt
 *
 */
@Stateless
public class PClogOnOffLackOfStamp {

	@Inject
	private BasicScheduleService basicScheduleService;
// fix bug 106204
//	@Inject
//	private ManagedParallelWithContext managedParallelWithContext;

	public EmployeeDailyPerError pClogOnOffLackOfStamp(String companyId, String employeeId, GeneralDate processingDate,
			PCLogOnInfoOfDaily pCLogOnInfoOfDaily, WorkInfoOfDailyPerformance workInfoOfDailyPerformance) {
		
		EmployeeDailyPerError employeeDailyPerError = null;
		
		// 1日半日出勤・1日休日系の判定
		WorkStyle workStyle = basicScheduleService
				.checkWorkDay(workInfoOfDailyPerformance.getRecordInfo().getWorkTypeCode().v());
		if (workStyle != WorkStyle.ONE_DAY_REST) {
			if (pCLogOnInfoOfDaily != null && !pCLogOnInfoOfDaily.getLogOnInfo().isEmpty()) {
				List<Integer> attendanceItemIDList = new ArrayList<>();
				
				List<LogOnInfo> logOnInfos = pCLogOnInfoOfDaily.getLogOnInfo();
				// fix bug 106204
				logOnInfos.forEach(logOnInfo ->  {
					// ログオフのみ存在している(only has Logoff time)
					if ((logOnInfo.getLogOff() != null && logOnInfo.getLogOff().isPresent() && !logOnInfo.getLogOn().isPresent())
							|| (logOnInfo.getLogOff() != null && logOnInfo.getLogOff().isPresent()
								&& logOnInfo.getLogOn().isPresent() && logOnInfo.getLogOn().get() == null)) {
						if (logOnInfo.getWorkNo().v() == 1) {
							attendanceItemIDList.add(794);
						} else if (logOnInfo.getWorkNo().v() == 2) {
							attendanceItemIDList.add(796);
						}
					}
					// ログオンのみ存在している(only has Logon time)
					if ((logOnInfo.getLogOn() != null && logOnInfo.getLogOn().isPresent() && !logOnInfo.getLogOff().isPresent())
							|| (logOnInfo.getLogOn() != null && logOnInfo.getLogOn().isPresent() 
							&& logOnInfo.getLogOff().isPresent() && logOnInfo.getLogOff().get() == null)) {
						if (logOnInfo.getWorkNo().v() == 1) {
							attendanceItemIDList.add(795);
						} else if (logOnInfo.getWorkNo().v() == 2) {
							attendanceItemIDList.add(797);
						}
					}
					// 両方存在しない(both has not data)
					if ((!logOnInfo.getLogOn().isPresent() && !logOnInfo.getLogOff().isPresent())
							|| (logOnInfo.getLogOn().isPresent() && logOnInfo.getLogOff().isPresent()
							&& logOnInfo.getLogOn().get() == null && logOnInfo.getLogOff().get() == null)) {
						if (logOnInfo.getWorkNo().v() == 1) {
							attendanceItemIDList.add(794);
							attendanceItemIDList.add(795);
						} else if (logOnInfo.getWorkNo().v() == 2) {
							attendanceItemIDList.add(796);
							attendanceItemIDList.add(797);
						}
					}
				});

				if (!attendanceItemIDList.isEmpty()){
					employeeDailyPerError = new EmployeeDailyPerError(companyId,
							employeeId, processingDate, new ErrorAlarmWorkRecordCode("S002"),
							attendanceItemIDList);
				}
			}
		}
		return employeeDailyPerError;
	}

}

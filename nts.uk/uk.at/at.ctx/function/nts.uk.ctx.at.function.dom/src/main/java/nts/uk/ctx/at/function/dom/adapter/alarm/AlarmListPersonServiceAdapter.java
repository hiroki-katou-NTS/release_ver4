package nts.uk.ctx.at.function.dom.adapter.alarm;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceHistImport;
import nts.uk.ctx.at.function.dom.adapter.companyRecord.StatusOfEmployeeAdapter;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.schedaily.ScheduleDailyAlarmCheckCond;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.daily.DailyAlarmCondition;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;

public interface AlarmListPersonServiceAdapter {
	/**
	 * マスタチェック
	 * @param cid
	 * @param lstSid
	 * @param dPeriod
	 * @param errorMasterCheckId
	 * @param lstWplHist
	 * @param lstStatusEmp
	 * @param lstResultCondition
	 * @param lstCheckInfor
	 */
	void extractMasterCheckResult(String cid, List<String> lstSid, DatePeriod dPeriod,
			String errorMasterCheckId, List<WorkPlaceHistImport> lstWplHist,
			List<StatusOfEmployeeAdapter> lstStatusEmp,List<ResultOfEachCondition> lstResultCondition,
			List<AlarmListCheckInfor> lstCheckInfor, Consumer<Integer> counter,
			Supplier<Boolean> shouldStop);
	
	/**
	 * 日次チェック
	 * @param cid
	 * @param lstSid
	 * @param dPeriod
	 * @param errorMasterCheckId
	 * @param lstWplHist
	 * @param lstStatusEmp
	 * @param lstResultCondition
	 * @param lstCheckInfor
	 */
	void extractDailyCheckResult(String cid, List<String> lstSid, DatePeriod dPeriod, 
			String errorDailyCheckId, DailyAlarmCondition dailyAlarmCondition,
			List<WorkPlaceHistImport> getWplByListSidAndPeriod, 
			List<StatusOfEmployeeAdapter> lstStatusEmp, 
			List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckType, Consumer<Integer> counter,
			Supplier<Boolean> shouldStop);
	/**
	 * 月次
	 * @param cid
	 * @param lstSid
	 * @param mPeriod
	 * @param fixConId
	 * @param lstAnyConID
	 * @param lstWplHist
	 * @param lstStatusEmp
	 * @param lstResultCondition
	 * @param lstCheckInfor
	 */
	void extractMonthCheckResult(String cid, List<String> lstSid, YearMonthPeriod mPeriod, String fixConId,
			List<String> lstAnyConID, List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition,
			List<AlarmListCheckInfor> lstCheckInfor, Consumer<Integer> counter,
			Supplier<Boolean> shouldStop);
	/**
	 * 複数月次
	 * @param cid
	 * @param lstSid
	 * @param mPeriod
	 * @param lstAnyConID
	 * @param lstWplHist
	 * @param lstResultCondition
	 * @param lstCheckInfor
	 */
	void extractMultiMonthCheckResult(String cid, List<String> lstSid, YearMonthPeriod mPeriod,
			List<String> lstAnyConID, 
			List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition,
			List<AlarmListCheckInfor> lstCheckInfor);
	
	/**
	 * スケジュール日
	 * @param cid
	 * @param lstSid
	 * @param dPeriod
	 * @param errorDailyCheckId
	 * @param scheDailyAlarmCondition
	 * @param getWplByListSidAndPeriod
	 * @param lstStatusEmp
	 * @param lstResultCondition
	 * @param lstCheckType
	 * @param counter
	 * @param shouldStop
	 */
	void extractScheDailyCheckResult(String cid, List<String> lstSid, DatePeriod dPeriod, 
			String errorDailyCheckId, ScheduleDailyAlarmCheckCond scheDailyAlarmCondition,
			List<WorkPlaceHistImport> getWplByListSidAndPeriod, 
			List<StatusOfEmployeeAdapter> lstStatusEmp, 
			List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckType, Consumer<Integer> counter,
			Supplier<Boolean> shouldStop);
}

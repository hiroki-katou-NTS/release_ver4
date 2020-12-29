package nts.uk.ctx.at.function.dom.adapter.alarm;

import java.util.List;

import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceHistImport;
import nts.uk.ctx.at.function.dom.adapter.companyRecord.StatusOfEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.CategoryCondValueDto;
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
			List<AlarmListCheckInfor> lstCheckInfor);
	
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
			String errorMasterCheckId,
			List<WorkPlaceHistImport> lstWplHist,
			List<StatusOfEmployeeAdapter> lstStatusEmp,List<ResultOfEachCondition> lstResultCondition,
			List<AlarmListCheckInfor> lstCheckInfor);
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
			List<AlarmListCheckInfor> lstCheckInfor);
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
}

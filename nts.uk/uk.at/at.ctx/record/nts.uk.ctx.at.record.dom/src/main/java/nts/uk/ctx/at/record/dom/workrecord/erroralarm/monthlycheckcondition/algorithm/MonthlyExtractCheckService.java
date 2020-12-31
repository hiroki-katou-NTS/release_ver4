package nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.algorithm;

import java.util.List;

import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.mastercheck.algorithm.WorkPlaceHistImportAl;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;

public interface MonthlyExtractCheckService {
	/**
	 * 月次の集計処理
	 * @param cid 会社ID
	 * @param lstSid　社員ID
	 * @param dPeriod　期間
	 * @param fixConId　固定抽出条件ID
	 * @param lstAnyConID　任意抽出条件ID
	 * @param getWplByListSidAndPeriod　List＜社員IDと職場履歴＞
	 * @param lstResultCondition　　各チェック条件の結果
	 * @param lstCheckType　List＜チェック種類、コード＞
	 */
	void extractMonthlyAlarm(String cid, List<String> lstSid, YearMonthPeriod mPeriod, String fixConId,List<String> lstAnyConID,
			List<WorkPlaceHistImportAl> getWplByListSidAndPeriod, 
			List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckType);

}

package nts.uk.ctx.at.request.dom.vacation.history.service;

/*import nts.uk.ctx.at.shared.dom.yearholidaygrant.service.Period;*/
import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.vacation.history.PlanVacationHistory;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.申請承認.設定.会社別.計画休暇のルール.Export
 * @author do_dt
 *
 */
public interface PlanVacationRuleExport {
	/**
	 * 計画年休の上限チェック
	 * @param cid ・会社ID
	 * @param employeeId ・社員ID
	 * @param workTypeCode ・勤務種類コード
	 * @param dateData ・申請開始日
	 * ・申請終了日
	 * @return 	true：上限エラーあり
	 * 			false：上限エラーなし
	 */
	List<PlanVacationRuleError> checkMaximumOfPlan(String cid, String employeeId, String workTypeCode, DatePeriod dateData, List<GeneralDate> lstDateIsHoliday);
	/**
	 * 申請期間が利用可能な期間外かチェックする
	 * @param lstVactionPeriod ・ドメインモデル「計画休暇のルールの履歴」一覧
	 * @param dateData 	・申請開始日
	 * 					・申請終了日
	 * @return 期間外フラグ
	 * true：期間外
	 * false：期間内
	 */
	boolean checkOutThePeriod(List<PlanVacationHistory> lstVactionPeriod, DatePeriod dateData);
	/**
	 * 指定期間の計画年休の上限チェック
	 * @param cid 
	 * @param employeeId
	 * @param workTypeCode
	 * @param checkDate ・チェック開始日 ・チェック終了日
	 * @param maxDay ・上限日数
	 * @param appDate ・申請開始日 ・申請終了日
	 * @return
	 */
	boolean checkMaxPlanSpecification(CheckMaximumOfPlanParam planParam, List<GeneralDate> lstDateIsHoliday);
	/**
	 * チェック期間による申請期間を編集する
	 * @param checkData
	 * @param appDate
	 * @return
	 */
	DatePeriod getEditDate(DatePeriod checkData, DatePeriod appDate);
	/**
	 * 使用済の計画年休日数を取得する
	 * @param lstDate
	 * @param dataDate
	 * @return
	 */
	int getUseDays(List<GeneralDate> lstDate, DatePeriod dataDate);
	/**
	 * 取得期間の算出
	 * @param cid
	 * @param employeeId
	 * @param dateCheck
	 * @return
	 */
	PeriodVactionCalInfor calGetPeriod(String cid, String employeeId, DatePeriod dateCheck);
}

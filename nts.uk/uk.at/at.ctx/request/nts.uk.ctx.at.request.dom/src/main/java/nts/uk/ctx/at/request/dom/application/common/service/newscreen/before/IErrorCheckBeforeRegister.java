package nts.uk.ctx.at.request.dom.application.common.service.newscreen.before;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeCheckResult;

/**
 * 登録前エラーチェック
 * @author ThangNQ
 *
 */
public interface IErrorCheckBeforeRegister {
	
	/**
	 * 計算ボタン未クリックチェック
	 * @param 計算フラグ:CalculateFlg(0,1)
	 * @param companyID
	 * @param employeeID
	 * @param rootAtr
	 * @param targetApp
	 * @param appDate
	 */
	void calculateButtonCheck(int CalculateFlg, String companyID, String employeeID, int rootAtr, ApplicationType targetApp, GeneralDate appDate);
	
	/**
	 * 03-01_事前申請超過チェック
	 * @param companyId: 会社ID
	 * @param appDate: 申請日
	 * @param inputDate: 入力日
	 * @param prePostAtr: 事前事後区分
	 * @param attendanceId: 勤怠種類
	 * @param overtimeInputs: 申請時間(input time in a ATTENDANCE)
	 * @return 0: Normal. 1: 背景色を設定する
	 */
	OvertimeCheckResult preApplicationExceededCheck(String companyId, GeneralDate appDate, GeneralDateTime inputDate, PrePostAtr prePostAtr, int attendanceId, 
			List<OverTimeInput> overtimeInputs, String employeeID) ;
	
	/**
	 * 実績超過チェック
	 * @param companyId: 会社ID
	 * @param appDate: 画面上の申請日付
	 * @param usAtr: 残業休出申請共通設定.実績超過区分
	 */
	void OvercountCheck(String companyId, GeneralDate appDate, PrePostAtr prePostAtr);
	
	/**
	 * 03-03_３６上限チェック（月間） KAF005
	 */
	Optional<AppOvertimeDetail> registerOvertimeCheck36TimeLimit(String companyId, String employeeId,
			GeneralDate appDate, List<OverTimeInput> overTimeInput);

	/**
	 * 05_３６上限チェック(詳細) KAF005
	 */
	Optional<AppOvertimeDetail> updateOvertimeCheck36TimeLimit(String companyId, String appId, String enteredPersonId,
			String employeeId, GeneralDate appDate, List<OverTimeInput> overTimeInput);

	/**
	 * 03-03_３６上限チェック（月間） KAF010
	 */
	Optional<AppOvertimeDetail> registerHdWorkCheck36TimeLimit(String companyId, String employeeId, GeneralDate appDate,
			List<HolidayWorkInput> holidayWorkInputs);

	/**
	 * ３６上限チェック(詳細) KAF010
	 */
	Optional<AppOvertimeDetail> updateHdWorkCheck36TimeLimit(String companyId, String appId, String enteredPersonId,
			String employeeId, GeneralDate appDate, List<HolidayWorkInput> holidayWorkInputs);
	
	//３６協定時間上限チェック（年間）
	void TimeUpperLimitYearCheck();
	
	/**
	 * 03-05_事前否認チェック
	 * @param companyId: 会社ID
	 * @param appDate: 申請日
	 * @param inputDate: 入力日
	 * @param prePostAtr: 事前事後区分
	 * @return true: show confirm dialog,
	 */
	OvertimeCheckResult preliminaryDenialCheck(String companyId, String employeeID, GeneralDate appDate, GeneralDateTime inputDate, PrePostAtr prePostAtr,int appType);
	
	/**
	 * 03-08_申請日の矛盾チェック
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @return true: show confirm dialog
	 */
	public List<String> inconsistencyCheck(String companyID, String employeeID, GeneralDate appDate);
}

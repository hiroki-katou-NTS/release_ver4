package nts.uk.ctx.at.request.dom.application.overtime.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.shared.dom.employmentrules.employmenttimezone.BreakTimeZoneSharedOutPut;

public interface OvertimeService {
	/**
	 * 02_残業区分チェック 
	 * @param url
	 * @return
	 */
	public int checkOvertimeAtr(String url);
	
	/**
	 * 07_勤務種類取得
	 * @param companyID
	 * @param employeeID
	 * @param personalLablorCodition
	 * @param requestAppDetailSetting
	 * @return
	 */
	public List<WorkTypeOvertime> getWorkType(String companyID,String employeeID,ApprovalFunctionSetting approvalFunctionSetting,List<AppEmploymentSetting> appEmploymentSettings);
	
	/**
	 * 08_就業時間帯取得
	 * @param companyID
	 * @param employeeID
	 * @param personalLablorCodition
	 * @param requestAppDetailSetting
	 * @return
	 */
	public List<SiftType> getSiftType(String companyID,String employeeID,ApprovalFunctionSetting approvalFunctionSetting,GeneralDate baseDate);
	
	/**
	 * 09_勤務種類就業時間帯の初期選択をセットする
	 * @param companyID
	 * @param employeeID
	 * @param baseDate
	 * @param workTypes
	 * @param siftTypes
	 * @return
	 */
	public WorkTypeAndSiftType getWorkTypeAndSiftTypeByPersonCon(String companyID,String employeeID,GeneralDate baseDate,List<WorkTypeOvertime> workTypes, List<SiftType> siftTypes);
	
	
	void CreateOvertime(AppOverTime domain, Application_New newApp);
	
	/**
	 * 起動時の36協定時間の状態を取得する
	 * @param appOvertimeDetail
	 * @return
	 */
	public Integer getTime36Detail(AppOvertimeDetail appOvertimeDetail);
	
	/**
	 * 休憩時間帯を取得する
	 * @param companyID
	 * @param workTypeCode
	 * @param workTimeCode
	 * @return
	 */
	public BreakTimeZoneSharedOutPut getBreakTimes(String companyID, String workTypeCode, String workTimeCode);
	
	/**
	 * 12.マスタ勤務種類、就業時間帯データをチェック
	 * @param companyID
	 * @param wkTypeCode
	 * @param wkTimeCode
	 * @return
	 */
	public CheckWorkingInfoResult checkWorkingInfo(String companyID, String wkTypeCode, String wkTimeCode);
}

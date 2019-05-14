package nts.uk.ctx.at.request.dom.application.overtime.service;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SWkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.service.output.RecordWorkOutput;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.overtimerestappcommon.OvertimeRestAppCommonSetting;
import nts.uk.ctx.at.request.dom.setting.company.divergencereason.DivergenceReason;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BonusPayTimeItem;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;

/**
 * 01_初期データ取得
 * @author Doan Duy Hung
 *
 */
public interface IOvertimePreProcess {
	/**
	 * 01-01_残業通知情報を取得
	 * @param appCommonSettingOutput
	 * @return
	 */
	public OvertimeInstructInfomation getOvertimeInstruct(AppCommonSettingOutput appCommonSettingOutput,String appDate,String employeeID);
	
	/**
	 * 01-03_残業枠を取得
	 * @param overtimeAtr
	 */
	public List<OvertimeWorkFrame> getOvertimeHours(int overtimeAtr,String companyID);
	
	/**
	 * @param companyID
	 * @return
	 */
	public List<WorkdayoffFrame> getBreaktimeFrame(String companyID);
	
	/**
	 * 01-04_加給時間を取得
	 * @param employeeID
	 * @param overtimeRestAppCommonSet
	 * @param appDate
	 */
	public List<BonusPayTimeItem> getBonusTime(String employeeID,Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet, String appDate, String companyID, SiftType siftType);
	/**
	 * 01-05_申請定型理由を取得
	 * @param companyID
	 * @param appType
	 * @return
	 */
	public List<ApplicationReason> getApplicationReasonType(String companyID,int appType, Optional<AppTypeDiscreteSetting> appTypeDiscreteSetting);
	
	/**
	 * 01-06_申請理由を取得
	 * @param appTypeDiscreteSetting
	 * @return
	 */
	public boolean displayAppReasonContentFlg(Optional<AppTypeDiscreteSetting> appTypeDiscreteSetting);
	
	/**
	 * 01-07_乖離理由を取得
	 * @param overtimeRestAppCommonSet
	 * @return
	 */
	public boolean displayDivergenceReasonInput(Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet);
	
	/**
	 * 01-08_乖離定型理由を取得
	 * @param companyID
	 * @param appType
	 * @param overtimeRestAppCommonSet
	 * @return
	 */
	public List<DivergenceReason> getDivergenceReasonForm(String companyID,int appType, Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet);
	/**
	 * 01-09_事前申請を取得
	 * @param employeeId
	 * @param overtimeRestAppCommonSet
	 * @param appDate
	 * @return
	 */
	
	public AppOverTime getPreApplication(String companyID, String employeeId, Optional<OvertimeRestAppCommonSetting> overtimeRestAppCommonSet,String appDate, int prePostAtr);
	
	/**
	 * 01-10_0時跨ぎチェック
	 * @return
	 */
	public boolean displayBreaktime();
	
	/**
	 * 01-13_事前事後区分を取得
	 * @param companyID
	 * @param applicationDto
	 * @param result
	 * @param uiType
	 */
	public DisplayPrePost getDisplayPrePost(String companyID,int uiType,String appDate,int appType,int overtimeAtr);
	
	/**
	 * 01-14_勤務時間取得
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @param requestAppDetailSetting
	 */
	public RecordWorkOutput getWorkingHours(String companyID,String employeeID, String changeEmployee, String appDate,ApprovalFunctionSetting approvalFunctionSetting, String siftCD, boolean isOverTime);
	/**
	 *  01-17_休憩時間取得
	 * @param requestAppDetailSetting
	 * @return
	 */
	public boolean getRestTime(ApprovalFunctionSetting approvalFunctionSetting);
	
	/**
	 * 01-18_実績の内容を表示し直す
	 * @param prePostAtr
	 * @return
	 */
	public AppOvertimeReference getResultContentActual(int prePostAtr,String siftCode,String companyID,String employeeID,String appDate,ApprovalFunctionSetting approvalFunctionSetting,List<CaculationTime> overtimeHours,List<OvertimeInputCaculation> overtimeInputCaculations);
	
	/**
	 * @param employeeID
	 * @param siftCode
	 * @param companyID
	 * @param sWkpHistImport
	 * @return
	 */
	public Optional<BonusPaySetting> getBonusPaySetting(String employeeID,String siftCode,String companyID,SWkpHistImport sWkpHistImport);
	
	/**
	 * if is 当日の場合 return true
	 * @param appDate
	 * @param workTimeSet
	 * @return
	 */
	public boolean checkTimeDay(String appDate, PredetemineTimeSetting workTimeSet);
}

package nts.uk.ctx.at.request.dom.application.gobackdirectly.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleImport;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSetting;

public interface GoBackDirectlyRegisterService {
	/**
	 * 直行直帰登録
	 * 
	 * @param employeeID
	 * @param application
	 * @param goBackDirectly
	 */
	public ProcessResult register(GoBackDirectly goBackDirectly, Application_New application);
	
	/**
	 * 
	 * @param goBackDirectly
	 * @param application
	 * @param appApprovalPhases
	 */
	public void checkBeforRegister(GoBackDirectly goBackDirectly, Application_New application, boolean checkOver1Year);

	/**
	 * アルゴリズム「直行直帰するチェック」を実行する
	 * 
	 * @param goBackDirectly
	 * @param goBackAtr
	 */
	public GoBackDirectAtr goBackDirectCheck(GoBackDirectly goBackDirectly);

	/**
	 * 直行直帰遅刻早退のチェック
	 * 
	 * @param goBackDirectly
	 * @return
	 */
	public GoBackDirectLateEarlyOuput goBackDirectLateEarlyCheck(GoBackDirectly goBackDirectly, Application_New application);

	/**
	 * Check Validity
	 * 
	 */
	public CheckValidOutput goBackLateEarlyCheckValidity(GoBackDirectly goBackDirectly,
			GoBackDirectlyCommonSetting goBackCommonSet, int line, ScBasicScheduleImport scBasicScheduleImport);
	
	public void createThrowMsg(String msgConfirm, List<String> msgLst);
	
	/**
	 * 直行直帰申請日の矛盾チェック
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @return
	 */
	public List<String> inconsistencyCheck(String companyID, String employeeID, GeneralDate appDate);
}

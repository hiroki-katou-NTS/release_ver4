package nts.uk.ctx.at.request.dom.application.appabsence.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.HolidayAppType;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.AppliedDate;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;

public interface AbsenceServiceProcess {
	/**
	 * @param workTypeCode
	 * @return
	 */
	public SpecialLeaveInfor getSpecialLeaveInfor(String workTypeCode);
	
	void createAbsence(AppAbsence domain, Application_New newApp);
	/**
	 * @author hoatt
	 * 13.計画年休上限チェック
	 * @param cID: 会社ID
	 * @param sID: 社員ID
	 * @param workTypeCD: 勤務種類コード
	 * @param sDate: 申請開始日
	 * @param eDate: 申請終了日
	 * @param hdAppType: 休暇種類
	 * @param lstDateIsHoliday: 休日の申請日
	 */
	void checkLimitAbsencePlan(String cID, String sID, String workTypeCD, 
			GeneralDate sDate, GeneralDate eDate, HolidayAppType hdAppType, List<GeneralDate> lstDateIsHoliday);
	/**
	 * @author hoatt
	 * 14.休暇種類表示チェック
	 * @param companyID
	 * @param sID
	 * @param baseDate
	 * @return
	 */
	public CheckDispHolidayType checkDisplayAppHdType(String companyID, String sID, GeneralDate baseDate);
	/**
	 * @author hoatt
	 * 代休振休優先消化チェック
	 * @param pridigCheck - 休暇申請設定．年休より優先消化チェック区分 - HdAppSet
	 * @param isSubVacaManage - 振休管理設定．管理区分
	 * @param subVacaTypeUseFlg - 休暇申請対象勤務種類．休暇種類を利用しない（振休） - AppEmploymentSetting
	 * @param isSubHdManage - 代休管理設定．管理区分
	 * @param subHdTypeUseFlg - 休暇申請対象勤務種類．休暇種類を利用しない（代休） - AppEmploymentSetting
	 * @param numberSubHd - 代休残数
	 * @param numberSubVaca - 振休残数
	 * @return エラーメッセージ - 確認メッセージ
	 */
	public void checkDigestPriorityHd(AppliedDate pridigCheck, boolean isSubVacaManage, boolean subVacaTypeUseFlg,
			boolean isSubHdManage, boolean subHdTypeUseFlg, int numberSubHd, int numberSubVaca);
	/**
	 * @author hoatt
	 * 振休代休優先チェック
	 * @param pridigCheck - 休暇申請設定．年休より優先消化チェック区分 - HdAppSet
	 * @param isSubVacaManage - 振休管理設定．管理区分
	 * @param subVacaTypeUseFlg - 休暇申請対象勤務種類．休暇種類を利用しない（振休） - AppEmploymentSetting
	 * @param isSubHdManage - 代休管理設定．管理区分
	 * @param subHdTypeUseFlg - 休暇申請対象勤務種類．休暇種類を利用しない（代休） - AppEmploymentSetting
	 * @param numberSubHd - 代休残数
	 * @param numberSubVaca - 振休残数
	 * @return エラーメッセージ - 確認メッセージ
	 */
	public void checkPriorityHoliday(AppliedDate pridigCheck,
			boolean isSubVacaManage, boolean subVacaTypeUseFlg, boolean isSubHdManage, boolean subHdTypeUseFlg,
			int numberSubHd, int numberSubVaca);
	/**
	 * @author hoatt
	 * 残数取得する
	 * @param companyID - 会社ID
	 * @param employeeID - 社員ID　＝申請者社員ID
	 * @param baseDate - 基準日
	 * @return 年休残数-代休残数-振休残数-ストック休暇残数
	 */
	public NumberOfRemainOutput getNumberOfRemaining(String companyID, String employeeID, GeneralDate baseDate, 
			List<AppEmploymentSetting> appEmpSetAs);
}

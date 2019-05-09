package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.overtime.service.CheckWorkingInfoResult;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService;
@Stateless
public class DetailBeforeUpdateImpl implements DetailBeforeUpdate {

	@Inject
	private NewBeforeRegister_New newBeforeRegister;

	@Inject
	private ApplicationRepository_New applicationRepository;

	@Inject
	private OvertimeService OvertimeService;
	
	public void processBeforeDetailScreenRegistration(String companyID, String employeeID, GeneralDate appDate,
			int employeeRouteAtr, String appID, PrePostAtr postAtr, Long version, String wkTypeCode,
			String wkTimeCode) {
		//勤務種類、就業時間帯チェックのメッセージを表示
		displayWorkingHourCheck(companyID, wkTypeCode, wkTimeCode);
		// 選択した勤務種類の矛盾チェック(check sự mâu thuẫn của worktype đã chọn)
		// selectedWorkTypeConflictCheck();
		
		Application_New application = applicationRepository.findByID(companyID, appID).get();
		GeneralDate startDate = application.getAppDate();
		GeneralDate endDate = application.getAppDate();
		// 申請する開始日～申請する終了日までループする
		for(GeneralDate loopDate = startDate; loopDate.beforeOrEquals(endDate); loopDate = loopDate.addDays(1)){
			if(application.getPrePostAtr().equals(PrePostAtr.PREDICT) && application.isAppOverTime()){
				newBeforeRegister.confirmCheckOvertime(application.getCompanyID(), application.getEmployeeID(), loopDate);
			}else{
				// アルゴリズム「確定チェック」を実施する
				newBeforeRegister.confirmationCheck(application.getCompanyID(), application.getEmployeeID(), loopDate);
			}
		}

		exclusiveCheck(companyID, appID, version);
	}
	
	/**
	 * 勤務種類、就業時間帯チェックのメッセージを表示
	 * @param companyID
	 * @param wkTypeCode
	 * @param wkTimeCode
	 */
	@Override
	public void displayWorkingHourCheck(String companyID, String wkTypeCode, String wkTimeCode) {
		// 12.マスタ勤務種類、就業時間帯データをチェック
		CheckWorkingInfoResult checkResult = this.OvertimeService.checkWorkingInfo(companyID, wkTypeCode, wkTimeCode);
		if (checkResult.isWkTypeError() || checkResult.isWkTimeError()) {
			String text = "";
			if (checkResult.isWkTypeError()) {
				text = "勤務種類コード" + wkTypeCode;
			}
			if (checkResult.isWkTimeError()) {
				text = "就業時間帯コード" + wkTimeCode;
			}
			if (checkResult.isWkTypeError() && checkResult.isWkTimeError()) {
				text = "勤務種類コード" + wkTypeCode + "、" + "就業時間帯コード" + wkTimeCode;
				;
			}
			throw new BusinessException("Msg_1530", text);
		}
	}

	/**
	 * 1.排他チェック
	 */
	public void exclusiveCheck(String companyID, String appID, Long version) {
		if (applicationRepository.findByID(companyID, appID).isPresent()) {
			Application_New application = applicationRepository.findByID(companyID, appID).get();
			if (!application.getVersion().equals(version)) {
				throw new BusinessException("Msg_197");
			}
		} else {
			throw new BusinessException("Msg_198");
		}
	}

	/**
	 * 4-1.詳細画面登録前の処理 (CMM045)
	 * 
	 * @author hoatt
	 */
	@Override
	public boolean processBefDetailScreenReg(String companyID, String employeeID, GeneralDate appDate,
			int employeeRouteAtr, String appID, PrePostAtr postAtr, Long version) {
		// 選択した勤務種類の矛盾チェック(check sự mâu thuẫn của worktype đã chọn)
		// selectedWorkTypeConflictCheck();

		Application_New application = applicationRepository.findByID(companyID, appID).get();
		GeneralDate startDate = application.getAppDate();
		GeneralDate endDate = application.getAppDate();
		// 申請する開始日～申請する終了日までループする
		for(GeneralDate loopDate = startDate; loopDate.beforeOrEquals(endDate); loopDate = loopDate.addDays(1)){
			if(loopDate.equals(GeneralDate.today()) && application.getPrePostAtr().equals(PrePostAtr.PREDICT) && application.isAppOverTime()){
				newBeforeRegister.confirmCheckOvertime(application.getCompanyID(), application.getEmployeeID(), loopDate);
			}else{
				// アルゴリズム「確定チェック」を実施する
				newBeforeRegister.confirmationCheck(application.getCompanyID(), application.getEmployeeID(), loopDate);
			}
		}
		
		// アルゴリズム「排他チェック」を実行する(thực hiện xử lý 「排他チェック」)
		return exclusiveCheckErr(companyID, appID, version);
	}

	@Override
	public boolean exclusiveCheckErr(String companyID, String appID, Long version) {
		if (applicationRepository.findByID(companyID, appID).isPresent()) {
			Application_New application = applicationRepository.findByID(companyID, appID).get();
			if (!application.getVersion().equals(version)) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}

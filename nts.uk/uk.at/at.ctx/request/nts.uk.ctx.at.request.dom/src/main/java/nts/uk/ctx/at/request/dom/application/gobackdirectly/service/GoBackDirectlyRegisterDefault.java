package nts.uk.ctx.at.request.dom.application.gobackdirectly.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.Application;
import nts.uk.ctx.at.request.dom.application.common.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.common.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.RegisterAtApproveReflectionInfoService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeProcessRegister;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.primitive.UseAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSetting;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.CheckAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.WorkChangeFlg;
import nts.uk.ctx.at.request.dom.setting.requestofearch.SettingFlg;
import nts.uk.shr.com.context.AppContexts;

/**
 * 直行直帰登録
 * 
 * @author ducpm
 */
@Stateless
public class GoBackDirectlyRegisterDefault implements GoBackDirectlyRegisterService {
	@Inject
	RegisterAtApproveReflectionInfoService registerAppReplection;
	@Inject
	GoBackDirectlyRepository goBackDirectRepo;
	@Inject
	ApplicationRepository appRepo;
	@Inject
	NewBeforeProcessRegister processBeforeRegister;
	@Inject
	GoBackDirectlyCommonSettingRepository goBackDirectCommonSetRepo;

	/**
	 * 
	 */
	@Override
	// public void register(int approvalRoot, String employeeID, Application
	// application, GoBackDirectly goBackDirectly) {
	public void register(int approvalRoot, String employeeID, String appID) {
		String companyID = AppContexts.user().companyId();
		/**
		 * アルゴリズム「直行直帰登録前チェック」を実行する
		 */
		GoBackDirectly goBackDirectly = goBackDirectRepo.findByApplicationID(companyID, appID).get();
		GoBackDirectlyCommonSetting goBackCommonSet = goBackDirectCommonSetRepo.findByCompanyID(companyID).get();
		Application application = appRepo.getAppById(companyID, appID).get();
		GeneralDate date = application.getApplicationDate();
		PrePostAtr prePost = application.getPrePostAtr();
		ApplicationType appType = application.getApplicationType();
		processBeforeRegister.processBeforeRegister(companyID, employeeID, date, prePost, approvalRoot, appType.value);
		// if hasError return
		// if no Error
		// アルゴリズム「直行直帰するチェック」を実行する
		if (this.goBackDirectCheck(goBackDirectly) == GoBackDirectAtr.IS) {
			// アルゴリズム「直行直帰遅刻早退のチェック」を実行する
			GoBackDirectLateEarlyOuput goBackLateEarly = this.goBackDirectLateEarlyCheck(goBackDirectly);
			//エラーあり
			if(goBackLateEarly.isError) {
				//直行直帰申請共通設定.早退遅刻設定がチェックする
				if(goBackCommonSet.getLateLeaveEarlySettingAtr() == CheckAtr.CHECKREGISTER) {
					throw new BusinessException("Msg_297");
				}else {
					throw new BusinessException("Msg_298");	
				}
			}else {
				//アルゴリズム「直行直帰登録」を実行する
				//2-2.新規画面登録時承認反映情報の整理 
				registerAppReplection.newScreenRegisterAtApproveInfoReflect(employeeID, application);
			}
		} else {
			// メッセージ（Msg_338）を表示する
			throw new BusinessException("Msg_338");
		}

//		/**
//		 * 2-2.新規画面登録時承認反映情報の整理
//		 */
//		registerAppReplection.newScreenRegisterAtApproveInfoReflect(employeeID, application);
//		/**
//		 * ドメインモデル「直行直帰申請」の新規登録する
//		 */
//		goBackDirectRepo.insert(goBackDirectly);
	}

	/**
	 *  
	 */
	@Override
	public GoBackDirectAtr goBackDirectCheck(GoBackDirectly goBackDirectly) {
		if (goBackDirectly.getGoWorkAtr1() == UseAtr.NOTUSE && goBackDirectly.getBackHomeAtr1() == UseAtr.NOTUSE
				&& goBackDirectly.getGoWorkAtr2() == UseAtr.NOTUSE
				&& goBackDirectly.getBackHomeAtr2() == UseAtr.NOTUSE) {
			return GoBackDirectAtr.NOT;
		} else {
			return GoBackDirectAtr.IS;
		}
	}

	/**
	 * アルゴリズム「直行直帰遅刻早退のチェック」を実行する
	 */
	@Override
	public GoBackDirectLateEarlyOuput goBackDirectLateEarlyCheck(GoBackDirectly goBackDirectly) {
		// ドメインモデル「直行直帰申請共通設定」を取得する
		String companyID = AppContexts.user().companyId();
		GoBackDirectLateEarlyOuput output = new GoBackDirectLateEarlyOuput();
		output.isError = false;
		GoBackDirectlyCommonSetting goBackCommonSet = goBackDirectCommonSetRepo.findByCompanyID(companyID).get();
		// 設定：直行直帰申請共通設定.早退遅刻設定
		if (goBackCommonSet.getLateLeaveEarlySettingAtr() != CheckAtr.NOTCHECK) {
			output.lateOrLeaveAppSettingFlg = SettingFlg.SETTING;
			// check Valid 1
			CheckValidOutput validOut1 = this.goBackLateEarlyCheckValidity(goBackDirectly, goBackCommonSet, 1);
			// check Valid 2
			CheckValidOutput validOut2 = this.goBackLateEarlyCheckValidity(goBackDirectly, goBackCommonSet, 2);
			// チェック対象１またはチェック対象２がTrueの場合
			if (validOut1.isCheckValid || validOut2.isCheckValid) {
				// アルゴリズム「1日分の勤怠時間を仮計算」を実行する
				// GOI 1日分の勤怠時間を仮計算 ben HibetsuJisseki
				int attendanceTime = 0;

				if (attendanceTime < 0) {
					output.isError = true;
					throw new BusinessException("Msg_296");
					// output.isError = true;
				} else {
					// Lai check thang Time lan nua
					// Merge Node 1
					if (attendanceTime < 0) {
						output.isError = true;
						throw new BusinessException("Msg_295");
					}
				}
			}
		} else {
			output.lateOrLeaveAppSettingFlg = SettingFlg.NOTSETTING;
		}
		return output;
	}

	/**
	 * アルゴリズム「直行直帰遅刻早退有効チェック」を実行する
	 */
	@Override
	public CheckValidOutput goBackLateEarlyCheckValidity(GoBackDirectly goBackDirectly,
			GoBackDirectlyCommonSetting goBackCommonSet, int line) {

		CheckValidOutput result = new CheckValidOutput();
		result.isCheckValid = false;
		// 変更する
		if (goBackCommonSet.getWorkChangeFlg() == WorkChangeFlg.CHANGE
				&& goBackCommonSet.getWorkChangeFlg() == WorkChangeFlg.DECIDECHANGE) {
			// 勤務種類及び銃所時間帯はチェック対象
			result.setSiftCd(goBackDirectly.getSiftCD());
			result.setWorkTypeCD(goBackDirectly.getWorkTypeCD());

		} else if (goBackCommonSet.getWorkChangeFlg() == WorkChangeFlg.NOTCHANGE
				&& goBackCommonSet.getWorkChangeFlg() == WorkChangeFlg.DECIDENOTCHANGE) {
			// ・出張申請.勤務種類 ＝空白
			// ・出張申請.就業時間帯 ＝空白
			// 勤務種類及び銃所時間帯はチェック対象外
			result.setSiftCd(null);
			result.setWorkTypeCD(null);
		}
		if (line == 1) {
			// MERGE NODE 1
			// 勤務直行の確認
			if (goBackDirectly.getGoWorkAtr1() == UseAtr.USE && goBackDirectly.getWorkTimeStart1().v() != 0) {
				// 入力する
				result.setCheckValid(true);
			} else {
				result.setWorkTimeStart(null);
			}
			// 勤務直帰の確認
			if (goBackDirectly.getBackHomeAtr1() == UseAtr.USE && goBackDirectly.getWorkTimeEnd1().v() != 0) {
				result.setCheckValid(true);
			} else {
				result.setWorkTimeEnd(null);
			}
		} else {
			// MERGE NODE 1
			// 勤務直行の確認
			if (goBackDirectly.getGoWorkAtr2() == UseAtr.USE && goBackDirectly.getWorkTimeStart2().v() != 0) {
				// 入力する
				result.setCheckValid(true);
			} else {
				result.setWorkTimeStart(null);
			}
			// 勤務直帰の確認
			if (goBackDirectly.getBackHomeAtr2() == UseAtr.USE && goBackDirectly.getWorkTimeEnd2().v() != 0) {
				result.setCheckValid(true);
			} else {
				result.setWorkTimeEnd(null);
			}
		}
		return result;
	}

}

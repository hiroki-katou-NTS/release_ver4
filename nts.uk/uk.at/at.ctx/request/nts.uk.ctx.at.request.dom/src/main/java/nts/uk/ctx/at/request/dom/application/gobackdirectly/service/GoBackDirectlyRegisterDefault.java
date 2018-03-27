package nts.uk.ctx.at.request.dom.application.gobackdirectly.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.RegisterAtApproveReflectionInfoService_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.after.NewAfterRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSetting;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.CheckAtr;
import nts.uk.ctx.at.request.dom.setting.workplace.SettingFlg;
import nts.uk.shr.com.context.AppContexts;

/**
 * 直行直帰登録
 * 
 * @author ducpm
 */
@Stateless
public class GoBackDirectlyRegisterDefault implements GoBackDirectlyRegisterService {
	@Inject
	RegisterAtApproveReflectionInfoService_New registerAppReplection;
	@Inject
	GoBackDirectlyRepository goBackDirectRepo;
	@Inject
	ApplicationApprovalService_New appRepo;
	@Inject
	NewBeforeRegister_New processBeforeRegister;
	@Inject
	GoBackDirectlyCommonSettingRepository goBackDirectCommonSetRepo;
	@Inject 
	NewAfterRegister_New newAfterRegister;
	@Inject
	ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	

	/**
	 * 
	 */
	@Override
	public void register(GoBackDirectly goBackDirectly, Application_New application) {
		String employeeID = application.getEmployeeID();
		//アルゴリズム「直行直帰登録」を実行する		
		goBackDirectRepo.insert(goBackDirectly);
		appRepo.insert(application);
		// 2-2.新規画面登録時承認反映情報の整理
		registerAppReplection.newScreenRegisterAtApproveInfoReflect(employeeID, application);
		//アルゴリズム「2-3.新規画面登録後の処理」を実行する 
		newAfterRegister.processAfterRegister(application);
		
	}

	
	@Override
	public void checkBeforRegister(GoBackDirectly goBackDirectly, Application_New application) {
		String companyID = AppContexts.user().companyId();
		GoBackDirectlyCommonSetting goBackCommonSet = goBackDirectCommonSetRepo.findByCompanyID(companyID).get();
		//アルゴリズム「2-1.新規画面登録前の処理」を実行する
		processBeforeRegister.processBeforeRegister(application);
		// アルゴリズム「直行直帰するチェック」を実行する - client da duoc check
		// アルゴリズム「直行直帰遅刻早退のチェック」を実行する
		GoBackDirectLateEarlyOuput goBackLateEarly = this.goBackDirectLateEarlyCheck(goBackDirectly);
		//直行直帰遅刻早退のチェック
		//TODO: chua the thuc hien duoc nen mac dinh luc nao cung co loi エラーあり
		if(goBackLateEarly.isError) {
			//直行直帰申請共通設定.早退遅刻設定がチェックする
			if(goBackCommonSet.getLateLeaveEarlySettingAtr() == CheckAtr.CHECKREGISTER) {
				throw new BusinessException("Msg_297");
			}else if(goBackCommonSet.getLateLeaveEarlySettingAtr() == CheckAtr.CHECKNOTREGISTER) {
				throw new BusinessException("Msg_298");	
			}
		}
	}
	
	/**
	 *  アルゴリズム「直行直帰するチェック」を実行する
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
		
		String companyID = AppContexts.user().companyId();
		// ドメインモデル「直行直帰申請共通設定」を取得する
		GoBackDirectLateEarlyOuput output = new GoBackDirectLateEarlyOuput();
		output.isError = true;
		//ドメインモデル「直行直帰申請共通設定」を取得する 
		GoBackDirectlyCommonSetting goBackCommonSet = goBackDirectCommonSetRepo.findByCompanyID(companyID).get();
		// 設定：直行直帰申請共通設定.早退遅刻設定		
		if (goBackCommonSet.getLateLeaveEarlySettingAtr() == CheckAtr.CHECKREGISTER) {//チェックする
			output.lateOrLeaveAppSettingFlg = SettingFlg.SETTING;
			// check Valid 1
			CheckValidOutput validOut1 = this.goBackLateEarlyCheckValidity(goBackDirectly, goBackCommonSet, 1);
			// check Valid 2
			CheckValidOutput validOut2 = this.goBackLateEarlyCheckValidity(goBackDirectly, goBackCommonSet, 2);
			// チェック対象１またはチェック対象２がTrueの場合
			if (validOut1.isCheckValid || validOut2.isCheckValid) {
				// アルゴリズム「1日分の勤怠時間を仮計算」を実行する
				//Mac Dinh tra ve 0
				int earlyTime = 0;
				//日別実績の勤怠時間.実働時間.総労働時間.早退時間.時間 
				//TODO: chua the lam duoc do chua co 日別実績
				// So sách tới 日別実績 để biết đi sớm về muộn, nếu  
				
				if (earlyTime < 0) {
					output.isError = true;
					throw new BusinessException("Msg_296");
				} else {
					int lateTime = 0;
					// Merge Node 1
					if (lateTime < 0) {
						output.isError = true;
						throw new BusinessException("Msg_295");
					}
				}
			}
		} else {//チェックしない
			output.lateOrLeaveAppSettingFlg = SettingFlg.NOTSETTING;
		}
		output.isError = false; // đối ứng ngày 24/03/2018 để tránh lỗi setting
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
		// if (goBackCommonSet.getWorkChangeFlg() == WorkChangeFlg.CHANGE
		// || goBackCommonSet.getWorkChangeFlg() == WorkChangeFlg.DECIDECHANGE) {
		if (goBackDirectly.getWorkChangeAtr() == UseAtr.USE) {
			// 勤務種類及び銃所時間帯はチェック対象
			result.setSiftCd(goBackDirectly.getSiftCD());
			result.setWorkTypeCD(goBackDirectly.getWorkTypeCD());
			// } else if (goBackCommonSet.getWorkChangeFlg() == WorkChangeFlg.NOTCHANGE
			// || goBackCommonSet.getWorkChangeFlg() == WorkChangeFlg.DECIDENOTCHANGE) {
		} else {
			// ・出張申請.勤務種類 ＝空白
			// ・出張申請.就業時間帯 ＝空白
			// 勤務種類及び銃所時間帯はチェック対象外
			result.setSiftCd(null);
			result.setWorkTypeCD(null);
		}
		if (line == 1) {
			// MERGE NODE 1
			// 勤務直行の確認
			if (goBackDirectly.getGoWorkAtr1() == UseAtr.USE && goBackDirectly.getWorkTimeStart1() != null) {
				// 入力する
				result.setCheckValid(true);
			} else {
				result.setWorkTimeStart(null);
			}
			// 勤務直帰の確認
			if (goBackDirectly.getBackHomeAtr1() == UseAtr.USE && goBackDirectly.getWorkTimeEnd1() != null) {
				result.setCheckValid(true);
			} else {
				result.setWorkTimeEnd(null);
			}
		} else {
			// MERGE NODE 1
			// 勤務直行の確認
			if (goBackDirectly.getGoWorkAtr2() == UseAtr.USE && goBackDirectly.getWorkTimeStart2() != null) {
				// 入力する
				result.setCheckValid(true);
			} else {
				result.setWorkTimeStart(null);
			}
			// 勤務直帰の確認
			if (goBackDirectly.getBackHomeAtr2() == UseAtr.USE && goBackDirectly.getWorkTimeEnd2() != null) {
				result.setCheckValid(true);
			} else {
				result.setWorkTimeEnd(null);
			}
		}
		return result;
	}
}

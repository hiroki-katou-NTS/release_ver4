package nts.uk.ctx.at.request.dom.application.gobackdirectly.service;
/*import nts.arc.error.BusinessException;*/

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository_Old;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly_Old;
//import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
//import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;

@Stateless
public class GoBackDirectlyUpdateDefault implements GoBackDirectlyUpdateService {

	@Inject
	private DetailBeforeUpdate detailBeforeUpdate;

	@Inject
	private GoBackDirectlyRepository_Old goBackDirectlyRepo;
	
	@Inject 
	private ApplicationRepository appRepo;

//	@Inject
//	private ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private DetailAfterUpdate detailAfterUpdate;
	
//	@Inject
//	private GoBackDirectlyCommonSettingRepository goBackDirectCommonSetRepo;
	
	@Inject
	private GoBackDirectlyRegisterService goBackDirectlyRegisterService;

//	@Inject
//	private AppTypeDiscreteSettingRepository appTypeDiscreteSettingRepository;
	
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	
	@Inject
	private WorkTimeSettingService workTimeSettingService;
	
	@Inject
	private CollectAchievement collectAchievement;
	
	/**
	 * アルゴリズム「直行直帰更新前チェック」を実行する
	 */
	@Override
	public void checkErrorBeforeUpdate(GoBackDirectly_Old goBackDirectly, String companyID, String appID, Long version) {
		// アルゴリズム「4-1.詳細画面登録前の処理」を実行する
		Application application_New = appRepo.findByID(companyID, appID).get();
		// error EA refactor 4
		/*this.detailBeforeUpdate.processBeforeDetailScreenRegistration(
				companyID, 
				application_New.getEmployeeID(), 
				application_New.getAppDate(), 
				EmploymentRootAtr.APPLICATION.value,
				application_New.getAppID(), 
				application_New.getPrePostAtr(),  
				version,
				!goBackDirectly.getWorkTypeCD().isPresent() || goBackDirectly.getWorkTypeCD().get() == null ? null : goBackDirectly.getWorkTypeCD().get().v(),
				!goBackDirectly.getSiftCD().isPresent() || goBackDirectly.getSiftCD().get() == null ? null : goBackDirectly.getSiftCD().get().v());*/
//		GoBackDirectlyCommonSetting goBackCommonSet = goBackDirectCommonSetRepo.findByCompanyID(companyID).get();
		// アルゴリズム「直行直帰するチェック」を実行する - client da duoc check
		// アルゴリズム「直行直帰遅刻早退のチェック」を実行する
		// GoBackDirectLateEarlyOuput goBackLateEarly = goBackDirectlyRegisterService.goBackDirectLateEarlyCheck(goBackDirectly, application_New);
		//直行直帰遅刻早退のチェック
		//TODO: chua the thuc hien duoc nen mac dinh luc nao cung co loi エラーあり
		/*
		if(goBackLateEarly.isError) {
			//直行直帰申請共通設定.早退遅刻設定がチェックする
			if(goBackCommonSet.getLateLeaveEarlySettingAtr() == CheckAtr.CHECKREGISTER) {
				goBackDirectlyRegisterService.createThrowMsg("Msg_297", goBackLateEarly.msgLst);
			}else if(goBackCommonSet.getLateLeaveEarlySettingAtr() == CheckAtr.CHECKNOTREGISTER) {
				goBackDirectlyRegisterService.createThrowMsg("Msg_298", goBackLateEarly.msgLst);	
			}
		}
		*/
	}

	/**
	 * アルゴリズム「直行直帰更新」を実行する
	 */
	@Override
	public ProcessResult updateGoBackDirectly(GoBackDirectly_Old goBackDirectly, Application application, Long version) {
		String workTimeCD = "";
		String workTypeCD = "";
		// ドメインモデル「直行直帰申請共通設定」．勤務の変更をチェックする
//		GoBackDirectlyCommonSetting setting = goBackDirectCommonSetRepo.findByCompanyID(application.getCompanyID()).get();
//		if(setting.getWorkChangeFlg()==WorkChangeFlg.DECIDECHANGE){
//			workTypeCD = goBackDirectly.getWorkTypeCD().map(x -> x.v()).orElse("");
//			workTimeCD = goBackDirectly.getSiftCD().map(x -> x.v()).orElse("");
//		} else {
//			// 実績の取得
//			/*AchievementOutput achievementOutput = collectAchievement.getAchievement(application.getCompanyID(), application.getEmployeeID(), application.getAppDate());*/
//			AchievementOutput achievementOutput = null;
//			workTimeCD = achievementOutput.getWorkTime().getWorkTimeCD();
//			workTypeCD = achievementOutput.getWorkType().getWorkTypeCode();
//		}
//		// 取得した「勤務種類コード」「就業時間帯コード」をチェックする
//		Optional<TimezoneUse> opTimezoneUse = Optional.empty();
//		if(Strings.isNotBlank(workTypeCD) && Strings.isNotBlank(workTimeCD)){
//			// 所定時間帯を取得する
//			PredetermineTimeSetForCalc predetermineTimeSetForCalc = workTimeSettingService.getPredeterminedTimezone(application.getCompanyID(), workTimeCD, workTypeCD, null); 
//			opTimezoneUse = predetermineTimeSetForCalc.getTimezones().stream().filter(x -> x.getWorkNo() == 1).findAny();
//		}
//		// 勤務開始1に時刻が入力されたか
//		if(!goBackDirectly.getWorkTimeStart1().isPresent()){
//			// 取得した「勤務種類コード」「就業時間帯コード」をチェックする
//			if(Strings.isNotBlank(workTypeCD) && Strings.isNotBlank(workTimeCD)){
//				// 勤務開始1に(勤務NO=1)の「計算用所定時間設定」．時間帯．開始を入れる
//				goBackDirectly.setWorkTimeStart1(opTimezoneUse.map(x -> new WorkTimeGoBack(x.getStart().v())));
//			}
//		}
//		// 勤務終了1に時刻が入力されたか
//		if(!goBackDirectly.getWorkTimeEnd1().isPresent()){
//			// 取得した「勤務種類コード」「就業時間帯コード」をチェックする
//			if(Strings.isNotBlank(workTypeCD) && Strings.isNotBlank(workTimeCD)){
//				// 勤務終了1に(勤務NO=1)の「計算用所定時間設定」．時間帯．終了を入れる
//				goBackDirectly.setWorkTimeEnd1(opTimezoneUse.map(x -> new WorkTimeGoBack(x.getEnd().v())));
//			}
//		}
//		// ドメインモデル「直行直帰申請」の更新する
//		this.goBackDirectlyRepo.update(goBackDirectly);
//		application.setVersion(version);
//		appRepo.updateWithVersion(application);
//		
//		// 暫定データの登録
//		interimRemainDataMngRegisterDateChange.registerDateChange(
//				application.getCompanyID(), 
//				application.getEmployeeID(), 
//				Arrays.asList(application.getAppDate()));
		
		// アルゴリズム「4-2.詳細画面登録後の処理」を実行する
		// error EA refactor 4
		/*return this.detailAfterUpdate.processAfterDetailScreenRegistration(application);*/
		return null;
	}

}

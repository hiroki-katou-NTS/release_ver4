package nts.uk.ctx.at.request.dom.application.gobackdirectly.service;
/*
import java.util.Optional;
import java.util.List;
import java.util.Optional;*/
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttenTimeLateLeaveImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttenTimeParam;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleImport;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.RegisterAtApproveReflectionInfoService_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.after.NewAfterRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.primitive.WorkTimeGoBack;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSetting;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.CheckAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.WorkChangeFlg;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
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
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	@Inject
	DailyAttendanceTimeCaculation dailyAttendanceTimeCaculation;
	@Inject
	ScBasicScheduleAdapter scBasicScheduleAdapter;
	
	@Inject
	private WorkTimeSettingService workTimeSettingService;
	
	@Inject
	private CollectAchievement collectAchievement;
	
	/**
	 * 
	 */
	@Override
	public ProcessResult register(GoBackDirectly goBackDirectly, Application_New application) {
		String employeeID = application.getEmployeeID();
		String workTimeCD = "";
		String workTypeCD = "";
		// ドメインモデル「直行直帰申請共通設定」．勤務の変更をチェックする
		GoBackDirectlyCommonSetting setting = goBackDirectCommonSetRepo.findByCompanyID(application.getCompanyID()).get();
		if(setting.getWorkChangeFlg()==WorkChangeFlg.DECIDECHANGE){
			workTypeCD = goBackDirectly.getWorkTypeCD().map(x -> x.v()).orElse("");
			workTimeCD = goBackDirectly.getSiftCD().map(x -> x.v()).orElse("");
		} else {
			// 実績の取得
			AchievementOutput achievementOutput = collectAchievement.getAchievement(application.getCompanyID(), application.getEmployeeID(), application.getAppDate());
			workTimeCD = achievementOutput.getWorkTime().getWorkTimeCD();
			workTypeCD = achievementOutput.getWorkType().getWorkTypeCode();
		}
		// 取得した「勤務種類コード」「就業時間帯コード」をチェックする
		Optional<TimezoneUse> opTimezoneUse = Optional.empty();
		if(Strings.isNotBlank(workTypeCD) && Strings.isNotBlank(workTimeCD)){
			// 所定時間帯を取得する
			PredetermineTimeSetForCalc predetermineTimeSetForCalc = workTimeSettingService.getPredeterminedTimezone(application.getCompanyID(), workTimeCD, workTypeCD, null); 
			opTimezoneUse = predetermineTimeSetForCalc.getTimezones().stream().filter(x -> x.getWorkNo() == 1).findAny();
		}
		// 勤務開始1に時刻が入力されたか
		if(!goBackDirectly.getWorkTimeStart1().isPresent()){
			// 取得した「勤務種類コード」「就業時間帯コード」をチェックする
			if(Strings.isNotBlank(workTypeCD) && Strings.isNotBlank(workTimeCD)){
				// 勤務開始1に(勤務NO=1)の「計算用所定時間設定」．時間帯．開始を入れる
				goBackDirectly.setWorkTimeStart1(opTimezoneUse.map(x -> new WorkTimeGoBack(x.getStart().v())));
			}
		}
		// 勤務終了1に時刻が入力されたか
		if(!goBackDirectly.getWorkTimeEnd1().isPresent()){
			// 取得した「勤務種類コード」「就業時間帯コード」をチェックする
			if(Strings.isNotBlank(workTypeCD) && Strings.isNotBlank(workTimeCD)){
				// 勤務終了1に(勤務NO=1)の「計算用所定時間設定」．時間帯．終了を入れる
				goBackDirectly.setWorkTimeEnd1(opTimezoneUse.map(x -> new WorkTimeGoBack(x.getEnd().v())));
			}
		}
		//アルゴリズム「直行直帰登録」を実行する		
		goBackDirectRepo.insert(goBackDirectly);
		appRepo.insert(application);
		// 2-2.新規画面登録時承認反映情報の整理
		registerAppReplection.newScreenRegisterAtApproveInfoReflect(employeeID, application);
		
		// 暫定データの登録
		interimRemainDataMngRegisterDateChange.registerDateChange(
				application.getCompanyID(), 
				employeeID, 
				Arrays.asList(application.getAppDate()));
		
		//アルゴリズム「2-3.新規画面登録後の処理」を実行する 
		return newAfterRegister.processAfterRegister(application);
		
	}

	
	@Override
	public void checkBeforRegister(GoBackDirectly goBackDirectly, Application_New application) {
		String companyID = AppContexts.user().companyId();
		GoBackDirectlyCommonSetting goBackCommonSet = goBackDirectCommonSetRepo.findByCompanyID(companyID).get();
		//アルゴリズム「2-1.新規画面登録前の処理」を実行する
		processBeforeRegister.processBeforeRegister(application,0);
		// アルゴリズム「直行直帰するチェック」を実行する - client da duoc check
		// アルゴリズム「直行直帰遅刻早退のチェック」を実行する
		GoBackDirectLateEarlyOuput goBackLateEarly = this.goBackDirectLateEarlyCheck(goBackDirectly, application);
		//直行直帰遅刻早退のチェック
		//TODO: chua the thuc hien duoc nen mac dinh luc nao cung co loi エラーあり
		if(goBackLateEarly.isError) {
			//直行直帰申請共通設定.早退遅刻設定がチェックする
			if(goBackCommonSet.getLateLeaveEarlySettingAtr() == CheckAtr.CHECKREGISTER) {
				this.createThrowMsg("Msg_297", goBackLateEarly.msgLst);
			}else if(goBackCommonSet.getLateLeaveEarlySettingAtr() == CheckAtr.CHECKNOTREGISTER) {
				this.createThrowMsg("Msg_298", goBackLateEarly.msgLst);
			}
		}
	}
	
	public void createThrowMsg(String msgConfirm, List<String> msgLst){
		if(msgLst.size() >= 4){
			throw new BusinessException(msgConfirm, msgLst.get(0), msgLst.get(1), msgLst.get(2), msgLst.get(3));
		} else if (msgLst.size() >= 2){
			throw new BusinessException(msgConfirm, msgLst.get(0), msgLst.get(1));
		} else {
			throw new BusinessException(msgConfirm);
		}
	}
	
	/**
	 *  アルゴリズム「直行直帰するチェック」を実行する
	 */
	@Override
	public GoBackDirectAtr goBackDirectCheck(GoBackDirectly goBackDirectly) {
		if (goBackDirectly.getGoWorkAtr1() == UseAtr.NOTUSE && goBackDirectly.getBackHomeAtr1() == UseAtr.NOTUSE
				&& goBackDirectly.getGoWorkAtr2().get() == UseAtr.NOTUSE
				&& goBackDirectly.getBackHomeAtr2().get() == UseAtr.NOTUSE) {
			return GoBackDirectAtr.NOT;
		} else {
			return GoBackDirectAtr.IS;
		}
	}

	/**
	 * アルゴリズム「直行直帰遅刻早退のチェック」を実行する
	 */
	@Override
	public GoBackDirectLateEarlyOuput goBackDirectLateEarlyCheck(GoBackDirectly goBackDirectly, Application_New application) {
		
		String companyID = AppContexts.user().companyId();
		// ドメインモデル「直行直帰申請共通設定」を取得する
		GoBackDirectLateEarlyOuput output = new GoBackDirectLateEarlyOuput();
		output.isError = false;
		//ドメインモデル「直行直帰申請共通設定」を取得する 
		GoBackDirectlyCommonSetting goBackCommonSet = goBackDirectCommonSetRepo.findByCompanyID(companyID).get();
		// 設定：直行直帰申請共通設定.早退遅刻設定		
		if (goBackCommonSet.getLateLeaveEarlySettingAtr() != CheckAtr.NOTCHECK) {//チェックする
			ScBasicScheduleImport scBasicScheduleImport = scBasicScheduleAdapter.findByID(application.getEmployeeID(), application.getAppDate()).orElse(null);
			// check Valid 1
			CheckValidOutput validOut1 = this.goBackLateEarlyCheckValidity(goBackDirectly, goBackCommonSet, 1, scBasicScheduleImport);
			// check Valid 2
//			CheckValidOutput validOut2 = this.goBackLateEarlyCheckValidity(goBackDirectly, goBackCommonSet, 2);
			// チェック対象１またはチェック対象２がTrueの場合
			if (validOut1.isCheckValid) {
				// アルゴリズム「1日分の勤怠時間を仮計算」を実行する
				//Mac Dinh tra ve 0
				
				//日別実績の勤怠時間.実働時間.総労働時間.早退時間.時間 
				//TODO: chua the lam duoc do chua co 日別実績
				
				DailyAttenTimeParam dailyAttenTimeParam = new DailyAttenTimeParam(
						application.getEmployeeID(), 
						application.getAppDate(), 
						validOut1.workTypeCD, 
						validOut1.siftCd, 
						validOut1.workTimeStart == null ? null : new AttendanceTime(validOut1.workTimeStart.v()), 
						validOut1.workTimeEnd == null ? null : new AttendanceTime(validOut1.workTimeEnd.v()), 
						goBackDirectly.getWorkTimeStart2().map(x -> new AttendanceTime(x.v())).orElse(null), 
						goBackDirectly.getWorkTimeEnd2().map(x -> new AttendanceTime(x.v())).orElse(null));
				DailyAttenTimeLateLeaveImport dailyAttenTimeLateLeaveImport = dailyAttendanceTimeCaculation.calcDailyLateLeave(dailyAttenTimeParam);
				
				// So sách tới 日別実績 để biết đi sớm về muộn, nếu  
				
				if (dailyAttenTimeLateLeaveImport.getLeaveEarlyTime().v() > 0) {
					output.isError = true;
					output.msgLst.add("s=Msg_296");
					output.msgLst.add(goBackDirectly.getWorkTimeEnd1().map(x -> x.v().toString()).orElse(""));
				} 
				if (dailyAttenTimeLateLeaveImport.getLateTime().v() > 0) {
					output.isError = true;
					output.msgLst.add("s=Msg_295");
					output.msgLst.add(goBackDirectly.getWorkTimeStart1().map(x -> x.v().toString()).orElse(""));
				}
			}
		}
		return output;
	}

	/**
	 * アルゴリズム「直行直帰遅刻早退有効チェック」を実行する
	 */
	@Override
	public CheckValidOutput goBackLateEarlyCheckValidity(GoBackDirectly goBackDirectly,
			GoBackDirectlyCommonSetting goBackCommonSet, int line, ScBasicScheduleImport scBasicScheduleImport) {
		WorkTypeCode bsWorkTypeCD = scBasicScheduleImport == null ? new WorkTypeCode("") : new WorkTypeCode(scBasicScheduleImport.getWorkTypeCode());
		WorkTimeCode bsSiftCd = scBasicScheduleImport == null ? new WorkTimeCode("") : new WorkTimeCode(scBasicScheduleImport.getWorkTimeCode());
		WorkTimeGoBack bsWorkTimeStart1 = scBasicScheduleImport == null ? null : new WorkTimeGoBack(scBasicScheduleImport.getScheduleStartClock1());
		WorkTimeGoBack bsWorkTimeEnd1 = scBasicScheduleImport == null ? null : new WorkTimeGoBack(scBasicScheduleImport.getScheduleEndClock1());
		CheckValidOutput result = new CheckValidOutput();
		result.isCheckValid = false;
		// 直行直帰申請共通設定.勤務の変更 (Thay đổi 直行直帰申請共通設定.勤務)
		WorkChangeFlg workChangeFlg = goBackCommonSet.getWorkChangeFlg();
		if(workChangeFlg == WorkChangeFlg.NOTCHANGE){
			result.setWorkTypeCD(bsWorkTypeCD);
			result.setSiftCd(bsSiftCd);
		} else if(workChangeFlg == WorkChangeFlg.CHANGE){
			result.setWorkTypeCD(goBackDirectly.getWorkTypeCD().orElse(bsWorkTypeCD));
			result.setSiftCd(goBackDirectly.getSiftCD().orElse(bsSiftCd));
		} else {
			// 勤務を変更するのチェック状態 (Kiểm tra tình trạng thay đổi worktime)
			if(workChangeFlg == WorkChangeFlg.DECIDECHANGE){
				result.setWorkTypeCD(goBackDirectly.getWorkTypeCD().orElse(bsWorkTypeCD));
				result.setSiftCd(goBackDirectly.getSiftCD().orElse(bsSiftCd));
			} else {
				result.setWorkTypeCD(bsWorkTypeCD);
				result.setSiftCd(bsSiftCd);
			}
		}
		if (line == 1) {
			// MERGE NODE 1
			// 勤務直行の確認
			if (goBackDirectly.getGoWorkAtr1() == UseAtr.USE && goBackDirectly.getWorkTimeStart1() != null) {
				// 入力する
				result.setWorkTimeStart(goBackDirectly.getWorkTimeStart1().orElse(bsWorkTimeStart1));
				result.setCheckValid(true);
			} else {
				result.setWorkTimeStart(bsWorkTimeStart1);
			}
			// 勤務直帰の確認
			if (goBackDirectly.getBackHomeAtr1() == UseAtr.USE && goBackDirectly.getWorkTimeEnd1() != null) {
				result.setWorkTimeEnd(goBackDirectly.getWorkTimeEnd1().orElse(bsWorkTimeEnd1));
				result.setCheckValid(true);
			} else {
				result.setWorkTimeEnd(bsWorkTimeEnd1);
			}
		} else {
			// MERGE NODE 1
			// 勤務直行の確認
			if (goBackDirectly.getGoWorkAtr2().get() == UseAtr.USE && goBackDirectly.getWorkTimeStart2() != null) {
				// 入力する
				result.setCheckValid(true);
			} else {
				result.setWorkTimeStart(null);
			}
			// 勤務直帰の確認
			if (goBackDirectly.getBackHomeAtr2().get() == UseAtr.USE && goBackDirectly.getWorkTimeEnd2() != null) {
				result.setCheckValid(true);
			} else {
				result.setWorkTimeEnd(null);
			}
		}
		return result;
	}
}

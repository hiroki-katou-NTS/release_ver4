package nts.uk.ctx.at.request.dom.application.workchange;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.RegisterAtApproveReflectionInfoService_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.after.NewAfterRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@Transactional
public class WorkChangeRegisterServiceImpl implements IWorkChangeRegisterService {

	@Inject
	private NewBeforeRegister_New newBeforeRegister;

	@Inject
	private RegisterAtApproveReflectionInfoService_New registerService;

	@Inject
	ApplicationApprovalService_New appRepository;

	@Inject
	NewAfterRegister_New newAfterRegister;

	@Inject
	private IAppWorkChangeRepository workChangeRepository;
	
	@Inject 
	private BasicScheduleService basicScheduleService;
	
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	@Inject
	private OtherCommonAlgorithm otherCommonAlg;	
	@Override
	public ProcessResult registerData(AppWorkChange workChange, Application_New app, boolean checkOver1Year) {

		// アルゴリズム「2-1.新規画面登録前の処理」を実行する
		newBeforeRegister.processBeforeRegister(app, 0, checkOver1Year);
		
		// ドメインモデル「勤務変更申請設定」の新規登録をする
		appRepository.insert(app);
		workChangeRepository.add(workChange);
		
		// アルゴリズム「2-2.新規画面登録時承認反映情報の整理」を実行する
		registerService.newScreenRegisterAtApproveInfoReflect(app.getEmployeeID(), app);
		
		// 暫定データの登録
		GeneralDate startDateParam = app.getStartDate().orElse(app.getAppDate());
		GeneralDate endDateParam = app.getEndDate().orElse(app.getAppDate());
		List<GeneralDate> listDate = new ArrayList<>();
		List<GeneralDate> lstHoliday = otherCommonAlg.lstDateIsHoliday(app.getCompanyID(), app.getEmployeeID(), new DatePeriod(startDateParam, endDateParam));
		
		for(GeneralDate loopDate = startDateParam; loopDate.beforeOrEquals(endDateParam); loopDate = loopDate.addDays(1)){
			if(workChange.getExcludeHolidayAtr() == 0
					|| (workChange.getExcludeHolidayAtr() == 1 && !lstHoliday.contains(loopDate))) {
				listDate.add(loopDate);	
			}
		}
		interimRemainDataMngRegisterDateChange.registerDateChange(
				app.getCompanyID(), 
				app.getEmployeeID(), 
				listDate);
		
		// 共通アルゴリズム「2-3.新規画面登録後の処理」を実行する
		return newAfterRegister.processAfterRegister(app);
	}

	@Override
	public void checkWorkHour(AppWorkChange workChange) {
		// 就業時間（開始時刻：終了時刻）
		// 開始時刻 ＞ 終了時刻
		if (workChange.getWorkTimeStart1() > workChange.getWorkTimeEnd1()) {
			// エラーメッセージ(Msg_579)
			// エラーリストにセットする
			throw new BusinessException("Msg_579");
		}
		// 就業時間２（開始時刻：終了時刻）
		// 開始時刻 ＞ 終了時刻
		if (workChange.getWorkTimeStart2() > workChange.getWorkTimeEnd2()) {
			// エラーメッセージ(Msg_580)
			// エラーリストにセットする
			throw new BusinessException("Msg_580");
		}
		// 就業時間：就業時間
		// 就業時間（終了時刻） > 就業時刻２（開始時刻）
		if (workChange.getWorkTimeEnd1() > workChange.getWorkTimeStart2()) {
			// エラーメッセージ(Msg_581)
			// エラーリストにセットする
			throw new BusinessException("Msg_581");
		}
	}

	@Override
	public void checkBreakTime1(AppWorkChange workChange) {
		// 開始時刻 ＞ 終了時刻
		if (workChange.getBreakTimeStart1() > workChange.getBreakTimeEnd1()) {
			// エラーメッセージ(Msg_582)
			// エラーリストにセットする
			throw new BusinessException("Msg_582");
		}
	}

	@Override
	public boolean isTimeRequired(String workTypeCD) {
		SetupType setupType = basicScheduleService.checkNeededOfWorkTimeSetting(workTypeCD);
		if(setupType==SetupType.REQUIRED){
			return true;
		} else {
			return false;
		}
	}
}

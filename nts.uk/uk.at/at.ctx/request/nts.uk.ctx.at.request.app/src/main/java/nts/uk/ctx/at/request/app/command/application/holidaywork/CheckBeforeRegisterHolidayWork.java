package nts.uk.ctx.at.request.app.command.application.holidaywork;

import static java.util.stream.Collectors.groupingBy;

/*import java.lang.reflect.Array;*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.AppOvertimeDetailDto;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeCheckResultDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculationImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.TimeWithCalculationImport;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.IErrorCheckBeforeRegister;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.HolidayThreeProcess;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.IFactoryHolidayWork;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeCheckResult;
import nts.uk.ctx.at.request.dom.application.overtime.service.CaculationTime;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CheckBeforeRegisterHolidayWork {
	@Inject
	private NewBeforeRegister_New newBeforeRegister;
	@Inject
	private IErrorCheckBeforeRegister beforeCheck;
	@Inject
	private IFactoryHolidayWork factoryHolidayWork;
	@Inject
	private HolidayThreeProcess holidayThreeProcess;
	@Inject
	private DailyAttendanceTimeCaculation dailyAttendanceTimeCaculation;

	public OvertimeCheckResultDto CheckBeforeRegister(CreateHolidayWorkCommand command) {
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();

		// Create Application
		Application_New appRoot = factoryHolidayWork.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason(),command.getApplicantSID());

		Integer workClockStart1 = command.getWorkClockStart1() == null ? null : command.getWorkClockStart1().intValue();
		Integer workClockEnd1 = command.getWorkClockEnd1() == null ? null : command.getWorkClockEnd1().intValue();
		Integer workClockStart2 = command.getWorkClockStart2() == null ? null : command.getWorkClockStart2().intValue();
		Integer workClockEnd2 = command.getWorkClockEnd2() == null ? null : command.getWorkClockEnd2().intValue();
		int goAtr1 = command.getGoAtr1() == null ? 0 : command.getGoAtr1().intValue();
		int backAtr1 = command.getBackAtr1() == null ? 0 : command.getBackAtr1().intValue();
		int goAtr2 = command.getGoAtr2() == null ? 0 : command.getGoAtr2().intValue();
		int backAtr2 = command.getBackAtr2() == null ? 0 : command.getBackAtr2().intValue();

		AppHolidayWork holidayWorkDomain = factoryHolidayWork.buildHolidayWork(companyId, appID,
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockStart1, workClockEnd1, workClockStart2,
				workClockEnd2, goAtr1,backAtr1,goAtr2,backAtr2,command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				 command.getOverTimeShiftNight(),
				CheckBeforeRegisterHolidayWork.getHolidayWorkInput(command, companyId, appID), Optional.empty());

		return CheckBeforeRegister(command.getCalculateFlag(), appRoot, holidayWorkDomain);
	}

	public OvertimeCheckResultDto CheckBeforeRegister(int calculateFlg, Application_New app, AppHolidayWork appHolidayWork) {
		// 社員ID
		String employeeId = AppContexts.user().employeeId();
		OvertimeCheckResultDto result = new OvertimeCheckResultDto(0, 0, 0, false, null);
		OvertimeCheckResult res = new OvertimeCheckResult();
		// 2-1.新規画面登録前の処理を実行する
		newBeforeRegister.processBeforeRegister(app,0);
		// 登録前エラーチェック
		// 計算ボタン未クリックチェック
		//03-06_計算ボタンチェック
		beforeCheck.calculateButtonCheck(calculateFlg, app.getCompanyID(), employeeId, 1,
				ApplicationType.BREAK_TIME_APPLICATION, app.getAppDate());
		// 03-01_事前申請超過チェック
		Map<AttendanceType, List<HolidayWorkInput>> findMap = appHolidayWork.getHolidayWorkInputs().stream()
				.collect(groupingBy(HolidayWorkInput::getAttendanceType));
		// Only check for [残業時間]
		// 時間①～フレ超過時間 まで 背景色をピンク
		List<HolidayWorkInput> holidayWorkInputs = findMap.get(AttendanceType.BREAKTIME);
		
		if (holidayWorkInputs != null && !holidayWorkInputs.isEmpty()) {
			res = holidayThreeProcess.preApplicationExceededCheck(app.getCompanyID(), app.getAppDate(),
					app.getInputDate(), app.getPrePostAtr(), AttendanceType.BREAKTIME.value, holidayWorkInputs, app.getEmployeeID());
			if (res.getErrorCode() != 0) {
				result.setErrorCode(res.getErrorCode());
				result.setFrameNo(res.getFrameNo());
				result.setAttendanceId(AttendanceType.BREAKTIME.value);
				return result;
			}
		}
		// 6.計算処理 : TODO
		DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation.getCalculation(employeeId,
				app.getAppDate(),
				appHolidayWork.getWorkTypeCode()== null ? "" : appHolidayWork.getWorkTypeCode().toString(),
				appHolidayWork.getWorkTimeCode() == null ? "" : appHolidayWork.getWorkTimeCode().toString(), 
				appHolidayWork.getWorkClock1().getStartTime() == null ? null : appHolidayWork.getWorkClock1().getStartTime().v(),
				appHolidayWork.getWorkClock1().getEndTime() == null? null: appHolidayWork.getWorkClock1().getEndTime().v(),
				Arrays.asList(100),
				Arrays.asList(200));
		// 03-02_実績超過チェック
		for(CaculationTime breakTime : convertList(holidayWorkInputs)){
			for(Map.Entry<Integer,TimeWithCalculationImport> entry : dailyAttendanceTimeCaculationImport.getHolidayWorkTime().entrySet()){
				if(breakTime.getFrameNo() == entry.getKey()){
					holidayThreeProcess.checkCaculationActualExcess(app.getPrePostAtr().value, ApplicationType.BREAK_TIME_APPLICATION.value, employeeId, appHolidayWork.getCompanyID(), app.getAppDate(), breakTime, 
							appHolidayWork.getWorkTimeCode() == null ?"" : appHolidayWork.getWorkTimeCode().toString(), entry.getValue().getCalTime(), false);
				}
			}
		}
		
		// ３６協定時間上限チェック（月間）
		Optional<AppOvertimeDetail> appOvertimeDetailOtp = beforeCheck.registerHdWorkCheck36TimeLimit(
				app.getCompanyID(), app.getEmployeeID(), app.getAppDate(), appHolidayWork.getHolidayWorkInputs());
		result.setAppOvertimeDetail(AppOvertimeDetailDto.fromDomain(appOvertimeDetailOtp));
		// TODO: ３６協定時間上限チェック（年間）
		beforeCheck.TimeUpperLimitYearCheck();
		// 事前否認チェック
		res = beforeCheck.preliminaryDenialCheck(app.getCompanyID(), app.getEmployeeID(), app.getAppDate(), app.getInputDate(),
				app.getPrePostAtr(),ApplicationType.BREAK_TIME_APPLICATION.value);
		result.setConfirm(res.isConfirm());

		return result;
	}
	
	public OvertimeCheckResultDto checkBeforeUpdate(CreateHolidayWorkCommand command) {

		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();

		// Create Application
		Application_New appRoot = factoryHolidayWork.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason(),command.getApplicantSID());

		Integer workClockStart1 = command.getWorkClockStart1() == null ? null : command.getWorkClockStart1().intValue();
		Integer workClockEnd1 = command.getWorkClockEnd1() == null ? null : command.getWorkClockEnd1().intValue();
		Integer workClockStart2 = command.getWorkClockStart2() == null ? null : command.getWorkClockStart2().intValue();
		Integer workClockEnd2 = command.getWorkClockEnd2() == null ? null : command.getWorkClockEnd2().intValue();
		int goAtr1 = command.getGoAtr1() == null ? 0 : command.getGoAtr1().intValue();
		int backAtr1 = command.getBackAtr1() == null ? 0 : command.getBackAtr1().intValue();
		int goAtr2 = command.getGoAtr2() == null ? 0 : command.getGoAtr2().intValue();
		int backAtr2 = command.getBackAtr2() == null ? 0 : command.getBackAtr2().intValue();

		AppHolidayWork holidayWorkDomain = factoryHolidayWork.buildHolidayWork(companyId, appID,
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockStart1, workClockEnd1, workClockStart2,
				workClockEnd2, goAtr1,backAtr1,goAtr2,backAtr2,command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				 command.getOverTimeShiftNight(),
				CheckBeforeRegisterHolidayWork.getHolidayWorkInput(command, companyId, appID), Optional.empty());
		// 社員ID
		String employeeId = AppContexts.user().employeeId();
		OvertimeCheckResultDto result = new OvertimeCheckResultDto(0, 0, 0, false, null);
		OvertimeCheckResult res = new OvertimeCheckResult();
		int calculateFlg = command.getCalculateFlag();
		// 登録前エラーチェック
		// 計算ボタン未クリックチェック
		beforeCheck.calculateButtonCheck(calculateFlg, appRoot.getCompanyID(), employeeId, 1,
				ApplicationType.BREAK_TIME_APPLICATION, appRoot.getAppDate());
		// 事前申請超過チェック
		Map<AttendanceType, List<HolidayWorkInput>> findMap = holidayWorkDomain.getHolidayWorkInputs().stream()
				.collect(groupingBy(HolidayWorkInput::getAttendanceType));
		// Only check for [残業時間]
		// 時間①～フレ超過時間 まで 背景色をピンク
		List<HolidayWorkInput> holidayWorkInputs = findMap.get(AttendanceType.BREAKTIME);
		
		if (holidayWorkInputs != null && !holidayWorkInputs.isEmpty()) {
			res = holidayThreeProcess.preApplicationExceededCheck(appRoot.getCompanyID(), appRoot.getAppDate(),
					appRoot.getInputDate(), appRoot.getPrePostAtr(), AttendanceType.BREAKTIME.value, holidayWorkInputs, command.getApplicantSID());
			if (res.getErrorCode() != 0) {
				result.setErrorCode(res.getErrorCode());
				result.setFrameNo(res.getFrameNo());
				result.setAttendanceId(AttendanceType.BREAKTIME.value);
				return result;
			}
		}
		
		// 6.計算処理 : TODO
		DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = dailyAttendanceTimeCaculation.getCalculation(employeeId,
						appRoot.getAppDate(),
						holidayWorkDomain.getWorkTypeCode() == null ? ""
								: holidayWorkDomain.getWorkTypeCode().toString(),
						holidayWorkDomain.getWorkTimeCode() == null ? ""
								: holidayWorkDomain.getWorkTimeCode().toString(),
						holidayWorkDomain.getWorkClock1().getStartTime() == null ? null
								: holidayWorkDomain.getWorkClock1().getStartTime().v(),
						holidayWorkDomain.getWorkClock1().getEndTime() == null ? null
								: holidayWorkDomain.getWorkClock1().getEndTime().v(),
								Arrays.asList(100),
								Arrays.asList(200));
		// 03-02_実績超過チェック
		for(CaculationTime breakTime : convertList(holidayWorkInputs)){
			for(Map.Entry<Integer,TimeWithCalculationImport> entry : dailyAttendanceTimeCaculationImport.getHolidayWorkTime().entrySet()){
				if(breakTime.getFrameNo() == entry.getKey()){
					holidayThreeProcess.checkCaculationActualExcess(appRoot.getPrePostAtr().value, ApplicationType.BREAK_TIME_APPLICATION.value, employeeId, holidayWorkDomain.getCompanyID(), appRoot.getAppDate(), breakTime, 
							holidayWorkDomain.getWorkTimeCode() == null ? "" : holidayWorkDomain.getWorkTimeCode().toString(), entry.getValue().getCalTime(), false);
				}
			}
		}
		// ３６上限チェック(詳細)
		Optional<AppOvertimeDetail> appOvertimeDetailOtp = beforeCheck.updateHdWorkCheck36TimeLimit(
				appRoot.getCompanyID(), command.getAppID(), appRoot.getEnteredPersonID(), appRoot.getEmployeeID(),
				command.getApplicationDate(), holidayWorkInputs);
		result.setAppOvertimeDetail(AppOvertimeDetailDto.fromDomain(appOvertimeDetailOtp));
		// TODO: ３６協定時間上限チェック（年間）
		beforeCheck.TimeUpperLimitYearCheck();
		// 事前否認チェック
		res = beforeCheck.preliminaryDenialCheck(appRoot.getCompanyID(), appRoot.getEmployeeID(), appRoot.getAppDate(), appRoot.getInputDate(),
				appRoot.getPrePostAtr(),ApplicationType.BREAK_TIME_APPLICATION.value);
		result.setConfirm(res.isConfirm());

		return result;
	}

	public static List<HolidayWorkInput> getHolidayWorkInput(CreateHolidayWorkCommand command, String Cid, String appId) {
		List<HolidayWorkInput> holidayWorkInputs = new ArrayList<HolidayWorkInput>();
		/**
		 * 休憩時間  ATTENDANCE_ID = 2
		 */
		if (null != command.getBreakTimes()) {
			holidayWorkInputs.addAll(getHolidayWorkInput(command.getBreakTimes(), Cid, appId, AttendanceType.BREAKTIME.value));
		}
		/**
		 * 残業時間 ATTENDANCE_ID = 1
		 */
		if (null != command.getOvertimeHours()) {
			holidayWorkInputs
					.addAll(getHolidayWorkInput(command.getOvertimeHours(), Cid, appId, AttendanceType.NORMALOVERTIME.value));
		}
		/**
		 * 休出時間 ATTENDANCE_ID = 0
		 */
		if (null != command.getRestTime()) {
			holidayWorkInputs
					.addAll(getHolidayWorkInput(command.getRestTime(), Cid, appId, AttendanceType.RESTTIME.value));
		}
		/**
		 * 加給時間 ATTENDANCE_ID = 3
		 */
		if (null != command.getBonusTimes()) {
			holidayWorkInputs.addAll(getHolidayWorkInput(command.getBonusTimes(), Cid, appId, AttendanceType.BONUSPAYTIME.value));
		}
		return holidayWorkInputs;
	}

	private static List<HolidayWorkInput> getHolidayWorkInput(List<HolidayWorkInputCommand> inputCommand, String Cid, String appId,
			int attendanceId) {
		return inputCommand.stream().filter(item -> {
			Integer startTime = item.getStartTime() == null ? null : item.getStartTime().intValue();
			Integer endTime = item.getEndTime() == null ? null : item.getEndTime().intValue();
			int appTime = item.getApplicationTime() == null ? -1 : item.getApplicationTime().intValue();
			return startTime != null || endTime != null || appTime != -1;
		}).map(item -> {
			Integer startTime = item.getStartTime() == null ? null : item.getStartTime().intValue();
			Integer endTime = item.getEndTime() == null ? null : item.getEndTime().intValue();
			int appTime = item.getApplicationTime() == null ? -1 : item.getApplicationTime().intValue();
			return HolidayWorkInput.createSimpleFromJavaType(Cid, appId, attendanceId, item.getFrameNo(), startTime,
					endTime, appTime);
		}).collect(Collectors.toList());
	}
	private static List<CaculationTime> convertList(List<HolidayWorkInput> holidayInput){
		List<CaculationTime> calculations = new ArrayList<>();
		if(CollectionUtil.isEmpty(holidayInput)){
			return calculations;
		}
		for(HolidayWorkInput holidayWork : holidayInput){
			CaculationTime cal = new CaculationTime();
			cal.setAttendanceID(holidayWork.getAttendanceType().value);
			cal.setFrameNo(holidayWork.getFrameNo());
			cal.setApplicationTime(holidayWork.getApplicationTime().v());
			calculations.add(cal);
		}
		return calculations;
	}
}

package nts.uk.ctx.at.request.app.command.application.overtime;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.request.app.find.application.overtime.dto.OvertimeCheckResultDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.IErrorCheckBeforeRegister;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.NewBeforeRegister_New;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeCheckResult;
import nts.uk.ctx.at.request.dom.application.overtime.service.IFactoryOvertime;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CheckBeforeRegisterOvertime {

	@Inject
	private NewBeforeRegister_New newBeforeRegister;
	@Inject
	private IErrorCheckBeforeRegister beforeCheck;
	@Inject
	private IFactoryOvertime factoryOvertime;

	public OvertimeCheckResultDto CheckBeforeRegister(CreateOvertimeCommand command) {
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();

		// Create Application
		Application_New appRoot = factoryOvertime.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason());

		Integer workClockFrom1 = command.getWorkClockFrom1() == null ? null : command.getWorkClockFrom1().intValue();
		Integer workClockTo1 = command.getWorkClockTo1() == null ? null : command.getWorkClockTo1().intValue();
		Integer workClockFrom2 = command.getWorkClockFrom2() == null ? null : command.getWorkClockFrom2().intValue();
		Integer workClockTo2 = command.getWorkClockTo2() == null ? null : command.getWorkClockTo2().intValue();

		AppOverTime overTimeDomain = factoryOvertime.buildAppOverTime(companyId, appID, command.getOvertimeAtr(),
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockFrom1, workClockTo1, workClockFrom2,
				workClockTo2, command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				command.getFlexExessTime(), command.getOverTimeShiftNight(),
				CheckBeforeRegisterOvertime.getOverTimeInput(command, companyId, appID));

		return CheckBeforeRegister(command.getCalculateFlag(), appRoot, overTimeDomain);
	}

	public OvertimeCheckResultDto CheckBeforeRegister(int calculateFlg, Application_New app, AppOverTime overtime) {
		// 社員ID
		String employeeId = AppContexts.user().employeeId();
		OvertimeCheckResultDto result = new OvertimeCheckResultDto(0, 0, 0, false);
		OvertimeCheckResult res = new OvertimeCheckResult();
		// 2-1.新規画面登録前の処理を実行する
		newBeforeRegister.processBeforeRegister(app);
		// 登録前エラーチェック
		// 計算ボタン未クリックチェック
		beforeCheck.calculateButtonCheck(calculateFlg, app.getCompanyID(), employeeId, 1,
				ApplicationType.OVER_TIME_APPLICATION, app.getAppDate());
		// 事前申請超過チェック
		Map<AttendanceType, List<OverTimeInput>> findMap = overtime.getOverTimeInput().stream()
				.collect(groupingBy(OverTimeInput::getAttendanceType));
		// Only check for [残業時間]
		// 時間①～フレ超過時間 まで 背景色をピンク
		List<OverTimeInput> overtimeInputs = findMap.get(AttendanceType.NORMALOVERTIME);
		
		if (overtimeInputs != null && !overtimeInputs.isEmpty()) {
			res = beforeCheck.preApplicationExceededCheck(app.getCompanyID(), app.getAppDate(),
					app.getInputDate(), app.getPrePostAtr(), AttendanceType.NORMALOVERTIME.value, overtimeInputs);
			if (res.getErrorCode() != 0) {
				result.setErrorCode(res.getErrorCode());
				result.setFrameNo(res.getFrameNo());
				result.setAttendanceId(AttendanceType.NORMALOVERTIME.value);
				return result;
			}
		}
		
		// TODO: 実績超過チェック
		beforeCheck.OvercountCheck(app.getCompanyID(), app.getAppDate(), app.getPrePostAtr());
		// TODO: ３６協定時間上限チェック（月間）
		beforeCheck.TimeUpperLimitMonthCheck();
		// TODO: ３６協定時間上限チェック（年間）
		beforeCheck.TimeUpperLimitYearCheck();
		// 事前否認チェック
		res = beforeCheck.preliminaryDenialCheck(app.getCompanyID(), app.getAppDate(), app.getInputDate(),
				app.getPrePostAtr(),ApplicationType.OVER_TIME_APPLICATION.value);
		result.setConfirm(res.isConfirm());

		return result;
	}
	
	public OvertimeCheckResultDto checkBeforeUpdate(CreateOvertimeCommand command) {

		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 申請ID
		String appID = IdentifierUtil.randomUniqueId();

		// Create Application
		Application_New appRoot = factoryOvertime.buildApplication(appID, command.getApplicationDate(),
				command.getPrePostAtr(), command.getApplicationReason(), command.getApplicationReason());

		Integer workClockFrom1 = command.getWorkClockFrom1() == null ? null : command.getWorkClockFrom1().intValue();
		Integer workClockTo1 = command.getWorkClockTo1() == null ? null : command.getWorkClockTo1().intValue();
		Integer workClockFrom2 = command.getWorkClockFrom2() == null ? null : command.getWorkClockFrom2().intValue();
		Integer workClockTo2 = command.getWorkClockTo2() == null ? null : command.getWorkClockTo2().intValue();

		AppOverTime overTimeDomain = factoryOvertime.buildAppOverTime(companyId, appID, command.getOvertimeAtr(),
				command.getWorkTypeCode(), command.getSiftTypeCode(), workClockFrom1, workClockTo1, workClockFrom2,
				workClockTo2, command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator()),
				command.getFlexExessTime(), command.getOverTimeShiftNight(),
				CheckBeforeRegisterOvertime.getOverTimeInput(command, companyId, appID));
		// 社員ID
		String employeeId = AppContexts.user().employeeId();
		OvertimeCheckResultDto result = new OvertimeCheckResultDto(0, 0, 0, false);
		OvertimeCheckResult res = new OvertimeCheckResult();
		int calculateFlg = command.getCalculateFlag();
		// 登録前エラーチェック
		// 計算ボタン未クリックチェック
		beforeCheck.calculateButtonCheck(calculateFlg, appRoot.getCompanyID(), employeeId, 1,
				ApplicationType.OVER_TIME_APPLICATION, appRoot.getAppDate());
		// 事前申請超過チェック
		Map<AttendanceType, List<OverTimeInput>> findMap = overTimeDomain.getOverTimeInput().stream()
				.collect(groupingBy(OverTimeInput::getAttendanceType));
		// Only check for [残業時間]
		// 時間①～フレ超過時間 まで 背景色をピンク
		List<OverTimeInput> overtimeInputs = findMap.get(AttendanceType.NORMALOVERTIME);
		
		if (overtimeInputs != null && !overtimeInputs.isEmpty()) {
			res = beforeCheck.preApplicationExceededCheck(appRoot.getCompanyID(), appRoot.getAppDate(),
					appRoot.getInputDate(), appRoot.getPrePostAtr(), AttendanceType.NORMALOVERTIME.value, overtimeInputs);
			if (res.getErrorCode() != 0) {
				result.setErrorCode(res.getErrorCode());
				result.setFrameNo(res.getFrameNo());
				result.setAttendanceId(AttendanceType.NORMALOVERTIME.value);
				return result;
			}
		}
		
		// TODO: 実績超過チェック
		beforeCheck.OvercountCheck(appRoot.getCompanyID(), appRoot.getAppDate(), appRoot.getPrePostAtr());
		// TODO: ３６協定時間上限チェック（月間）
		beforeCheck.TimeUpperLimitMonthCheck();
		// TODO: ３６協定時間上限チェック（年間）
		beforeCheck.TimeUpperLimitYearCheck();
		// 事前否認チェック
		res = beforeCheck.preliminaryDenialCheck(appRoot.getCompanyID(), appRoot.getAppDate(), appRoot.getInputDate(),
				appRoot.getPrePostAtr(),ApplicationType.OVER_TIME_APPLICATION.value);
		result.setConfirm(res.isConfirm());

		return result;
	}

	public static List<OverTimeInput> getOverTimeInput(CreateOvertimeCommand command, String Cid, String appId) {
		List<OverTimeInput> overTimeInputs = new ArrayList<OverTimeInput>();
		/**
		 * 休憩時間  ATTENDANCE_ID = 2
		 */
		if (null != command.getBreakTimes()) {
			overTimeInputs.addAll(getOverTimeInput(command.getBreakTimes(), Cid, appId, AttendanceType.BREAKTIME.value));
		}
		/**
		 * 残業時間 ATTENDANCE_ID = 1
		 */
		if (null != command.getOvertimeHours()) {
			overTimeInputs
					.addAll(getOverTimeInput(command.getOvertimeHours(), Cid, appId, AttendanceType.NORMALOVERTIME.value));
		}
		/**
		 * 休出時間 ATTENDANCE_ID = 0
		 */
		if (null != command.getRestTime()) {
			overTimeInputs
					.addAll(getOverTimeInput(command.getRestTime(), Cid, appId, AttendanceType.RESTTIME.value));
		}
		/**
		 * 加給時間 ATTENDANCE_ID = 3
		 */
		if (null != command.getBonusTimes()) {
			overTimeInputs.addAll(getOverTimeInput(command.getBonusTimes(), Cid, appId, AttendanceType.BONUSPAYTIME.value));
		}
		return overTimeInputs;
	}

	private static List<OverTimeInput> getOverTimeInput(List<OvertimeInputCommand> inputCommand, String Cid, String appId,
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
			return OverTimeInput.createSimpleFromJavaType(Cid, appId, attendanceId, item.getFrameNo(), startTime,
					endTime, appTime, item.getTimeItemTypeAtr());
		}).collect(Collectors.toList());
	}
}

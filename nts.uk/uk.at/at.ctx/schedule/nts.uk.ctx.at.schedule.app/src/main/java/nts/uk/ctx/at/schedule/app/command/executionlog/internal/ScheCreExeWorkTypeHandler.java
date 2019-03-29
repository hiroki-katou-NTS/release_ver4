/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog.internal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.app.command.executionlog.CreateScheduleMasterCache;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.app.command.executionlog.WorkCondItemDto;
import nts.uk.ctx.at.schedule.dom.adapter.employmentstatus.EmploymentInfoImported;
import nts.uk.ctx.at.schedule.dom.employeeinfo.TimeZoneScheduledMasterAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.DateRegistedEmpSche;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.BasicWorkSetting;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * The Class ScheCreExeWorkTypeHandler.
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class ScheCreExeWorkTypeHandler {

	/** The sche cre exe basic work setting handler. */
	@Inject
	private ScheCreExeBasicWorkSettingHandler scheCreExeBasicWorkSettingHandler;

	/** The sche cre exe work time handler. */
	@Inject
	private ScheCreExeWorkTimeHandler scheCreExeWorkTimeHandler;

	/** The sche cre exe error log handler. */
	@Inject
	private ScheCreExeErrorLogHandler scheCreExeErrorLogHandler;

	/** The basic schedule service. */
	@Inject
	private BasicScheduleService basicScheduleService;

	/** The sche cre exe basic schedule handler. */
	@Inject
	private ScheCreExeBasicScheduleHandler scheCreExeBasicScheduleHandler;

	/** The Constant FIRST_DATA. */
	public static final int FIRST_DATA = 0;

	/**
	 * Creates the work schedule.
	 * 
	 * 営業日カレンダーで勤務予定を作成する
	 * 
	 * @param command
	 * @param workingConditionItem
	 * @param empGeneralInfo
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createWorkSchedule(
			ScheduleCreatorExecutionCommand command,
			GeneralDate dateInPeriod,
			WorkCondItemDto workingConditionItem,
			CreateScheduleMasterCache masterCache,
			List<BasicSchedule> listBasicSchedule,
			DateRegistedEmpSche dateRegistedEmpSche) {

		// 登録前削除区分をTrue（削除する）とする
		// command.setIsDeleteBeforInsert(true); FIX BUG #87113

		// setup command getter
		WorkTypeGetterCommand commandWorktypeGetter = new WorkTypeGetterCommand();
		commandWorktypeGetter.setBaseGetter(command.toBaseCommand(dateInPeriod));
		commandWorktypeGetter.setEmployeeId(workingConditionItem.getEmployeeId());
		if (workingConditionItem.getScheduleMethod().isPresent()
				&& workingConditionItem.getScheduleMethod().get().getWorkScheduleBusCal().isPresent()) {
			commandWorktypeGetter.setReferenceBasicWork(workingConditionItem.getScheduleMethod().get()
					.getWorkScheduleBusCal().get().getReferenceBasicWork().value);
		}
		if (workingConditionItem.getScheduleMethod().isPresent()
				&& workingConditionItem.getScheduleMethod().get().getWorkScheduleBusCal().isPresent()) {
			commandWorktypeGetter.setReferenceBusinessDayCalendar(workingConditionItem.getScheduleMethod().get()
					.getWorkScheduleBusCal().get().getReferenceBusinessDayCalendar().value);
			commandWorktypeGetter.setReferenceWorkingHours(workingConditionItem.getScheduleMethod().get()
					.getWorkScheduleBusCal().get().getReferenceWorkingHours().value);
		}

		// 勤務種類を取得する(lấy dữ liệu worktype)
		Optional<WorktypeDto> optWorktype = this.getWorktype(commandWorktypeGetter, masterCache);

		if (optWorktype.isPresent()) {
			WorkTimeGetterCommand commandWorkTimeGetter = commandWorktypeGetter.toWorkTime();
			commandWorkTimeGetter.setWorkTypeCode(optWorktype.get().getWorktypeCode());
			// 就業時間帯を取得する(lấy dữ liệu worktime)
			Pair<Boolean, Optional<String>> pair = this.scheCreExeWorkTimeHandler.getWorktime(commandWorkTimeGetter, masterCache);
			// neu pair.getKey() == false nghia la khong tim duoc worktimeCode,
			// da ghi errorLog, dung xu ly hien tai, chuyen sang ngay ke tiep
			if (pair.getKey()) {
				// update all basic schedule
				this.scheCreExeBasicScheduleHandler.updateAllDataToCommandSave(command, dateInPeriod,
						workingConditionItem.getEmployeeId(), optWorktype.get(),
						pair.getValue().isPresent() ? pair.getValue().get() : null, masterCache, listBasicSchedule,
						dateRegistedEmpSche);
			}
		}
	}

	/**
	 * Gets the worktype code in office.
	 * 
	 * 在職の勤務種類コードを返す
	 * 
	 * @param command
	 * @param listWorkingConItem
	 * @return
	 */
	private String getWorktypeCodeInOffice(WorkTypeByEmpStatusGetterCommand command,
			List<WorkCondItemDto> listWorkingConItem) {

		// check default work time code
		if (this.scheCreExeWorkTimeHandler.checkNullOrDefaulCode(command.getWorkingCode())) {
			// return work type code of command
			return command.getWorkTypeCode();
		} else {
			// EA修正履歴 No1834
			Optional<WorkCondItemDto> optionalWorkingConditionItem = listWorkingConItem.stream()
					.filter(x -> x.getDatePeriod().contains(command.getBaseGetter().getToDate())
							&& command.getEmployeeId().equals(x.getEmployeeId()))
					.findFirst();

			// check not exits data
			if (!optionalWorkingConditionItem.isPresent()) {
				// return work type code of command
				return command.getWorkTypeCode();
			}

			// find work time code by day of week
			String worktimeCode = this.scheCreExeWorkTimeHandler.getWorkTimeCodeOfDayOfWeekPersonalCondition(
					command.toWorktimeConvert(), optionalWorkingConditionItem.get());

			// check default work type code
			if (!this.scheCreExeWorkTimeHandler.checkNullOrDefaulCode(worktimeCode)) {
				return command.getWorkTypeCode();
			}

			// return work type code by holiday time
			return optionalWorkingConditionItem.get().getWorkCategory().getHolidayTime().getWorkTypeCode().isPresent()
					? optionalWorkingConditionItem.get().getWorkCategory().getHolidayTime().getWorkTypeCode().get().v()
					: null;
		}

	}

	/**
	 * Gets the worktype code leave holiday type.
	 * 
	 * Có 2 xử lý tên giống nhau
	 * đều là 「休業休職の勤務種類コードを返す」
	 * nhưng xử lý khác nhau
	 * 1 cái là xử lý của KSC001
	 * 1 cái là xử lý chung bên context share 
	 * 
	 * @param command
	 * @param optEmploymentInfo
	 * @param listWorkingConItem
	 * @return
	 */
	private String getWorktypeCodeLeaveHolidayType(WorkTypeByEmpStatusGetterCommand command,
			Optional<EmploymentInfoImported> optEmploymentInfo, List<WorkCondItemDto> listWorkingConItem) {
		String employeeId = command.getEmployeeId(); 
		String workTypeCode = command.getWorkTypeCode(); 
		GeneralDate day = command.getBaseGetter().getToDate();
		Optional<WorkCondItemDto> optWorkCondItemDto = listWorkingConItem.stream()
				.filter(x -> (x.getEmployeeId().equals(employeeId)
						&& x.getDatePeriod().contains(day)))
				.findFirst();
		if(!optWorkCondItemDto.isPresent()){
			// Input「勤務種類コード」を返す
			return workTypeCode;
		} 
		WorkCondItemDto workCondItemDto = optWorkCondItemDto.get();
		Optional<WorkingConditionItem> optWorkingConditionItem = Optional.of(new WorkingConditionItem(
				workCondItemDto.getHistoryId(),
				workCondItemDto.getScheduleManagementAtr(),
				workCondItemDto.getWorkDayOfWeek(),
				workCondItemDto.getWorkCategory(),
				workCondItemDto.getAutoStampSetAtr(),
				workCondItemDto.getAutoIntervalSetAtr(),
				workCondItemDto.getEmployeeId(),
				workCondItemDto.getVacationAddedTimeAtr(),
				workCondItemDto.getContractTime(),
				workCondItemDto.getLaborSystem(),
				workCondItemDto.getHolidayAddTimeSet().orElse(null),
				workCondItemDto.getScheduleMethod().orElse(null),
				Integer.valueOf(workCondItemDto.getHourlyPaymentAtr().value),
				workCondItemDto.getTimeApply().orElse(null),
				workCondItemDto.getMonthlyPattern().orElse(null)));
		
		String workTypeCd = this.basicScheduleService.getWorktypeCodeLeaveHolidayType(
				command.getBaseGetter().getCompanyId(), employeeId, day, workTypeCode,
				optEmploymentInfo.get().getTempAbsenceFrNo().get().intValue(), optWorkingConditionItem);
		// 取得した勤務種類コードをチェック
		if(workTypeCd == null){
			// add message error log 601
			this.scheCreExeErrorLogHandler.addError(command.getBaseGetter(), employeeId, "Msg_601");
		}
		
		return workTypeCd;
	}

	/**
	 * Convert worktype code by day of week personal.
	 * 
	 * 個人曜日別と在職状態から「勤務種類コード」を変換する
	 * 
	 * @param command
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 * @return
	 */
	private String convertWorktypeCodeByDayOfWeekPersonal(WorkTypeByEmpStatusGetterCommand command,
			Map<String, List<EmploymentInfoImported>> mapEmploymentStatus, List<WorkCondItemDto> listWorkingConItem) {

		// EA No1690
		List<EmploymentInfoImported> listEmploymentInfo = mapEmploymentStatus.get(command.getEmployeeId());
		Optional<EmploymentInfoImported> optEmploymentInfo = Optional.empty();
		if (listEmploymentInfo != null) {
			optEmploymentInfo = listEmploymentInfo.stream().filter(
					employmentInfo -> employmentInfo.getStandardDate().equals(command.getBaseGetter().getToDate()))
					.findFirst();
		}
		// Khong co truong hop k lay duoc thong tin nhan vien
		// van check isPresent cho dung luong tam!
		if (!optEmploymentInfo.isPresent()) {
			return null;
		}

		// employment status is INCUMBENT
		if (optEmploymentInfo.get().getEmploymentState() == ScheCreExeWorkTimeHandler.INCUMBENT) {
			return this.getWorktypeCodeInOffice(command, listWorkingConItem);
		}

		// employment status is HOLIDAY or LEAVE_OF_ABSENCE
		if (optEmploymentInfo.get().getEmploymentState() == ScheCreExeWorkTimeHandler.HOLIDAY
				|| optEmploymentInfo.get().getEmploymentState() == ScheCreExeWorkTimeHandler.LEAVE_OF_ABSENCE) {
			return this.getWorktypeCodeLeaveHolidayType(command, optEmploymentInfo, listWorkingConItem);
		}

		return ScheCreExeWorkTimeHandler.DEFAULT_CODE;
	}

	/**
	 * Convert worktype code by working status.
	 * 
	 * 在職状態から「勤務種類コード」を変換する
	 * 
	 * @param command
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 * @return
	 */
	private String convertWorktypeCodeByWorkingStatus(WorkTypeByEmpStatusGetterCommand command,
			Map<String, List<EmploymentInfoImported>> mapEmploymentStatus, List<WorkCondItemDto> listWorkingConItem) {

		// EA No1685
		List<EmploymentInfoImported> listEmploymentInfo = mapEmploymentStatus.get(command.getEmployeeId());
		Optional<EmploymentInfoImported> optEmploymentInfo = Optional.empty();
		if (listEmploymentInfo != null) {
			optEmploymentInfo = listEmploymentInfo.stream().filter(
					employmentInfo -> employmentInfo.getStandardDate().equals(command.getBaseGetter().getToDate()))
					.findFirst();
		}

		if (!optEmploymentInfo.isPresent())
			return null;

		// employment status is 在職
		if (optEmploymentInfo.get().getEmploymentState() == ScheCreExeWorkTimeHandler.INCUMBENT) {
			return command.getWorkTypeCode();
		}

		// employment status is 休業 or 休職
		if (optEmploymentInfo.get().getEmploymentState() == ScheCreExeWorkTimeHandler.HOLIDAY
				|| optEmploymentInfo.get().getEmploymentState() == ScheCreExeWorkTimeHandler.LEAVE_OF_ABSENCE) {
			return this.getWorktypeCodeLeaveHolidayType(command, optEmploymentInfo, listWorkingConItem);
		}
		return ScheCreExeWorkTimeHandler.DEFAULT_CODE;
	}

	/**
	 * 勤務種類を取得する
	 * 
	 * Gets the worktype.
	 * 
	 * @param command
	 * @param empGeneralInfo
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 * @param listWorkType
	 * @return
	 */
	public Optional<WorktypeDto> getWorktype(WorkTypeGetterCommand command, CreateScheduleMasterCache masterCache) {

		// setup command getter
		BasicWorkSettingGetterCommand commandBasicGetter = command.toBasicWorkSetting();

		// get basic work setting.
		// 「基本勤務設定」を取得する
		Optional<BasicWorkSetting> optionalBasicWorkSetting = this.scheCreExeBasicWorkSettingHandler
				.getBasicWorkSetting(commandBasicGetter, masterCache.getEmpGeneralInfo());

		if (optionalBasicWorkSetting.isPresent()) {
			// setup command employment status getter
			WorkTypeByEmpStatusGetterCommand commandWorkTypeEmploymentStatus = command.toWorkTypeEmploymentStatus();

			// get basic work setting by optional
			BasicWorkSetting basicWorkSetting = optionalBasicWorkSetting.get();

			// set working code to command
			commandWorkTypeEmploymentStatus.setWorkingCode(
					basicWorkSetting.getWorkingCode() == null ? null : basicWorkSetting.getWorkingCode().v());

			// set work type code to command
			commandWorkTypeEmploymentStatus.setWorkTypeCode(basicWorkSetting.getWorktypeCode().v());
			// 在職状態に対応する「勤務種類コード」を取得する(lâý thông tin worktype code theo trạng thái làm việc(在職状態))
			return this.getWorkTypeByEmploymentStatus(commandWorkTypeEmploymentStatus, masterCache);
		}

		return Optional.empty();
	}

	/**
	 * Gets the work type by employment status.
	 * 
	 * 在職状態に対応する「勤務種類コード」を取得する
	 * 
	 * @param command
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 * @return
	 */
	public Optional<WorktypeDto> getWorkTypeByEmploymentStatus(
			WorkTypeByEmpStatusGetterCommand command,
			CreateScheduleMasterCache masterCache) {
		
		String workTypeCd = null;
		// check 就業時間帯の参照先 == 個人曜日別
		if (command.getReferenceWorkingHours() == TimeZoneScheduledMasterAtr.PERSONAL_DAY_OF_WEEK.value) {
			workTypeCd = this.convertWorktypeCodeByDayOfWeekPersonal(command, masterCache.getMapEmploymentStatus(), masterCache.getListWorkingConItem());
		} else {
			// マスタ参照区分に従う、個人勤務日別
			workTypeCd = this.convertWorktypeCodeByWorkingStatus(command, masterCache.getMapEmploymentStatus(), masterCache.getListWorkingConItem());
		}
		
		if(workTypeCd == null) return Optional.empty();

		// get work type by code
		// EA No2019
		// EA No2021
		// 勤務種類一覧から変換した勤務種類コードと一致する情報を取得する
		final String workTypeCode = workTypeCd;
		Optional<WorkType> optionalWorktype = masterCache.getListWorkType().stream()
				.filter(x -> (x.getCompanyId().equals(command.getBaseGetter().getCompanyId())
						&& x.getWorkTypeCode().toString().equals(workTypeCode)))
				.findFirst();

		if (optionalWorktype.isPresent()
				&& optionalWorktype.get().getDeprecate() == DeprecateClassification.NotDeprecated) {
			return Optional.of(new WorktypeDto(workTypeCd, optionalWorktype.get().getWorkTypeSet()));
		} else {
			// add error log message 590
			this.scheCreExeErrorLogHandler.addError(command.getBaseGetter(), command.getEmployeeId(), "Msg_590");
		}
		return Optional.empty();
	}
}

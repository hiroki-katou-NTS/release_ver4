/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog.internal;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.EmploymentStatusDto;
import nts.uk.ctx.at.schedule.dom.employeeinfo.TimeZoneScheduledMasterAtr;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.BasicWorkSetting;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workingcondition.PersonalWorkCategory;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;

/**
 * The Class ScheCreExeWorkTypeHandler.
 */
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
	
	/** The work type repository. */
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	/** The sche cre exe basic schedule handler. */
	@Inject
	private ScheCreExeBasicScheduleHandler scheCreExeBasicScheduleHandler;
	
	/** The Constant FIRST_DATA. */
	public static final int FIRST_DATA = 0;
	
	
	/**
	 * Creates the work schedule.
	 *
	 * @param command the command
	 * @param workingConditionItem the personal work schedule cre set
	 */
	// 営業日カレンダーで勤務予定を作成する
	public void createWorkSchedule(ScheduleCreatorExecutionCommand command,
			WorkingConditionItem workingConditionItem) {

		// 登録前削除区分をTrue（削除する）とする
		//command.setIsDeleteBeforInsert(true); FIX BUG #87113

		// setup command getter
		WorkTypeGetterCommand commandWorktypeGetter = new WorkTypeGetterCommand();
		commandWorktypeGetter.setBaseGetter(command.toBaseCommand());
		commandWorktypeGetter.setEmployeeId(workingConditionItem.getEmployeeId());
		if (workingConditionItem.getScheduleMethod().isPresent() && workingConditionItem
				.getScheduleMethod().get().getWorkScheduleBusCal().isPresent()) {
			commandWorktypeGetter.setReferenceBasicWork(workingConditionItem.getScheduleMethod()
					.get().getWorkScheduleBusCal().get().getReferenceBasicWork().value);
		}
		if (workingConditionItem.getScheduleMethod().isPresent() && workingConditionItem
				.getScheduleMethod().get().getWorkScheduleBusCal().isPresent()) {
			commandWorktypeGetter
					.setReferenceBusinessDayCalendar(workingConditionItem.getScheduleMethod().get()
							.getWorkScheduleBusCal().get().getReferenceBusinessDayCalendar().value);
			commandWorktypeGetter.setReferenceWorkingHours(
					workingConditionItem.getScheduleMethod().get().getWorkScheduleBusCal().get().getReferenceWorkingHours().value);
		}
		
		
		Optional<WorktypeDto> optWorktype = this.getWorktype(commandWorktypeGetter);

		if (optWorktype.isPresent()) {
			WorkTimeGetterCommand commandWorkTimeGetter = commandWorktypeGetter.toWorkTime();
			commandWorkTimeGetter.setWorkTypeCode(optWorktype.get().getWorktypeCode());	
			Optional<String> optionalWorkTime = this.scheCreExeWorkTimeHandler.getWorktime(commandWorkTimeGetter);

			if (optionalWorkTime == null || optionalWorkTime.isPresent()) {
				// update all basic schedule
				this.scheCreExeBasicScheduleHandler.updateAllDataToCommandSave(command,
						workingConditionItem.getEmployeeId(), optWorktype.get(),
						optionalWorkTime == null ? null : optionalWorkTime.get());
			}

		}
	}
	
	/**
	 * Gets the worktype code in office.
	 *
	 * @param command the command
	 * @return the worktype code in office
	 */
	// 在職の勤務種類コードを返す
	private String getWorktypeCodeInOffice(WorkTypeByEmpStatusGetterCommand command) {

		// check default work time code
		if (this.scheCreExeWorkTimeHandler.checkNullOrDefaulCode(command.getWorkingCode())) {

			// return work type code of command
			return command.getWorkTypeCode();
		} else {

			// get working condition item
			Optional<WorkingConditionItem> optionalWorkingConditionItem = this.scheCreExeWorkTimeHandler
					.getLaborConditionItem(command.getBaseGetter().getCompanyId(), command.getEmployeeId(),
							command.getBaseGetter().getToDate());
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
			return optionalWorkingConditionItem.get().getWorkCategory().getHolidayTime().getWorkTypeCode().v();
		}

	}
	
	/**
	 * Gets the worktype code leave holiday type.
	 *
	 * @param command the command
	 * @param employmentStatus the employment status
	 * @return the worktype code leave holiday type
	 */
	// 休業休職の勤務種類コードを返す
	private String getWorktypeCodeLeaveHolidayType(WorkTypeByEmpStatusGetterCommand command,
			EmploymentStatusDto employmentStatus) {

		// get work style by work type code
		WorkStyle workStyle = this.basicScheduleService.checkWorkDay(command.getWorkTypeCode());

		// is one day rest
		if (workStyle.equals(WorkStyle.ONE_DAY_REST)) {
			return command.getWorkTypeCode();
		}
		// find work type
		WorkType worktype = this.workTypeRepository
				.findByPK(command.getBaseGetter().getCompanyId(), command.getWorkTypeCode()).get();

		if (this.scheCreExeWorkTimeHandler.checkHolidayWork(worktype.getDailyWork())) {
			// 休日出勤

			Optional<WorkingConditionItem> optionalWorkingConditionItem = this.scheCreExeWorkTimeHandler
					.getLaborConditionItem(command.getBaseGetter().getCompanyId(), command.getEmployeeId(),
							command.getBaseGetter().getToDate());
			// check not exits data
			if (!optionalWorkingConditionItem.isPresent()) {
				return command.getWorkTypeCode();
			}
			return optionalWorkingConditionItem.get().getWorkCategory().getHolidayTime().getWorkTypeCode().v();
		} else {

			// find work type set by close atr employment status
			List<WorkTypeSet> worktypeSets = this.workTypeRepository.findWorkTypeSetCloseAtr(
					command.getBaseGetter().getCompanyId(), employmentStatus.getLeaveHolidayType());

			// check empty work type set
			if (CollectionUtil.isEmpty(worktypeSets)) {

				// add message error log 601
				this.scheCreExeErrorLogHandler.addError(command.getBaseGetter(), command.getEmployeeId(), "Msg_601");
				return ScheCreExeWorkTimeHandler.DEFAULT_CODE;
			}
			return worktypeSets.get(FIRST_DATA).getWorkTypeCd().v();
		}
	}

	/**
	 * Convert worktype code by day of week personal.
	 *
	 * @param command the command
	 * @return the string
	 */
	// 個人曜日別と在職状態から「勤務種類コード」を変換する
	private String convertWorktypeCodeByDayOfWeekPersonal(WorkTypeByEmpStatusGetterCommand command) {
		
		// get employment status
		EmploymentStatusDto employmentStatus = this.scheCreExeWorkTimeHandler
				.getStatusEmployment(command.getEmployeeId(), command.getBaseGetter().getToDate());

		// employment status is INCUMBENT
		if (employmentStatus.getStatusOfEmployment() == ScheCreExeWorkTimeHandler.INCUMBENT) {
			return this.getWorktypeCodeInOffice(command);
		}

		// employment status is HOLIDAY or LEAVE_OF_ABSENCE
		if (employmentStatus.getStatusOfEmployment() == ScheCreExeWorkTimeHandler.HOLIDAY
				|| employmentStatus
						.getStatusOfEmployment() == ScheCreExeWorkTimeHandler.LEAVE_OF_ABSENCE) {
			return this.getWorktypeCodeLeaveHolidayType(command, employmentStatus);
		}
		return ScheCreExeWorkTimeHandler.DEFAULT_CODE;
	}
	
	

	/**
	 * Convert worktype code by working status.
	 *
	 * @param command the command
	 * @return the string
	 */
	// 在職状態から「勤務種類コード」を変換する
	private String convertWorktypeCodeByWorkingStatus(WorkTypeByEmpStatusGetterCommand command) {

		// get employment status
		EmploymentStatusDto employmentStatus = this.scheCreExeWorkTimeHandler
				.getStatusEmployment(command.getEmployeeId(), command.getBaseGetter().getToDate());

		// employment status is 在職
		if (employmentStatus.getStatusOfEmployment() == ScheCreExeWorkTimeHandler.INCUMBENT) {
			return command.getWorkTypeCode();
		}

		// employment status is 休業 or 休職
		if (employmentStatus.getStatusOfEmployment() == ScheCreExeWorkTimeHandler.HOLIDAY
				|| employmentStatus.getStatusOfEmployment() == ScheCreExeWorkTimeHandler.LEAVE_OF_ABSENCE) {
			return this.getWorktypeCodeLeaveHolidayType(command, employmentStatus);
		}
		return ScheCreExeWorkTimeHandler.DEFAULT_CODE;
	}
	
	/**
	 * Gets the worktype.
	 *
	 * @param command the command
	 * @return the worktype
	 */
	// 勤務種類を取得する
	private Optional<WorktypeDto> getWorktype(WorkTypeGetterCommand command) {

		// setup command getter
		BasicWorkSettingGetterCommand commandBasicGetter = command.toBasicWorkSetting();
		
		// get basic work setting.
		Optional<BasicWorkSetting> optionalBasicWorkSetting = this.scheCreExeBasicWorkSettingHandler
				.getBasicWorkSetting(commandBasicGetter);

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
			return this.getWorkTypeByEmploymentStatus(commandWorkTypeEmploymentStatus);
		}

		return Optional.empty();
	}
	
	/**
	 * Gets the work type by employment status.
	 *
	 * @param command the command
	 * @return the work type by employment status
	 */
	// 在職状態に対応する「勤務種類コード」を取得する
	private Optional<WorktypeDto> getWorkTypeByEmploymentStatus(WorkTypeByEmpStatusGetterCommand command) {
		String worktypeCode = null;
		// check 就業時間帯の参照先  == 個人曜日別
		if (command.getReferenceWorkingHours() == TimeZoneScheduledMasterAtr.PERSONAL_DAY_OF_WEEK.value) {
			worktypeCode = this.convertWorktypeCodeByDayOfWeekPersonal(command);
		} else
		// マスタ参照区分に従う、個人勤務日別
		{
			worktypeCode = this.convertWorktypeCodeByWorkingStatus(command);
		}

		// get work type by code
		Optional<WorkType> optionalWorktype = this.workTypeRepository.findByPK(command.getBaseGetter().getCompanyId(),
				worktypeCode);

		if (optionalWorktype.isPresent()
				&& optionalWorktype.get().getDeprecate() == DeprecateClassification.NotDeprecated) {
			return Optional.of(new WorktypeDto(worktypeCode, optionalWorktype.get().getWorkTypeSet()));
		} else {
			// add error log message 590
			this.scheCreExeErrorLogHandler.addError(command.getBaseGetter(), command.getEmployeeId(), "Msg_590");
		}
		return Optional.empty();
	}

}

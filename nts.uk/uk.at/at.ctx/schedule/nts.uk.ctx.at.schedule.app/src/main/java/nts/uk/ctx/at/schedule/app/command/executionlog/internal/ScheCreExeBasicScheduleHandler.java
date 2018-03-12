/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.app.command.processbatch.ScheBatchCorrectSetCheckSaveCommand;
import nts.uk.ctx.at.schedule.app.command.schedule.basicschedule.BasicScheduleSaveCommand;
import nts.uk.ctx.at.schedule.app.command.schedule.basicschedule.ChildCareScheduleSaveCommand;
import nts.uk.ctx.at.schedule.app.command.schedule.basicschedule.WorkScheduleBreakSaveCommand;
import nts.uk.ctx.at.schedule.dom.adapter.ScTimeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.ScTimeImport;
import nts.uk.ctx.at.schedule.dom.adapter.ScTimeParam;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.ScShortWorkTimeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.ShortChildCareFrameDto;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.ShortWorkTimeDto;
import nts.uk.ctx.at.schedule.dom.schedule.algorithm.BusinessDayCal;
import nts.uk.ctx.at.schedule.dom.schedule.algorithm.CreScheWithBusinessDayCalService;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.ConfirmedAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletime.WorkScheduleTime;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.BounceAtr;
import nts.uk.ctx.at.schedule.dom.schedule.commonalgorithm.ScheduleMasterInformationDto;
import nts.uk.ctx.at.schedule.dom.schedule.commonalgorithm.ScheduleMasterInformationRepository;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfo;
import nts.uk.ctx.at.shared.app.command.worktime.predset.dto.PrescribedTimezoneSettingDto;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSetCheck;

/**
 * The Class ScheCreExeBasicScheduleHandler.
 */
@Stateless
public class ScheCreExeBasicScheduleHandler {
	
	/** The sc short work time adapter. */
	@Inject
	private ScShortWorkTimeAdapter scShortWorkTimeAdapter;
	
	/** The basic schedule repository. */
	@Inject
	private BasicScheduleRepository basicScheduleRepository;
	
	/** The sche cre exe error log handler. */
	@Inject
	private ScheCreExeErrorLogHandler scheCreExeErrorLogHandler;
	
	/** The sche cre exe work time handler. */
	@Inject
	private ScheCreExeWorkTimeHandler scheCreExeWorkTimeHandler;
	
	/** The work type repository. */
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private ScheduleMasterInformationRepository scheduleMasterInformationRepo;
	
	@Inject
	private CreScheWithBusinessDayCalService scheWithBusinessDayCalService;
	
	@Inject 
	private ScTimeAdapter scTimeAdapter;
	
	/** The Constant DEFAULT_VALUE. */
	private static final int DEFAULT_VALUE = 0;
		
	/**
	 * Update all data to command save.
	 *
	 * @param command the command
	 * @param employeeId the employee id
	 * @param worktypeDto the worktype code
	 * @param workTimeCode the work time code
	 */
	public void updateAllDataToCommandSave(ScheduleCreatorExecutionCommand command, String employeeId,
			WorktypeDto worktypeDto, String workTimeCode) {

		// get short work time
		Optional<ShortWorkTimeDto> optionalShortTime = this.getShortWorkTime(employeeId, command.getToDate());

		// add command save
		BasicScheduleSaveCommand commandSave = new BasicScheduleSaveCommand();
		commandSave.setWorktypeCode(worktypeDto.getWorktypeCode());
		commandSave.setEmployeeId(employeeId);
		commandSave.setWorktimeCode(workTimeCode);
		commandSave.setYmd(command.getToDate());

		if (optionalShortTime.isPresent()) {
			commandSave
					.setChildCareSchedules(
							optionalShortTime.get().getLstTimeSlot().stream()
									.map(shortime -> this.convertShortTimeChildCareToChildCare(shortime,
											optionalShortTime.get().getChildCareAtr().value))
									.collect(Collectors.toList()));
		}

		//勤務予定マスタ情報を取得する
		this.saveScheduleMaster(commandSave);
		//休憩予定時間帯を取得する
		this.saveBreakTime(command.getCompanyId(), commandSave);
		//勤務予定時間
		this.saveScheduleTime(commandSave);
		
		// check not exist error
		if (!this.scheCreExeErrorLogHandler.checkExistError(command.toBaseCommand(), employeeId)) {

			WorkTimeSetGetterCommand commandGetter = new WorkTimeSetGetterCommand();
			commandGetter.setWorktypeCode(worktypeDto.getWorktypeCode());
			commandGetter.setCompanyId(command.getCompanyId());
			commandGetter.setWorkingCode(workTimeCode);

			Optional<PrescribedTimezoneSetting> optionalWorkTimeSet = this.scheCreExeWorkTimeHandler
					.getScheduleWorkHour(commandGetter);
			if (optionalWorkTimeSet.isPresent()) {
				PrescribedTimezoneSetting workTimeSet = optionalWorkTimeSet.get();
				commandSave.updateWorkScheduleTimeZones(workTimeSet);
			}
		}

		if (worktypeDto.getWorktypeSet() != null && !commandSave.getWorkScheduleTimeZones().isEmpty()) {
			Optional<BounceAtr> bounceAtrOpt = Optional.of(this.getBounceAtr(worktypeDto.getWorktypeSet()));
		
			if (bounceAtrOpt.isPresent()) {
				commandSave.setWorkScheduleTimeZones(commandSave.getWorkScheduleTimeZones().stream().map(timeZone -> {
					timeZone.setBounceAtr(bounceAtrOpt.get().value);
					return timeZone;
				}).collect(Collectors.toList()));
			}
		}
		
		// update is confirm
		commandSave.setConfirmedAtr(this.getConfirmedAtr(command.getConfirm(), ConfirmedAtr.UNSETTLED).value);

		// check parameter is delete before insert
		if (command.getIsDeleteBeforInsert()) {
			this.basicScheduleRepository.delete(employeeId, command.getToDate());
		}

		// save command
		this.saveBasicSchedule(commandSave);;
	}
	
	/**
	 * Gets the short work time.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the short work time
	 */
	// アルゴリズム (WorkTime)
	private Optional<ShortWorkTimeDto> getShortWorkTime(String employeeId, GeneralDate baseDate) {
		return this.scShortWorkTimeAdapter.findShortWorkTime(employeeId, baseDate);
	}
	
	
	/**
	 * Gets the confirmed atr.
	 *
	 * @param isConfirmContent the is confirm content
	 * @param confirmedAtr the confirmed atr
	 * @return the confirmed atr
	 */
	// 予定確定区分を取得
	private ConfirmedAtr getConfirmedAtr(boolean isConfirmContent, ConfirmedAtr confirmedAtr) {

		// check is confirm content
		if (isConfirmContent) {
			return ConfirmedAtr.CONFIRMED;
		} else {
			return confirmedAtr;
		}
	}
	
	/**
	 * Save basic schedule.
	 *
	 * @param command the command
	 */
	// 勤務予定情報を登録する
	private void saveBasicSchedule(BasicScheduleSaveCommand command) {

		// find basic schedule by id
		boolean optionalBasicSchedule = this.basicScheduleRepository.isExists(command.getEmployeeId(),
				command.getYmd());

		// check exist data
		if (optionalBasicSchedule) {

			// update domain
			this.basicScheduleRepository.update(command.toDomain());
		} else {

			// insert domain
			this.basicScheduleRepository.insert(command.toDomain());
		}
	}
	
	/**
	 * Convert short tim child care to child care.
	 *
	 * @param shortChildCareFrameDto the short child care frame dto
	 * @return the child care schedule save command
	 */
	// 勤務予定育児介護時間帯
	private ChildCareScheduleSaveCommand convertShortTimeChildCareToChildCare(
			ShortChildCareFrameDto shortChildCareFrameDto, int childCareAtr) {
		ChildCareScheduleSaveCommand command = new ChildCareScheduleSaveCommand();

		// 予定育児介護回数 = 取得した短時間勤務. 時間帯. 回数
		command.setChildCareNumber(shortChildCareFrameDto.getTimeSlot());

		// 予定育児介護開始時刻 = 取得した短時間勤務. 時間帯. 開始時刻
		command.setChildCareScheduleStart(shortChildCareFrameDto.getStartTime().valueAsMinutes());

		// 予定育児介護終了時刻 = 取得した短時間勤務. 時間帯. 終了時刻
		command.setChildCareScheduleEnd(shortChildCareFrameDto.getEndTime().valueAsMinutes());

		// 育児介護区分 = 取得した短時間勤務. 育児介護区分
		command.setChildCareAtr(childCareAtr);
		return command;
	}
	
	
	
	/**
	 * Reset all data to command save.
	 *
	 * @param command the command
	 */
	public void resetAllDataToCommandSave(BasicScheduleResetCommand command, GeneralDate toDate) {
		// add command save
		BasicScheduleSaveCommand commandSave = new BasicScheduleSaveCommand();
		commandSave.setWorktypeCode(command.getWorkTypeCode());
		commandSave.setEmployeeId(command.getEmployeeId());
		commandSave.setWorktimeCode(command.getWorkingCode());
		commandSave.setYmd(toDate);
		commandSave = this.resetCreatedData(command, commandSave);
		// update is confirm
		commandSave.setConfirmedAtr(this.getConfirmedAtr(command.getConfirm(), ConfirmedAtr.UNSETTLED).value);

		// save command
		this.saveBasicSchedule(commandSave);;
	}
	
	/**
	 * Reset created data.
	 *
	 * @param resetAtr the reset atr
	 */
	// 作成済みのデータを再設定する
	private BasicScheduleSaveCommand resetCreatedData(BasicScheduleResetCommand command,
			BasicScheduleSaveCommand commandSave) {
		Optional<BounceAtr> optionalBounceAtr = this.resetDirectLineBounce(command);
		commandSave = this.resetWorkTime(command, commandSave);
		// check exist and not empty list
		if (optionalBounceAtr.isPresent() && !CollectionUtil.isEmpty(commandSave.getWorkScheduleTimeZones())) {
			commandSave.setWorkScheduleTimeZones(commandSave.getWorkScheduleTimeZones().stream().map(timeZone -> {
				timeZone.setBounceAtr(optionalBounceAtr.get().value);
				return timeZone;
			}).collect(Collectors.toList()));
		}
		return commandSave;
	}

	/**
	 * Reset direct line bounce.
	 */
	// 直行直帰再設定
	private Optional<BounceAtr> resetDirectLineBounce(BasicScheduleResetCommand command) {
		// check is 直行直帰再設定 TRUE
		if (command.getResetAtr().getResetDirectLineBounce()) {

			WorkType worktype = this.workTypeRepository.findByPK(command.getCompanyId(),
					command.getWorkTypeCode()).get();

			if (worktype.getWorkTypeSet() != null) {
				return Optional.of(this.getBounceAtr(worktype.getWorkTypeSet()));
			}

		}
		return Optional.empty();
	}
	
	/**
	 * Gets the bounce atr.
	 *
	 * @param workTypeSet the work type set
	 * @return the bounce atr
	 */
	private BounceAtr getBounceAtr(WorkTypeSet workTypeSet) {

		// 出勤時刻を直行とする：False AND 退勤時刻を直行とする：False⇒ 直行直帰なし
		if (workTypeSet.getAttendanceTime() == WorkTypeSetCheck.NO_CHECK
				&& workTypeSet.getTimeLeaveWork() == WorkTypeSetCheck.NO_CHECK) {
			return BounceAtr.DIRECT_BOUNCE;
		}

		// 出勤時刻を直行とする：True AND 退勤時刻を直行とする：False⇒ 直行のみ
		if (workTypeSet.getAttendanceTime() == WorkTypeSetCheck.CHECK
				&& workTypeSet.getTimeLeaveWork() == WorkTypeSetCheck.NO_CHECK) {
			return BounceAtr.BOUNCE_ONLY;
		}

		// 出勤時刻を直行とする：False AND 退勤時刻を直行とする：True⇒ 直帰のみ
		if (workTypeSet.getAttendanceTime() == WorkTypeSetCheck.NO_CHECK
				&& workTypeSet.getTimeLeaveWork() == WorkTypeSetCheck.CHECK) {
			return BounceAtr.NO_DIRECT_BOUNCE;
		}

		// 出勤時刻を直行とする：True AND 退勤時刻を直行とする：True⇒ 直行直帰
		if (workTypeSet.getAttendanceTime() == WorkTypeSetCheck.CHECK
				&& workTypeSet.getTimeLeaveWork() == WorkTypeSetCheck.CHECK) {
			return BounceAtr.DIRECTLY_ONLY;
		}
		return BounceAtr.DIRECTLY_ONLY;
	}
	
	/**
	 * Reset work time.
	 *
	 * @param command the command
	 * @return the basic schedule save command
	 */
	// 就業時間帯再設定
	private BasicScheduleSaveCommand resetWorkTime(BasicScheduleResetCommand command,
			BasicScheduleSaveCommand commandSave) {

		// check 就業時間帯再設定 is TRUE
		if (command.getResetAtr().getResetWorkingHours()) {
			WorkTimeSetGetterCommand commandGetter = new WorkTimeSetGetterCommand();
			commandGetter.setWorktypeCode(command.getWorkTypeCode());
			commandGetter.setCompanyId(command.getCompanyId());
			commandGetter.setWorkingCode(command.getWorkingCode());
			Optional<PrescribedTimezoneSetting> optionalWorkTimeSet = this.scheCreExeWorkTimeHandler
					.getScheduleWorkHour(commandGetter);
			if (optionalWorkTimeSet.isPresent()) {
				PrescribedTimezoneSetting workTimeSet = optionalWorkTimeSet.get();
				commandSave.updateWorkScheduleTimeZones(workTimeSet);
			}
		}
		return commandSave;
	}
	
	/**
	 * 勤務予定休憩
	 * @param employeeId
	 * @param toDate
	 */
	private BasicScheduleSaveCommand saveBreakTime(String companyId,
			BasicScheduleSaveCommand commandSave) {
		BusinessDayCal businessDayCal = this.scheWithBusinessDayCalService.getScheduleBreakTime(companyId, commandSave.getWorktypeCode(), commandSave.getWorktimeCode());
		if (businessDayCal == null) {
			return commandSave;
		}
		List<WorkScheduleBreakSaveCommand> workScheduleBreaks = new ArrayList<WorkScheduleBreakSaveCommand>();
		List<DeductionTime> timeZones = businessDayCal.getTimezones();
		for(int i=0;i< timeZones.size();i++){
			WorkScheduleBreakSaveCommand wBreakSaveCommand = new WorkScheduleBreakSaveCommand();
			wBreakSaveCommand.setScheduleBreakCnt(i+1);
			wBreakSaveCommand.setScheduledStartClock(timeZones.get(i).getStart().valueAsMinutes());
			wBreakSaveCommand.setScheduledEndClock(timeZones.get(i).getEnd().valueAsMinutes());
			workScheduleBreaks.add(wBreakSaveCommand);
		}
		commandSave.setWorkScheduleBreaks(workScheduleBreaks);
		return commandSave;
	}
	
	/**
	 * 勤務予定マスタ情報を取得する
	 * @param employeeId
	 * @param toDate
	 */
	private BasicScheduleSaveCommand saveScheduleMaster(BasicScheduleSaveCommand commandSave) {
		//勤務予定マスタ情報を取得する
		ScheduleMasterInformationDto scheduleMasterInfor = scheduleMasterInformationRepo.getScheduleMasterInformationDto(commandSave.getEmployeeId(), commandSave.getYmd());
		ScheMasterInfo workScheduleMaster = new ScheMasterInfo(
				commandSave.getEmployeeId(), 
				commandSave.getYmd(), 
				scheduleMasterInfor.getEmployeeCode(), 
				scheduleMasterInfor.getClassificationCode(), 
				commandSave.getWorktypeCode(), 
				scheduleMasterInfor.getJobId(), 
				scheduleMasterInfor.getWorkplaceId());	
		commandSave.setWorkScheduleMaster(workScheduleMaster);
		return commandSave;
	}
	
	/**
	 * 勤務予定時間
	 */
	private BasicScheduleSaveCommand saveScheduleTime(BasicScheduleSaveCommand commandSave) {
		ScTimeParam param = ScTimeParam.builder()
				.employeeId(commandSave.getEmployeeId())
				.workTypeCode(new WorkTypeCode(commandSave.getWorktypeCode()))
				.workTimeCode(new WorkTimeCode(commandSave.getWorktimeCode()))
				.build();
		ScTimeImport scTimeImport = scTimeAdapter.calculation(param);
		WorkScheduleTime workScheduleTime = new WorkScheduleTime(
				Collections.emptyList(), //TODO 
				scTimeImport.getBreakTime(), 
				scTimeImport.getActualWorkTime(), 
				scTimeImport.getWeekDayTime(), 
				scTimeImport.getPreTime(),
				scTimeImport.getTotalWorkTime(), 
				scTimeImport.getChildCareTime());
		commandSave.setWorkScheduleTime(Optional.ofNullable(workScheduleTime));
		return commandSave;
	}
	
	/**
	 * Create a basic schedule command to save
	 * @param basicSchedule the basic schedule
	 * @param optPrescribedSetting the Optional prescribed setting
	 * @param command the work time set getter command
	 * @param employeeId the employee Id
	 * @param baseDate the base date (input from screen A)
	 */
	public void registerBasicScheduleSaveCommand(Optional<BasicSchedule> optBasicSchedule, Optional<PrescribedTimezoneSetting> optPrescribedSetting,
			WorkTimeSetGetterCommand command, String employeeId, GeneralDate baseDate) {
		BasicSchedule basicSchedule;
		
		// Create basic schedule
		if (!optBasicSchedule.isPresent()) {
			basicSchedule = new BasicSchedule(employeeId, baseDate, command.getWorktypeCode(), command.getWorkingCode(), ConfirmedAtr.CONFIRMED);
		}
		else {
			basicSchedule = optBasicSchedule.get();
		}
		BasicScheduleSaveCommand basicScheduleSaveCommand = new BasicScheduleSaveCommand();
		basicScheduleSaveCommand.setEmployeeId(basicSchedule.getEmployeeId());
		basicScheduleSaveCommand.setWorktimeCode(command.getWorkingCode());
		basicScheduleSaveCommand.setWorktypeCode(command.getWorktypeCode());
		basicScheduleSaveCommand.setYmd(basicSchedule.getDate());
		basicScheduleSaveCommand.setWorkScheduleTime(basicSchedule.getWorkScheduleTime());
		
		PrescribedTimezoneSetting prescribedTimezoneSetting;
		
		// 該当日の該当社員の個人勤務予定が既に存在するかチェック
		if (optPrescribedSetting.isPresent()) {
			// 存在しない場合
			
			// ドメインモデル「勤務予定基本情報」を追加する
			prescribedTimezoneSetting = optPrescribedSetting.get();
		}
		else {
			// 存在する場合
			
			// ドメインモデル「勤務予定基本情報」を更新する 
			PrescribedTimezoneSettingDto prescribedTimezoneSettingDto = new PrescribedTimezoneSettingDto();
			prescribedTimezoneSettingDto.setMorningEndTime(DEFAULT_VALUE);
			prescribedTimezoneSettingDto.setAfternoonStartTime(DEFAULT_VALUE);
			prescribedTimezoneSettingDto.setLstTimezone(new ArrayList<>());
			prescribedTimezoneSetting = new PrescribedTimezoneSetting(prescribedTimezoneSettingDto);
		}
		
		basicScheduleSaveCommand.updateWorkScheduleTimeZones(prescribedTimezoneSetting);
		
		saveBasicSchedule(basicScheduleSaveCommand);
	}
}

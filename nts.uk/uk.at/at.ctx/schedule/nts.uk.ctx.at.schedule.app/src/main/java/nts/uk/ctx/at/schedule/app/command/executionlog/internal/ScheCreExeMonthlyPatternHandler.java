package nts.uk.ctx.at.schedule.app.command.executionlog.internal;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.schedule.app.command.executionlog.CreateScheduleMasterCache;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.app.command.executionlog.WorkCondItemDto;
import nts.uk.ctx.at.schedule.dom.adapter.employmentstatus.EmploymentInfoImported;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.EmployeeGeneralInfoImported;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.workplace.ExWorkPlaceHistoryImported;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.workplace.ExWorkplaceHistItemImported;
import nts.uk.ctx.at.schedule.dom.executionlog.ImplementAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ReCreateAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.RebuildTargetAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.ConfirmedAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.DateRegistedEmpSche;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfo;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySetting;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySettingRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.NotUseAtr;

/**
 * 
 * @author chinhbv
 *
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class ScheCreExeMonthlyPatternHandler {
	@Inject
	private ScheCreExeErrorLogHandler scheCreExeErrorLogHandler;
	@Inject
	private ScheCreExeWorkTimeHandler scheCreExeWorkTimeHandler;
	@Inject
	private ScheCreExeWorkTypeHandler scheCreExeWorkTypeHandler;
	@Inject
	private WorkMonthlySettingRepository workMonthlySettingRepo;
	@Inject
	private ScheCreExeBasicScheduleHandler scheCreExeBasicScheduleHandler;
	@Inject
	private WorkScheduleStateRepository workScheduleStateRepo;

	/**
	 * 月間パターンで勤務予定を作成する
	 * 
	 * @param command
	 * @param workingConditionItem
	 * @param empGeneralInfo
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createScheduleWithMonthlyPattern(
			ScheduleCreatorExecutionCommand command,
			GeneralDate dateInPeriod,
			WorkCondItemDto workingConditionItem,
			CreateScheduleMasterCache masterCache,
			List<BasicSchedule> listBasicSchedule,
			DateRegistedEmpSche dateRegistedEmpSche,
			EmploymentInfoImported employmentInfo) {
		
		// ドメインモデル「月間勤務就業設定」を取得する
		Optional<WorkMonthlySetting> workMonthlySetOpt = this.workMonthlySettingRepo.findById(command.getCompanyId(),
				workingConditionItem.getMonthlyPattern().get().v(), dateInPeriod);

		// パラメータ．月間パターンをチェックする, 対象日の「月間勤務就業設定」があるかチェックする
		if (!checkMonthlyPattern(command, dateInPeriod, workingConditionItem, workMonthlySetOpt)) {
			return;
		}

		// ドメインモデル「勤務予定基本情報」を取得する
		// fix for response
		Optional<BasicSchedule> basicScheOpt = listBasicSchedule.stream()
				.filter(x -> (x.getEmployeeId().equals(workingConditionItem.getEmployeeId())
						&& x.getDate().compareTo(dateInPeriod) == 0))
				.findFirst();
		if (basicScheOpt.isPresent()) {
			BasicSchedule basicSche = basicScheOpt.get();
			// 入力パラメータ「実施区分」を判断(kiểm tra parameter 「実施区分」)
			if (ImplementAtr.GENERALLY_CREATED == command.getContent().getImplementAtr()) {
				// 通常作成
				return;
			}
			// 入力パラメータ「再作成区分」を判断(kiểm tra parameter 「再作成区分」)
			if (command.getContent().getReCreateContent().getReCreateAtr() == ReCreateAtr.ONLY_UNCONFIRM) {
				// 未確定データのみ
				// 取得したドメインモデル「勤務予定基本情報」の「予定確定区分」を判断(kiểm tra trường 「予定確定区分」
				// của domain 「勤務予定基本情報」 lấy được)
				ConfirmedAtr confirmedAtr = basicSche.getConfirmedAtr();
				if (confirmedAtr == ConfirmedAtr.CONFIRMED) {
					// 確定済み
					return;
				}
			}

			// アルゴリズム「スケジュール作成判定処理」を実行する
			if (!this.scheduleCreationDeterminationProcess(command, dateInPeriod, basicSche, employmentInfo, workingConditionItem,
					masterCache)) {
				return;
			}
			// 登録前削除区分をTrue（削除する）とする(chuyển 登録前削除区分 = true)
			// checked2018
//			command.setIsDeleteBeforInsert(true);
		} else {
			// EA修正履歴 No1840
			// 入力パラメータ「実施区分」を判断
			ScheMasterInfo scheMasterInfo = new ScheMasterInfo(null);
			BasicSchedule basicSche = new BasicSchedule(null, scheMasterInfo);
			if (ImplementAtr.RECREATE == command.getContent().getImplementAtr()
					&& !this.scheduleCreationDeterminationProcess(command, dateInPeriod, basicSche, employmentInfo,
							workingConditionItem, masterCache)) {
				return;
			}
			// need set false if not wrong
			// 「勤務予定基本情報」 データなし
			// checked2018
//			command.setIsDeleteBeforInsert(false);
		}

		// 月間勤務就業設定
		WorkMonthlySetting workMonthlySet = workMonthlySetOpt.get();

		// 在職状態に対応する「勤務種類コード」を取得する
		WorkTypeGetterCommand commandWorktypeGetter = this.getWorkTypeGetter(command, dateInPeriod, workingConditionItem);
		Optional<WorktypeDto> workTypeOpt = this.getWorkTypeByEmploymentStatus(workMonthlySet, commandWorktypeGetter,
				masterCache);
		if (workTypeOpt.isPresent()) {// 取得エラーなし
			// 在職状態に対応する「就業時間帯コード」を取得する
			Pair<Boolean, Optional<String>> pair = this.getWorkingTimeZoneCode(workMonthlySet, commandWorktypeGetter, masterCache);
			// neu pair.getKey() == false nghia la khong tim duoc worktimeCode, da ghi errorLog, dung xu ly hien tai, chuyen sang ngay ke tiep
			if(pair.getKey()){
				// 取得エラーなし
				// 休憩予定時間帯を取得する
				// 勤務予定マスタ情報を取得する
				// 勤務予定時間帯を取得する
				// アルゴリズム「社員の短時間勤務を取得」を実行し、短時間勤務を取得する // request list #72
				// 取得した情報をもとに「勤務予定基本情報」を作成する (create basic schedule)
				// 予定確定区分を取得し、「勤務予定基本情報. 確定区分」に設定する
				scheCreExeBasicScheduleHandler.updateAllDataToCommandSave(command, dateInPeriod,
						workingConditionItem.getEmployeeId(), workTypeOpt.get(),
						pair.getValue().isPresent() ? pair.getValue().get() : null, masterCache, listBasicSchedule,
						dateRegistedEmpSche);
			}
		}

	}

	/**
	 * Get work type getter
	 * 
	 * @param command
	 * @param workingConditionItem
	 * @return
	 */
	private WorkTypeGetterCommand getWorkTypeGetter(ScheduleCreatorExecutionCommand command, GeneralDate dateInPeriod,
			WorkCondItemDto workingConditionItem) {
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
		return commandWorktypeGetter;
	}

	/**
	 * 在職状態に対応する「勤務種類コード」を取得する
	 * 
	 * @return
	 */
	private Optional<WorktypeDto> getWorkTypeByEmploymentStatus(
			WorkMonthlySetting workMonthlySet,
			WorkTypeGetterCommand commandWorktypeGetter,
			CreateScheduleMasterCache masterCache) {
		// setup command employment status getter
		WorkTypeByEmpStatusGetterCommand commandWorkTypeEmploymentStatus = commandWorktypeGetter
				.toWorkTypeEmploymentStatus();

		// set working code to command
		commandWorkTypeEmploymentStatus
				.setWorkingCode(workMonthlySet.getWorkingCode() == null ? null : workMonthlySet.getWorkingCode().v());

		// set work type code to command
		commandWorkTypeEmploymentStatus.setWorkTypeCode(workMonthlySet.getWorkTypeCode().v());

		return this.scheCreExeWorkTypeHandler.getWorkTypeByEmploymentStatus(commandWorkTypeEmploymentStatus, masterCache);
	}

	/**
	 * 在職状態に対応する「就業時間帯コード」を取得する
	 * 
	 * @param workMonthlySet
	 * @param command
	 * @return Pair<Boolean, Optional<String>>, if boolean = false is mean has error
	 */
	public Pair<Boolean, Optional<String>> getWorkingTimeZoneCode(
			WorkMonthlySetting workMonthlySet,
			WorkTypeGetterCommand commandWorktypeGetter,
			CreateScheduleMasterCache masterCache) {
		WorkTimeGetterCommand workTimeGetterCommand = commandWorktypeGetter.toWorkTime();
		WorkTimeZoneGetterCommand commandGetter = workTimeGetterCommand.toWorkTimeZone();
		commandGetter.setWorkTypeCode(workMonthlySet.getWorkTypeCode().v());
		commandGetter
				.setWorkingCode(workMonthlySet.getWorkingCode() == null ? null : workMonthlySet.getWorkingCode().v());
		if (StringUtil.isNullOrEmpty(commandGetter.getWorkingCode(), true)) {
			commandGetter.setWorkingCode(null);
		}
		return this.scheCreExeWorkTimeHandler.getWorkingTimeZoneCode(commandGetter, masterCache);
	}

	/**
	 * check employment status (在職状態を判断)
	 * 
	 * @param command
	 */
//	private boolean checkEmploymentStatus(Optional<EmploymentInfoImported> optEmploymentInfo) {
//		// 退職、取得できない(退職 OR không lấy được)
//		if (!optEmploymentInfo.isPresent() || optEmploymentInfo.get().getEmploymentState() == 6) {// RETIREMENT
//			return false;
//		}
//
//		// 入社前
//		if (optEmploymentInfo.get().getEmploymentState() == 4) { // BEFORE_JOINING
//			return false;
//		}
//
//		return true;
//	}

	/**
	 * check monthly pattern
	 * 
	 * @param command
	 * @param workingConditionItem
	 */
	private boolean checkMonthlyPattern(ScheduleCreatorExecutionCommand command, GeneralDate dateInPeriod, WorkCondItemDto workingConditionItem,
			Optional<WorkMonthlySetting> workMonthlySetOpt) {
		// ドメインモデル「スケジュール作成エラーログ」を登録する
		if (!workingConditionItem.getMonthlyPattern().isPresent()
				|| StringUtil.isNullOrEmpty(workingConditionItem.getMonthlyPattern().get().v(), true)) {
			// log Msg_603
			scheCreExeErrorLogHandler.addError(command.toBaseCommand(dateInPeriod), workingConditionItem.getEmployeeId(),
					"Msg_603");
			return false;
		}

		// 対象日の「月間勤務就業設定」があるかチェックする

		// 存在しない場合
		// ドメインモデル「スケジュール作成エラーログ」を登録する
		if (!workMonthlySetOpt.isPresent()) {
			// log Msg_604
			scheCreExeErrorLogHandler.addError(command.toBaseCommand(dateInPeriod), workingConditionItem.getEmployeeId(),
					"Msg_604");
			return false;
		}

		return true;
	}

	/**
	 * アルゴリズム「スケジュール作成判定処理」を実行する
	 */
	public boolean scheduleCreationDeterminationProcess(
			ScheduleCreatorExecutionCommand command,
			GeneralDate dateInPeriod,
			BasicSchedule basicSche,
			EmploymentInfoImported employmentInfo,
			WorkCondItemDto workingConditionItem,
			CreateScheduleMasterCache masterCache) {
		// 再作成対象区分を判定する
		if (command.getContent().getReCreateContent().getRebuildTargetAtr() == RebuildTargetAtr.ALL) {
			return true;
		}
		// 異動者を再作成するか判定する
		boolean valueIsCreate = this.isCreate(workingConditionItem.getEmployeeId(), dateInPeriod,
				command.getContent().getReCreateContent().getRebuildTargetDetailsAtr().getRecreateConverter(),
				basicSche.getWorkScheduleMaster().getWorkplaceId(), masterCache.getEmpGeneralInfo());
		if (!valueIsCreate)
			return false;

		// 休職休業者を再作成するか判定する
		boolean valueIsReEmpOnLeaveOfAbsence = this.isReEmpOnLeaveOfAbsence(
				command.getContent().getReCreateContent().getRebuildTargetDetailsAtr().getRecreateEmployeeOffWork(),
				employmentInfo.getEmploymentState());
		if (!valueIsReEmpOnLeaveOfAbsence) {
			return false;
		}

		// 直行直帰者を再作成するか判定する
		boolean valueIsReDirectBounceBackEmp = this.isReDirectBounceBackEmp(
				command.getContent().getReCreateContent().getRebuildTargetDetailsAtr().getRecreateDirectBouncer(),
				workingConditionItem.getAutoStampSetAtr());
		if (!valueIsReDirectBounceBackEmp) {
			return false;
		}

		// 短時間勤務者を再作成するか判定する
		boolean valueIsReShortTime = masterCache.getShortWorkTimeDtos().isReShortTime(workingConditionItem.getEmployeeId(), dateInPeriod,
				command.getContent().getReCreateContent().getRebuildTargetDetailsAtr().getRecreateShortTermEmployee());
		if (!valueIsReShortTime) {
			return false;
		}

		// 勤務種別変更者を再作成するか判定する
		boolean valueIsReWorkerTypeChangePerson = masterCache.getBusinessTypeOfEmpDtos().isReWorkerTypeChangePerson(
				workingConditionItem.getEmployeeId(),
				dateInPeriod,
				command.getContent().getReCreateContent().getRebuildTargetDetailsAtr().getRecreateWorkTypeChange(),
				basicSche.getWorkScheduleMaster().getBusinessTypeCd());
		if (!valueIsReWorkerTypeChangePerson) {
			return false;
		}

		// 手修正を保護するか判定する
		boolean valueIsProtectHandCorrect = this.isProtectHandCorrect(workingConditionItem.getEmployeeId(),
				dateInPeriod,
				command.getContent().getReCreateContent().getRebuildTargetDetailsAtr().getProtectHandCorrection());
		if (!valueIsProtectHandCorrect) {
			return false;
		}

		return true;
	}

	/**
	 * 異動者を再作成するか判定する
	 * 
	 * (異動者再作成を判定する)
	 * 
	 * @param empId
	 * @param targetDate
	 * @param recreateConverter
	 * @param wkpId
	 * @return
	 */
	private boolean isCreate(String empId, GeneralDate targetDate, Boolean recreateConverter, String wkpId,
			EmployeeGeneralInfoImported empGeneralInfo) {
		// パラメータ.異動者を再作成を判定する
		if (!recreateConverter) {
			return true;
		}

		// EA No1842
		if (null == wkpId) {
			return false;
		}

		// EA No1677
		Map<String, List<ExWorkplaceHistItemImported>> mapWorkplaceHist = empGeneralInfo.getWorkplaceDto().stream()
				.collect(Collectors.toMap(ExWorkPlaceHistoryImported::getEmployeeId,
						ExWorkPlaceHistoryImported::getWorkplaceItems));

		List<ExWorkplaceHistItemImported> listWorkplaceHistItem = mapWorkplaceHist.get(empId);
		Optional<ExWorkplaceHistItemImported> optWorkplaceHistItem = Optional.empty();
		if (listWorkplaceHistItem != null) {
			optWorkplaceHistItem = listWorkplaceHistItem.stream()
					.filter(workplaceHistItem -> workplaceHistItem.getPeriod().contains(targetDate)).findFirst();
		}

		if (!optWorkplaceHistItem.isPresent()) {
			return true;
		}

		// 取得した職場IDとパラメータ.職場IDを比較する
		if (!optWorkplaceHistItem.get().getWorkplaceId().equals(wkpId)) {
			return true;
		}

		return false;
	}

	/**
	 * 休職休業者を再作成するか判定する
	 * 
	 * @param recreateEmployeeOffWork
	 * @param statusOfEmployment
	 * @return
	 */
	private boolean isReEmpOnLeaveOfAbsence(Boolean recreateEmployeeOffWork, int statusOfEmployment) {
		// 「在職状態.休職」 = 2 または 「在職状態.休業」 = 3 の場合
		if (!recreateEmployeeOffWork || statusOfEmployment == 2 || statusOfEmployment == 3) {
			return true;
		}
		return false;
	}

	/**
	 * 直行直帰者を再作成するか判定する
	 * 
	 * @param recreateEmployeeOffWork
	 * @param statusOfEmployment
	 * @return
	 */
	private boolean isReDirectBounceBackEmp(Boolean recreateDirectBouncer, NotUseAtr autoStampSetAtr) {
		// 「在職状態.休職」 = 2 または 「在職状態.休業」 = 3 の場合
		if (!recreateDirectBouncer || autoStampSetAtr == NotUseAtr.USE) {
			return true;
		}
		return false;
	}


	/**
	 * 手修正を保護するか判定する
	 * 
	 * @param empId
	 * @param targetDate
	 * @param protectHandCorrection
	 * @return
	 */
	private boolean isProtectHandCorrect(String empId, GeneralDate targetDate, Boolean protectHandCorrection) {
		if (!protectHandCorrection)
			return true;
		List<WorkScheduleState> listWorkScheduleState = this.workScheduleStateRepo.findByDateAndEmpId(empId,
				targetDate);
		if (listWorkScheduleState.size() == 0)
			return true;

		return false;
	}
}

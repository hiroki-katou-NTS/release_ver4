/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.schedule.basicschedule;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author sonnh1
 *
 */	
@Transactional(value = TxType.SUPPORTS)
@Stateless
public class DefaultBasicScheduleService implements BasicScheduleService {

	@Inject
	public WorkTypeRepository workTypeRepo;
	@Inject
	private WorkTimeSettingRepository workTimeRepository;

	/** The Constant FIRST_DATA. */
	public static final int FIRST_DATA = 0;
	
	/**
	 *  就業時間帯の必須チェック
	 */
	@Override
	public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
		String companyId = AppContexts.user().companyId();
		Optional<WorkType> workType = workTypeRepo.findByPK(companyId, workTypeCode);

		if (!workType.isPresent()) {
			return SetupType.OPTIONAL;
		}
		DailyWork dailyWork = workType.get().getDailyWork();
		WorkTypeUnit workTypeUnit = dailyWork.getWorkTypeUnit();
		// All day
		if (WorkTypeUnit.OneDay == workTypeUnit) {

			WorkTypeClassification workTypeClass = dailyWork.getOneDay();

			if (WorkTypeClassification.AnnualHoliday == workTypeClass
					|| WorkTypeClassification.YearlyReserved == workTypeClass
					|| WorkTypeClassification.SubstituteHoliday == workTypeClass
					|| WorkTypeClassification.Absence == workTypeClass
					|| WorkTypeClassification.SpecialHoliday == workTypeClass
					|| WorkTypeClassification.TimeDigestVacation == workTypeClass) {

				return this.checkRequiredOfInputType(workTypeClass);
			}

			if (WorkTypeClassification.Attendance == workTypeClass
					|| WorkTypeClassification.HolidayWork == workTypeClass
					|| WorkTypeClassification.Shooting == workTypeClass) {
				return SetupType.REQUIRED;
			}

			if (WorkTypeClassification.AnnualHoliday == workTypeClass
					|| WorkTypeClassification.YearlyReserved == workTypeClass
					|| WorkTypeClassification.SpecialHoliday == workTypeClass
					|| WorkTypeClassification.TimeDigestVacation == workTypeClass
					|| WorkTypeClassification.ContinuousWork == workTypeClass
					|| WorkTypeClassification.Closure == workTypeClass
					|| WorkTypeClassification.LeaveOfAbsence == workTypeClass) {
				return SetupType.OPTIONAL;
			}

			if (WorkTypeClassification.Holiday == workTypeClass || WorkTypeClassification.Absence == workTypeClass
					|| WorkTypeClassification.Pause == workTypeClass) {
				return SetupType.NOT_REQUIRED;
			}
		}

		// Half day
		if (WorkTypeUnit.MonringAndAfternoon == workTypeUnit) {
			WorkStyle workStyle = this.checkWorkDay(workTypeCode);
			if (WorkStyle.ONE_DAY_REST == workStyle) {

				SetupType morningWorkStyle = this.checkRequiredOfInputType(dailyWork.getMorning());
				SetupType afternoonWorkStyle = this.checkRequiredOfInputType(dailyWork.getAfternoon());

				return this.checkRequired(morningWorkStyle, afternoonWorkStyle);
			} else {
				return SetupType.REQUIRED;
			}
		}
		throw new RuntimeException("NOT FOUND SETUP TYPE");
	}
	
	@Override
	public SetupType checkNeedWorkTimeSetByList(String workTypeCode, List<WorkType> listWorkType) {
		Optional<WorkType> workType = listWorkType.stream()
				.filter(x -> x.getWorkTypeCode().v().equals(workTypeCode)).findFirst();

		if (!workType.isPresent()) {
			return SetupType.OPTIONAL;
		}
		DailyWork dailyWork = workType.get().getDailyWork();
		WorkTypeUnit workTypeUnit = dailyWork.getWorkTypeUnit();
		// All day
		if (WorkTypeUnit.OneDay == workTypeUnit) {

			WorkTypeClassification workTypeClass = dailyWork.getOneDay();

			if (WorkTypeClassification.AnnualHoliday == workTypeClass
					|| WorkTypeClassification.YearlyReserved == workTypeClass
					|| WorkTypeClassification.SubstituteHoliday == workTypeClass
					|| WorkTypeClassification.Absence == workTypeClass
					|| WorkTypeClassification.SpecialHoliday == workTypeClass
					|| WorkTypeClassification.TimeDigestVacation == workTypeClass) {

				return this.checkRequiredOfInputType(workTypeClass);
			}

			if (WorkTypeClassification.Attendance == workTypeClass
					|| WorkTypeClassification.HolidayWork == workTypeClass
					|| WorkTypeClassification.Shooting == workTypeClass) {
				return SetupType.REQUIRED;
			}

			if (WorkTypeClassification.AnnualHoliday == workTypeClass
					|| WorkTypeClassification.YearlyReserved == workTypeClass
					|| WorkTypeClassification.SpecialHoliday == workTypeClass
					|| WorkTypeClassification.TimeDigestVacation == workTypeClass
					|| WorkTypeClassification.ContinuousWork == workTypeClass
					|| WorkTypeClassification.Closure == workTypeClass
					|| WorkTypeClassification.LeaveOfAbsence == workTypeClass) {
				return SetupType.OPTIONAL;
			}

			if (WorkTypeClassification.Holiday == workTypeClass || WorkTypeClassification.Absence == workTypeClass
					|| WorkTypeClassification.Pause == workTypeClass) {
				return SetupType.NOT_REQUIRED;
			}
		}

		// Half day
		if (WorkTypeUnit.MonringAndAfternoon == workTypeUnit) {
			WorkStyle workStyle = this.checkWorkDay(workTypeCode);
			if (WorkStyle.ONE_DAY_REST == workStyle) {

				SetupType morningWorkStyle = this.checkRequiredOfInputType(dailyWork.getMorning());
				SetupType afternoonWorkStyle = this.checkRequiredOfInputType(dailyWork.getAfternoon());

				return this.checkRequired(morningWorkStyle, afternoonWorkStyle);
			} else {
				return SetupType.REQUIRED;
			}
		}
		throw new RuntimeException("NOT FOUND SETUP TYPE");
	}

	/**
	 * 入力必須区分チェック
	 */
	@Override
	public SetupType checkRequiredOfInputType(WorkTypeClassification workTypeClass) {
		// TO-DO
		// Because of this stage WORK_SETTING_FOR_VACATION is not made
		// holidayWorkSetting - 休暇時の勤務設定 is not made
		// so return SetupType.OPTIONAL
		return SetupType.OPTIONAL;
	}

	/**
	 * 1日半日出勤・1日休日系の判定
	 */
	@Override
	public WorkStyle checkWorkDay(String workTypeCode) {
		String companyId = AppContexts.user().companyId();
		Optional<WorkType> workTypeOpt = workTypeRepo.findByPK(companyId, workTypeCode);

		if (!workTypeOpt.isPresent()) {
			return null;
		}

		WorkType workType = workTypeOpt.get();
		DailyWork dailyWork = workTypeOpt.get().getDailyWork();

		// All day
		if (workType.isOneDay()) {
			if (dailyWork.IsLeaveForADay()) {
				return WorkStyle.ONE_DAY_REST;
			}

			return WorkStyle.ONE_DAY_WORK;
		}

		// Half day
		if (dailyWork.IsLeaveForMorning()) {
			if (dailyWork.IsLeaveForAfternoon()) {
				return WorkStyle.ONE_DAY_REST;
			}

			return WorkStyle.AFTERNOON_WORK;
		}

		if (dailyWork.IsLeaveForAfternoon()) {
			return WorkStyle.MORNING_WORK;
		}

		return WorkStyle.ONE_DAY_WORK;
	}
	
	@Override
	public WorkStyle checkWorkDay(Optional<WorkType> workTypeOpt) {

		if (!workTypeOpt.isPresent()) {
			return null;
		}

		WorkType workType = workTypeOpt.get();
		DailyWork dailyWork = workTypeOpt.get().getDailyWork();

		// All day
		if (workType.isOneDay()) {
			if (dailyWork.IsLeaveForADay()) {
				return WorkStyle.ONE_DAY_REST;
			}

			return WorkStyle.ONE_DAY_WORK;
		}

		// Half day
		if (dailyWork.IsLeaveForMorning()) {
			if (dailyWork.IsLeaveForAfternoon()) {
				return WorkStyle.ONE_DAY_REST;
			}

			return WorkStyle.AFTERNOON_WORK;
		}

		if (dailyWork.IsLeaveForAfternoon()) {
			return WorkStyle.MORNING_WORK;
		}

		return WorkStyle.ONE_DAY_WORK;
	}
	
	@Override
	public WorkStyle checkWorkDayByList(String workTypeCode, List<WorkType> listWorkType) {
		Optional<WorkType> workTypeOpt = listWorkType.stream()
				.filter(workType -> workType.getWorkTypeCode().v().equals(workTypeCode)).findFirst();

		if (!workTypeOpt.isPresent()) {
			return null;
		}

		WorkType workType = workTypeOpt.get();
		DailyWork dailyWork = workTypeOpt.get().getDailyWork();

		// All day
		if (workType.isOneDay()) {
			if (dailyWork.IsLeaveForADay()) {
				return WorkStyle.ONE_DAY_REST;
			}

			return WorkStyle.ONE_DAY_WORK;
		}

		// Half day
		if (dailyWork.IsLeaveForMorning()) {
			if (dailyWork.IsLeaveForAfternoon()) {
				return WorkStyle.ONE_DAY_REST;
			}

			return WorkStyle.AFTERNOON_WORK;
		}

		if (dailyWork.IsLeaveForAfternoon()) {
			return WorkStyle.MORNING_WORK;
		}

		return WorkStyle.ONE_DAY_WORK;
	}
	
	@Override
	public SetupType checkRequired(SetupType morningWorkStyle, SetupType afternoonWorkStyle) {

		if (morningWorkStyle == SetupType.REQUIRED) {
			return SetupType.REQUIRED;
		}

		if (morningWorkStyle == SetupType.NOT_REQUIRED) {
			return afternoonWorkStyle;
		}

		if (afternoonWorkStyle == SetupType.REQUIRED) {
			return SetupType.REQUIRED;
		}

		if (afternoonWorkStyle == SetupType.NOT_REQUIRED || afternoonWorkStyle == SetupType.OPTIONAL) {
			return SetupType.OPTIONAL;
		}

		throw new RuntimeException("NOT FOUND SETUP TYPE");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService#
	 * checkPairWorkTypeWorkTime(java.lang.String, java.lang.String)
	 */
	@Override
	public void checkPairWorkTypeWorkTime(String workTypeCode, String workTimeCode) {
		SetupType setupType = this.checkNeededOfWorkTimeSetting(workTypeCode);

		// In case of Required and work time is not set.
		if (setupType == SetupType.REQUIRED && !this.isWorkTimeValid(workTimeCode)) {
			throw new BusinessException("Msg_435");
		}

		// In case of Not Required and work time is set.
		if (setupType == SetupType.NOT_REQUIRED && this.isWorkTimeValid(workTimeCode)) {
			throw new BusinessException("Msg_434");
		}
	}
	
	/**
	 * check Pair WorkType and WorkTime With List WorkType
	 * @param workTypeCode
	 * @param workTimeCode
	 * @param listWorkType
	 */
	@Override
	public void checkPairWTypeTimeWithLstWType(String workTypeCode, String workTimeCode, List<WorkType> listWorkType) {
		SetupType setupType = this.checkNeedWorkTimeSetByList(workTypeCode, listWorkType);

		// In case of Required and work time is not set.
		if (setupType == SetupType.REQUIRED && !this.isWorkTimeValid(workTimeCode)) {
			throw new BusinessException("Msg_435");
		}

		// In case of Not Required and work time is set.
		if (setupType == SetupType.NOT_REQUIRED && this.isWorkTimeValid(workTimeCode)) {
			throw new BusinessException("Msg_434");
		}
	}

	@Override
	public void checkWorkTypeMaster(String companyId, String worktypeCode) {
		// call repository find work type by code
		Optional<WorkType> optionalWorkType = this.workTypeRepo.findByPK(companyId, worktypeCode);

		// check work type not exist
		if (!optionalWorkType.isPresent()) {
			throw new BusinessException("Msg_436");
		}

		// work type exist => check deprecate of work type is 廃止する
		if (optionalWorkType.isPresent()
				&& optionalWorkType.get().getDeprecate() == DeprecateClassification.Deprecated) {
			throw new BusinessException("Msg_468");
		}
	}

	@Override
	public void checkWorkTimeMater(String companyId, String workTimeCode) {
		// check default work time code
		// 設定されていない（就業時間帯コード＝NULL || 就業時間帯コード＝000）
		if (StringUtil.isNullOrEmpty(workTimeCode, false)) {
			return;
		}

		// call repository find work time by code
		// ドメインモデル「就業時間帯の設定」に該当の就業時間帯コードが存在するかチェックする
		Optional<WorkTimeSetting> optionalWorkTime = this.workTimeRepository.findByCode(companyId, workTimeCode);

		// check work time not exits
		if (!optionalWorkTime.isPresent()) {
			throw new BusinessException("Msg_437");
		}

		// work time exits => check display attr not display
		if (optionalWorkTime.isPresent() && optionalWorkTime.get().getAbolishAtr().value == AbolishAtr.ABOLISH.value) {
			throw new BusinessException("Msg_469");
		}
	}
	
	/**
	 * 休業休職の勤務種類コードを返す
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param day
	 * @param workTypeCd
	 * @param tempAbsenceFrNo
	 * @param exeId
	 * @return
	 */
	@Override
	public String getWorktypeCodeLeaveHolidayType(String companyId, String employeeId, GeneralDate day,
			String workTypeCd, int tempAbsenceFrNo, Optional<WorkingConditionItem> optWorkingConditionItem) {

		// アルゴリズム「1日半日出勤・1日休日系の判定」を実行し、「出勤休日区分」を取得する
		WorkStyle workStyle = this.checkWorkDay(workTypeCd);

		// 入力パラメータ「勤務種類コード」を返す
		if (workStyle.equals(WorkStyle.ONE_DAY_REST)) {
			return workTypeCd;
		}
		// 勤務種類の分類を判断
		WorkType worktype = this.workTypeRepo
				.findByPK(companyId, workTypeCd).get();

		if (this.checkHolidayWork(worktype.getDailyWork())) {
			// 休日出勤
			if(!optWorkingConditionItem.isPresent() || optWorkingConditionItem.get().getWorkCategory() == null){
				// 取得失敗
				return workTypeCd;
			}
			// パラメータ「労働条件項目」．区分別勤務．休日時を取得する
			if(optWorkingConditionItem.get().getWorkCategory().getHolidayTime() != null){
				// 取得できた
				return optWorkingConditionItem.get().getWorkCategory().getHolidayTime().getWorkTypeCode().get().v();
			}
			
		} else {
			// 休日出勤でない
			// đoạn này trong EAP k viết rõ
			int closeAtr = 0;
			String wTypeCd = null;
			 // convert TEMP_ABS_FRAME_NO -> CLOSE_ATR
	        switch (tempAbsenceFrNo) {
	        case 1:
				List<WorkType> lstWorkType = this.workTypeRepo
						.findByCompanyIdAndLeaveAbsence(companyId);
	            if(lstWorkType.isEmpty()){
	                break;
	            }
	            wTypeCd = lstWorkType.get(FIRST_DATA).getWorkTypeCode().v();
	            break;
	        case 2:
	            closeAtr = 0;
	            break;
	        case 3:
	            closeAtr = 1;
	            break;
	        case 4:
	            closeAtr = 2;
	            break;
	        case 5:
	            closeAtr = 3;
	            break;
	        default:
	            // 6,7,8,9,10
	            closeAtr = 4;
	            break;
	        }
            if (wTypeCd != null) {
                return wTypeCd;
            }
	        
			// 休業区分の勤務種類コードを取得する
			List<WorkTypeSet> lstWorkTypeSet = this.workTypeRepo.findWorkTypeSetCloseAtrDeprecateAtr(companyId,
					closeAtr, DeprecateClassification.NotDeprecated.value);
			if(!lstWorkTypeSet.isEmpty()){
				return lstWorkTypeSet.get(FIRST_DATA).getWorkTypeCd().v();
			}
		}
		
		return null;
	}

	/**
	 * Checks if is work time valid.
	 *
	 * @param workTimeCode
	 *            the work time code
	 * @return true, if is work time valid
	 */
	private boolean isWorkTimeValid(String workTimeCode) {
		if (StringUtil.isNullOrEmpty(workTimeCode, true) || StringUtil.isNullOrEmpty(workTimeCode, true)) {
			return false;
		}
		return true;
	}
	
	private boolean checkHolidayWork(DailyWork dailyWork) {
		if (dailyWork.getWorkTypeUnit().value == WorkTypeUnit.OneDay.value) {
			return dailyWork.getOneDay().value == WorkTypeClassification.HolidayWork.value;
		}
		return (dailyWork.getMorning().value == WorkTypeClassification.HolidayWork.value
				|| dailyWork.getAfternoon().value == WorkTypeClassification.HolidayWork.value);
	}

	/**
	 * Check Enum WorkTypeClassification.
	 * 
	 * @param morningType
	 * @return true/false
	 */
	public boolean checkType(WorkTypeClassification workTypeClassification) {
		if (WorkTypeClassification.Holiday == workTypeClassification
				|| WorkTypeClassification.Pause == workTypeClassification
				|| WorkTypeClassification.AnnualHoliday == workTypeClassification
				|| WorkTypeClassification.YearlyReserved == workTypeClassification
				|| WorkTypeClassification.SpecialHoliday == workTypeClassification
				|| WorkTypeClassification.TimeDigestVacation == workTypeClassification
				|| WorkTypeClassification.SubstituteHoliday == workTypeClassification
				|| WorkTypeClassification.Absence == workTypeClassification
				|| WorkTypeClassification.ContinuousWork == workTypeClassification
				|| WorkTypeClassification.Closure == workTypeClassification
				|| WorkTypeClassification.LeaveOfAbsence == workTypeClassification) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 */
	@Override
	public boolean isReverseStartAndEndTime(TimeWithDayAttr scheduleStartClock, TimeWithDayAttr scheduleEndClock) {
		return scheduleStartClock.greaterThanOrEqualTo(scheduleEndClock);
	}

}

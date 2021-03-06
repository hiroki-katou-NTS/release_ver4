package nts.uk.ctx.at.shared.app.command.workingcondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.workingcondition.HourlyPaymentAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.LaborContractTime;
import nts.uk.ctx.at.shared.dom.workingcondition.ManageAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.MonthlyPatternCode;
import nts.uk.ctx.at.shared.dom.workingcondition.MonthlyPatternWorkScheduleCre;
import nts.uk.ctx.at.shared.dom.workingcondition.NotUseAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.PersonalDayOfWeek;
import nts.uk.ctx.at.shared.dom.workingcondition.PersonalWorkCategory;
import nts.uk.ctx.at.shared.dom.workingcondition.ScheduleMethod;
import nts.uk.ctx.at.shared.dom.workingcondition.SingleDaySchedule;
import nts.uk.ctx.at.shared.dom.workingcondition.TimeZone;
import nts.uk.ctx.at.shared.dom.workingcondition.TimeZoneScheduledMasterAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkByIndividualWorkDay;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleBusCal;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleMasterReferenceAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkTypeByIndividualWorkDay;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.pereg.app.command.MyCustomizeException;

@Stateless
public class AddWorkingConditionCommandAssembler {
	public WorkingConditionItem fromDTO(String histId, AddWorkingConditionCommand command){
		List<TimeZone> listTimeZone = new ArrayList<>();
		// ---------------------- PersonalDayOfWeek
		/** The monday. */
		// ?????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getMondayStartTime1(),
//				command.getMondayEndTime1(), command.getMondayStartTime2(), command.getMondayEndTime2());
		SingleDaySchedule mondaySchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getMondayWorkTimeCode()));

		/** The tuesday. */
		// ?????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getTuesdayStartTime1(),
//				command.getTuesdayEndTime1(), command.getTuesdayStartTime2(), command.getTuesdayEndTime2());
		SingleDaySchedule tuesdaySchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getTuesdayWorkTimeCode()));

		/** The wednesday. */
		// ?????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getWednesdayStartTime1(),
//				command.getWednesdayEndTime1(), command.getWednesdayStartTime2(), command.getWednesdayEndTime2());
		SingleDaySchedule wedSchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getWednesdayWorkTimeCode()));

		/** The thursday. */
		// ?????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getThursdayStartTime1(),
//				command.getThursdayEndTime1(), command.getThursdayStartTime2(), command.getThursdayEndTime2());
		SingleDaySchedule thurSchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getThursdayWorkTimeCode()));

		/** The friday. */
		// ?????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getFridayStartTime1(),
//				command.getFridayEndTime1(), command.getFridayStartTime2(), command.getFridayEndTime2());
		SingleDaySchedule friSchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getFridayWorkTimeCode()));

		/** The saturday. */
		// ?????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getSaturdayStartTime1(),
//				command.getSaturdayEndTime1(), command.getSaturdayStartTime2(), command.getSaturdayEndTime2());
		SingleDaySchedule satSchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getSaturdayWorkTimeCode()));

		/** The sunday. */
		// ?????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getSundayStartTime1(),
//				command.getSundayEndTime1(), command.getSundayStartTime2(), command.getSundayEndTime2());
		SingleDaySchedule sunSchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getSundayWorkTimeCode()));

		PersonalDayOfWeek workDayOfWeek = new PersonalDayOfWeek(
				WorkingConditionCommandUtils.getOptionalSingleDay(mondaySchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(tuesdaySchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(wedSchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(thurSchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(friSchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(satSchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(sunSchedule));
		// -------------------------

		// ------------------ PersonalWorkCategory
		/** The weekday time. */
		// ?????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getWeekDayStartTime1(),
//				command.getWeekDayEndTime1(), command.getWeekDayStartTime2(), command.getWeekDayEndTime2());
		SingleDaySchedule weekdaySchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getWeekdayWorkTimeCode()));

		/** The holiday work. */
		// ???????????????
//		listTimeZone = WorkingConditionCommandUtils.getTimeZone(command.getWorkInHolidayStartTime1(),
//				command.getWorkInHolidayEndTime1(), command.getWorkInHolidayStartTime2(),
//				command.getWorkInHolidayEndTime2());
		SingleDaySchedule wholidaySchedule = new SingleDaySchedule(listTimeZone,
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getWorkInHolidayWorkTimeCode()));

		// set worktype
		//?????????: ?????????????????????
		WorkTypeCode whenCommuting = new WorkTypeCode(command.getWeekdayWorkTypeCode());
		//???????????????: ?????????????????????
		WorkTypeCode goToWorkOnHolidays  = new WorkTypeCode(command.getWorkInHolidayWorkTypeCode());
		//?????????: ?????????????????????
		WorkTypeCode onHolidays = new WorkTypeCode(command.getHolidayWorkTypeCode());
		//Optional ???????????????: ?????????????????????
		Optional<WorkTypeCode> duringLegalHolidays = command.getInLawBreakTimeWorkTypeCode() != null ? 
				Optional.ofNullable(new WorkTypeCode(command.getInLawBreakTimeWorkTypeCode())) : Optional.empty();
		//Optional ???????????????: ?????????????????????
		Optional<WorkTypeCode> duringExorbitantHolidays = command.getOutsideLawBreakTimeWorkTypeCode() != null ? 
				Optional.ofNullable(new WorkTypeCode(command.getOutsideLawBreakTimeWorkTypeCode())) : Optional.empty();
		//Optinal ???????????????: ?????????????????????
		Optional<WorkTypeCode> holidays = command.getHolidayAttendanceTimeWorkTypeCode() != null ? 
				Optional.ofNullable(new WorkTypeCode(command.getHolidayAttendanceTimeWorkTypeCode())) : Optional.empty();
	
		PersonalWorkCategory workTime = new PersonalWorkCategory(weekdaySchedule, wholidaySchedule, workDayOfWeek);
		WorkTypeByIndividualWorkDay workType = new WorkTypeByIndividualWorkDay(whenCommuting, goToWorkOnHolidays, onHolidays, duringLegalHolidays, duringExorbitantHolidays, holidays);
		WorkByIndividualWorkDay workCategory = new WorkByIndividualWorkDay(workTime, workType); 
		
		// ----------------------- BreakdownTimeDay
		BreakDownTimeDay holidayAddTimeSet = new BreakDownTimeDay(command.getOneDay() != null?
				new AttendanceTime(command.getOneDay().intValue()):null,
				command.getMorning() != null? new AttendanceTime(command.getMorning().intValue()): null ,
				command.getAfternoon() != null ? new AttendanceTime(command.getAfternoon().intValue()): null);
		// ------------------------
		
		
		// ScheduleMethod
		// WorkScheduleBusCal - ???????????????????????????????????????????????????
		int basicCreateMethod;
		WorkScheduleMasterReferenceAtr referenceBusinessDayCalendar;
		if (command.getBasicCreateMethod() == null) {
			basicCreateMethod = 0;
			referenceBusinessDayCalendar = WorkScheduleMasterReferenceAtr.COMPANY;
		} else {
			switch (command.getBasicCreateMethod().intValue()) {
			case 1:
				basicCreateMethod = 0;
				referenceBusinessDayCalendar = WorkScheduleMasterReferenceAtr.WORK_PLACE;
				break;
			case 2:
				basicCreateMethod = 0;
				referenceBusinessDayCalendar = WorkScheduleMasterReferenceAtr.CLASSIFICATION;
				break;
			case 3:
				basicCreateMethod = 1;
				referenceBusinessDayCalendar = null;
				break;
			case 4:
				basicCreateMethod = 2;
				referenceBusinessDayCalendar = null;
				break;
			case 0:
			default:
				basicCreateMethod = 0;
				referenceBusinessDayCalendar = WorkScheduleMasterReferenceAtr.COMPANY;
				break;
			}
		}
		WorkScheduleBusCal busCal = new WorkScheduleBusCal(
						referenceBusinessDayCalendar,
						command.getReferenceType() != null ? EnumAdaptor.valueOf(command.getReferenceType().intValue(),TimeZoneScheduledMasterAtr.class) : EnumAdaptor.valueOf(0,TimeZoneScheduledMasterAtr.class));
		// MonthlyPatternWorkScheduleCre
		MonthlyPatternWorkScheduleCre monthlySchedule = new MonthlyPatternWorkScheduleCre(
				command.getReferenceType() == null ? 0 : command.getReferenceType().intValue());
		ScheduleMethod scheduleMethod = new ScheduleMethod(basicCreateMethod, busCal,
				monthlySchedule);
		WorkingConditionItem workingCond = new WorkingConditionItem(
				histId,
				// Default value is Use ??????
				EnumAdaptor.valueOf(
						command.getScheduleManagementAtr() != null ? command.getScheduleManagementAtr().intValue() : ManageAtr.USE.value,
								ManageAtr.class),
				workCategory,
				// Default value is Notuse ?????????
				EnumAdaptor.valueOf(command.getAutoStampSetAtr() != null ? command.getAutoStampSetAtr().intValue() : NotUseAtr.NOTUSE.value,
						NotUseAtr.class),
				// Default value is Notuse ?????????
				EnumAdaptor.valueOf(NotUseAtr.NOTUSE.value, NotUseAtr.class),
				command.getEmployeeId(),
				// Default value is Notuse ?????????
				EnumAdaptor.valueOf(
						command.getVacationAddedTimeAtr() != null ? command.getVacationAddedTimeAtr().intValue() : NotUseAtr.NOTUSE.value,
						NotUseAtr.class),
				// Default vaule is 0
				command.getContractTime() != null ? new LaborContractTime(command.getContractTime().intValue())
						: new LaborContractTime(0),
				command.getLaborSystem() != null
						? EnumAdaptor.valueOf(command.getLaborSystem().intValue(), WorkingSystem.class)
						: WorkingSystem.REGULAR_WORK,
				// HourlyPaymentAtr default value is ???????????????
				holidayAddTimeSet, 
				scheduleMethod, 
				command.getHourlyPaymentAtr() != null? command.getHourlyPaymentAtr().intValue() : HourlyPaymentAtr.OOUTSIDE_TIME_PAY.value,
				command.getTimeApply() != null ? new BonusPaySettingCode(command.getTimeApply()) : null,
				command.getMonthlyPattern() != null ? new MonthlyPatternCode(command.getMonthlyPattern()) : null);
		return workingCond;
	}
	
	public WokingConditionCommandCustom fromDTOCustom(String histId, AddWorkingConditionCommand command){
		List<MyCustomizeException> exLst= new ArrayList<>();
		TimeZoneCustom  timezone = new TimeZoneCustom();
		// ---------------------- PersonalDayOfWeek
		/** The monday. */
		// ????????? ?????????????????????1, ?????????????????????1
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1", 
//					command.getMondayStartTime1(),
//					command.getMondayEndTime1(), command.getMondayStartTime2(), command.getMondayEndTime2());
			SingleDaySchedule mondaySchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
					WorkingConditionCommandUtils.getOptionalWorkTime(command.getMondayWorkTimeCode()));
		if (!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}


		/** The tuesday. */
		// ?????????
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1", 
//				command.getTuesdayStartTime1(),
//				command.getTuesdayEndTime1(), command.getTuesdayStartTime2(), command.getTuesdayEndTime2());
		SingleDaySchedule tuesdaySchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getTuesdayWorkTimeCode()));
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		/** The wednesday. */
		// ?????????
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1", 
//				command.getWednesdayStartTime1(),
//				command.getWednesdayEndTime1(), command.getWednesdayStartTime2(), command.getWednesdayEndTime2());
		SingleDaySchedule wedSchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getWednesdayWorkTimeCode()));
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		/** The thursday. */
		// ?????????
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1",
//				command.getThursdayStartTime1(),
//				command.getThursdayEndTime1(), command.getThursdayStartTime2(), command.getThursdayEndTime2());
		SingleDaySchedule thurSchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getThursdayWorkTimeCode()));
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		/** The friday. */
		// ?????????
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1", 
//				command.getFridayStartTime1(),
//				command.getFridayEndTime1(), command.getFridayStartTime2(), command.getFridayEndTime2());
		SingleDaySchedule friSchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getFridayWorkTimeCode()));
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		/** The saturday. */
		// ?????????
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1", 
//				command.getSaturdayStartTime1(),
//				command.getSaturdayEndTime1(), command.getSaturdayStartTime2(), command.getSaturdayEndTime2());
		SingleDaySchedule satSchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getSaturdayWorkTimeCode()));
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		/** The sunday. */
		// ?????????
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1", 
//				command.getSundayStartTime1(),
//				command.getSundayEndTime1(), command.getSundayStartTime2(), command.getSundayEndTime2());
		SingleDaySchedule sunSchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getSundayWorkTimeCode()));
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		PersonalDayOfWeek workDayOfWeek = new PersonalDayOfWeek(
				WorkingConditionCommandUtils.getOptionalSingleDay(mondaySchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(tuesdaySchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(wedSchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(thurSchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(friSchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(satSchedule),
				WorkingConditionCommandUtils.getOptionalSingleDay(sunSchedule));
		// -------------------------

		// ------------------ PersonalWorkCategory
		/** The weekday time. */
		// ?????????
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1", 
//				command.getWeekDayStartTime1(),
//				command.getWeekDayEndTime1(), command.getWeekDayStartTime2(), command.getWeekDayEndTime2());
		SingleDaySchedule weekdaySchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getWeekdayWorkTimeCode()));
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		/** The holiday work. */
		// ???????????????
//		timezone = WorkingConditionCommandUtils.getCustomTimeZone(command.getEmployeeId(), "?????????????????????1","?????????????????????1", 
//				command.getWorkInHolidayStartTime1(),
//				command.getWorkInHolidayEndTime1(), command.getWorkInHolidayStartTime2(),
//				command.getWorkInHolidayEndTime2());
		SingleDaySchedule wholidaySchedule = new SingleDaySchedule(timezone.getTimezoneLst(),
				WorkingConditionCommandUtils.getOptionalWorkTime(command.getWorkInHolidayWorkTimeCode()));
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		/** The in law break time. */
		// ???????????????
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		

		/** The outside law break time. */
		// ???????????????
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		/** The holiday attendance time. */
		// ???????????????
		if(!CollectionUtil.isEmpty(timezone.getErrors())) {
			exLst.addAll(timezone.getErrors());
		}
		
		// set worktype
		//?????????: ?????????????????????
		WorkTypeCode whenCommuting = new WorkTypeCode(command.getWeekdayWorkTypeCode());
		//???????????????: ?????????????????????
		WorkTypeCode goToWorkOnHolidays  = new WorkTypeCode(command.getWorkInHolidayWorkTypeCode());
		//?????????: ?????????????????????
		WorkTypeCode onHolidays = new WorkTypeCode(command.getHolidayWorkTypeCode());
		//Optional ???????????????: ?????????????????????
		Optional<WorkTypeCode> duringLegalHolidays = command.getInLawBreakTimeWorkTypeCode() != null ? 
				Optional.ofNullable(new WorkTypeCode(command.getInLawBreakTimeWorkTypeCode())) : Optional.empty();
		//Optional ???????????????: ?????????????????????
		Optional<WorkTypeCode> duringExorbitantHolidays = command.getOutsideLawBreakTimeWorkTypeCode() != null ? 
				Optional.ofNullable(new WorkTypeCode(command.getOutsideLawBreakTimeWorkTypeCode())) : Optional.empty();
		//Optinal ???????????????: ?????????????????????
		Optional<WorkTypeCode> holidays = command.getHolidayAttendanceTimeWorkTypeCode() != null ? 
				Optional.ofNullable(new WorkTypeCode(command.getHolidayAttendanceTimeWorkTypeCode())) : Optional.empty();
		
		PersonalWorkCategory workTime = new PersonalWorkCategory(weekdaySchedule, wholidaySchedule, workDayOfWeek);
		WorkTypeByIndividualWorkDay workType = new WorkTypeByIndividualWorkDay(whenCommuting, goToWorkOnHolidays, onHolidays, duringLegalHolidays, duringExorbitantHolidays, holidays);
		WorkByIndividualWorkDay workCategory = new WorkByIndividualWorkDay(workTime, workType); 
		
		// -------------------------------
		
		
		// ----------------------- BreakdownTimeDay
		BreakDownTimeDay holidayAddTimeSet = new BreakDownTimeDay(command.getOneDay() != null?
				new AttendanceTime(command.getOneDay().intValue()):null,
				command.getMorning() != null? new AttendanceTime(command.getMorning().intValue()): null ,
				command.getAfternoon() != null ? new AttendanceTime(command.getAfternoon().intValue()): null);
		// ------------------------
		
		
		// ScheduleMethod
		// WorkScheduleBusCal - ???????????????????????????????????????????????????
		int basicCreateMethod;
		WorkScheduleMasterReferenceAtr referenceBusinessDayCalendar;
		if (command.getBasicCreateMethod() == null) {
			basicCreateMethod = 0;
			referenceBusinessDayCalendar = WorkScheduleMasterReferenceAtr.COMPANY;
		} else {
			switch (command.getBasicCreateMethod().intValue()) {
			case 1:
				basicCreateMethod = 0;
				referenceBusinessDayCalendar = WorkScheduleMasterReferenceAtr.WORK_PLACE;
				break;
			case 2:
				basicCreateMethod = 0;
				referenceBusinessDayCalendar = WorkScheduleMasterReferenceAtr.CLASSIFICATION;
				break;
			case 3:
				basicCreateMethod = 1;
				referenceBusinessDayCalendar = null;
				break;
			case 4:
				basicCreateMethod = 2;
				referenceBusinessDayCalendar = null;
				break;
			case 0:
			default:
				basicCreateMethod = 0;
				referenceBusinessDayCalendar = WorkScheduleMasterReferenceAtr.COMPANY;
				break;
			}
		}
		WorkScheduleBusCal busCal = new WorkScheduleBusCal(
						referenceBusinessDayCalendar,
						command.getReferenceType() != null ? EnumAdaptor.valueOf(command.getReferenceType().intValue(),TimeZoneScheduledMasterAtr.class) : EnumAdaptor.valueOf(0,TimeZoneScheduledMasterAtr.class));
		// MonthlyPatternWorkScheduleCre
		MonthlyPatternWorkScheduleCre monthlySchedule = new MonthlyPatternWorkScheduleCre(
				command.getReferenceType() == null ? 0 : command.getReferenceType().intValue());
		ScheduleMethod scheduleMethod = new ScheduleMethod(basicCreateMethod, busCal,
				monthlySchedule);
		WorkingConditionItem workingCond = new WorkingConditionItem(histId,
				// Default value is Use ??????
				EnumAdaptor.valueOf(
						command.getScheduleManagementAtr() != null ? command.getScheduleManagementAtr().intValue() : ManageAtr.USE.value,
								ManageAtr.class),
				workCategory,
				// Default value is Notuse ?????????
				EnumAdaptor.valueOf(command.getAutoStampSetAtr() != null ? command.getAutoStampSetAtr().intValue() : NotUseAtr.NOTUSE.value,
						NotUseAtr.class),
				// Default value is Notuse ?????????
				EnumAdaptor.valueOf(NotUseAtr.NOTUSE.value, NotUseAtr.class),
				command.getEmployeeId(),
				// Default value is Notuse ?????????
				EnumAdaptor.valueOf(
						command.getVacationAddedTimeAtr() != null ? command.getVacationAddedTimeAtr().intValue() : NotUseAtr.NOTUSE.value,
						NotUseAtr.class),
				// Default vaule is 0
				command.getContractTime() != null ? new LaborContractTime(command.getContractTime().intValue())
						: new LaborContractTime(0),
				command.getLaborSystem() != null
						? EnumAdaptor.valueOf(command.getLaborSystem().intValue(), WorkingSystem.class)
						: WorkingSystem.REGULAR_WORK,
				// HourlyPaymentAtr default value is ???????????????
				holidayAddTimeSet, scheduleMethod, command.getHourlyPaymentAtr() != null? command.getHourlyPaymentAtr().intValue() : HourlyPaymentAtr.OOUTSIDE_TIME_PAY.value,
				command.getTimeApply() != null ? new BonusPaySettingCode(command.getTimeApply()) : null,
				command.getMonthlyPattern() != null ? new MonthlyPatternCode(command.getMonthlyPattern()) : null);
		return new WokingConditionCommandCustom(workingCond, exLst);
	}
	
}

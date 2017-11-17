/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog.internal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.dom.adapter.ScClassificationAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.ScWorkplaceAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.ClassificationDto;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.WorkplaceDto;
import nts.uk.ctx.at.schedule.dom.employeeinfo.PersonalWorkScheduleCreSet;
import nts.uk.ctx.at.schedule.dom.employeeinfo.WorkScheduleMasterReferenceAtr;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.BasicWorkSetting;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.ClassifiBasicWorkRepository;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.ClassificationBasicWork;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.CompanyBasicWork;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.CompanyBasicWorkRepository;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.WorkplaceBasicWork;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.WorkplaceBasicWorkRepository;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarClass;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarClassRepository;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarCompany;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarCompanyRepository;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarWorkPlaceRepository;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarWorkplace;

/**
 * The Class ScheCreExeBasicWorkSettingHandler.
 */
@Stateless
public class ScheCreExeBasicWorkSettingHandler {
	
	/** The sche cre exe error log handler. */
	@Inject
	private ScheCreExeErrorLogHandler scheCreExeErrorLogHandler;
	
	/** The sc workplace adapter. */
	@Inject
	private ScWorkplaceAdapter scWorkplaceAdapter;
	
	/** The sc classification adapter. */
	@Inject
	private ScClassificationAdapter scClassificationAdapter;
	
	/** The work place basic work repository. */
	@Inject
	private WorkplaceBasicWorkRepository workplaceBasicWorkRepository;
	
	/** The calendar work place repository. */
	@Inject
	private CalendarWorkPlaceRepository calendarWorkPlaceRepository;
	
	/** The classification basic work repository. */
	@Inject
	private ClassifiBasicWorkRepository classificationBasicWorkRepository;
	
	/** The calendar class repository. */
	@Inject
	private CalendarClassRepository calendarClassRepository;
	
	/** The calendar company repository. */
	@Inject
	private CalendarCompanyRepository calendarCompanyRepository;
	
	/** The company basic work repository. */
	@Inject
	private CompanyBasicWorkRepository companyBasicWorkRepository;
	
	/** The Constant MUL_YEAR. */
	public static final int MUL_YEAR = 10000;
	
	/** The Constant MUL_MONTH. */
	public static  final int MUL_MONTH = 100;
	
	/**
	 * Gets the workday division by wkp.
	 *
	 * @param employeeId the employee id
	 * @param workplaceIds the workplace ids
	 * @return the workday division by wkp
	 */
	// 職場の稼働日区分を取得する
	public Optional<Integer> getWorkdayDivisionByWkp(ScheduleCreatorExecutionCommand command, String employeeId,
			List<String> workplaceIds) {
		for (String workplaceId : workplaceIds) {

			// find calendar work place by id
			Optional<CalendarWorkplace> optionalCalendarWorkplace = this.calendarWorkPlaceRepository
					.findCalendarWorkplaceByDate(workplaceId, this.toYearMonthDate(command.getToDate()));

			// check exist data calendar work place
			if (optionalCalendarWorkplace.isPresent()) {
				return Optional.of(optionalCalendarWorkplace.get().getWorkingDayAtr().value);
			}
		}

		// find calendar company by id
		Optional<CalendarCompany> optionalCalendarCompany = this.calendarCompanyRepository
				.findCalendarCompanyByDate(command.getCompanyId(), this.toYearMonthDate(command.getToDate()));
		
		// check exist data calendar company
		if (optionalCalendarCompany.isPresent()) {
			return Optional.of(optionalCalendarCompany.get().getWorkingDayAtr().value);
		}
		// add error messageId Msg_588
		this.scheCreExeErrorLogHandler.addError(command, employeeId, "Msg_588");
		return Optional.empty();
	}
	
	/**
	 * To basic work setting.
	 *
	 * @param domain the domain
	 * @param workdayAtr the workday atr
	 * @return the optional
	 */
	private Optional<BasicWorkSetting> toBasicWorkSetting(WorkplaceBasicWork domain,
			int workdayAtr) {
		
		// check of basic work by work day atr
		for (BasicWorkSetting basicWorkSetting : domain.getBasicWorkSetting()) {
			if (basicWorkSetting.getWorkdayDivision().value == workdayAtr) {
				return Optional.ofNullable(basicWorkSetting);
			}
		}
		return Optional.empty();
	}
	/**
	 * Gets the basic work setting.
	 *
	 * @param workplaceIds the workplace ids
	 * @param workdayAtr the workday atr
	 * @return the basic work setting
	 */
	// 職場の基本勤務設定を取得する
	private Optional<BasicWorkSetting> getBasicWorkSetting(List<String> workplaceIds, int workdayAtr) {
		for (String workplaceId : workplaceIds) {

			// find basic work by id
			Optional<WorkplaceBasicWork> optionalWorkplaceBasicWork = this.workplaceBasicWorkRepository
					.findById(workplaceId);

			// check exist data WorkplaceBasicWork
			if (optionalWorkplaceBasicWork.isPresent()) {
				return this.toBasicWorkSetting(optionalWorkplaceBasicWork.get(), workdayAtr);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the basic work setting by workday division.
	 *
	 * @param personalWorkScheduleCreSet the personal work schedule cre set
	 * @param workdayDivision the workday division
	 * @return the basic work setting by workday division
	 */
	// 「稼働日区分」に対応する「基本勤務設定」を取得する
	private Optional<BasicWorkSetting> getBasicWorkSettingByWorkdayDivision(ScheduleCreatorExecutionCommand command,
			PersonalWorkScheduleCreSet personalWorkScheduleCreSet, int workdayDivision) {
		// check 営業日カレンダーの参照先 is 職場 (referenceBusinessDayCalendar is WORKPLACE)
		if (personalWorkScheduleCreSet.getWorkScheduleBusCal().getReferenceBusinessDayCalendar()
				.equals(WorkScheduleMasterReferenceAtr.WORKPLACE)) {

			// find work place by id
			Optional<WorkplaceDto> optionalWorkplace = this.scWorkplaceAdapter
					.findWorkplaceById(personalWorkScheduleCreSet.getEmployeeId(), command.getToDate());

			// check exist data work place
			if (optionalWorkplace.isPresent()) {
				WorkplaceDto workplaceDto = optionalWorkplace.get();

				// find by level work place
				List<String> workplaceIds = this.findLevelWorkplace(command, workplaceDto.getWorkplaceId());

				// return basic work setting
				return this.getBasicWorkSetting(workplaceIds, workdayDivision);

			} else {
				// add log error employee => 602
				this.scheCreExeErrorLogHandler.addError(command, personalWorkScheduleCreSet.getEmployeeId(), "Msg_602");
			}

		}
		// 営業日カレンダーの参照先 is 分類 (referenceBusinessDayCalendar is CLASSIFICATION)
		else {
			// find classification by id
			Optional<ClassificationDto> optionalClass = this.scClassificationAdapter
					.findByDate(personalWorkScheduleCreSet.getEmployeeId(), command.getToDate());
			
			// check exist data classification
			if (optionalClass.isPresent()) {
				
				// return basic work setting by classification
				return this.getBasicWorkSettingByClassification(command, personalWorkScheduleCreSet.getEmployeeId(),
						optionalClass.get().getClassificationCode(), workdayDivision);
			} else {

				// add message error log 602
				this.scheCreExeErrorLogHandler.addError(command, personalWorkScheduleCreSet.getEmployeeId(), "Msg_602");
			}
		}
		
		// return default optional
		return Optional.empty();
	}

	/**
	 * Find level work place.
	 *
	 * @param command the command
	 * @param workplaceId the workplace id
	 * @return the list
	 */
	// 所属職場を含む上位職場を取得
	private List<String> findLevelWorkplace(ScheduleCreatorExecutionCommand command, String workplaceId) {
		return this.scWorkplaceAdapter.findWpkIdList(command.getCompanyId(), workplaceId, command.getToDate().date());
	}

	/**
	 * Gets the basic work setting.
	 *
	 * @param personalWorkScheduleCreSet the personal work schedule cre set
	 * @return the basic work setting
	 */
	// 基本勤務設定を取得する
	public Optional<BasicWorkSetting> getBasicWorkSetting(ScheduleCreatorExecutionCommand command,
			PersonalWorkScheduleCreSet personalWorkScheduleCreSet) {

		// get work day atr by data business day calendar
		Optional<Integer> optionalBusinessDayCalendar = this.getBusinessDayCalendar(command,
				personalWorkScheduleCreSet);

		// check exist data
		if (optionalBusinessDayCalendar.isPresent()) {
			return this.getBasicWorkSettingByWorkdayDivision(command, personalWorkScheduleCreSet,
					optionalBusinessDayCalendar.get());
		}
		// return default optional
		return Optional.empty();
	}
	
	/**
	 * To year month date.
	 *
	 * @param baseDate the base date
	 * @return the big decimal
	 */
	private BigDecimal toYearMonthDate(GeneralDate baseDate) {
		return new BigDecimal(baseDate.year() * MUL_YEAR + baseDate.month() * MUL_MONTH + baseDate.day());
	}
	/**
	 * Gets the workday division by class.
	 *
	 * @param employeeId the employee id
	 * @param classficationCode the classfication code
	 * @return the workday division by class
	 */
	// 分類の稼働日区分を取得する
	private Optional<Integer> getWorkdayDivisionByClass(ScheduleCreatorExecutionCommand command,
			String employeeId, String classficationCode) {
		
		// find calendar classification by id
		Optional<CalendarClass> optionalCalendarClass = this.calendarClassRepository
				.findCalendarClassByDate(command.getCompanyId(), classficationCode,
						this.toYearMonthDate(command.getToDate()));
		
		// check exist data
		if (optionalCalendarClass.isPresent()) {
			return Optional.ofNullable(optionalCalendarClass.get().getWorkingDayAtr().value);
		} else {
			
			// find calendar company by id
			Optional<CalendarCompany> optionalCalendarCompany = this.calendarCompanyRepository
					.findCalendarCompanyByDate(command.getCompanyId(), this.toYearMonthDate(command.getToDate()));
			
			// check exits data
			if (optionalCalendarCompany.isPresent()) {
				return Optional.ofNullable(optionalCalendarCompany.get().getWorkingDayAtr().value);
			}
			
			// add error messageId Msg_588
			this.scheCreExeErrorLogHandler.addError(command, employeeId, "Msg_588");
			return Optional.empty();
		}
	}
	
	/**
	 * Gets the business day calendar.
	 *
	 * @param personalWorkScheduleCreSet the personal work schedule cre set
	 * @return the business day calendar
	 */
	// 営業日カレンダーから「稼働日区分」を取得する
	private Optional<Integer> getBusinessDayCalendar(ScheduleCreatorExecutionCommand command,
			PersonalWorkScheduleCreSet personalWorkScheduleCreSet) {
		// check 営業日カレンダーの参照先 is 職場 (referenceBusinessDayCalendar is WORKPLACE)
		if (personalWorkScheduleCreSet.getWorkScheduleBusCal().getReferenceBusinessDayCalendar()
				.equals(WorkScheduleMasterReferenceAtr.WORKPLACE)) {

			// find work place by id
			Optional<WorkplaceDto> optionalWorkplace = this.scWorkplaceAdapter
					.findWorkplaceById(personalWorkScheduleCreSet.getEmployeeId(), command.getToDate());

			// check exist data work place
			if (optionalWorkplace.isPresent()) {
				WorkplaceDto workplaceDto = optionalWorkplace.get();
				List<String> workplaceIds = this.findLevelWorkplace(command, workplaceDto.getWorkplaceId());
				
				// return work day atr by work place id
				return this.getWorkdayDivisionByWkp(command, personalWorkScheduleCreSet.getEmployeeId(), workplaceIds);
			} else {
				// add log error employee => 602
				this.scheCreExeErrorLogHandler.addError(command, personalWorkScheduleCreSet.getEmployeeId(), "Msg_602");
			}

		} else
		// CLASSIFICATION
		{
			// find classification by id
			Optional<ClassificationDto> optionalClassification = this.scClassificationAdapter
					.findByDate(personalWorkScheduleCreSet.getEmployeeId(), command.getToDate());
			
			// check exist data classification
			if (optionalClassification.isPresent()) {
				ClassificationDto classificationDto = optionalClassification.get();
				
				// return work day atr by classification
				return this.getWorkdayDivisionByClass(command, personalWorkScheduleCreSet.getEmployeeId(),
						classificationDto.getClassificationCode());

			} else {
				// add log error employee => 602
				this.scheCreExeErrorLogHandler.addError(command, personalWorkScheduleCreSet.getEmployeeId(), "Msg_602");
			}
		}
		return Optional.empty();

	}
	
	/**
	 * To basic work setting classification.
	 *
	 * @param domain the domain
	 * @param workdayAtr the workday atr
	 * @return the optional
	 */
	private Optional<BasicWorkSetting> toBasicWorkSettingClassification(
			ClassificationBasicWork domain, int workdayAtr) {
		
		// find by work day atr
		for (BasicWorkSetting basicWorkSetting : domain.getBasicWorkSetting()) {
			if (basicWorkSetting.getWorkdayDivision().value == workdayAtr) {
				return Optional.ofNullable(basicWorkSetting);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * To basic work setting company.
	 *
	 * @param domain the domain
	 * @param workdayAtr the workday atr
	 * @return the optional
	 */
	private Optional<BasicWorkSetting> toBasicWorkSettingCompany(CompanyBasicWork domain,
			int workdayAtr) {
		
		// find by work day atr
		for (BasicWorkSetting basicWorkSetting : domain.getBasicWorkSetting()) {
			if (basicWorkSetting.getWorkdayDivision().value == workdayAtr) {
				return Optional.ofNullable(basicWorkSetting);
			}
		}
		return Optional.empty();
	}
	/**
	 * Gets the basic work setting by classification.
	 *
	 * @param classificationCode the classification code
	 * @param workdayAtr the workday atr
	 * @return the basic work setting by classification
	 */
	// 分類の基本勤務設定を取得する
	private Optional<BasicWorkSetting> getBasicWorkSettingByClassification(
			ScheduleCreatorExecutionCommand command, String employeeId, String classificationCode,
			int workdayAtr) {
		// find classification basic work by id
		Optional<ClassificationBasicWork> optionalClassificationBasicWork = this.classificationBasicWorkRepository
				.findById(command.getCompanyId(), classificationCode, workdayAtr);
		
		// check exist data classification basic work
		if (optionalClassificationBasicWork.isPresent()) {
			
			// return basic work setting by classification
			return this.toBasicWorkSettingClassification(optionalClassificationBasicWork.get(),
					workdayAtr);
		} else {
			
			// find company basic work by id
			Optional<CompanyBasicWork> optionalCompanyBasicWork = this.companyBasicWorkRepository
					.findById(command.getCompanyId(), workdayAtr);

			// check exist data company basic work 
			if (optionalCompanyBasicWork.isPresent()) {
				return this.toBasicWorkSettingCompany(optionalCompanyBasicWork.get(), workdayAtr);
			} else {
				
				// add error message 589
				this.scheCreExeErrorLogHandler.addError(command, employeeId, "Msg_589");
			}
		}
		return Optional.empty();
	}
}

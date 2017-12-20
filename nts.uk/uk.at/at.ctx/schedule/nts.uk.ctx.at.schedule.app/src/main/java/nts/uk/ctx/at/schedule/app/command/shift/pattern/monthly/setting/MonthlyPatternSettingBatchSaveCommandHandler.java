/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.shift.pattern.monthly.setting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.app.find.shift.pattern.WeeklyWorkSettingFinder;
import nts.uk.ctx.at.schedule.app.find.shift.pattern.dto.WeeklyWorkSettingDto;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.WorkdayDivision;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.holiday.PublicHoliday;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.holiday.PublicHolidayRepository;
import nts.uk.ctx.at.schedule.dom.shift.pattern.monthly.MonthlyPattern;
import nts.uk.ctx.at.schedule.dom.shift.pattern.monthly.MonthlyPatternRepository;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySetting;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySettingRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class MonthlyPatternSettingBatchSaveCommandHandler.
 */
@Stateless
public class MonthlyPatternSettingBatchSaveCommandHandler
		extends CommandHandler<MonthlyPatternSettingBatchSaveCommand> {
	
	/** The monthly pattern repository. */
	@Inject
	private MonthlyPatternRepository monthlyPatternRepository;
	
	/** The public holiday repository. */
	@Inject
	private PublicHolidayRepository publicHolidayRepository;
	
	/** The work monthly setting repository. */
	@Inject
	private WorkMonthlySettingRepository workMonthlySettingRepository;
	
	/** The weekly work setting finder. */
	@Inject
	private WeeklyWorkSettingFinder weeklyWorkSettingFinder;
	
	
	/** The Basic schedule service. */
	@Inject
	private BasicScheduleService basicScheduleService;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<MonthlyPatternSettingBatchSaveCommand> context) {
		
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();
		
		// get company id
		String companyId = loginUserContext.companyId();
		
		// get command
		MonthlyPatternSettingBatchSaveCommand command = context.getCommand();
		

		// check pair work days
		basicScheduleService.checkPairWorkTypeWorkTime(command.getSettingWorkDays().getWorkTypeCode(),
				command.getSettingWorkDays().getWorkingCode());

		// check pair statutory holiday
		basicScheduleService.checkPairWorkTypeWorkTime(
				command.getSettingStatutoryHolidays().getWorkTypeCode(),
				command.getSettingStatutoryHolidays().getWorkingCode());

		// check pair none statutory holiday
		basicScheduleService.checkPairWorkTypeWorkTime(
				command.getSettingNoneStatutoryHolidays().getWorkTypeCode(),
				command.getSettingNoneStatutoryHolidays().getWorkingCode());

		// check pair public holiday
		basicScheduleService.checkPairWorkTypeWorkTime(
				command.getSettingPublicHolidays().getWorkTypeCode(),
				command.getSettingPublicHolidays().getWorkingCode());

		// find by id monthly pattern code
		Optional<MonthlyPattern> opMonthlyPattern = this.monthlyPatternRepository
				.findById(companyId, command.getMonthlyPatternCode());
		
		// check exist data 
		if(opMonthlyPattern.isPresent()){
			this.monthlyPatternRepository.update(command.toDomainMonthlyPattern(companyId));
		}
		else {
			this.monthlyPatternRepository.add(command.toDomainMonthlyPattern(companyId));
		}
		
		// startDate
		Date toStartDate = this.toDate(command.getStartYearMonth() * MONTH_MUL + NEXT_DAY);
		
		// data update setting batch
		List<WorkMonthlySetting> updateWorkMonthlySettings = new ArrayList<>();
		
		// data insert setting batch
		List<WorkMonthlySetting> addWorkMonthlySettings = new ArrayList<>();
		
		
		// check by next day of begin end
		while (this.getYearMonth(toStartDate) <= command.getEndYearMonth()) {
			
			// get ymd to day
			int ymdk = this.getYearMonthDate(toStartDate);
			
			int year = ymdk/10000;
			
			int month = (ymdk - year*10000)/100;
			
			int day = (ymdk - year*10000 - month*100);
			
			GeneralDate date = GeneralDate.ymd(year, month, day);
			
			// check public holiday by base date
			Optional<PublicHoliday> publicHoliday = this.publicHolidayRepository.getHolidaysByDate(companyId, date);

			// find by id
			Optional<WorkMonthlySetting> workMonthlySetting = this.workMonthlySettingRepository
					.findById(companyId, command.getMonthlyPatternCode(), ymdk);
			
			// check day is public holiday
			if (publicHoliday.isPresent()) {

				// data public holiday setting
				WorkMonthlySetting dataPublic = command.toDomainPublicHolidays(companyId, ymdk);
				
				// check exist data
				if (!workMonthlySetting.isPresent()) {
					addWorkMonthlySettings.add(dataPublic);
				} else if (command.isOverwrite()) {
					updateWorkMonthlySettings.add(dataPublic);
				}
			} else {
				WeeklyWorkSettingDto dto = this.weeklyWorkSettingFinder
						.checkWeeklyWorkSetting(this.getBaseDate(toStartDate));
				
				// is work day
				switch (EnumAdaptor.valueOf(dto.getWorkdayDivision(), WorkdayDivision.class)) {
				case WORKINGDAYS:
					// data working day setting
					WorkMonthlySetting dataWorking = command.toDomainWorkDays(companyId, ymdk);
					
					// check exist data
					if (!workMonthlySetting.isPresent()) {
						addWorkMonthlySettings.add(dataWorking);
					} else if (command.isOverwrite()) {
						updateWorkMonthlySettings.add(dataWorking);
					}
					
					break;
					
					// is none statutory holiday
				case NON_WORKINGDAY_EXTRALEGAL:
					
					// data none statutory holiday setting
					WorkMonthlySetting dataNoneStatutory = command
							.toDomainNoneStatutoryHolidays(companyId, ymdk);
					
					// check exist data
					if (!workMonthlySetting.isPresent()) {
						addWorkMonthlySettings.add(dataNoneStatutory);
					} else if (command.isOverwrite()) {
						updateWorkMonthlySettings.add(dataNoneStatutory);
					}
					
					break;
					
					// is statutory holiday
				case NON_WORKINGDAY_INLAW:
					
					// data none statutory holiday setting
					WorkMonthlySetting dataStatutory = command.toDomainStatutoryHolidays(companyId,
							ymdk);
					
					// check exist data
					if (!workMonthlySetting.isPresent()) {
						addWorkMonthlySettings.add(dataStatutory);
					} else if (command.isOverwrite()) {
						updateWorkMonthlySettings.add(dataStatutory);
					}
					
					break;
				}
			}
			toStartDate = this.nextDay(toStartDate);
		}
		// add all data setting
		this.workMonthlySettingRepository.addAll(addWorkMonthlySettings);
		
		// update all data setting
		this.workMonthlySettingRepository.updateAll(updateWorkMonthlySettings);
	}
	
	/** The Constant NEXT_DAY. */
	public static final int NEXT_DAY = 1;
	
	/** The Constant BEGIN_END_MONTH. */
	public static final int BEGIN_END_MONTH = 12;
	
	/** The Constant ZERO_DAY_MONTH. */
	public static final int ZERO_DAY_MONTH = 0;
	
	/** The Constant YEAR_MUL. */
	public static final int YEAR_MUL = 10000;
	
	/** The Constant MONTH_MUL. */
	public static final int MONTH_MUL = 100;

	/**
	 * Next day.
	 *
	 * @param day the day
	 * @return the date
	 */
	public Date nextDay(Date day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.add(Calendar.DAY_OF_MONTH, NEXT_DAY); 
		return cal.getTime();
	}
	
	/**
	 * Previous day.
	 *
	 * @param day the day
	 * @return the date
	 */
	public Date previousDay(Date day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.add(Calendar.DAY_OF_MONTH, -NEXT_DAY); 
		return cal.getTime();
	}
	
	/**
	 * Gets the year month date.
	 *
	 * @param day the day
	 * @return the year month date
	 */
	public int getYearMonthDate(Date day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		return cal.get(Calendar.YEAR) * YEAR_MUL + (cal.get(Calendar.MONTH) + NEXT_DAY) * MONTH_MUL
				+ cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Gets the year month.
	 *
	 * @param day the day
	 * @return the year month
	 */
	public int getYearMonth(Date day) {
		return getYearMonthDate(day) / MONTH_MUL;
	}
	
	/**
	 * Gets the year month.
	 *
	 * @param day the day
	 * @return the year month
	 */
	public GeneralDate getBaseDate(Date day) {
		Calendar cal = Calendar.getInstance();
		return GeneralDate.ymd(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + NEXT_DAY),
				cal.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * To date.
	 *
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @return the date
	 */
	public Date toDate(int yearMonthDate) {
		Calendar cal = Calendar.getInstance();
		cal.set(yearMonthDate / YEAR_MUL, (yearMonthDate % YEAR_MUL) / MONTH_MUL - NEXT_DAY,
				yearMonthDate % MONTH_MUL, ZERO_DAY_MONTH, ZERO_DAY_MONTH);
		return cal.getTime();
	}
	
	
}

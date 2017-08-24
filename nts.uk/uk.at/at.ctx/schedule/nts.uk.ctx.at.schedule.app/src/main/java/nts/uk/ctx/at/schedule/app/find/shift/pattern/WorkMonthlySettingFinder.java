/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.shift.pattern;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.i18n.custom.IInternationalization;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.schedule.app.find.shift.pattern.dto.WorkMonthlySettingDto;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySetting;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.WorkTime;
import nts.uk.ctx.at.shared.dom.worktime.WorkTimeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class WorkMonthlySettingFinder.
 */
@Stateless
public class WorkMonthlySettingFinder {
	
	/** The Constant START_DAY. */
	public static final int START_DAY = 1;
	
	/** The Constant NEXT_DAY. */
	public static final int NEXT_DAY = 1;
	
	/** The Constant YEAR_MUL. */
	public static final int YEAR_MUL = 10000;
	
	/** The Constant MONTH_MUL. */
	public static final int MONTH_MUL = 100;
	
	 /** The Constant HOLIDAY. */
 	public static final int HOLIDAY = 0;
     
     /** The Constant ATTENDANCE. */
     public static final int ATTENDANCE = 1;
     
     /** The Constant NONE_SETTING. */
     public static final String NONE_SETTING = "KSM005_43";
     
	/** The work monthly setting repository. */
	@Inject
	private WorkMonthlySettingRepository workMonthlySettingRepository;
	
	/** The work time repository. */
	@Inject
	private WorkTimeRepository workTimeRepository;
	
	/** The work type repository. */
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	/** The internationalization. */
	@Inject
	private IInternationalization internationalization;
	
	/**
	 * Find by month.
	 *
	 * @param monthlyPatternCode the monthly pattern code
	 * @param yearMonth the year month
	 * @return the list
	 */
	
	public List<WorkMonthlySettingDto> findByMonth(String monthlyPatternCode, int yearMonth){

		// month setting by connection
		YearMonth monthSetting = YearMonth.of(yearMonth);
		
		// set start month
		GeneralDate startMonth = GeneralDate.ymd(monthSetting.year(), monthSetting.month(),
				START_DAY);
		
		// set end month
		GeneralDate endMonth = GeneralDate.ymd(monthSetting.nextMonth().year(),
				monthSetting.nextMonth().month(), START_DAY);
		
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();
		
		// get company id
		String companyId = loginUserContext.companyId();
		
		// call repository find by month
		List<WorkMonthlySettingDto> workMonthlySettings = this.workMonthlySettingRepository
				.findByStartEndDate(companyId, monthlyPatternCode, this.getYearMonthDay(startMonth),
						this.getYearMonthDay(endMonth))
				.stream()
				.map(domain -> {
					WorkMonthlySettingDto dto = new WorkMonthlySettingDto();
					domain.saveToMemento(dto);
					return dto;
				}).collect(Collectors.toList());
		
		// convert to map
		Map<Integer, WorkMonthlySettingDto> mapWorkMonthlySetting = workMonthlySettings.stream()
				.collect(Collectors.toMap((settings) -> {
					return settings.getYmdk();
				}, Function.identity()));

		List<WorkMonthlySettingDto> resDataWorkMonthlySetting  = new ArrayList<>();
		
		// list work time of company id
		List<WorkTime> workTimes = workTimeRepository.findByCompanyID(companyId);
		
		// convert to map work time map
		Map<String, WorkTime> mapWorkTime = workTimes.stream()
				.collect(Collectors.toMap((worktime) -> {
					return worktime.getSiftCD().v();
				}, Function.identity()));
		
		// list work type of company id
		List<WorkType> workTypes = workTypeRepository.findByCompanyId(companyId);
		
		// convert to map work type map
		Map<String, WorkType> mapWorkType = workTypes.stream()
				.collect(Collectors.toMap((worktype) -> {
					return worktype.getWorkTypeCode().v();
				}, Function.identity()));

		// loop in month setting
		while (this.getYearMonth(startMonth) == yearMonth) {
			
			// check exist data
			if (mapWorkMonthlySetting.containsKey(this.getYearMonthDay(startMonth))) {
				
				WorkType worktype = null;
				WorkTime worktime = null;
				
				// get data object by year month day of start month
				WorkMonthlySettingDto dto = mapWorkMonthlySetting
						.get(this.getYearMonthDay(startMonth));

				// check type color
				if (mapWorkTime.containsKey(dto.getWorkingCode())
						&& mapWorkType.containsKey(dto.getWorkTypeCode())) {
					
					// set type color ATTENDANCE
					dto.setTypeColor(ATTENDANCE);
					
					// get work type of map
					worktype = mapWorkType.get(dto.getWorkTypeCode());
					
					// set work type name
					dto.setWorkTypeName(worktype.getName().v());
					
					// get work time of map
					worktime = mapWorkTime.get(dto.getWorkingCode());
					
					// set work time name
					dto.setWorkingName(worktime.getWorkTimeDisplayName().getWorkTimeName().v());
				}
				else {
					
					// set type color HOLIDAY
					dto.setTypeColor(HOLIDAY);

					// check exist work type of map
					if (mapWorkType.containsKey(dto.getWorkTypeCode())) {
						// get work type of map
						worktype = mapWorkType.get(dto.getWorkTypeCode());

						// set work type name
						dto.setWorkTypeName(worktype.getName().v());
					} else {
						// set work type name NONE_SETTING
						dto.setWorkTypeName(internationalization.getItemName(NONE_SETTING).get());
					}
					
					// set work time name ""
					dto.setWorkingName("");
				}
				resDataWorkMonthlySetting.add(dto);
				
			} else {
				// data default
				WorkMonthlySettingDto dto = new WorkMonthlySettingDto();
				WorkMonthlySetting domain = new WorkMonthlySetting(
						BigDecimal.valueOf(this.getYearMonthDay(startMonth)), monthlyPatternCode);
				domain.saveToMemento(dto);
				
				// set type color HOLIDAY
				dto.setTypeColor(HOLIDAY);
				
				// set work type name ""
				dto.setWorkTypeName("");

				// set work time name ""
				dto.setWorkingName("");
				
				resDataWorkMonthlySetting.add(dto);
			}
			startMonth = startMonth.addDays(NEXT_DAY);
		}
		return resDataWorkMonthlySetting;
	}
	
	/**
	 * Gets the year month day.
	 *
	 * @param baseDate the base date
	 * @return the year month day
	 */
	public int getYearMonthDay(GeneralDate baseDate){
		return baseDate.year() * YEAR_MUL + baseDate.month() * MONTH_MUL + baseDate.day();
	}
	
	/**
	 * Gets the year month.
	 *
	 * @param baseDate the base date
	 * @return the year month
	 */
	public int getYearMonth(GeneralDate baseDate){
		return baseDate.year() * MONTH_MUL + baseDate.month();
	}

}

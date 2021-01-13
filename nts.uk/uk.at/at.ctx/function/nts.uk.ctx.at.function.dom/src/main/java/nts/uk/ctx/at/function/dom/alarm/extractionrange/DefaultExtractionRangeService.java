package nts.uk.ctx.at.function.dom.alarm.extractionrange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingAdapter;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingImport;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSettingRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.Period;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.EndSpecify;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.ExtractionPeriodDaily;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.StartSpecify;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.month.ExtractionPeriodMonth;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.month.SpecifyEndMonth;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.month.SpecifyStartMonth;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.month.mutilmonth.AverageMonth;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.periodunit.ExtractionPeriodUnit;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.year.AYear;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfPublicHoliday;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHoliday;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHolidayGrantDate;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHolidayManagementClassification;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHolidayPeriod;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHolidaySetting;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHolidaySettingRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class DefaultExtractionRangeService implements ExtractionRangeService {

	@Inject
	private AlarmPatternSettingRepository alarmRepo;
	@Inject
	private ClosureRepository closureRepo;
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;
	@Inject
	private PublicHolidaySettingRepository publicHolidaySettingRepo;
	@Inject
	private AgreementOperationSettingAdapter agreementOperationSettingAdapter;
	

	@Override
	public List<CheckConditionTimeDto> getPeriodByCategory(String alarmCode, String companyId, int closureId,
			Integer processingYm) {
		List<CheckConditionTimeDto> result = new ArrayList<CheckConditionTimeDto>();
		List<CheckCondition> checkConList = alarmRepo.getCheckCondition(companyId, alarmCode);

		checkConList.forEach(c -> {
			if (c.isDaily() || c.isApplication()) {
				CheckConditionTimeDto daily = this.getDailyTime(c, closureId, new YearMonth(processingYm));
				result.add(daily);
				
			} else if (c.is4W4D()) {
				CheckConditionTimeDto schedual_4week = this.get4WeekTime(c, closureId, new YearMonth(processingYm), companyId);
				result.add(schedual_4week);
				
			} else if(c.isMonthly() || c.isMultipleMonth()) {
				CheckConditionTimeDto other = this.getMonthlyTime(c,closureId,new YearMonth(processingYm));
				result.add(other);
				
			} else if(c.isAgrrement()) {
				result.addAll(this.getAgreementTime(c, closureId, new YearMonth(processingYm), companyId));
			} else if(c.isAttHoliday() || c.isMasterChk()) {
				CheckConditionTimeDto attHoliday = new CheckConditionTimeDto(c.getAlarmCategory().value, c.getAlarmCategory().nameId, 0);
				attHoliday.setTabOrder(7);
				result.add(attHoliday);
			}
			

		});
		
        Collections.sort(result, Comparator.comparing(CheckConditionTimeDto::getCategory)
                .thenComparing(CheckConditionTimeDto::getTabOrder));
		return result;
	}
	
	//36協定の期間を算出する
	private List<CheckConditionTimeDto> getAgreementTime(CheckCondition c, int closureId, YearMonth yearMonth, String cid){
		List<CheckConditionTimeDto> result = new ArrayList<CheckConditionTimeDto>();
		
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = null;
		Date endDate = null;
//		String startMonth =null;
//		String endMonth = null;
		YearMonth startMonth = yearMonth;
		YearMonth endMonth = yearMonth;
		int year = 0;
		//int checkMonth = 1;
		for(ExtractionRangeBase extractBase : c.getExtractPeriodList()) {
			
			if(extractBase instanceof ExtractionPeriodDaily) {
				ExtractionPeriodDaily extraction = (ExtractionPeriodDaily) extractBase;				
				CheckConditionPeriod period  = this.getPeriodDaily(extraction, closureId, yearMonth);
				startDate = period.getStartDate();
				endDate = period.getEndDate();
				CheckConditionTimeDto dailyDto = new CheckConditionTimeDto(c.getAlarmCategory().value, 
						textAgreementTime(1), formatter.format(startDate), formatter.format(endDate), null, null);
				dailyDto.setTabOrder(1);
				dailyDto.setPeriod36Agreement(Period.One_Week.value);
				result.add(dailyDto);
				
			}else if(extractBase instanceof ExtractionPeriodMonth) {
				ExtractionPeriodMonth extraction = (ExtractionPeriodMonth) extractBase;
				
				if(extraction.getStartMonth().isDesignateMonth()) {
					startMonth = yearMonth.addMonths((-1)*extraction.getStartMonth().getStrMonthNo().get().getMonthNo());
				}else {
					Calendar calendar = Calendar.getInstance();
					int currentYear = calendar.get(Calendar.YEAR);
					startMonth = YearMonth.of(currentYear, extraction.getStartMonth().getFixedMonthly().get().getDesignatedMonth());
				}
				
				if(extraction.getEndMonth().isSpecifyClose()) {
					endMonth = yearMonth.addMonths((-1)*extraction.getEndMonth().getEndMonthNo().get().getMonthNo());
				}else {
					endMonth = yearMonth.addMonths((-1)*extraction.getEndMonth().getEndMonthNo().get().getMonthNo());
				}
				CheckConditionTimeDto monthDto = new CheckConditionTimeDto(c.getAlarmCategory().value, 
						textAgreementTime(extraction.getNumberOfMonth().value +2), null, null, startMonth.toString(), endMonth.toString());
				monthDto.setTabOrder(extraction.getNumberOfMonth().value +2);
				if (extraction.getNumberOfMonth() == NumberOfMonth.ONE_MONTH) {
					monthDto.setPeriod36Agreement(Period.One_Month.value);
				} else if (extraction.getNumberOfMonth() == NumberOfMonth.TWO_MONTH) {
					monthDto.setPeriod36Agreement(Period.Two_Month.value);
				} else if (extraction.getNumberOfMonth() == NumberOfMonth.THREE_MONTH) {
					monthDto.setPeriod36Agreement(Period.Three_Month.value);
				}
				//checkMonth++;
				result.add(monthDto);
			//基準月の期間を算出する
			}else if(extractBase instanceof AverageMonth) {
				AverageMonth extraction = (AverageMonth) extractBase;
				CheckConditionTimeDto monthDto = new CheckConditionTimeDto(c.getAlarmCategory().value, 
						TextResource.localize("KAL010_208") + "　" + TextResource.localize("KAL004_120"), null, null, 
						yearMonth.toString(), yearMonth.toString());
				if(extraction.getStrMonth() != StandardMonth.CURRENT_MONTH ) {
					monthDto.setStartMonth(yearMonth.addMonths(-extraction.getStrMonth().value).toString());
					monthDto.setEndMonth(yearMonth.addMonths(-extraction.getStrMonth().value).toString());
				}
				monthDto.setTabOrder(6);
				monthDto.setPeriod36Agreement(Period.Months_Average.value);
				result.add(monthDto);
				
			}else if(extractBase instanceof AYear) {
				AYear extraction = (AYear) extractBase;
				Optional<AgreementOperationSettingImport> agreementOperationSettingImport =  agreementOperationSettingAdapter.findForAlarm(cid);
				int firstMonth = 0;
				int yearMonthYear = yearMonth.year();
				if (agreementOperationSettingImport.isPresent()){
					firstMonth = agreementOperationSettingImport.get().getStartingMonth().value + 1;
					if(extraction.isToBeThisYear()){
						if(firstMonth <= yearMonth.month()) {
							year = yearMonthYear;
						}else {
							year = yearMonthYear - 1;
						}
						if (firstMonth == 1) {
							startMonth = YearMonth.of(year, firstMonth);
							endMonth = YearMonth.of(year, 12);
						} else {
							startMonth = YearMonth.of(year, firstMonth);
							firstMonth = firstMonth - 1;
							endMonth = YearMonth.of(year + 1, firstMonth);
						}
						
					}else {
						year = extraction.getYear();
						if (firstMonth == 1) {
							startMonth = YearMonth.of(year, firstMonth);
							endMonth = YearMonth.of(year, 12);
						}else{
							startMonth = YearMonth.of(year, firstMonth);
							firstMonth = firstMonth - 1;
							endMonth = YearMonth.of(year + 1, firstMonth);
						}
					}
				} else {
					startMonth = YearMonth.of(GeneralDate.today().year(), 1);
					endMonth = YearMonth.of(GeneralDate.today().year(), 12);
				}
				CheckConditionTimeDto yearDto = new CheckConditionTimeDto(c.getAlarmCategory().value,
						textAgreementTime(5), null, null, startMonth.toString(), endMonth.toString(), year );
				yearDto.setTabOrder(5);
				yearDto.setPeriod36Agreement(Period.Yearly.value);
				result.add(yearDto);
			}
		}
		
		result.sort((a, b) ->  a.getCategoryName().compareTo(b.getCategoryName()));
		return result;
	}
	
	private String textAgreementTime(int tabOrder) {
		switch(tabOrder) {
			case 1 : return TextResource.localize("KAL010_208") + "　" + TextResource.localize("KAL004_69");
			case 2 : return TextResource.localize("KAL010_208") + "　" + TextResource.localize("KAL004_70"); 
			case 3 : return TextResource.localize("KAL010_208") + "　" + TextResource.localize("KAL004_71"); 
			case 4 : return TextResource.localize("KAL010_208") + "　" + TextResource.localize("KAL004_72"); 
			case 5 : return TextResource.localize("KAL010_208") + "　" + TextResource.localize("KAL004_73"); 
			default : return "";
		}
		
	}
	
	//日次単位の期間の算出する
	public CheckConditionTimeDto getDailyTime(CheckCondition c, int closureId, YearMonth yearMonth) {
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = null;
		Date endDate = null;
	
		ExtractionPeriodDaily extraction = (ExtractionPeriodDaily) c.getExtractPeriodList().get(0);
		
		CheckConditionPeriod period = this.getPeriodDaily(extraction, closureId, yearMonth);
		startDate = period.getStartDate();
		endDate = period.getEndDate();
		
		return new CheckConditionTimeDto(c.getAlarmCategory().value,
				EnumAdaptor.convertToValueName(c.getAlarmCategory()).getLocalizedName(), formatter.format(startDate),
				formatter.format(endDate), null, null);
	}
	
	/**
	 * 月次単位の期間を算出する
	 * @param c
	 * @param closureId
	 * @param yearMonth
	 * @return
	 */
	public CheckConditionTimeDto getMonthlyTime(CheckCondition c, int closureId, YearMonth yearMonth) {
		//DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		YearMonth startMonthly = yearMonth;
		YearMonth endMonthly = yearMonth;
		ExtractionPeriodMonth extractionPeriodMonth =  (ExtractionPeriodMonth) c.getExtractPeriodList().get(0);
		if(extractionPeriodMonth.getStartMonth().getSpecifyStartMonth().value == SpecifyStartMonth.DESIGNATE_CLOSE_START_MONTH.value) {
			startMonthly = yearMonth.addMonths((-1)*extractionPeriodMonth.getStartMonth().getStrMonthNo().get().getMonthNo());
		}
		if(extractionPeriodMonth.getEndMonth().getSpecifyEndMonth().value == SpecifyEndMonth.SPECIFY_CLOSE_END_MONTH.value ) {
			endMonthly = yearMonth.addMonths((-1)*extractionPeriodMonth.getEndMonth().getEndMonthNo().get().getMonthNo());
		}
		
		return new CheckConditionTimeDto(c.getAlarmCategory().value,
				EnumAdaptor.convertToValueName(c.getAlarmCategory()).getLocalizedName(), null,
				null, startMonthly.toString(), endMonthly.toString());
	}
	
	/**
	 * 日次単位の期間の算出する
	 * @param extraction
	 * @param closureId
	 * @param yearMonth
	 * @return
	 */
	private CheckConditionPeriod getPeriodDaily(ExtractionPeriodDaily extraction, int closureId, YearMonth yearMonth ) {
		val require = ClosureService.createRequireM1(closureRepo, closureEmploymentRepo);
		Date startDate =null;
		Date endDate =null;
		YearMonth startMonthly = yearMonth;
		YearMonth endMonthly = yearMonth;
		// Calculate start date
		//ドメインモデル「チェック条件」．抽出期間から開始日の指定方法をチェックする
		if (extraction.getStartDate().getStartSpecify() == StartSpecify.DAYS) {
			Calendar calendar = Calendar.getInstance();
			if (extraction.getStartDate().getStrDays().get().getDayPrevious() == PreviousClassification.BEFORE)
				calendar.add(Calendar.DAY_OF_YEAR, -extraction.getStartDate().getStrDays().get().getDay().v());
			else
				calendar.add(Calendar.DAY_OF_YEAR, extraction.getStartDate().getStrDays().get().getDay().v());
				startDate = calendar.getTime();

		} else {
			DatePeriod datePeriod = ClosureService.getClosurePeriod(require, closureId, startMonthly.addMonths((-1)*extraction.getStartDate().getStrMonth().get().getMonth()));
			startDate = datePeriod.start().date();
		}

		// Calculate endDate
		//メインモデル「チェック条件」．抽出期間から終了日の指定方法をチェックする
		if (extraction.getEndDate().getEndSpecify() == EndSpecify.DAYS) {
			Calendar calendar = Calendar.getInstance();
			if (extraction.getEndDate().getEndDays().get().getDayPrevious() == PreviousClassification.BEFORE)
				calendar.add(Calendar.DAY_OF_YEAR, -extraction.getEndDate().getEndDays().get().getDay().v());
			else
				calendar.add(Calendar.DAY_OF_YEAR, extraction.getEndDate().getEndDays().get().getDay().v());

			endDate = calendar.getTime();
		} else {
			DatePeriod datePeriod = ClosureService.getClosurePeriod(require, closureId, endMonthly.addMonths((-1)*extraction.getEndDate().getEndMonth().get().getMonth()));
			endDate = datePeriod.end().date();
		}
		
		  
		return new CheckConditionPeriod(startDate, endDate);
		
	}
	
	//http://192.168.50.4:3000/issues/112749
	//周期単位の期間を算出する
	public CheckConditionTimeDto get4WeekTime(CheckCondition c, int closureId, YearMonth yearMonth, String companyId) {
		LocalDate sDate = null;
		LocalDate eDate = null;

		ExtractionPeriodUnit periodUnit = (ExtractionPeriodUnit) c.getExtractPeriodList().get(0);
		
		LocalDate countingDate;
		boolean isYMD;
		
		// Get from PublicHolidaySetting domain
//		Optional<PublicHolidaySetting> publicHolidaySettingOpt = publicHolidaySettingRepo.findByCID(companyId);
//		if (!publicHolidaySettingOpt.isPresent())
//			return new CheckConditionTimeDto(c.getAlarmCategory().value,
//					EnumAdaptor.convertToValueName(c.getAlarmCategory()).getLocalizedName(), null, null, null, null);
		
//		if(publicHolidaySettingOpt.get().getPublicHdManagementClassification()==PublicHolidayManagementClassification._1_MONTH) {
//			PublicHolidayGrantDate publicHolidayGrantDate = (PublicHolidayGrantDate)publicHolidaySettingOpt.get().getPublicHdManagementStartDate();
//			
//			if(publicHolidayGrantDate.getPeriod()==PublicHolidayPeriod.FIRST_DAY_TO_LAST_DAY) {
//				String processingMonth = yearMonth.toString();				
//				sDate = LocalDate.of(Integer.valueOf(processingMonth.substring(0, 4)).intValue(), Integer.valueOf(processingMonth.substring(4, 6)).intValue(), 1);
//				eDate = sDate.plusMonths(1);
//				eDate = eDate.minusDays(1);
//				
//			}else {
//				DatePeriod datePeriod = ClosureService.getClosurePeriod(
//															ClosureService.createRequireM1(closureRepo, closureEmploymentRepo),
//															closureId, yearMonth);
//				sDate = datePeriod.start().localDate();
//				eDate = datePeriod.end().localDate();								
//			}
//			
//		}else {
//			PublicHoliday publicHoliday = (PublicHoliday) publicHolidaySettingOpt.get().getPublicHdManagementStartDate();
//			if (publicHoliday.getDetermineStartDate() == DayOfPublicHoliday.DESIGNATE_BY_YEAR_MONTH_DAY) {
//				countingDate= publicHoliday.getDate().localDate();
//				isYMD = true;
//			}else {			
//				String dayMonth= publicHoliday.getDayMonth().toString();
//				if(dayMonth.length()<4) dayMonth ="0" +dayMonth;
//				countingDate = LocalDate.of(LocalDate.now().getYear(), Integer.valueOf(dayMonth.substring(0, 2)).intValue(),
//						Integer.valueOf(dayMonth.substring(2, 4)).intValue());
//				isYMD= false;
//			}
//			LocalDate currentDate = LocalDate.now();
//			if (isYMD) {
//				long totalDays = ChronoUnit.DAYS.between(countingDate, currentDate);
//				long index = totalDays / 28;
//	
//				switch (periodUnit.getSegmentationOfCycle()) {
//				case ThePreviousCycle:
//					index--;
//				case Period:
//					break;
//				case TheNextCycle:
//					index++;
//				default:
//					break;
//				}
//				if (index == -1)
//					throw new RuntimeException("1周期目の期間がシステム日付を含む期間となりますが周期の区分 は 実行日を含む周期の前周期！");
//	
//				sDate = countingDate.plusDays(index * 28);
//				eDate = sDate.plusDays(27);
//	
//			} else {
//				long totalDays = ChronoUnit.DAYS.between(countingDate, currentDate);
//				long index = totalDays / 28;
//	
//				switch (periodUnit.getSegmentationOfCycle()) {
//				case ThePreviousCycle:
//					index--;
//				case Period:
//					break;
//				case TheNextCycle:
//					index++;
//				default:
//					break;
//				}
//	
//				if (index == -1) {
//					countingDate.minusYears(1);
//					index = 13;
//					sDate = countingDate.plusDays(index * 28);
//					eDate = LocalDate.of(countingDate.getYear(), 12, 31);
//				} else if (index == 14) {
//					countingDate.plusYears(1);
//					index = 0;
//					sDate = countingDate.plusDays(index * 28);
//					eDate = sDate.plusDays(27);
//				} else {
//					sDate = countingDate.plusDays(index * 28);
//					eDate = sDate.plusDays(27);
//				}
//	
//			}
//		}
		return new CheckConditionTimeDto(c.getAlarmCategory().value,
				EnumAdaptor.convertToValueName(c.getAlarmCategory()).getLocalizedName(),
				null,
				null, null, null);
	}

}

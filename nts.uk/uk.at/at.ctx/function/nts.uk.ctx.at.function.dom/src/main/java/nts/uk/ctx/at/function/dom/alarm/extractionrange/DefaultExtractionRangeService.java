package nts.uk.ctx.at.function.dom.alarm.extractionrange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSettingRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.EndSpecify;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.ExtractionPeriodDaily;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.StartSpecify;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.periodunit.ExtractionPeriodUnit;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfPublicHoliday;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHoliday;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHolidayManagementStartDate;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHolidaySetting;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.PublicHolidaySettingRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DefaultExtractionRangeService implements ExtractionRangeService {

	@Inject
	private AlarmPatternSettingRepository alarmRepo;
	@Inject
	private ClosureService closureService;
	@Inject
	private PublicHolidaySettingRepository publicHolidaySettingRepo; 
	
	

	@Override
	public List<CheckConditionTimeDto> getPeriodByCategory(String alarmCode, String companyId, int closureId,
			Integer processingYm) {
		List<CheckConditionTimeDto> result = new ArrayList<>();
		List<CheckCondition> checkConList = alarmRepo.getCheckCondition(companyId, alarmCode);

		checkConList.forEach(c -> {
			if (c.getAlarmCategory() == AlarmCategory.DAILY) {

				CheckConditionTimeDto daily = this.getDailyTime(c, closureId, new YearMonth(processingYm));
				result.add(daily);
			} else if (c.getAlarmCategory() == AlarmCategory.SCHEDULE_4WEEK) {

				CheckConditionTimeDto schedual_4week = this.get4WeekTime(c, closureId, new YearMonth(processingYm), companyId);
				result.add(schedual_4week);
			} else if(c.getAlarmCategory() == AlarmCategory.MONTHLY) {
				CheckConditionTimeDto other = new CheckConditionTimeDto(c.getAlarmCategory().value,
						EnumAdaptor.convertToValueName(c.getAlarmCategory()).getLocalizedName(), null, null, null, null);
				result.add(other);
			}

		});
		return result;
	}

	public CheckConditionTimeDto getDailyTime(CheckCondition c, int closureId, YearMonth yearMonth) {
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date startDate = null;
		Date endDate = null;
		ExtractionPeriodDaily extraction = (ExtractionPeriodDaily) c.getExtractPeriod();

		// Calculate start date
		if (extraction.getStartDate().getStartSpecify() == StartSpecify.DAYS) {
			Calendar calendar = Calendar.getInstance();
			if (extraction.getStartDate().getStrDays().get().getDayPrevious() == PreviousClassification.BEFORE)
				calendar.add(Calendar.DAY_OF_YEAR, -extraction.getStartDate().getStrDays().get().getDay().v());
			else
				calendar.add(Calendar.DAY_OF_YEAR, extraction.getStartDate().getStrDays().get().getDay().v());

			startDate = calendar.getTime();

		} else {
			DatePeriod datePeriod = closureService.getClosurePeriod(closureId, yearMonth);
			startDate = datePeriod.start().date();
		}

		// Calculate endDate
		if (extraction.getEndDate().getEndSpecify() == EndSpecify.DAYS) {
			Calendar calendar = Calendar.getInstance();
			if (extraction.getEndDate().getEndDays().get().getDayPrevious() == PreviousClassification.BEFORE)
				calendar.add(Calendar.DAY_OF_YEAR, -extraction.getEndDate().getEndDays().get().getDay().v());
			else
				calendar.add(Calendar.DAY_OF_YEAR, extraction.getEndDate().getEndDays().get().getDay().v());

			endDate = calendar.getTime();
		} else {
			DatePeriod datePeriod = closureService.getClosurePeriod(closureId, yearMonth);
			endDate = datePeriod.end().date();
		}
		return new CheckConditionTimeDto(c.getAlarmCategory().value,
				EnumAdaptor.convertToValueName(c.getAlarmCategory()).getLocalizedName(), formatter.format(startDate),
				formatter.format(endDate), null, null);
	}

	public CheckConditionTimeDto get4WeekTime(CheckCondition c, int closureId, YearMonth yearMonth, String companyId) {
		LocalDate sDate = null;
		LocalDate eDate = null;

		ExtractionPeriodUnit periodUnit = (ExtractionPeriodUnit) c.getExtractPeriod();
		
		LocalDate countingDate;
		boolean isYMD;
		
		// Get from PublicHolidaySetting domain
		Optional<PublicHolidaySetting> publicHolidaySettingOpt = publicHolidaySettingRepo.findByCID(companyId);
		if (!publicHolidaySettingOpt.isPresent())
			throw new RuntimeException("「公休設定」ドメインが見つかりません！");
		PublicHoliday publicHoliday = (PublicHoliday) publicHolidaySettingOpt.get().getPublicHdManagementStartDate();
		if (publicHoliday.getDetermineStartDate() == DayOfPublicHoliday.DESIGNATE_BY_YEAR_MONTH_DAY) {
			countingDate= publicHoliday.getDate().localDate();
			isYMD = true;
		}else {			
			String dayMonth= publicHoliday.getDayMonth().toString();
			if(dayMonth.length()<4) dayMonth ="0" +dayMonth;
			countingDate = LocalDate.of(LocalDate.now().getYear(), Integer.valueOf(dayMonth.substring(0, 2)).intValue(),
					Integer.valueOf(dayMonth.substring(2, 4)).intValue());
			isYMD= false;
		}
		LocalDate currentDate = LocalDate.now();
		if (isYMD) {
			long totalDays = ChronoUnit.DAYS.between(countingDate, currentDate);
			long index = totalDays / 28;

			switch (periodUnit.getSegmentationOfCycle()) {
			case ThePreviousCycle:
				index--;
			case Period:
				break;
			case TheNextCycle:
				index++;
			default:
				break;
			}
			if (index == -1)
				throw new RuntimeException("1周期目の期間がシステム日付を含む期間となりますが周期の区分 は 実行日を含む周期の前周期！");

			sDate = countingDate.plusDays(index * 28);
			eDate = sDate.plusDays(27);

		} else {
			long totalDays = ChronoUnit.DAYS.between(countingDate, currentDate);
			long index = totalDays / 28;

			switch (periodUnit.getSegmentationOfCycle()) {
			case ThePreviousCycle:
				index--;
			case Period:
				break;
			case TheNextCycle:
				index++;
			default:
				break;
			}

			if (index == -1) {
				countingDate.minusYears(1);
				index = 13;
				sDate = countingDate.plusDays(index * 28);
				eDate = LocalDate.of(countingDate.getYear(), 12, 31);
			} else if (index == 14) {
				countingDate.plusYears(1);
				index = 0;
				sDate = countingDate.plusDays(index * 28);
				eDate = sDate.plusDays(27);
			} else {
				sDate = countingDate.plusDays(index * 28);
				eDate = sDate.plusDays(27);
			}

		}

		return new CheckConditionTimeDto(c.getAlarmCategory().value,
				EnumAdaptor.convertToValueName(c.getAlarmCategory()).getLocalizedName(),
				sDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
				eDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), null, null);
	}

}

package nts.uk.file.at.app.export.dailyschedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.dailyattendanceitem.AttendanceResultImport;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.file.at.app.export.dailyschedule.totalsum.DayType;
import nts.uk.file.at.app.export.dailyschedule.totalsum.NumberOfItem;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalCountDay;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * Total day count class
 * @author HoangNDH
 *
 */
@Stateless
public class TotalDayCountWs {
	@Inject
	private AttendanceResultImportAdapter attendanceResultAdapter;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private WorkInformationRepository workInformationRepo;
	
	/**
	 * 所定日数を計算す�
	 * @param employeeId
	 * @param lstDate
	 */
	private TotalCountDay calculatePredeterminedDay(String employeeId, DateRange dateRange, TotalCountDay totalCountDay) {
		// ドメインモッ�「勤務種類」を取得す�
		List<WorkType> lstWorkType = workTypeRepository.findByCompanyId(AppContexts.user().companyId()).stream().filter(x -> 
			x.getDailyWork().getOneDay().isAttendance() || x.getDailyWork().getMorning().isAttendance() || x.getDailyWork().getAfternoon().isAttendance() 
		).collect(Collectors.toList());
		if (lstWorkType.size() > 0) {
			// ドメインモッ�「日別実績の勤務情報」を取得す�
			List<String> empList = new ArrayList<>();
			empList.add(employeeId);
			// 予定勤務種類コードを取得す�
			List<WorkInfoOfDailyPerformance> dailyPerformanceList = workInformationRepo.findByPeriodOrderByYmdAndEmps(empList, new DatePeriod(dateRange.getStartDate(), dateRange.getEndDate()));
			
			int dayCount = 0;
			for (WorkType workType: lstWorkType) {
				dayCount += dailyPerformanceList.stream().filter(x -> x.getScheduleInfo().getWorkTypeCode().v().equals(workType.getWorkTypeCode().v())).collect(Collectors.toList()).size();
			}
			//dayCount = lstWorkType.size();
			
			// 所定日数をカウントす�
			totalCountDay.setPredeterminedDay(dayCount);
		}
		else {
			totalCountDay.setPredeterminedDay(0);
		}
		return totalCountDay;
	}
	
	/**
	 * 所定外�日数を計算す�
	 * @param employeeId
	 * @param dateRange
	 * @param dayType
	 * @return
	 */
	private TotalCountDay calculateNonPredeterminedDay(String employeeId, DateRange dateRange, TotalCountDay totalCountDay) {
		// ドメインモッ�「日別実績の勤務情報」を取得す�
		List<String> empList = new ArrayList<>();
		empList.add(employeeId);
		// 予定勤務種類コードを取得す�
		//List<WorkInfoOfDailyPerformanceDetailDto> dailyPerformanceList = dailyPerformanceRepo.find(empList, dateRange);
		
		List<WorkInfoOfDailyPerformance> dailyPerformanceList = workInformationRepo.findByPeriodOrderByYmdAndEmps(empList, new DatePeriod(dateRange.getStartDate(), dateRange.getEndDate()));
		
		String companyId = AppContexts.user().companyId();
		
		for (DayType dayType : DayType.values()) {
			// ドメインモッ�「勤務種類」を取得す�
			List<WorkType> lstWorkType = workTypeRepository.findByCompanyId(companyId).stream().filter(x -> 
				x.getDailyWork().getOneDay().value == dayType.value || 
				x.getDailyWork().getMorning().value == dayType.value || 
				x.getDailyWork().getAfternoon().value == dayType.value).collect(Collectors.toList());
			
			if (lstWorkType.size() > 0) {
				
				for (WorkType workType: lstWorkType) {
					// 日数をカウントす�
					int dayCount = dailyPerformanceList.stream().filter(x -> StringUtils.equals(x.getScheduleInfo().getWorkTypeCode().v(),workType.getWorkTypeCode().v())
							|| StringUtils.equals(x.getRecordInfo().getWorkTypeCode().v(),workType.getWorkTypeCode().v())).collect(Collectors.toList()).size();
					//int dayCount = lstWorkType.size();
					switch (dayType) {
					case HOLIDAY:
						totalCountDay.setHolidayDay(totalCountDay.getHolidayDay() + dayCount);
						break;
					case ABSENCE:
						totalCountDay.setAbsenceDay(totalCountDay.getAbsenceDay() + dayCount);
						break;
					case ANNUAL_HOLIDAY:
						totalCountDay.setYearOffUsage(totalCountDay.getYearOffUsage() + dayCount);
						break;
					case OFF_WORK:
						totalCountDay.setOffDay(totalCountDay.getOffDay() + dayCount);
						break;
					case SPECIAL:
						totalCountDay.setSpecialHoliday(totalCountDay.getSpecialHoliday() + dayCount);
						break;
					case YEARLY_RESERVED:
						totalCountDay.setHeavyHolDay(totalCountDay.getHeavyHolDay() + dayCount);
						break;
					}
				}
				
			}
		}
		return totalCountDay;
	}
	
	/**
	 * 回数を計算す�
	 * @param employeeId
	 * @param dateRange
	 * @param totalCountDay
	 */
	private TotalCountDay calculateDayCount(String employeeId, DateRange dateRange, TotalCountDay totalCountDay) {
		// 勤�頛�ID = 592 or 598 or 604 or 610
		List<Integer> lstAttendanceId = new ArrayList<>();
		lstAttendanceId.add(592);
		lstAttendanceId.add(598);
		lstAttendanceId.add(604);
		lstAttendanceId.add(610);
		
		List<String> lstEmployeeId = new ArrayList<>();
		lstEmployeeId.add(employeeId);
		
		List<AttendanceResultImport> attendanceResultImport = attendanceResultAdapter.getValueOf(lstEmployeeId, new DatePeriod(dateRange.getStartDate(), dateRange.getEndDate()), lstAttendanceId);
		
		
		for (NumberOfItem item : NumberOfItem.values()) {
			List<GeneralDate> lstDate = dateRange.toListDate();
			
			// Variable to count by item through date list
			int dayCount = 0;
			
			for (GeneralDate currentDate : lstDate) {
				// Literally 0-1 result
				Optional<AttendanceResultImport> optAttendanceResult = attendanceResultImport.stream().filter(x -> x.getWorkingDate().compareTo(currentDate) == 0).findFirst();
				if (optAttendanceResult.isPresent()) {
					AttendanceResultImport attendanceResult = optAttendanceResult.get();
					
					// Count by item attendance id
					dayCount += attendanceResult.getAttendanceItems().stream().filter(x -> x.getItemId() == item.attendanceId).
									filter(x -> x.getValue() != null && Integer.parseInt(x.getValue()) != 0).count();
				}
			}
			
			switch (item) {
			case LATE_COME_1:
			case LATE_COME_2:
				totalCountDay.setLateComeDay(totalCountDay.getLateComeDay() + dayCount);
				break;
			case EARLY_LEAVE_1:
			case EARLY_LEAVE_2:
				totalCountDay.setEarlyLeaveDay(totalCountDay.getEarlyLeaveDay() + dayCount);
				break;
			}
		}
		return totalCountDay;
	}
	
	/**
	 * 日数計を合算す�
	 * @param employeeId
	 * @param dateRange
	 * @param totalCountDay
	 */
	public TotalCountDay calculateAllDayCount(String employeeId, DateRange dateRange, TotalCountDay totalCountDay) {
		totalCountDay = calculatePredeterminedDay(employeeId, dateRange, totalCountDay);
		totalCountDay = calculateNonPredeterminedDay(employeeId, dateRange, totalCountDay);
		totalCountDay = calculateDayCount(employeeId, dateRange, totalCountDay);
		return totalCountDay;
	}
}

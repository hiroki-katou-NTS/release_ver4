package nts.uk.screen.at.app.ktgwidget.find;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.adapter.application.ApplicationAdapter;
import nts.uk.ctx.at.function.dom.adapter.application.importclass.ApplicationDeadlineImport;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.AnnualLeaveRemainingNumberImport;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.ApplicationTimeImport;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.DailyExcessTotalTimeImport;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.EmployeeErrorImport;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.NextAnnualLeaveGrantImport;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.OptionalWidgetAdapter;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.OptionalWidgetImport;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.NumAnnLeaReferenceDateImport;
import nts.uk.ctx.at.function.dom.adapter.widgetKtg.WidgetDisplayItemImport;
import nts.uk.ctx.at.function.dom.employmentfunction.checksdailyerror.ChecksDailyPerformanceErrorRepository;
import nts.uk.ctx.at.function.dom.employmentfunction.checksdailyerror.GetNumberOfRemainingHolidaysRepository;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.holidayinstruction.HolidayInstructRepository;
import nts.uk.ctx.at.request.dom.overtimeinstruct.OvertimeInstructRepository;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.screen.at.app.ktgwidget.find.dto.DatePeriodDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.DeadlineOfRequest;
import nts.uk.screen.at.app.ktgwidget.find.dto.OptionalWidgetDisplay;
import nts.uk.screen.at.app.ktgwidget.find.dto.OptionalWidgetInfoDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.TimeOT;
import nts.uk.screen.at.app.ktgwidget.find.dto.WidgetDisplayItemTypeImport;
import nts.uk.screen.at.app.ktgwidget.find.dto.YearlyHoliday;
import nts.uk.screen.at.app.ktgwidget.find.dto.YearlyHolidayInfo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class OptionalWidgetKtgFinder {
	
	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;
	
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;
	
	@Inject
	private ClosureRepository closureRepo;
	
	@Inject
	private ClosureService closureService;
	
	@Inject
	private OptionalWidgetAdapter optionalWidgetAdapter; 

	@Inject
	private OvertimeInstructRepository overtimeInstructRepo;
	
	@Inject
	private HolidayInstructRepository holidayInstructRepo;
	
	@Inject
	private ApplicationRepository_New applicationRepo_New;
	
	@Inject
	private ApplicationAdapter applicationAdapter;
	
	@Inject
	private ChecksDailyPerformanceErrorRepository checksDailyPerformanceErrorRepo;
	
	@Inject
	private GetNumberOfRemainingHolidaysRepository GetNumberOfRemainingHolidaysRepo;
	

	public DatePeriodDto getCurrentMonth() {
		String companyId = AppContexts.user().companyId();
		Integer closureId = this.getClosureId();
		
		Optional<Closure> closure = closureRepo.findById(companyId, closureId);
		if(!closure.isPresent())
			return null;
		
		YearMonth processingDate = closure.get().getClosureMonth().getProcessingYm();
		
		DatePeriod currentMonth = closureService.getClosurePeriod(closureId, processingDate);
		
		DatePeriod nextMonth = closureService.getClosurePeriod(closureId, processingDate.addMonths(1));
		
		DatePeriodDto dto = new DatePeriodDto(currentMonth.start(), currentMonth.end(), nextMonth.start(), nextMonth.end());
		
		return dto;
	}
	/**
	 * @return employment code
	 */
	private String getEmploymentCode() {
		String companyId = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();
		
		Optional<BsEmploymentHistoryImport> EmploymentHistoryImport = shareEmploymentAdapter.findEmploymentHistory(companyId, employeeId, GeneralDate.today());
		if(!EmploymentHistoryImport.isPresent())
			throw new RuntimeException("Not found EmploymentHistory by closureID");
		String employmentCode = EmploymentHistoryImport.get().getEmploymentCode();
		return employmentCode;
	}
	
	/**
	 * @return ClosureId
	 */
	private Integer getClosureId() {
		String companyId = AppContexts.user().companyId();
		String employmentCode = this.getEmploymentCode();
		Optional<ClosureEmployment> closureEmployment = closureEmploymentRepo.findByEmploymentCD(companyId, employmentCode);
		if(!closureEmployment.isPresent())
			return null;
		return closureEmployment.get().getClosureId();
	}
	
	public OptionalWidgetImport findOptionalWidgetByCode(String topPagePartCode) {
		String companyId = AppContexts.user().companyId(); 
		Optional<OptionalWidgetImport> dto = optionalWidgetAdapter.getSelectedWidget(companyId, topPagePartCode);
		if(!dto.isPresent())
			return null;
		return optionalWidgetAdapter.getSelectedWidget(companyId, topPagePartCode).get();
	}
	
	public OptionalWidgetDisplay getOptionalWidgetDisplay(String topPagePartCode) {
		DatePeriodDto datePeriodDto = getCurrentMonth();
		OptionalWidgetImport optionalWidgetImport = findOptionalWidgetByCode(topPagePartCode);
		return new OptionalWidgetDisplay(datePeriodDto, optionalWidgetImport);
	}
	
	
	public OptionalWidgetInfoDto getDataRecord(String code, GeneralDate startDate, GeneralDate endDate) {
		String companyId = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();
		DatePeriod datePeriod = new DatePeriod(startDate, endDate);
		OptionalWidgetInfoDto dto = new OptionalWidgetInfoDto();
		List<WidgetDisplayItemImport> widgetDisplayItem = findOptionalWidgetByCode(code).getWidgetDisplayItemExport();
		//get requestList 193
		List<DailyExcessTotalTimeImport> dailyExcessTotalTimeImport = optionalWidgetAdapter.getExcessTotalTime(employeeId,datePeriod);
		
		for (WidgetDisplayItemImport item : widgetDisplayItem) {
			if(item.getNotUseAtr()==1) {
				if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.OVERTIME_WORK_NO.value) {
					//sử lý 01
					dto.setOverTime(overtimeInstructRepo.getAllOverTimeInstructBySId(employeeId, startDate, endDate).size());
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.INSTRUCTION_HD_NO.value) {
					//sử lý 02
					dto.setHolidayInstruction(holidayInstructRepo.getAllHolidayInstructBySId(employeeId, startDate, endDate).size());
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.APPROVED_NO.value) {
					//sử lý 03
					//lấy theo request list của anh hiếu. chỉ khác tham số đầu vào.
					//・反映状態　　＝　「反映済み」または「反映待ち」(「反映済み」 OR 「反映待ち」)
					List<Integer> reflected = new ArrayList<>();
					reflected.add(ReflectedState_New.REFLECTED.value);
					reflected.add(ReflectedState_New.WAITREFLECTION.value);
					dto.setApproved(applicationRepo_New.getByListRefStatus(employeeId, startDate, endDate, reflected).size());
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.UNAPPROVED_NO.value) {
					//sử lý 04
					//・反映状態　　＝　「未承認」または「差戻し」(「未承認」OR 「差戻し」)
					List<Integer> reflected = new ArrayList<>();
					reflected.add(ReflectedState_New.NOTREFLECTED.value);
					reflected.add(ReflectedState_New.REMAND.value);
					dto.setUnApproved(applicationRepo_New.getByListRefStatus(employeeId, startDate, endDate, reflected).size());
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.DENIED_NO.value) {
					//sử lý 05
					//・反映状態　　＝　「否認」
					List<Integer> reflected = new ArrayList<>();
					reflected.add(ReflectedState_New.DENIAL.value);
					dto.setDeniedNo(applicationRepo_New.getByListRefStatus(employeeId, startDate, endDate, reflected).size());
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.REMAND_NO.value) {
					//sử lý 06
					//・反映状態　　＝　「差戻し」
					List<Integer> reflected = new ArrayList<>();
					reflected.add(ReflectedState_New.REMAND.value);
					dto.setDeniedNo(applicationRepo_New.getByListRefStatus(employeeId, startDate, endDate, reflected).size());
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.APP_DEADLINE_MONTH.value) {
					//sử lý 07
					ApplicationDeadlineImport deadlineImport = applicationAdapter.getApplicationDeadline(companyId, this.getClosureId());
					dto.setAppDeadlineMonth(new DeadlineOfRequest(deadlineImport.isUseApplicationDeadline(), deadlineImport.getDateDeadline()));
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.PRESENCE_DAILY_PER.value) {
					//sử lý 08
					dto.setPresenceDailyPer(checksDailyPerformanceErrorRepo.checked(employeeId, startDate, endDate));
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.REFER_WORK_RECORD.value) {
					//sử lý 09
					List<EmployeeErrorImport> listEmployeeError = optionalWidgetAdapter.checkEmployeeErrorOnProcessingDate(employeeId, datePeriod);
					for (EmployeeErrorImport employeeErrorImport : listEmployeeError) {
						if(employeeErrorImport.getHasError()) {
							dto.setPresenceDailyPer(true);
							break;
						}
					}
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.OVERTIME_HOURS.value) {
					//sử lý 10 call Request list 236
					int timeOT = 0;
					List<ApplicationTimeImport> applicationOvertimeImport = optionalWidgetAdapter.acquireTotalApplicationOverTimeHours(employeeId, startDate, endDate);
					for (ApplicationTimeImport applicationOvertime : applicationOvertimeImport) {
						dailyExcessTotalTimeImport = dailyExcessTotalTimeImport.stream().
								filter(c -> !c.getDate().equals(applicationOvertime.getDate())).collect(Collectors.toList());
					}
					for (ApplicationTimeImport OvertimeImport : applicationOvertimeImport) {
						timeOT += OvertimeImport.getTotalOtHours();
					}
					for (DailyExcessTotalTimeImport dailyExcessTotalTime : dailyExcessTotalTimeImport) {
						timeOT += dailyExcessTotalTime.getTimeOT().getTime();
					}
					dto.setOvertimeHours(new TimeOT(timeOT/60, timeOT%60));
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.FLEX_TIME.value) {
					//sử lý 11 call Request list 298
					int time = 0;
					List<ApplicationTimeImport> applicationflexTimeImport = optionalWidgetAdapter.acquireTotalApplicationTimeUnreflected(employeeId, startDate, endDate);
					for (ApplicationTimeImport applicationOvertime : applicationflexTimeImport) {
						dailyExcessTotalTimeImport = dailyExcessTotalTimeImport.stream().
								filter(c -> !c.getDate().equals(applicationOvertime.getDate())).collect(Collectors.toList());
					}
					for (ApplicationTimeImport OvertimeImport : applicationflexTimeImport) {
						time += OvertimeImport.getTotalOtHours();
					}
					for (DailyExcessTotalTimeImport dailyExcessTotalTime : dailyExcessTotalTimeImport) {
						time += dailyExcessTotalTime.getTimeOT().getTime();
					}
					dto.setFlexTime(new TimeOT(time/60, time%60));
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.REST_TIME.value) {
					//sử lý 12 call Request list 299
					int time = 0;
					List<ApplicationTimeImport> applicationflexTimeImport = optionalWidgetAdapter.acquireTotalAppHdTimeNotReflected(employeeId, startDate, endDate);
					for (ApplicationTimeImport applicationOvertime : applicationflexTimeImport) {
						dailyExcessTotalTimeImport = dailyExcessTotalTimeImport.stream().
								filter(c -> !c.getDate().equals(applicationOvertime.getDate())).collect(Collectors.toList());
					}
					for (ApplicationTimeImport OvertimeImport : applicationflexTimeImport) {
						time += OvertimeImport.getTotalOtHours();
					}
					for (DailyExcessTotalTimeImport dailyExcessTotalTime : dailyExcessTotalTimeImport) {
						time += dailyExcessTotalTime.getTimeOT().getTime();
					}
					dto.setRestTime(new TimeOT(time/60, time%60));
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.NIGHT_WORK_HOURS.value) {
					//sử lý 13 call Request list 300
					int time = 0;
					List<ApplicationTimeImport> applicationflexTimeImport = optionalWidgetAdapter.acquireAppNotReflected(employeeId, startDate, endDate);
					for (ApplicationTimeImport applicationOvertime : applicationflexTimeImport) {
						dailyExcessTotalTimeImport = dailyExcessTotalTimeImport.stream().
								filter(c -> !c.getDate().equals(applicationOvertime.getDate())).collect(Collectors.toList());
					}
					for (ApplicationTimeImport OvertimeImport : applicationflexTimeImport) {
						time += OvertimeImport.getTotalOtHours();
					}
					for (DailyExcessTotalTimeImport dailyExcessTotalTime : dailyExcessTotalTimeImport) {
						time += dailyExcessTotalTime.getTimeOT().getTime();
					}
					dto.setNightWorktime(new TimeOT(time/60, time%60));
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.LATE_OR_EARLY_RETREAT.value) {
					//sử lý 14
					//chưa có requestList 446 nên tạm thời return 0;
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.YEARLY_HD.value) {
					//sử lý 15
					dto.setYearlyHoliday(this.setYearlyHoliday(companyId, employeeId, startDate));
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.HAFT_DAY_OFF.value) {
					
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.HOURS_OF_HOLIDAY_UPPER_LIMIT.value) {
					
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.RESERVED_YEARS_REMAIN_NO.value) {
					//sử lý 16
					// chờ request 201 bên nhật làm.
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.PLANNED_YEAR_HOLIDAY.value) {
					/*Delete display of planned number of inactivity days - 計画年休残数の表示は削除*/
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.REMAIN_ALTERNATION_NO.value) {
					//sử lý 18
					//requestList 203 team B
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.REMAINS_LEFT.value) {
					//sử lý 19
					//requestList 204 team B
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.PUBLIC_HD_NO.value) {
					
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.HD_REMAIN_NO.value) {
					//sử lý 21
					//requestList 206 team Anh ThànhNC
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.CARE_LEAVE_NO.value) {
					//sử lý 22
					//requestList 207 team Anh ThànhNC
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.SPHD_RAMAIN_NO.value) {
					//sử lý 23
					//requestList 208 Chưa có domain
				}else if(item.getDisplayItemType() == WidgetDisplayItemTypeImport.SIXTYH_EXTRA_REST.value) {
					
				}
			}
		}
		return dto;
	}
	private YearlyHoliday setYearlyHoliday(String cID, String employeeId, GeneralDate date) {
		YearlyHoliday yearlyHoliday = new YearlyHoliday();
		//lấy request list 210
/*		
		List<NextAnnualLeaveGrantImport> listNextAnnualLeaveGrant = optionalWidgetAdapter.acquireNextHolidayGrantDate(cID,employeeId, date);
		if(listNextAnnualLeaveGrant.isEmpty()) {
			return yearlyHoliday;
		}
		NextAnnualLeaveGrantImport NextAnnualLeaveGrant = listNextAnnualLeaveGrant.get(0); 
		//lấy request 198
		NumAnnLeaReferenceDateImport reNumAnnLeaReferenceDate = optionalWidgetAdapter.getReferDateAnnualLeaveRemainNumber(employeeId, date);
		
		yearlyHoliday.setNextTime(NextAnnualLeaveGrant.getGrantDate());
		yearlyHoliday.setNextGrantDate(NextAnnualLeaveGrant.getGrantDate());
		yearlyHoliday.setGrantedDaysNo(NextAnnualLeaveGrant.getGrantDays().intValueExact());
		AnnualLeaveRemainingNumberImport remainingNumber = reNumAnnLeaReferenceDate.getAnnualLeaveRemainNumberImport();
		yearlyHoliday.setNextTimeInfo(new YearlyHolidayInfo(remainingNumber.getAnnualLeaveGrantPreDay(),
															new TimeOT(remainingNumber.getAnnualLeaveGrantPreTime().intValue()/60, remainingNumber.getAnnualLeaveGrantPreTime().intValue()%60), 
															remainingNumber.getNumberOfRemainGrantPre(), 
															new TimeOT(remainingNumber.getTimeAnnualLeaveWithMinusGrantPre().intValue()/60,remainingNumber.getTimeAnnualLeaveWithMinusGrantPre().intValue()%60)));
		yearlyHoliday.setNextGrantDateInfo(new YearlyHolidayInfo(remainingNumber.getAnnualLeaveGrantPreDay(),
															new TimeOT(remainingNumber.getAnnualLeaveGrantPreTime().intValue()/60, remainingNumber.getAnnualLeaveGrantPreTime().intValue()%60), 
															remainingNumber.getNumberOfRemainGrantPre(), 
															new TimeOT(remainingNumber.getTimeAnnualLeaveWithMinusGrantPre().intValue()/60,remainingNumber.getTimeAnnualLeaveWithMinusGrantPre().intValue()%60)));
		yearlyHoliday.setAfterGrantDateInfo(new YearlyHolidayInfo(remainingNumber.getAnnualLeaveGrantPostDay(),
															new TimeOT(remainingNumber.getAnnualLeaveGrantPostTime().intValue()/60, remainingNumber.getAnnualLeaveGrantPostTime().intValue()%60), 
															remainingNumber.getNumberOfRemainGrantPost(), 
															new TimeOT(remainingNumber.getTimeAnnualLeaveWithMinusGrantPost().intValue()/60,remainingNumber.getTimeAnnualLeaveWithMinusGrantPost().intValue()%60)));
		yearlyHoliday.setAttendanceRate(remainingNumber.getAttendanceRate());
		yearlyHoliday.setWorkingDays(remainingNumber.getWorkingDays());*/
		return yearlyHoliday;
	}
}

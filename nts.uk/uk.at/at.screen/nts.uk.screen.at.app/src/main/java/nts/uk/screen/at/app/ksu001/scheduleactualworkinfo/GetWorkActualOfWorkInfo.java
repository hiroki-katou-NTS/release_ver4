/**
 * 
 */
package nts.uk.screen.at.app.ksu001.scheduleactualworkinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.layer.app.cache.DateHistoryCache;
import nts.arc.layer.app.cache.KeyDateHistoryCache;
import nts.arc.layer.app.cache.NestedMapCache;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordWorkFinder;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.ScheManaStatuTempo;
import nts.uk.ctx.at.schedule.dom.workschedule.domainservice.DailyResultAccordScheduleStatusService;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveHistoryAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveWorkHistoryAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveWorkPeriodImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmployeeLeaveJobPeriodImport;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.workinfomation.GetListWtypeWtimeUseDailyAttendRecordService;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.workinfomation.WorkTypeWorkTimeUseDailyAttendanceRecord;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemWithPeriod;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmpComHisAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmpEnrollPeriodImport;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmploymentHisScheduleAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmploymentPeriodImported;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeInfor;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.screen.at.app.ksu001.displayinworkinformation.DisplayInWorkInfoParam;
import nts.uk.screen.at.app.ksu001.displayinworkinformation.WorkScheduleWorkInforDto;
import nts.uk.screen.at.app.ksu001.start.SupportCategory;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author laitv
 * ScreenQuery : 勤務実績（勤務情報）を取得する
 */
@Stateless
public class GetWorkActualOfWorkInfo {
	
	@Inject
	private EmpComHisAdapter empComHisAdapter;
	@Inject
	private WorkingConditionRepository workCondRepo;
	@Inject
	private EmpLeaveHistoryAdapter empLeaveHisAdapter;
	@Inject
	private EmpLeaveWorkHistoryAdapter empLeaveWorkHisAdapter;
	@Inject
	private EmploymentHisScheduleAdapter employmentHisScheduleAdapter;
	@Inject
	private DailyRecordWorkFinder dailyRecordWorkFinder;
	@Inject
	private WorkTypeRepository workTypeRepo;
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepo;
	
	
	public List<WorkScheduleWorkInforDto> getDataActualOfWorkInfo(DisplayInWorkInfoParam param) {
		
		String companyId = AppContexts.user().companyId();
		// step 1 start
		// call 予定管理状態に応じて日別実績を取得する
		long start = System.nanoTime();
		DatePeriod period = new DatePeriod(param.startDate, param.endDate);
		RequireDailyImpl requireDailyImpl = new RequireDailyImpl(param.listSid, period, dailyRecordWorkFinder , empComHisAdapter, workCondRepo, empLeaveHisAdapter,
				empLeaveWorkHisAdapter, employmentHisScheduleAdapter);
		Map<ScheManaStatuTempo , Optional<IntegrationOfDaily>> map = DailyResultAccordScheduleStatusService.get(requireDailyImpl, param.listSid, period);
		
		long end = System.nanoTime();
		long duration = (end - start) / 1000000; // ms;
		System.out.println("thoi gian get data Daily cua "+ param.listSid.size() + " employee: " + duration + "ms");
		
		List<WorkInfoOfDailyAttendance> listWorkInfo = new ArrayList<WorkInfoOfDailyAttendance>();
		map.forEach((k, v) -> {
			if (v.isPresent()) {
				WorkInfoOfDailyAttendance workInfo = v.get().getWorkInformation();
				if (workInfo != null) {
					listWorkInfo.add(workInfo);
				}
			}
		});
		// step 1 end 
		
		// step 2
		// call 日別勤怠の実績で利用する勤務種類と就業時間帯のリストを取得する
		WorkTypeWorkTimeUseDailyAttendanceRecord wTypeWTimeUseDailyAttendRecord = GetListWtypeWtimeUseDailyAttendRecordService.getdata(listWorkInfo);
		
		// step 3
		List<String> lstWorkTypeCode = wTypeWTimeUseDailyAttendRecord.getLstWorkTypeCode().stream().map(i -> i.toString()).collect(Collectors.toList());
		//<<Public>> 指定した勤務種類をすべて取得する
		List<WorkTypeInfor> lstWorkTypeInfor = this.workTypeRepo.getPossibleWorkTypeAndOrder(companyId, lstWorkTypeCode);

		// step 4
		List<String> lstWorkTimeCode = wTypeWTimeUseDailyAttendRecord.getLstWorkTimeCode().stream().map(i -> i.toString()).collect(Collectors.toList());
		List<WorkTimeSetting> lstWorkTimeSetting = workTimeSettingRepo.getListWorkTimeSetByListCode(companyId, lstWorkTimeCode);

		// step 5
		List<WorkScheduleWorkInforDto> listWorkScheduleWorkInfor = new ArrayList<>();
		map.forEach((k, v) -> {
			ScheManaStatuTempo key = k;
			Optional<IntegrationOfDaily> value = v;

			// step 5.1
			boolean needToWork = key.getScheManaStatus().needCreateWorkSchedule();
			if (value.isPresent()) {
				// step 5.2
				IntegrationOfDaily daily = value.get();
				if (daily.getWorkInformation() != null) {
					WorkInformation workInformation = daily.getWorkInformation().getRecordInfo();

					String workTypeCode = workInformation.getWorkTypeCode() == null ? null : workInformation.getWorkTypeCode().toString();
					String workTypeName = null;
					Optional<WorkTypeInfor> workTypeInfor = lstWorkTypeInfor.stream().filter(i -> i.getWorkTypeCode().equals(workTypeCode)).findFirst();
					if (workTypeInfor.isPresent()) {
						workTypeName = workTypeInfor.get().getName();
					}
					String workTimeCode = workInformation.getWorkTimeCode() == null ? null: workInformation.getWorkTimeCode().toString();
					Optional<WorkTimeSetting> workTimeSetting = lstWorkTimeSetting.stream().filter(i -> i.getWorktimeCode().toString().equals(workTimeCode)).findFirst();
					String workTimeName = null;
					if (workTimeSetting.isPresent()) {
						if (workTimeSetting.get().getWorkTimeDisplayName() != null && workTimeSetting.get().getWorkTimeDisplayName().getWorkTimeAbName() != null) {
							workTimeName = workTimeSetting.get().getWorkTimeDisplayName().getWorkTimeAbName().toString();
						}
					}

					Integer startTime = null;
					if (daily.getAttendanceLeave().isPresent()) {
						Optional<TimeLeavingWork> timeLeavingWork = daily.getAttendanceLeave().get().getTimeLeavingWorks().stream().filter(i -> i.getWorkNo().v() == 1).findFirst();
						if (timeLeavingWork.isPresent()) {
							if (timeLeavingWork.get().getAttendanceStamp().isPresent()) {
								if (timeLeavingWork.get().getAttendanceStamp().get().getStamp().isPresent()) {
									if (timeLeavingWork.get().getAttendanceStamp().get().getStamp().get().getTimeDay() != null) {
										if (timeLeavingWork.get().getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay().isPresent()) {
											startTime = timeLeavingWork.get().getAttendanceStamp().get().getStamp().get().getTimeDay().getTimeWithDay().get().v();
										}
									}
								}
							}
						}
					}

					Integer endtTime = null;
					if (daily.getAttendanceLeave().isPresent()) {
						Optional<TimeLeavingWork> timeLeavingWork = daily.getAttendanceLeave().get().getTimeLeavingWorks().stream().filter(i -> i.getWorkNo().v() == 1).findFirst();
						if (timeLeavingWork.isPresent()) {
							if (timeLeavingWork.get().getLeaveStamp().isPresent()) {
								if (timeLeavingWork.get().getLeaveStamp().get().getStamp().isPresent()) {
									if (timeLeavingWork.get().getLeaveStamp().get().getStamp().get().getTimeDay() != null) {
										if (timeLeavingWork.get().getLeaveStamp().get().getStamp().get().getTimeDay().getTimeWithDay().isPresent()) {
											endtTime = timeLeavingWork.get().getLeaveStamp().get().getStamp().get().getTimeDay().getTimeWithDay().get().v();
										}
									}
								}
							}
						}
					}

					WorkScheduleWorkInforDto dto = WorkScheduleWorkInforDto.builder()
							.employeeId(key.getEmployeeID())
							.date(key.getDate())
							.haveData(true)
							.achievements(true)
							.confirmed(true)
							.needToWork(needToWork)
							.supportCategory(SupportCategory.NotCheering.value)
							.workTypeCode(workTypeCode)
							.workTypeName(workTypeName)
							.workTypeEditStatus(null)
							.workTimeCode(workTimeCode)
							.workTimeName(workTimeName)
							.workTimeEditStatus(null)
							.startTime(startTime)
							.startTimeEditState(null)
							.endTime(endtTime)
							.endTimeEditState(null)
							.workHolidayCls(null)
							.isEdit(false) //
							.isActive(false) //
							.build();

					listWorkScheduleWorkInfor.add(dto);
				}
			}
		});

		return listWorkScheduleWorkInfor;
	}

	@AllArgsConstructor
	private static class RequireWorkInforImpl implements WorkInformation.Require {
		
		private final String companyId = AppContexts.user().companyId();
		
		@Inject
		private WorkTypeRepository workTypeRepo;
		
		@Inject
		private WorkTimeSettingRepository workTimeSettingRepository;
		
		@Inject
		private WorkTimeSettingService workTimeSettingService;
		
		@Inject
		private BasicScheduleService basicScheduleService;

		@Override
		public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
			 return basicScheduleService.checkNeededOfWorkTimeSetting(workTypeCode);
		}

		@Override
		public Optional<WorkType> findByPK(String workTypeCd) {
			return workTypeRepo.findByPK(companyId, workTypeCd);
		}

		@Override
		public Optional<WorkTimeSetting> findByCode(String workTimeCode) {
			return workTimeSettingRepository.findByCode(companyId, workTimeCode);
		}

		@Override
		public PredetermineTimeSetForCalc getPredeterminedTimezone(String workTimeCd,
				String workTypeCd, Integer workNo) {
			return workTimeSettingService .getPredeterminedTimezone(companyId, workTimeCd, workTypeCd, workNo);
		}

		@Override
		public WorkStyle checkWorkDay(String workTypeCode) {
			return basicScheduleService.checkWorkDay(workTypeCode);
		}
	}

	@AllArgsConstructor
	private static class RequireDailyImpl implements DailyResultAccordScheduleStatusService.Require {

		private NestedMapCache<String, GeneralDate, DailyRecordDto> workScheduleCache;
		private KeyDateHistoryCache<String, EmpEnrollPeriodImport> affCompanyHistByEmployeeCache;
		private KeyDateHistoryCache<String, EmploymentPeriodImported> employmentPeriodCache;
		private KeyDateHistoryCache<String, EmployeeLeaveJobPeriodImport> empLeaveJobPeriodCache;
		private KeyDateHistoryCache<String, EmpLeaveWorkPeriodImport> empLeaveWorkPeriodCache;
		private KeyDateHistoryCache<String, WorkingConditionItemWithPeriod> workCondItemWithPeriodCache;
		
		public RequireDailyImpl(List<String> empIdList, DatePeriod period, DailyRecordWorkFinder dailyRecordWorkFinder,
				EmpComHisAdapter empComHisAdapter, WorkingConditionRepository workCondRepo,
				EmpLeaveHistoryAdapter empLeaveHisAdapter, EmpLeaveWorkHistoryAdapter empLeaveWorkHisAdapter,
				EmploymentHisScheduleAdapter employmentHisScheduleAdapter) {
			
			long start1 = System.nanoTime();
			List<DailyRecordDto> sDailyRecordDtos = dailyRecordWorkFinder.find(empIdList, period);
			workScheduleCache = NestedMapCache.preloadedAll(sDailyRecordDtos.stream(),
					workSchedule -> workSchedule.getEmployeeId(), 
					workSchedule -> workSchedule.getDate());
			System.out.println("thoi gian get data Daily " + ((System.nanoTime() - start1 )/1000000) + "ms");
			
			long start2 = System.nanoTime();
			List<EmpEnrollPeriodImport> affCompanyHists =  empComHisAdapter.getEnrollmentPeriod(empIdList, period);
			affCompanyHistByEmployeeCache = KeyDateHistoryCache.loaded(affCompanyHists.stream()
					.collect(Collectors.toMap( h -> h.getEmpID(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out.println("thoi gian get data affCompanyHistByEmp " + ((System.nanoTime() - start2 )/1000000) + "ms");
			
			long start3 = System.nanoTime();
			List<EmploymentPeriodImported> listEmploymentPeriodImported = employmentHisScheduleAdapter.getEmploymentPeriod(empIdList, period);
			employmentPeriodCache = KeyDateHistoryCache.loaded(listEmploymentPeriodImported.stream()
					.collect(Collectors.toMap( h -> h.getEmpID(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out.println("thoi gian get data EmploymentPeriod " + ((System.nanoTime() - start3 )/1000000) + "ms");
			
			long start4 = System.nanoTime();
			List<EmployeeLeaveJobPeriodImport> empLeaveJobPeriods = empLeaveHisAdapter.getLeaveBySpecifyingPeriod(empIdList, period);
			empLeaveJobPeriodCache = KeyDateHistoryCache.loaded(empLeaveJobPeriods.stream()
					.collect(Collectors.toMap( h -> h.getEmpID(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out.println("thoi gian get data EmployeeLeaveJob " + ((System.nanoTime() - start4 )/1000000) + "ms");
			
			long start5 = System.nanoTime();
			List<EmpLeaveWorkPeriodImport> empLeaveWorkPeriods =  empLeaveWorkHisAdapter.getHolidayPeriod(empIdList, period);
			empLeaveWorkPeriodCache = KeyDateHistoryCache.loaded(empLeaveWorkPeriods.stream()
					.collect(Collectors.toMap( h -> h.getEmpID(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out.println("thoi gian get data EmpLeaveWork " + ((System.nanoTime() - start5 )/1000000) + "ms");
			
			long start6 = System.nanoTime();
			List<WorkingConditionItemWithPeriod> listData = workCondRepo.getWorkingConditionItemWithPeriod(AppContexts.user().companyId(),empIdList, period);
			workCondItemWithPeriodCache = KeyDateHistoryCache.loaded(listData.stream()
					.collect(Collectors.toMap( h -> h.getWorkingConditionItem().getEmployeeId(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out.println("thoi gian get data WorkingConditionItem " + ((System.nanoTime() - start6 )/1000000) + "ms");
			
			System.out.println("thoi gian get data để lưu vào Cache" + ((System.nanoTime() - start1 )/1000000) + "ms");
		}
		
		@Override
		public Optional<IntegrationOfDaily> getDailyResults(String empId, GeneralDate date) {
			Optional<DailyRecordDto> dailyRecordDto = workScheduleCache.get(empId, date);
			if (dailyRecordDto.isPresent()) {
				IntegrationOfDaily data = dailyRecordDto.get().toDomain(empId, date);
				return Optional.of(data);
			}
			return Optional.empty();
		}

		@Override
		public Optional<EmpEnrollPeriodImport> getAffCompanyHistByEmployee(List<String> sids, DatePeriod datePeriod) {
			Optional<EmpEnrollPeriodImport> data = affCompanyHistByEmployeeCache.get(sids.get(0), datePeriod.start());
			return data;
		}

		@Override
		public Optional<WorkingConditionItem> getBySidAndStandardDate(String employeeId, GeneralDate baseDate) {
			Optional<WorkingConditionItemWithPeriod> data = workCondItemWithPeriodCache.get(employeeId, baseDate);
			return data.isPresent() ? Optional.of(data.get().getWorkingConditionItem()) : Optional.empty();
		}

		@Override
		public Optional<EmployeeLeaveJobPeriodImport> getByDatePeriod(List<String> lstEmpID, DatePeriod datePeriod) {
			Optional<EmployeeLeaveJobPeriodImport> data =  empLeaveJobPeriodCache.get(lstEmpID.get(0), datePeriod.start());
			return data;
		}

		@Override
		public Optional<EmpLeaveWorkPeriodImport> specAndGetHolidayPeriod(List<String> lstEmpID, DatePeriod datePeriod) {
			Optional<EmpLeaveWorkPeriodImport> data = empLeaveWorkPeriodCache.get(lstEmpID.get(0), datePeriod.start());
			return data;
		}

		@Override
		public Optional<EmploymentPeriodImported> getEmploymentHistory(List<String> lstEmpID, DatePeriod datePeriod) {
			Optional<EmploymentPeriodImported> data = employmentPeriodCache.get(lstEmpID.get(0), datePeriod.start());
			return data;
		}
	}
}

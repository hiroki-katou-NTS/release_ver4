package nts.uk.screen.at.app.ksu003.start;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import nts.uk.ctx.at.record.app.find.dailyperform.dto.TimeSpanForCalcDto;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.WorkInformationDto;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.ScheManaStatuTempo;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.TimeVacation;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkScheduleRepository;
import nts.uk.ctx.at.schedule.dom.workschedule.domainservice.WorkScheManaStatusService;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveHistoryAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveWorkHistoryAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveWorkPeriodImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmployeeLeaveJobPeriodImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.TimezoneToUseHourlyHoliday;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.GetWorkInforUsedDailyAttenRecordService;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemWithPeriod;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmpComHisAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmpEnrollPeriodImport;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmploymentHisScheduleAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmploymentPeriodImported;
import nts.uk.screen.at.app.ksu001.start.SupportCategory;
import nts.uk.screen.at.app.ksu003.start.dto.DailyAttdTimeVacationDto;
import nts.uk.screen.at.app.ksu003.start.dto.DisplayWorkInfoByDateDto;
import nts.uk.screen.at.app.ksu003.start.dto.DisplayWorkInfoParam;
import nts.uk.screen.at.app.ksu003.start.dto.EmployeeWorkInfoDto;
import nts.uk.screen.at.app.ksu003.start.dto.EmployeeWorkScheduleDto;
import nts.uk.screen.at.app.ksu003.start.dto.FixedWorkInforDto;
import nts.uk.screen.at.app.ksu003.start.dto.FixedWorkInformationDto;
import nts.uk.screen.at.app.ksu003.start.dto.TimeOfDayDto;
import nts.uk.screen.at.app.ksu003.start.dto.TimeVacationAndTypeDto;
import nts.uk.screen.at.app.ksu003.start.dto.TimeVacationDto;
import nts.uk.screen.at.app.ksu003.start.dto.TimeZoneDto;
import nts.uk.screen.at.app.ksu003.start.dto.WorkInforDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * 日付別勤務情報で表示する
 * UKDesign.UniversalK.就業.KSU_スケジュール.KSU003_個人スケジュール修正(日付別).A：個人スケジュール修正(日付別).メニュー別OCD
 * 
 * @author phongtq
 *
 * note 1 : ※今は応援に対応していないため常にempty。後ほど対応。
 */
@Stateless
public class DisplayWorkInfoByDateSc {
	
	@Inject
	private WorkScheduleRepository workScheduleRepo;
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
	private GetFixedWorkInformation fixedWorkInformation;

	// return ・List<社員勤務情報　dto,社員勤務予定　dto,勤務固定情報　dto>
	public List<DisplayWorkInfoByDateDto> displayDataKsu003(DisplayWorkInfoParam param) {
		GeneralDate generalDate = GeneralDate.fromString(param.getDate(), "yyyy/MM/dd");
		DatePeriod period = new DatePeriod(generalDate, generalDate);
		
		List<DisplayWorkInfoByDateDto> dateDtos = new ArrayList<>();
		
		RequireWorkScheManaStatusImpl requireImpl = new RequireWorkScheManaStatusImpl(param.getLstEmpId(), period,
				workScheduleRepo, empComHisAdapter, workCondRepo, empLeaveHisAdapter, empLeaveWorkHisAdapter,
				employmentHisScheduleAdapter);
		
		// 1 .取得する(Require, List<社員ID>, 期間):Map<社員の予定管理状態, Optional<勤務予定>>
		Map<ScheManaStatuTempo, Optional<WorkSchedule>> mapScheMana = WorkScheManaStatusService
				.getScheduleManagement(requireImpl, param.getLstEmpId(), period);
		List<ScheWorkDto> workDtos = mapScheMana.entrySet().stream().map(mapper-> new ScheWorkDto(mapper.getKey().getEmployeeID(), mapper.getKey(), mapper.getValue())).collect(Collectors.toList());
		workDtos = workDtos.stream().sorted((a, b) -> param.getLstEmpId().indexOf(a.getEmpId()) - param.getLstEmpId().indexOf(b.getEmpId())).collect(Collectors.toList());
		// 2
		for (ScheWorkDto action : workDtos) {
			
			FixedWorkInformationDto inforDto = null; // List＜勤務固定情報　dto>, List<休憩時間帯>
			
			EmployeeWorkInfoDto workInfoDto = null; // 社員勤務情報
			
			EmployeeWorkScheduleDto workScheduleDto = null; // 社員勤務予定
			DisplayWorkInfoByDateDto infoByDateDto = null;
			List<TimeVacationAndTypeDto> typeDto = new ArrayList<>();
			List<TimeZoneDto> shortTime  = new ArrayList<>();
			
			List<WorkInfoOfDailyAttendance> workInfoOfDailyAttendances = new ArrayList<>();
			List<WorkInformation> lstWorkInformation = new ArrayList<>();// 勤務情報
			Integer editState = null;
			Integer startTime1 = null;
			Integer startTime1Status = null;
			Integer startTime2 = null;
			Integer startTime2Status = null;
			String workTypeCode = null;
			Integer workTypeStatus = null;
			String workTimeCode = null;
			Optional<EditStateOfDailyAttd> editStateSet4 = null;
			Integer workTimeStatus = null;
			Integer endTime1 = null;
			Optional<EditStateOfDailyAttd> editStateSet5 = null;
			Integer endTime1Status = null;
			Integer endTime2 = null;
			Optional<EditStateOfDailyAttd> editStateSet6 = null;
			Integer endTime2Status = null;
			
			ScheManaStatuTempo key = action.getManaStatuTempo(); // 予定管理状態
			Optional<WorkSchedule> value = Optional.ofNullable(action.getSchedule()); // 勤務予定
			// 2.1
			boolean checkScheStatus = key.getScheManaStatus().needCreateWorkSchedule();

			// 2.3
			if (value.isPresent()) {
				// 2.3.1 日別勤怠の実績で利用する勤務情報のリストを取得する
				workInfoOfDailyAttendances.add(value.get().getWorkInfo());
				lstWorkInformation = GetWorkInforUsedDailyAttenRecordService.getListWorkInfo(workInfoOfDailyAttendances);
				
				// 2.3.2 取得する(List<勤務情報>)
				// SC 勤務固定情報を取得する
				List<WorkInforDto> informationDtos = lstWorkInformation.stream()
						.map(x-> new WorkInforDto(x.getWorkTypeCode().v(), x.getWorkTimeCode() != null ? x.getWorkTimeCode().v() : null)).collect(Collectors.toList());
				inforDto = fixedWorkInformation.getFixedWorkInfo(informationDtos);
				
				// 2.3.3 時間休暇を取得する():Map<時間休暇種類, 時間休暇>
				// Đợi bên nhật làm TQP
				Map<TimezoneToUseHourlyHoliday, TimeVacation> map = value.get().getTimeVacation();
				
				if(!map.isEmpty()) {
					// ※存在しない場合はempty
					
					typeDto = map.entrySet().stream().map(x-> 
					new TimeVacationAndTypeDto(x.getKey().value, new TimeVacationDto(
							x.getValue().getTimeList().stream().map(y-> new TimeSpanForCalcDto(y.getStart().v(), y.getEnd().v())).collect(Collectors.toList()), 
							new DailyAttdTimeVacationDto(x.getValue().getUseTime().getTimeAnnualLeaveUseTime().v(),
									x.getValue().getUseTime().getTimeCompensatoryLeaveUseTime().v(),
									x.getValue().getUseTime().getSixtyHourExcessHolidayUseTime().v(),
									x.getValue().getUseTime().getTimeSpecialHolidayUseTime().v(),
									x.getValue().getUseTime().getSpecialHolidayFrameNo().get().v(),
									x.getValue().getUseTime().getTimeChildCareHolidayUseTime().v(),
									x.getValue().getUseTime().getTimeCareHolidayUseTime().v())))).collect(Collectors.toList());
				}
				
				shortTime = value.get().getOptSortTimeWork().get().getShortWorkingTimeSheets().
						stream().map(x-> new TimeZoneDto(new TimeOfDayDto(x.getStartTime().getDayDivision().value, x.getStartTime().v()), 
								new TimeOfDayDto(x.getEndTime().getDayDivision().value, x.getEndTime().v()))).collect(Collectors.toList());
				
				workInfoDto = new EmployeeWorkInfoDto(
						// 応援か
						SupportCategory.NotCheering.value,
						// 確定済みか
						value.get().getConfirmedATR().value, 
						// 直帰区分
						value.get().getWorkInfo().getBackStraightAtr().value, 
						// 直行区分
						value.get().getWorkInfo().getGoStraightAtr().value, 
						// 勤務予定が必要か
						checkScheStatus == true ? 1 : 0, 
						// 社員ID
						key.getEmployeeID(), 
						// 年月日
						key.getDate().toString(), 
						// 応援時間帯 ※note 1
						null, 
						// 応援先の職場名称 ※note 1
						"", 
						// Map<時間休暇種類, 時間休暇>=Map<時間休暇種類, 時間休暇>, ※2.3.3で取得したもの。, 
						typeDto, 
						// シフトコード ※note 1
						"", 
						// シフト名称 ※note 1
						"" , 
						// List<育児介護短時間帯>
						shortTime) ;
				
				// List＜休憩時間帯＞＝勤務予定．休憩時間帯
				List<BreakTimeSheet> timeSheets = value.get().getLstBreakTime().stream().flatMap(x-> x.getBreakTimeSheets().stream()).collect(Collectors.toList());
				// comment tạm, cần sửa lại TQP
				List<TimeSpanForCalcDto> listBreakTimeZoneDto = inforDto.getListBreakTimeZoneDto(); //timeSheets.stream().map(x-> new TimeSpanForCalcDto(x.getStartTime().v(), x.getEndTime().v())).collect(Collectors.toList()); 
				
				// Lấy ID để so sánh ở \\192.168.50.4\share\500_新構想開発\04_設計\40_ドメイン設計\ドメイン仕様書\UK\at_就業\shared.scherec_shared(勤務予定、勤務実績)
				// 休憩時間帯編集状態 = 勤務予定．編集状態．編集状態
				Optional<EditStateOfDailyAttd> editStateDaily = value.get().getLstEditState().stream()
						.filter(x -> x.getAttendanceItemId() == 535).findFirst();
				editState = editStateDaily.isPresent() && editStateDaily.get().getEditStateSetting() != null ? editStateDaily.get().getEditStateSetting().value : null;

				// 開始時刻 1= 勤務予定．出退勤．出退勤．出勤
				Optional<TimeLeavingWork> dailyAttd = value.get().getOptTimeLeaving().get().getTimeLeavingWorks()
						.stream().filter(x -> x.getWorkNo().v() == 1).findFirst();
				startTime1 = null;
		
				
				if (dailyAttd.isPresent()) {
					startTime1 = dailyAttd.get().getAttendanceStamp().get().getStamp().get().getTimeDay()
							.getTimeWithDay().get().v();
				}

				// 開始時刻1編集状態 = 勤務予定．編集状態．編集状態
				Optional<EditStateOfDailyAttd> editStateSet = value.get().getLstEditState().stream()
						.filter(x -> x.getAttendanceItemId() == 31).findFirst();
				
				startTime1Status = editStateSet.isPresent() && editStateSet.get().getEditStateSetting() != null ? editStateSet.get().getEditStateSetting().value : null;

				// 開始時刻 2= 勤務予定．出退勤．出退勤．出勤
				Optional<TimeLeavingWork> dailyAttd2 = value.get().getOptTimeLeaving().get().getTimeLeavingWorks()
						.stream().filter(x -> x.getWorkNo().v() == 2).findFirst();
				
				if (dailyAttd2.isPresent()) {
					startTime2 = dailyAttd2.get().getAttendanceStamp().get().getStamp().get().getTimeDay()
							.getTimeWithDay().get().v();
				}

				// 開始時刻2編集状態 = 勤務予定．編集状態．編集状態
				Optional<EditStateOfDailyAttd> editStateSet2 = value.get().getLstEditState().stream()
						.filter(x -> x.getAttendanceItemId() == 41).findFirst();
				
				startTime2Status = editStateSet2.isPresent() && editStateSet2.get().getEditStateSetting()!= null ?  editStateSet2.get().getEditStateSetting().value : null;

				// 勤務種類コード = 勤務予定．勤務情報．勤務実績の勤務情報．勤務種類コード
				workTypeCode = value.get().getWorkInfo().getRecordInfo().getWorkTypeCode().v();

				// 勤務種類編集状態 = 勤務予定．編集状態．編集状態
				Optional<EditStateOfDailyAttd> editStateSet3 = value.get().getLstEditState().stream()
						.filter(x -> x.getAttendanceItemId() == 28).findFirst();
				
				workTypeStatus = editStateSet3.isPresent() && editStateSet3.get().getEditStateSetting() != null ? editStateSet3.get().getEditStateSetting().value : null;

				// 就業時間帯コード = 勤務予定．勤務情報．勤務実績の勤務情報．就業時間帯コード
				workTimeCode = value.get().getWorkInfo().getRecordInfo().getWorkTimeCode() == null ? null : value.get().getWorkInfo().getRecordInfo().getWorkTimeCode().v();

				// 就業時間帯編集状態 = 勤務予定．編集状態．編集状態
				editStateSet4 = value.get().getLstEditState().stream()
						.filter(x -> x.getAttendanceItemId() == 29).findFirst();
				workTimeStatus = editStateSet4.isPresent() && editStateSet4.get().getEditStateSetting() != null ? editStateSet4.get().getEditStateSetting().value : null;

				// 終了時刻 1= 勤務予定．出退勤．出退勤．退勤
				if(dailyAttd.isPresent()) {
					endTime1 = dailyAttd.get().getLeaveStamp().get().getStamp().get().getTimeDay().getTimeWithDay().get()
							.v();
				}

				// 終了時刻1編集状態 = 勤務予定．編集状態．編集状態
				editStateSet5 = value.get().getLstEditState().stream()
						.filter(x -> x.getAttendanceItemId() == 34).findFirst();
				endTime1Status = editStateSet5.isPresent() && editStateSet5.get().getEditStateSetting() != null ? editStateSet5.get().getEditStateSetting().value : null;

				// 終了時刻 2= 勤務予定．出退勤．出退勤．退勤
				
				if(dailyAttd2.isPresent()) {
				endTime2 = dailyAttd2.get().getLeaveStamp().get().getStamp().get().getTimeDay().getTimeWithDay()
						.get().v();
				}
				
				// 終了時刻2編集状態 = 勤務予定．編集状態．編集状態
				editStateSet6 = value.get().getLstEditState().stream()
						.filter(x -> x.getAttendanceItemId() == 44).findFirst();
				endTime2Status = editStateSet6.isPresent() && editStateSet6.get().getEditStateSetting() != null ? editStateSet6.get().getEditStateSetting().value : null;
				 
				workScheduleDto = new EmployeeWorkScheduleDto(
						startTime1, startTime1Status, endTime1, endTime1Status, 
						startTime2, startTime2Status, endTime2, endTime2Status, 
						listBreakTimeZoneDto, workTypeCode, 
						editState,
						workTypeStatus, workTimeCode, workTimeStatus);
				
			} else {
				// 2.2
				// ※今は応援に対応していないため常に「応援ではない」。後ほど対応。
				workInfoDto = new EmployeeWorkInfoDto(1, 0, 0, 0, // SupportAtr.NOT_CHEERING = 0
						checkScheStatus == true ? 1 : 0, key.getEmployeeID(), key.getDate().toString(), null, 
								null, 
						new ArrayList<>(), null, null, new ArrayList<>());
				
				workScheduleDto = null;
			}
			
			infoByDateDto = new DisplayWorkInfoByDateDto(key.getEmployeeID(), workInfoDto, workScheduleDto, inforDto == null ? null : inforDto.getFixedWorkInforDto().get(0));
			dateDtos.add(infoByDateDto);
		};
		
		dateDtos = dateDtos.stream().sorted((object1, object2) -> {
			 			return Integer.compare(param.getLstEmpId().indexOf(object1.getEmpId()), param.getLstEmpId().indexOf(object2.getEmpId()));
			 		}).collect(Collectors.toList());
		
		return dateDtos;
	}

	@AllArgsConstructor
	public static class RequireWorkScheManaStatusImpl implements WorkScheManaStatusService.Require {

		private NestedMapCache<String, GeneralDate, WorkSchedule> workScheduleCache;
		private KeyDateHistoryCache<String, EmpEnrollPeriodImport> affCompanyHistByEmployeeCache;
		private KeyDateHistoryCache<String, EmploymentPeriodImported> employmentPeriodCache;
		private KeyDateHistoryCache<String, EmployeeLeaveJobPeriodImport> empLeaveJobPeriodCache;
		private KeyDateHistoryCache<String, EmpLeaveWorkPeriodImport> empLeaveWorkPeriodCache;
		private KeyDateHistoryCache<String, WorkingConditionItemWithPeriod> workCondItemWithPeriodCache;

		public RequireWorkScheManaStatusImpl(List<String> empIdList, DatePeriod period,
				WorkScheduleRepository workScheduleRepo, EmpComHisAdapter empComHisAdapter,
				WorkingConditionRepository workCondRepo, EmpLeaveHistoryAdapter empLeaveHisAdapter,
				EmpLeaveWorkHistoryAdapter empLeaveWorkHisAdapter,
				EmploymentHisScheduleAdapter employmentHisScheduleAdapter) {

			long start1 = System.nanoTime();
			List<WorkSchedule> lstWorkSchedule = workScheduleRepo.getList(empIdList, period);
			workScheduleCache = NestedMapCache.preloadedAll(lstWorkSchedule.stream(),
					workSchedule -> workSchedule.getEmployeeID(), workSchedule -> workSchedule.getYmd());
			System.out.println("thoi gian get data WorkSchedule " + ((System.nanoTime() - start1) / 1000000) + "ms");

			long start2 = System.nanoTime();
			List<EmpEnrollPeriodImport> affCompanyHists = empComHisAdapter.getEnrollmentPeriod(empIdList, period);
			affCompanyHistByEmployeeCache = KeyDateHistoryCache.loaded(affCompanyHists.stream().collect(Collectors
					.toMap(h -> h.getEmpID(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out.println(
					"thoi gian get data affCompanyHistByEmp " + ((System.nanoTime() - start2) / 1000000) + "ms");

			long start3 = System.nanoTime();
			List<EmploymentPeriodImported> listEmploymentPeriodImported = employmentHisScheduleAdapter
					.getEmploymentPeriod(empIdList, period);
			employmentPeriodCache = KeyDateHistoryCache.loaded(listEmploymentPeriodImported.stream().collect(Collectors
					.toMap(h -> h.getEmpID(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out
					.println("thoi gian get data EmploymentPeriod " + ((System.nanoTime() - start3) / 1000000) + "ms");

			long start4 = System.nanoTime();
			List<EmployeeLeaveJobPeriodImport> empLeaveJobPeriods = empLeaveHisAdapter
					.getLeaveBySpecifyingPeriod(empIdList, period);
			empLeaveJobPeriodCache = KeyDateHistoryCache.loaded(empLeaveJobPeriods.stream().collect(Collectors
					.toMap(h -> h.getEmpID(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out
					.println("thoi gian get data EmployeeLeaveJob " + ((System.nanoTime() - start4) / 1000000) + "ms");

			long start5 = System.nanoTime();
			List<EmpLeaveWorkPeriodImport> empLeaveWorkPeriods = empLeaveWorkHisAdapter.getHolidayPeriod(empIdList,
					period);
			empLeaveWorkPeriodCache = KeyDateHistoryCache.loaded(empLeaveWorkPeriods.stream().collect(Collectors
					.toMap(h -> h.getEmpID(), h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out.println("thoi gian get data EmpLeaveWork " + ((System.nanoTime() - start5) / 1000000) + "ms");

			long start6 = System.nanoTime();
			List<WorkingConditionItemWithPeriod> listData = workCondRepo
					.getWorkingConditionItemWithPeriod(AppContexts.user().companyId(), empIdList, period);
			workCondItemWithPeriodCache = KeyDateHistoryCache
					.loaded(listData.stream().collect(Collectors.toMap(h -> h.getWorkingConditionItem().getEmployeeId(),
							h -> Arrays.asList(DateHistoryCache.Entry.of(h.getDatePeriod(), h)))));
			System.out.println(
					"thoi gian get data WorkingConditionItem " + ((System.nanoTime() - start6) / 1000000) + "ms");

			System.out
					.println("thoi gian get data để lưu vào Cache " + ((System.nanoTime() - start1) / 1000000) + "ms");
		}

		@Override
		public Optional<WorkSchedule> get(String employeeID, GeneralDate ymd) {
			Optional<WorkSchedule> result = workScheduleCache.get(employeeID, ymd);
			return result;
		}

		@Override
		public Optional<EmpEnrollPeriodImport> getAffCompanyHistByEmployee(String sid, GeneralDate startDate) {
			Optional<EmpEnrollPeriodImport> data = affCompanyHistByEmployeeCache.get(sid, startDate);
			return data;
		}

		@Override
		public Optional<WorkingConditionItem> getBySidAndStandardDate(String employeeId, GeneralDate baseDate) {
			Optional<WorkingConditionItemWithPeriod> data = workCondItemWithPeriodCache.get(employeeId, baseDate);
			return data.isPresent() ? Optional.of(data.get().getWorkingConditionItem()) : Optional.empty();
		}

		@Override
		public Optional<EmployeeLeaveJobPeriodImport> getByDatePeriod(String sid, GeneralDate startDate) {
			Optional<EmployeeLeaveJobPeriodImport> data = empLeaveJobPeriodCache.get(sid, startDate);
			return data;
		}

		@Override
		public Optional<EmpLeaveWorkPeriodImport> specAndGetHolidayPeriod(String sid, GeneralDate startDate) {
			Optional<EmpLeaveWorkPeriodImport> data = empLeaveWorkPeriodCache.get(sid, startDate);
			return data;
		}

		@Override
		public Optional<EmploymentPeriodImported> getEmploymentHistory(String sid, GeneralDate startDate) {
			Optional<EmploymentPeriodImported> data = employmentPeriodCache.get(sid, startDate);
			return data;
		}
	}
	
}
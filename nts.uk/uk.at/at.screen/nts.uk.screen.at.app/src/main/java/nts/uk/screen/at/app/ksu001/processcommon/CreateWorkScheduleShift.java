/**
 *
 */
package nts.uk.screen.at.app.ksu001.processcommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.schedule.dom.schedule.support.supportschedule.GetSupportInfoOfEmployee;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.EmployeeId;
import nts.uk.ctx.at.shared.dom.employeeworkway.EmployeeWorkingStatus;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.GetWorkInforUsedDailyAttenRecordService;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportableemployee.SupportableEmployee;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportableemployee.SupportableEmployeeRepository;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.EmpAffiliationInforAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.EmpOrganizationImport;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ColorCodeChar6;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.GetCombinationrAndWorkHolidayAtrService;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.Remarks;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMaster;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterCode;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterDisInfor;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterName;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterRepository;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.screen.at.app.ksu001.displayinshift.ShiftMasterMapWithWorkStyle;
import nts.uk.screen.at.app.ksu001.getworkscheduleshift.ScheduleOfShiftDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author laitv
 * ScreenQuery 勤務予定で勤務予定（シフト）dtoを作成する
 * Path: UKDesign.UniversalK.就業.KSU_スケジュール.KSU001_個人スケジュール修正(職場別).個人別と共通の処理.勤務予定で勤務予定（シフト）dtoを作成する
 */
@Stateless
public class CreateWorkScheduleShift {

	@Inject
	private BasicScheduleService basicScheduleService;
	@Inject
	private WorkTypeRepository workTypeRepo;
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;
	@Inject
	private ShiftMasterRepository shiftMasterRepo;
	@Inject
	private FixedWorkSettingRepository fixedWorkSet;
	@Inject
	private FlowWorkSettingRepository flowWorkSet;
	@Inject
	private FlexWorkSettingRepository flexWorkSet;
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSet;
	@Inject 
	private SupportableEmployeeRepository supportableEmpRepo;
	@Inject
	private EmpAffiliationInforAdapter empAffiliationInforAdapter;


	public WorkScheduleShiftResult getWorkScheduleShift(
			Map<EmployeeWorkingStatus, Optional<WorkSchedule>> mngStatusAndWScheMap,
<<<<<<< HEAD
			List<ShiftMasterMapWithWorkStyle> listShiftMasterNotNeedGetNew,
			TargetOrgIdenInfor targetOrg) {

=======
			List<ShiftMasterMapWithWorkStyle> listShiftMasterNotNeedGetNew) {
		
		String companyId = AppContexts.user().companyId();
		
>>>>>>> pj/at/release_ver4
		// step 1
		List<WorkInfoOfDailyAttendance>  workInfoOfDailyAttendances = new ArrayList<WorkInfoOfDailyAttendance>();
		mngStatusAndWScheMap.forEach((k, v) -> {
			if (v.isPresent()) {
				WorkInfoOfDailyAttendance workInfo = v.get().getWorkInfo();
				if (workInfo.getRecordInfo().getWorkTypeCode() != null) {
					workInfoOfDailyAttendances.add(workInfo);
				}
			}
		});
		// call 日別勤怠の実績で利用する勤務情報のリストを取得する
		List<WorkInformation> lstWorkInfo = GetWorkInforUsedDailyAttenRecordService.getListWorkInfo(workInfoOfDailyAttendances);

		// step 2
		// call シフトマスタと出勤休日区分の組み合わせを取得する
		GetCombinationrAndWorkHolidayAtrService.Require requireImpl = new RequireCombiAndWorkHolidayImpl();
		Map<ShiftMaster,Optional<WorkStyle>> mapShiftMasterWithWorkStyle = GetCombinationrAndWorkHolidayAtrService.getbyWorkInfo(requireImpl,AppContexts.user().companyId(), lstWorkInfo);

		List<ShiftMasterMapWithWorkStyle> listShiftMaster = listShiftMasterNotNeedGetNew;
		List<String> listShiftMasterCodeNotNeedGetNew     = listShiftMasterNotNeedGetNew.stream().map(mapper -> mapper.getShiftMasterCode()).collect(Collectors.toList()); // ko cần get mới

		for (Map.Entry<ShiftMaster, Optional<WorkStyle>> entry : mapShiftMasterWithWorkStyle.entrySet()) {
			String shiftMasterCd = entry.getKey().getShiftMasterCode().toString();
			if(listShiftMasterCodeNotNeedGetNew.contains(shiftMasterCd)){
				// remove di, roi add lai
				ShiftMasterMapWithWorkStyle obj = listShiftMaster.stream().filter(shiftLocal -> shiftLocal.shiftMasterCode.equals(shiftMasterCd)).findFirst().get();
				listShiftMaster.remove(obj);
			}
			ShiftMasterMapWithWorkStyle shift = new ShiftMasterMapWithWorkStyle(entry.getKey(), entry.getValue().isPresent() ? entry.getValue().get().value + "" : null);
			listShiftMaster.add(shift);
		}


		// step 3
		// call loop：entry(Map.Entry) in 管理状態と勤務予定Map
		List<ScheduleOfShiftDto> listWorkScheduleShift = new ArrayList<>();
<<<<<<< HEAD
		WorkInformation.Require requireWorkInformation = new RequireWorkInforImpl();
		
		mngStatusAndWScheMap.forEach((employeeWorkingStatus, workSchedule) -> {

			GetSupportInfoOfEmployee.Require requireGetSupportInfo = new RequireGetSupportInfoImpl(workSchedule, Optional.empty());
			
			ScheduleOfShiftDto dto = new ScheduleOfShiftDto(employeeWorkingStatus, workSchedule, listShiftMaster, targetOrg, requireWorkInformation, requireGetSupportInfo);
			listWorkScheduleShift.add(dto);
=======
		mngStatusAndWScheMap.forEach((k, v) -> {
			EmployeeWorkingStatus key = k;
			Optional<WorkSchedule> value = v;

			// step 3.1
			boolean needToWork = key.getWorkingStatus().needCreateWorkSchedule();
			if (value.isPresent()) {
				WorkSchedule workSchedule = value.get();
				WorkInformation workInformation = workSchedule.getWorkInfo().getRecordInfo();
				// 3.2.1
				WorkInformation.Require require2 = new RequireWorkInforImpl(workTypeRepo,workTimeSettingRepository,workTimeSettingService, basicScheduleService,fixedWorkSet,flowWorkSet,flexWorkSet, predetemineTimeSet);
				Optional<WorkStyle> workStyle = Optional.empty();
				if(workInformation.getWorkTypeCode() != null){
					workStyle = workInformation.getWorkStyle(require2, companyId);
				}

				// 3.2.2
				ShiftEditState shiftEditState = DeterEditStatusShiftService.toDecide(workSchedule);
				ShiftEditStateDto shiftEditStateDto = ShiftEditStateDto.toDto(shiftEditState);

				 String workTypeCode = workInformation.getWorkTypeCode() == null ? null : workInformation.getWorkTypeCode().toString().toString();
				 String workTimeCode = workInformation.getWorkTimeCode() == null ? null : workInformation.getWorkTimeCode().toString().toString();

				Optional<ShiftMasterMapWithWorkStyle> shiftMaster = listShiftMaster.stream().filter(x -> {
					boolean s = Objects.equals(x.workTypeCode, workTypeCode);
					boolean y = Objects.equals(x.workTimeCode, workTimeCode);
					return s&&y;
				}).findFirst();

				if(!shiftMaster.isPresent()){
					System.out.println("Schedule : workType - workTime chua dc dang ky: " + workTypeCode + " - " + workTimeCode);
				}

				// 3.2.3
				ScheduleOfShiftDto dto = ScheduleOfShiftDto.builder()
						.employeeId(key.getEmployeeID())
						.date(key.getDate())
						.haveData(true)
						.achievements(false)
						.confirmed(workSchedule.getConfirmedATR().value == ConfirmedATR.CONFIRMED.value)
						.needToWork(needToWork)
						.supportCategory(SupportCategory.NotCheering.value)
						.shiftCode(shiftMaster.isPresent() ? shiftMaster.get().shiftMasterCode : null)
						.shiftName(shiftMaster.isPresent() ? shiftMaster.get().shiftMasterName : null)
						.shiftEditState(shiftEditStateDto)
						.workHolidayCls(workStyle.isPresent() ? workStyle.get().value : null)
						.isEdit(true)
						.isActive(true)
						.conditionAa1(true)
						.conditionAa2(true)
						.build();

				/**※Aa1
				勤務予定（シフト）dto．実績か == true	           achievements	        ×	
				勤務予定（シフト）dto．確定済みか == true          confirmed		        ×	
				勤務予定（シフト）dto．勤務予定が必要か == false	   needToWork		    ×	
				勤務予定（シフト）dto．応援か == 時間帯応援先               supportCategory		×	
				対象の日 < A画面パラメータ. 修正可能開始日　の場合    targetDate		    ×	=> check dưới UI
				上記以外									   other                ○	
				*/
				if(dto.achievements == true || dto.confirmed == true || dto.needToWork == false || dto.supportCategory == SupportCategory.TimeSupport.value){
					dto.setEdit(false);
					dto.setConditionAa1(false);
				}
				
				/**
				 * ※Aa2			シフト確定																					
				勤務予定（シフト）dto．実績か == true               achievements		×	
				勤務予定（シフト）dto．勤務予定が必要か == false     needToWork	 		×	
				勤務予定（シフト）dto．応援か == 時間帯応援先	    supportCategory		×	
				対象の日 < A画面パラメータ. 修正可能開始日　の場合	targetDate			×	=> check dưới UI
				上記以外										other				○	
				 */
				if(dto.achievements == true || dto.needToWork == false || dto.supportCategory == SupportCategory.TimeSupport.value){
					dto.setActive(false);
					dto.setConditionAa2(false);
				}
				
				listWorkScheduleShift.add(dto);

			} else {
				// 3.3
				ScheduleOfShiftDto dto = ScheduleOfShiftDto.builder()
						.employeeId(key.getEmployeeID())
						.date(key.getDate())
						.haveData(false)
						.achievements(false)
						.confirmed(false)
						.needToWork(needToWork)
						.supportCategory(SupportCategory.NotCheering.value)
						.shiftCode(null)
						.shiftName(null)
						.shiftEditState(null)
						.workHolidayCls(null)
						.isEdit(true)
						.isActive(true)
						.conditionAa1(true)
						.conditionAa2(true)
						.build();
				// ※Aa1
				if(dto.achievements == true || dto.confirmed == true || dto.needToWork == false || dto.supportCategory == SupportCategory.TimeSupport.value){
					dto.setEdit(false);
					dto.setConditionAa1(false);
				}
				// ※Aa2
				if(dto.achievements == true || dto.needToWork == false || dto.supportCategory == SupportCategory.TimeSupport.value){
					dto.setActive(false);
					dto.setConditionAa2(false);
				}
				
				listWorkScheduleShift.add(dto);
			}
>>>>>>> pj/at/release_ver4
		});

		// convert list to Map
		Map<ShiftMaster,Optional<WorkStyle>> mapShiftMasterWithWorkStyle2 = new HashMap<>();
		for (ShiftMasterMapWithWorkStyle obj : listShiftMaster) {
			ShiftMasterDisInfor displayInfor = new ShiftMasterDisInfor(new ShiftMasterName(obj.shiftMasterName), new ColorCodeChar6(obj.color),new ColorCodeChar6(obj.color), Optional.of(new Remarks(obj.remark)));
			ShiftMaster ShiftMaster = new ShiftMaster(companyId, new ShiftMasterCode(obj.shiftMasterCode), displayInfor, obj.workTypeCode, obj.workTimeCode, Optional.empty());
			mapShiftMasterWithWorkStyle2.put(ShiftMaster, obj.workStyle == null ? Optional.empty() : Optional.of(EnumAdaptor.valueOf(Integer.valueOf(obj.workStyle), WorkStyle.class)));
		}

		return new WorkScheduleShiftResult(listWorkScheduleShift, mapShiftMasterWithWorkStyle2);
	}

	@AllArgsConstructor
	private class RequireWorkInforImpl implements WorkInformation.Require {

		private final String companyId = AppContexts.user().companyId();

		@Override
		public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
			 return basicScheduleService.checkNeededOfWorkTimeSetting(workTypeCode);
		}
		
		@Override
		public Optional<WorkType> workType(String companyId, WorkTypeCode workTypeCode) {
			return workTypeRepo.findByPK(companyId, workTypeCode.v());
		}
		
		@Override
		public Optional<WorkTimeSetting> workTimeSetting(String companyId, WorkTimeCode workTimeCode) {
			return workTimeSettingRepository.findByCode(companyId, workTimeCode.v());
		}

		@Override
		public Optional<FixedWorkSetting> fixedWorkSetting(String companyId, WorkTimeCode workTimeCode) {
			return fixedWorkSet.findByKey(companyId, workTimeCode.v());
		}
		
		@Override
		public Optional<FlowWorkSetting> flowWorkSetting(String companyId, WorkTimeCode workTimeCode) {
			return flowWorkSet.find(companyId, workTimeCode.v());
		}
		
		@Override
		public Optional<FlexWorkSetting> flexWorkSetting(String companyId, WorkTimeCode workTimeCode) {
			return flexWorkSet.find(companyId, workTimeCode.v());
		}
		
		@Override
		public Optional<PredetemineTimeSetting> predetemineTimeSetting(String companyId, WorkTimeCode workTimeCode) {
			return predetemineTimeSet.findByWorkTimeCode(companyId, workTimeCode.v());
		}

	}

	@AllArgsConstructor
	private class RequireCombiAndWorkHolidayImpl implements GetCombinationrAndWorkHolidayAtrService.Require {

		private final String companyId = AppContexts.user().companyId();

		@Override
		public List<ShiftMaster> getByListEmp(String companyID, List<String> lstShiftMasterCd) {
			return shiftMasterRepo.getByListShiftMaterCd2(companyId, lstShiftMasterCd);
		}

		@Override
		public List<ShiftMaster> getByListWorkInfo(String companyId, List<WorkInformation> lstWorkInformation) {
			return shiftMasterRepo.get(companyId, lstWorkInformation);
		}

		@Override
		public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
			 return basicScheduleService.checkNeededOfWorkTimeSetting(workTypeCode);
		}

		@Override
		public Optional<WorkType> workType(String companyId, WorkTypeCode workTypeCode) {
			return workTypeRepo.findByPK(companyId, workTypeCode.v());
		}

		@Override
		public Optional<WorkTimeSetting> workTimeSetting(String companyId, WorkTimeCode workTimeCode) {
			return workTimeSettingRepository.findByCode(companyId, workTimeCode.v());
		}

		@Override
		public Optional<FixedWorkSetting> fixedWorkSetting(String companyId, WorkTimeCode workTimeCode) {
			return fixedWorkSet.findByKey(companyId, workTimeCode.v());
		}
		@Override
		public Optional<FlowWorkSetting> flowWorkSetting(String companyId, WorkTimeCode workTimeCode) {
			return flowWorkSet.find(companyId, workTimeCode.v());
		}
		@Override
		public Optional<FlexWorkSetting> flexWorkSetting(String companyId, WorkTimeCode workTimeCode) {
			return flexWorkSet.find(companyId, workTimeCode.v());
		}
		@Override
		public Optional<PredetemineTimeSetting> predetemineTimeSetting(String companyId, WorkTimeCode workTimeCode) {
			return predetemineTimeSet.findByWorkTimeCode(companyId, workTimeCode.v());
		}
	}
	
	private class RequireGetSupportInfoImpl implements GetSupportInfoOfEmployee.Require {
		
		private Optional<WorkSchedule> workSchedule;
		private Optional<IntegrationOfDaily> integrationOfDaily;
		
		public RequireGetSupportInfoImpl(Optional<WorkSchedule> workSchedule, Optional<IntegrationOfDaily> integrationOfDaily) {
			this.workSchedule = workSchedule;
			this.integrationOfDaily = integrationOfDaily;
		}
		
		@Override
		public List<SupportableEmployee> getSupportableEmployee(EmployeeId employeeId, GeneralDate date) {
			return supportableEmpRepo.findByEmployeeIdWithPeriod(employeeId, DatePeriod.oneDay(date));
		}

		@Override
		public List<EmpOrganizationImport> getEmpOrganization(GeneralDate baseDate, List<String> lstEmpId) {
			return empAffiliationInforAdapter.getEmpOrganization(baseDate, lstEmpId);
		}

		@Override
		public Optional<WorkSchedule> getWorkSchedule(String employeeId, GeneralDate date) {
			return workSchedule;
		}

		@Override
		public Optional<IntegrationOfDaily> getRecord(String employeeId, GeneralDate date) {
			return integrationOfDaily;
		}
	}
}

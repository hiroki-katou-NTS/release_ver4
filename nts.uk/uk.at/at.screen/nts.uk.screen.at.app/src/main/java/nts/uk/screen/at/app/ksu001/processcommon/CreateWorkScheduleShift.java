/**
 *
 */
package nts.uk.screen.at.app.ksu001.processcommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.ConfirmedATR;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.ScheManaStatuTempo;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.schedule.dom.workschedule.domainservice.DeterEditStatusShiftService;
import nts.uk.ctx.at.schedule.dom.workschedule.domainservice.ShiftEditState;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.GetWorkInforUsedDailyAttenRecordService;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
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
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.screen.at.app.ksu001.displayinshift.ShiftMasterMapWithWorkStyle;
import nts.uk.screen.at.app.ksu001.getworkscheduleshift.ScheduleOfShiftDto;
import nts.uk.screen.at.app.ksu001.getworkscheduleshift.ShiftEditStateDto;
import nts.uk.screen.at.app.ksu001.start.SupportCategory;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author laitv
 * ScreenQuery 勤務予定で勤務予定（シフト）dtoを作成する
 *
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
	private WorkTimeSettingService workTimeSettingService;
	@Inject
	private ShiftMasterRepository shiftMasterRepo;


	public WorkScheduleShiftResult getWorkScheduleShift(
			Map<ScheManaStatuTempo, Optional<WorkSchedule>> mngStatusAndWScheMap,
			List<ShiftMasterMapWithWorkStyle> listShiftMasterNotNeedGetNew) {

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
		GetCombinationrAndWorkHolidayAtrService.Require requireImpl2 = new RequireCombiAndWorkHolidayImpl(shiftMasterRepo, basicScheduleService, workTypeRepo,workTimeSettingRepository,workTimeSettingService);
		Map<ShiftMaster,Optional<WorkStyle>> mapShiftMasterWithWorkStyle = GetCombinationrAndWorkHolidayAtrService.getbyWorkInfo(requireImpl2,AppContexts.user().companyId(), lstWorkInfo);

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
		mngStatusAndWScheMap.forEach((k, v) -> {
			ScheManaStatuTempo key = k;
			Optional<WorkSchedule> value = v;

			// step 3.1
			boolean needToWork = key.getScheManaStatus().needCreateWorkSchedule();
			if (value.isPresent()) {
				WorkSchedule workSchedule = value.get();
				WorkInformation workInformation = workSchedule.getWorkInfo().getRecordInfo();
				// 3.2.1
				WorkInformation.Require require2 = new RequireWorkInforImpl(workTypeRepo,workTimeSettingRepository,workTimeSettingService, basicScheduleService);
				Optional<WorkStyle> workStyle = Optional.empty();
				if(workInformation.getWorkTypeCode() != null){
					workStyle = workInformation.getWorkStyle(require2);
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
						.build();

				// ※Aa1
				boolean isEdit = true;
				if(dto.achievements == true || dto.confirmed == true || dto.needToWork == false || dto.supportCategory == SupportCategory.TimeSupport.value){
					isEdit = false;
				}
				// ※Aa2
				boolean isActive = true;
				if(dto.achievements == true || dto.needToWork == false || dto.supportCategory == SupportCategory.TimeSupport.value){
					isActive = false;
				}
				dto.setEdit(isEdit);
				dto.setActive(isActive);
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
						.build();
				// ※Aa1
				boolean isEdit = true;
				if(dto.achievements == true || dto.confirmed == true || dto.needToWork == false || dto.supportCategory == SupportCategory.TimeSupport.value){
					isEdit = false;
				}
				// ※Aa2
				boolean isActive = true;
				if(dto.achievements == true || dto.needToWork == false || dto.supportCategory == SupportCategory.TimeSupport.value){
					isActive = false;
				}
				dto.setEdit(isEdit);
				dto.setActive(isActive);
				listWorkScheduleShift.add(dto);
			}
		});

		// convert list to Map
		Map<ShiftMaster,Optional<WorkStyle>> mapShiftMasterWithWorkStyle2 = new HashMap<>();
		String companyId = AppContexts.user().companyId();
		for (ShiftMasterMapWithWorkStyle obj : listShiftMaster) {
			ShiftMasterDisInfor displayInfor = new ShiftMasterDisInfor(new ShiftMasterName(obj.shiftMasterName), new ColorCodeChar6(obj.color), new Remarks(obj.remark));
			ShiftMaster ShiftMaster = new ShiftMaster(companyId, new ShiftMasterCode(obj.shiftMasterCode), displayInfor, obj.workTypeCode, obj.workTimeCode);
			mapShiftMasterWithWorkStyle2.put(ShiftMaster, obj.workStyle == null ? Optional.empty() : Optional.of(EnumAdaptor.valueOf(Integer.valueOf(obj.workStyle), WorkStyle.class)));
		}

		return new WorkScheduleShiftResult(listWorkScheduleShift, mapShiftMasterWithWorkStyle2);
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
		public Optional<WorkType> getWorkType(String workTypeCd) {
			return workTypeRepo.findByPK(companyId, workTypeCd);
		}
		@Override
		public Optional<WorkTimeSetting> getWorkTime(String workTimeCode) {
			return workTimeSettingRepository.findByCode(companyId, workTimeCode);
		}
		@Override
		public PredetermineTimeSetForCalc getPredeterminedTimezone(String workTypeCd,
				String workTimeCd, Integer workNo) {
			return workTimeSettingService .getPredeterminedTimezone(companyId, workTimeCd, workTypeCd, workNo);
		}
		@Override
		public FixedWorkSetting getWorkSettingForFixedWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}
		@Override
		public FlowWorkSetting getWorkSettingForFlowWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}
		@Override
		public FlexWorkSetting getWorkSettingForFlexWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}
		@Override
		public PredetemineTimeSetting getPredetermineTimeSetting(WorkTimeCode wktmCd) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

	}

	@AllArgsConstructor
	private static class RequireCombiAndWorkHolidayImpl implements GetCombinationrAndWorkHolidayAtrService.Require {

		private final String companyId = AppContexts.user().companyId();

		@Inject
		private ShiftMasterRepository shiftMasterRepo;
		@Inject
		private BasicScheduleService service;
		@Inject
		private WorkTypeRepository workTypeRepo;
		@Inject
		private WorkTimeSettingRepository workTimeSettingRepository;
		@Inject
		private WorkTimeSettingService workTimeSettingService;

		@Override
		public List<ShiftMaster> getByListEmp(String companyID, List<String> lstShiftMasterCd) {
			List<ShiftMaster> data = shiftMasterRepo.getByListShiftMaterCd2(companyId, lstShiftMasterCd);
			return data;
		}

		@Override
		public List<ShiftMaster> getByListWorkInfo(String companyId, List<WorkInformation> lstWorkInformation) {
			List<ShiftMaster> data = shiftMasterRepo.get(companyId, lstWorkInformation);
			return data;
		}

		@Override
		public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
			 return service.checkNeededOfWorkTimeSetting(workTypeCode);
		}

		@Override
		public Optional<WorkType> getWorkType(String workTypeCd) {
			return workTypeRepo.findByPK(companyId, workTypeCd);
		}

		@Override
		public Optional<WorkTimeSetting> getWorkTime(String workTimeCode) {
			return workTimeSettingRepository.findByCode(companyId, workTimeCode);
		}

		@Override
		public PredetermineTimeSetForCalc getPredeterminedTimezone(String workTypeCd, String workTimeCd, Integer workNo) {
			return workTimeSettingService .getPredeterminedTimezone(companyId, workTimeCd, workTypeCd, workNo);
		}

		@Override
		public FixedWorkSetting getWorkSettingForFixedWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public FlowWorkSetting getWorkSettingForFlowWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public FlexWorkSetting getWorkSettingForFlexWork(WorkTimeCode code) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public PredetemineTimeSetting getPredetermineTimeSetting(WorkTimeCode wktmCd) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

	}
}
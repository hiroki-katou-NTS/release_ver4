package nts.uk.screen.at.app.ksu001.processcommon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.schedule.dom.schedule.support.supportschedule.GetSupportInfoOfEmployee;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.EmployeeId;
import nts.uk.ctx.at.shared.dom.employeeworkway.EmployeeWorkingStatus;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.GetListWtypeWtimeUseDailyAttendRecordService;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkTypeWorkTimeUseDailyAttendanceRecord;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportableemployee.SupportableEmployee;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportableemployee.SupportableEmployeeRepository;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.EmpAffiliationInforAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.EmpOrganizationImport;
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
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeInfor;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author laitv
 * ScreenQuery: ?????????????????????????????????????????????dto???????????????
 * Path : UKDesign.UniversalK.??????.KSU_??????????????????.KSU001_??????????????????????????????(?????????).???????????????????????????.?????????????????????????????????????????????dto???????????????
 */
@Stateless
public class CreateWorkScheduleWorkInfor {
	
	@Inject
	private WorkTypeRepository workTypeRepo;
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepo;
	@Inject
	private BasicScheduleService basicScheduleService;
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
	
	public List<WorkScheduleWorkInforDto> getDataScheduleOfWorkInfo(
			Map<EmployeeWorkingStatus, Optional<WorkSchedule>> mngStatusAndWScheMap, TargetOrgIdenInfor targetOrg) {		

		String companyId = AppContexts.user().companyId();
		List<WorkInfoOfDailyAttendance>  listWorkInfo = new ArrayList<WorkInfoOfDailyAttendance>();
		mngStatusAndWScheMap.forEach((k,v)->{
			if (v.isPresent()) {
				listWorkInfo.add(v.get().getWorkInfo());
			}
		});
		
		// step 1 call DomainService: ?????????????????????????????????????????????????????????????????????????????????????????????
		WorkTypeWorkTimeUseDailyAttendanceRecord wTypeWTimeUseDailyAttendRecord = GetListWtypeWtimeUseDailyAttendRecordService.getdata(listWorkInfo);

		// step 2
		List<WorkTypeCode> workTypeCodes = wTypeWTimeUseDailyAttendRecord.getLstWorkTypeCode().stream().filter(wt -> wt != null).collect(Collectors.toList());
		List<String> lstWorkTypeCode     = workTypeCodes.stream().map(i -> i.toString()).collect(Collectors.toList());
		//<<Public>> ????????????????????????????????????????????????
		List<WorkTypeInfor> lstWorkTypeInfor = this.workTypeRepo.getPossibleWorkTypeAndOrder(companyId, lstWorkTypeCode).stream().collect(Collectors.toList());

		// step 3
		List<WorkTimeCode> workTimeCodes   = wTypeWTimeUseDailyAttendRecord.getLstWorkTimeCode().stream().filter(wt -> wt != null).collect(Collectors.toList());
		List<String> lstWorkTimeCode       = workTimeCodes.stream().map(i -> i.toString()).collect(Collectors.toList());
		List<WorkTimeSetting> lstWorkTimeSetting =  workTimeSettingRepo.getListWorkTime(companyId, lstWorkTimeCode);

		// step 4
		List<WorkScheduleWorkInforDto> listWorkScheduleWorkInfor = new ArrayList<>();
		WorkInformation.Require requireWorkInfo = new RequireWorkInforImpl();

		mngStatusAndWScheMap.forEach((employeeWorkingStatus, workScheduleOpt) -> {
			
			Optional<WorkTypeInfor> workTypeInfor = Optional.empty();
			Optional<WorkTimeSetting> workTimeSetting = Optional.empty();
			if (workScheduleOpt.isPresent()) {
				//??????????????????2???List<????????????> filter:$???????????????????????????????????????.????????????.?????????????????????
				WorkInformation workInformation = workScheduleOpt.get().getWorkInfo().getRecordInfo();
				String workTypeCode = workInformation.getWorkTypeCode() == null ? null : workInformation.getWorkTypeCode().toString();
				workTypeInfor = lstWorkTypeInfor.stream().filter(i -> i.getWorkTypeCode().equals(workTypeCode)).findFirst();
				
				//?????????????????????3???List<???????????????> filter:$??????????????????????????????????????????.????????????.????????????????????????
				String workTimeCode = workInformation.getWorkTimeCode() == null  ? null : workInformation.getWorkTimeCode().toString();
				workTimeSetting = lstWorkTimeSetting.stream().filter(i -> i.getWorktimeCode().toString().equals(workTimeCode)).findFirst();
			}
			
			GetSupportInfoOfEmployee.Require requireGetSupportInfo = new RequireGetSupportInfoImpl(workScheduleOpt, Optional.empty());
			
			WorkScheduleWorkInforDto dto = new WorkScheduleWorkInforDto(employeeWorkingStatus, workScheduleOpt, workTypeInfor, workTimeSetting, targetOrg, wTypeWTimeUseDailyAttendRecord, requireWorkInfo, requireGetSupportInfo);
			listWorkScheduleWorkInfor.add(dto);
		});

		return listWorkScheduleWorkInfor;
	}
	
	@AllArgsConstructor
	private class RequireWorkInforImpl implements WorkInformation.Require {

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
			return workTimeSettingRepo.findByCode(companyId, workTimeCode.v());
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

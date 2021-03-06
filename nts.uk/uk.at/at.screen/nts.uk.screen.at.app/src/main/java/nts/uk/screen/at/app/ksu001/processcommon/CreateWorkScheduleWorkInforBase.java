/**
 * 
 */
package nts.uk.screen.at.app.ksu001.processcommon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.schedule.dom.schedule.support.supportschedule.GetSupportInfoOfEmployee;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.EmployeeId;
import nts.uk.ctx.at.shared.dom.employeeworkway.EmployeeWorkingStatus;
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
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeInfor;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author laitv
 * ScreenQuery: ?????????????????????????????????????????????dto???????????????
 * Path: UKDesign.UniversalK.??????.KSU_??????????????????.KSU001_??????????????????????????????(?????????).???????????????????????????.?????????????????????????????????????????????dto???????????????
 */

@Stateless
public class CreateWorkScheduleWorkInforBase {
	
	@Inject
	private WorkTypeRepository workTypeRepo;
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepo;
	@Inject 
	private SupportableEmployeeRepository supportableEmpRepo;
	@Inject
	private EmpAffiliationInforAdapter empAffiliationInforAdapter;
	
	public List<WorkScheduleWorkInforDto> getDataScheduleOfWorkInfo(
			Map<EmployeeWorkingStatus, Optional<IntegrationOfDaily>> map, TargetOrgIdenInfor targetOrg) {		
		
		String companyId = AppContexts.user().companyId();
		List<WorkInfoOfDailyAttendance> listWorkInfo = new ArrayList<WorkInfoOfDailyAttendance>();
		map.forEach((k, v) -> {
			if (v.isPresent()) {
				WorkInfoOfDailyAttendance workInfo = v.get().getWorkInformation();
				if (workInfo != null) {
					listWorkInfo.add(workInfo);
				}
			}
		});

		// call DomainService: ?????????????????????????????????????????????????????????????????????????????????????????????
		WorkTypeWorkTimeUseDailyAttendanceRecord wTypeWTimeUseDailyAttendRecord = GetListWtypeWtimeUseDailyAttendRecordService.getdata(listWorkInfo);

		// step 2
		List<WorkTypeCode> workTypeCodes = wTypeWTimeUseDailyAttendRecord.getLstWorkTypeCode().stream().filter(wt -> wt != null).collect(Collectors.toList());
		List<String> lstWorkTypeCode     = workTypeCodes.stream().map(i -> i.toString()).collect(Collectors.toList());
		//<<Public>> ????????????????????????????????????????????????
		List<WorkTypeInfor> lstWorkTypeInfor = this.workTypeRepo.getPossibleWorkTypeAndOrder(companyId, lstWorkTypeCode).stream().collect(Collectors.toList());;

		// step 3
		List<WorkTimeCode> workTimeCodes   = wTypeWTimeUseDailyAttendRecord.getLstWorkTimeCode().stream().filter(wt -> wt != null).collect(Collectors.toList());
		List<String> lstWorkTimeCode       = workTimeCodes.stream().map(i -> i.toString()).collect(Collectors.toList());
		List<WorkTimeSetting> lstWorkTimeSetting = workTimeSettingRepo.getListWorkTime(companyId, lstWorkTimeCode);

		// step 4
		List<WorkScheduleWorkInforDto> listWorkScheduleWorkInfor = new ArrayList<>();
		map.forEach((employeeWorkingStatus, integrationOfDaily) -> {
			
			Optional<WorkTypeInfor> workTypeInfor = Optional.empty();
			Optional<WorkTimeSetting> workTimeSetting = Optional.empty();
			if (integrationOfDaily.isPresent()) {
				//??????????????????2???List<????????????> filter:$???????????????????????????????????????(Work).????????????.?????????????????????
				WorkInformation workInformation = integrationOfDaily.get().getWorkInformation().getRecordInfo();
				String workTypeCode = workInformation.getWorkTypeCode() == null ? null : workInformation.getWorkTypeCode().toString();
				workTypeInfor = lstWorkTypeInfor.stream().filter(i -> i.getWorkTypeCode().equals(workTypeCode)).findFirst();
				
				//?????????????????????3???List<???????????????> filter:$??????????????????????????????????????????(Work).????????????.????????????????????????
				String workTimeCode = workInformation.getWorkTimeCode() == null  ? null : workInformation.getWorkTimeCode().toString();
				workTimeSetting = lstWorkTimeSetting.stream().filter(i -> i.getWorktimeCode().toString().equals(workTimeCode)).findFirst();
				
				GetSupportInfoOfEmployee.Require requireGetSupportInfo = new RequireGetSupportInfoImpl(Optional.empty(), integrationOfDaily);
				
				WorkScheduleWorkInforDto dto = new WorkScheduleWorkInforDto(integrationOfDaily, employeeWorkingStatus, workTypeInfor, workTimeSetting, targetOrg, wTypeWTimeUseDailyAttendRecord, requireGetSupportInfo);
				
				listWorkScheduleWorkInfor.add(dto);
			}
		});

		return listWorkScheduleWorkInfor;
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
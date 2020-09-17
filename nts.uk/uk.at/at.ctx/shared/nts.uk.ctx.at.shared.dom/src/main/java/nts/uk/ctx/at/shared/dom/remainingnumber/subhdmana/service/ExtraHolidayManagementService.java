package nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.PersonEmpBasicInfoImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.EmploymentManageDistinctDto;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SEmpHistoryImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SWkpHistImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SysEmploymentHisAdapter;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SysWorkplaceAdapter;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.ComDayOffManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.CompensatoryDayOffManaData;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveComDayOffManaRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveComDayOffManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManagementData;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveEmSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveEmSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ExtraHolidayManagementService {

	@Inject
	private LeaveManaDataRepository leaveManaDataRepository;

	@Inject
	private ComDayOffManaDataRepository comDayOffManaDataRepository;

	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;

	@Inject
	private SysEmploymentHisAdapter sysEmploymentHisAdapter;

	@Inject
	private LeaveComDayOffManaRepository leaveComDayOffManaRepository;

	@Inject
	private CompensLeaveEmSetRepository compensLeaveEmSetRepository;

	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;

	@Inject
	private SysWorkplaceAdapter syWorkplaceAdapter;

	@Inject
	private EmpEmployeeAdapter empEmployeeAdapter;
	
	public ExtraHolidayManagementOutput dataExtractionProcessing(int searchMode, String employeeId) {
		String cid = AppContexts.user().companyId();
		List<LeaveManagementData> listLeaveData = null;
		List<CompensatoryDayOffManaData> listCompensatoryData = null;
		List<LeaveComDayOffManagement> listLeaveComDayOffManagement = new ArrayList<>();
		SEmpHistoryImport empHistoryImport = null;
		ClosureEmployment closureEmploy = null;
		CompensatoryLeaveEmSetting compenLeaveEmpSetting = null;
		CompensatoryLeaveComSetting compensatoryLeaveComSetting = null;
		GeneralDate baseDate = GeneralDate.today();
		System.out.println(searchMode);
		
		// 全ての状況
		if (searchMode == 0) {
			
			listLeaveData = leaveManaDataRepository.getBySidNotUnUsed(cid, employeeId);
			listCompensatoryData = comDayOffManaDataRepository.getBySidWithReDay(cid, employeeId);
		} else if (searchMode == 1){
			
			listLeaveData = leaveManaDataRepository.getAllData();
			listCompensatoryData = comDayOffManaDataRepository.getAllData();
		}
		if (!listLeaveData.isEmpty()) {
			List<String> listLeaveID = listLeaveData.stream().map(x -> {
				return x.getID();
			}).collect(Collectors.toList());
			listLeaveComDayOffManagement.addAll(leaveComDayOffManaRepository.getByListComLeaveID(listLeaveID));
		}
		if (!listCompensatoryData.isEmpty()) {
			List<String> listComId = listCompensatoryData.stream().map(x -> {
				return x.getComDayOffID();
			}).collect(Collectors.toList());
			listLeaveComDayOffManagement.addAll(leaveComDayOffManaRepository.getByListComId(listComId));
		}
		Optional<SEmpHistoryImport> sEmpHistoryImport = sysEmploymentHisAdapter.findSEmpHistBySid(cid, employeeId,
				baseDate);
		if (sEmpHistoryImport.isPresent()) {
			empHistoryImport = sEmpHistoryImport.get();
			String sCd = empHistoryImport.getEmploymentCode();
			Optional<ClosureEmployment> closureEmployment = closureEmploymentRepository.findByEmploymentCD(cid, sCd);
			if (closureEmployment.isPresent()) {
				closureEmploy = closureEmployment.get();
			}
		}
		Optional<SWkpHistImport> sWkpHistImport = syWorkplaceAdapter.findBySid(employeeId, baseDate);
		if (!Objects.isNull(empHistoryImport)) {
			compenLeaveEmpSetting = compensLeaveEmSetRepository.find(cid, empHistoryImport.getEmploymentCode());
		}
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		List<PersonEmpBasicInfoImport> employeeBasicInfo = empEmployeeAdapter.getPerEmpBasicInfo(employeeIds);
		PersonEmpBasicInfoImport personEmpBasicInfoImport = null;
		if (!employeeBasicInfo.isEmpty()) {
			personEmpBasicInfoImport = employeeBasicInfo.get(0);
		}
		compensatoryLeaveComSetting = compensLeaveComSetRepository.find(cid);
		return new ExtraHolidayManagementOutput(listLeaveData, listCompensatoryData, listLeaveComDayOffManagement,
				empHistoryImport, closureEmploy, compenLeaveEmpSetting, compensatoryLeaveComSetting,
				sWkpHistImport.orElse(null), personEmpBasicInfoImport);
	}
	
	// Step 代休管理データを管理するかチェック
	public void CheckManageSubstituteHolidayManagementData(String empId) {
		EmploymentManageDistinctDto empManage = new EmploymentManageDistinctDto();
		empManage.setIsManage(ManageDistinct.NO);
	}
}

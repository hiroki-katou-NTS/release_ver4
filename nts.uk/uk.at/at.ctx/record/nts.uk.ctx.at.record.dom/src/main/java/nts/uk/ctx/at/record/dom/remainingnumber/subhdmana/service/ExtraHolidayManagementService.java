package nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.SEmpHistoryImport;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.SysEmploymentHisAdapter;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.ComDayOffManaDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.CompensatoryDayOffManaData;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.LeaveComDayOffManaRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.LeaveComDayOffManagement;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.LeaveManaDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.LeaveManagementDataAgg;
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
	
	
	public ExtraHolidayManagementOutput dataExtractionProcessing (int searchMode, String employeeId, GeneralDate startDate, GeneralDate endDate){
		String cid = AppContexts.user().companyId();
		List<LeaveManagementDataAgg> listLeaveData = null;
		List<CompensatoryDayOffManaData> listCompensatoryData = null;
		List<LeaveComDayOffManagement> listLeaveComDayOffManagement = new ArrayList<>();
		SEmpHistoryImport empHistoryImport = null;
		ClosureEmployment closureEmploy = null;
		GeneralDate baseDate = GeneralDate.today();
		if (searchMode == 0){
			listLeaveData = leaveManaDataRepository.getBySidNotUnUsed(cid, employeeId);
			listCompensatoryData = comDayOffManaDataRepository.getBySidWithReDay(cid, employeeId); 
		} else {
			listLeaveData = leaveManaDataRepository.getByDateCondition(cid, employeeId, startDate, endDate);
			listCompensatoryData = comDayOffManaDataRepository.getByDateCondition(cid, employeeId, startDate, endDate);
		}
		if (!listLeaveData.isEmpty() && !listCompensatoryData.isEmpty()){
			List<String> listLeaveID = listLeaveData.stream().map(x ->{
				return x.getID();
			}).collect(Collectors.toList());
			listLeaveComDayOffManagement = leaveComDayOffManaRepository.getByListComLeaveID(listLeaveID);
		}
		Optional<SEmpHistoryImport> sEmpHistoryImport = sysEmploymentHisAdapter.findSEmpHistBySid(cid, employeeId, baseDate);
		if (sEmpHistoryImport.isPresent()){
			empHistoryImport = sEmpHistoryImport.get();
			String sCd = empHistoryImport.getEmploymentCode();
			Optional<ClosureEmployment> closureEmployment = closureEmploymentRepository.findByEmploymentCD(cid, sCd);
			if (closureEmployment.isPresent()){
				closureEmploy = closureEmployment.get();
			}
		}
		return new ExtraHolidayManagementOutput(listLeaveData, listCompensatoryData, listLeaveComDayOffManagement, empHistoryImport, closureEmploy);
	}
}

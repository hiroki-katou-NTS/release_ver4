package nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.EmpPerformMonthParamImport;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalStatusOfDailyPerfor;
import nts.uk.ctx.at.record.dom.approvalmanagement.check.CheckApprovalOperation;
import nts.uk.ctx.at.record.dom.approvalmanagement.enums.ConfirmationOfManagerOrYouself;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalStatusOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author thanhnx <<Public>> 日の承認を登録する
 */
@Stateless
public class RegisterDayApproval {

	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessingRepository;
	@Inject
	private CheckApprovalOperation checkApprovalOperation;
	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepository;
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	// code old
//	@Inject
//	private OpOfDailyPerformance opOfDailyPerformance;
	
	@Inject
	private ApprovalStatusOfDailyPerforRepository approvalStatusOfDailyPerforRepository;
	
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	public Set<Pair<String, GeneralDate>> registerDayApproval(ParamDayApproval param, Set<Pair<String, GeneralDate>> updated) {
		if(param.getContentApproval().isEmpty()) return new HashSet<>();
		String companyId = AppContexts.user().companyId();
		Map<Pair<String, GeneralDate>, GeneralDate> employeeIdInsert = new HashMap<>();
		Map<Pair<String, GeneralDate>, GeneralDate> employeeIdRealse = new HashMap<>();
		Map<Pair<String, GeneralDate>, GeneralDate> employeeIdRealseAll = new HashMap<>();
		Set<Pair<String, GeneralDate>> insertApproval = new HashSet<>();
		Optional<ApprovalProcessingUseSetting> approvalSetting = approvalProcessingRepository
				.findByCompanyId(companyId);
		if (!approvalSetting.isPresent())
			return new HashSet<>();
		Optional<ConfirmationOfManagerOrYouself> checkOpt = checkApprovalOperation.checkApproval(approvalSetting.get());
		if (!checkOpt.isPresent())
			return new HashSet<>();
		if (checkOpt.get().value == ConfirmationOfManagerOrYouself.CAN_CHECK.value) {
			param.getContentApproval().forEach(x -> {
				if(x.isFlagRemmoveAll()){
					employeeIdRealseAll.put(Pair.of(x.getEmployeeId(), x.getDate()), x.getDate());
				}
				else if (x.isStatus()) {
					employeeIdInsert.put(Pair.of(x.getEmployeeId(), x.getDate()), x.getDate());
				} else {
					employeeIdRealse.put(Pair.of(x.getEmployeeId(), x.getDate()), x.getDate());
				}
			});
		} else {
			Map<String, List<GeneralDate>> mapEmpErrorDate = param.getContentApproval()
					.stream().collect(Collectors.groupingBy(ContentApproval::getEmployeeId, 
							Collectors.collectingAndThen(Collectors.toList(), list -> list.stream().map(x -> x.getDate()).collect(Collectors.toList()))));
			List<EmployeeDailyPerError> lstEmpError = employeeDailyPerErrorRepository.finds(mapEmpErrorDate);
			for (ContentApproval data : param.getContentApproval()) {
				List<EmployeeDailyPerError> employeeDailyPerErrors = lstEmpError.stream().filter(
						x -> x.getEmployeeID().equals(data.getEmployeeId()) && x.getDate().equals(data.getDate()))
						.collect(Collectors.toList());
				boolean isNotError = true;
				if (!employeeDailyPerErrors.isEmpty()) {
					List<ErrorAlarmWorkRecord> errorAlarmWorkRecords = errorAlarmWorkRecordRepository
							.getListErAlByListCodeError(companyId, employeeDailyPerErrors.stream()
									.map(x -> x.getErrorAlarmWorkRecordCode().v()).collect(Collectors.toList()));
					if (!errorAlarmWorkRecords.isEmpty()) {
						isNotError = false;
					}
				}
				if (isNotError) {
					if(data.isFlagRemmoveAll()){
						employeeIdRealseAll.put(Pair.of(data.getEmployeeId(), data.getDate()), data.getDate());
					}
					else if (data.isStatus()) {
						employeeIdInsert.put(Pair.of(data.getEmployeeId(), data.getDate()), data.getDate());
					} else {
						employeeIdRealse.put(Pair.of(data.getEmployeeId(), data.getDate()), data.getDate());
					}
				}
			}
		}
		
		Set<Pair<String, GeneralDate>> shoudUpVer = new HashSet<>();
		// release status == false
		if (!employeeIdRealse.isEmpty()) {
			approvalStatusAdapter.releaseApproval(param.getEmployeeId(),
					employeeIdRealse.keySet().stream().collect(Collectors.toList()), 1, companyId);
			updated.addAll(employeeIdRealse.keySet());
		}
		// register status == true
		if (!employeeIdInsert.isEmpty()) {
			approvalStatusAdapter.registerApproval(param.getEmployeeId(),
					employeeIdInsert.keySet().stream().collect(Collectors.toList()), 1, companyId);
			updated.addAll(employeeIdInsert.keySet());
			insertApproval.addAll(employeeIdInsert.keySet());
		}
		
		if(!employeeIdRealseAll.isEmpty()){
			employeeIdRealseAll.entrySet().forEach(x ->{
				Optional<ApprovalStatusOfDailyPerfor> dailyPerforOpt= approvalStatusOfDailyPerforRepository.find(x.getKey().getLeft(), x.getKey().getRight());
				if(dailyPerforOpt.isPresent()){
					approvalStatusAdapter.cleanApprovalRootState(dailyPerforOpt.get().getEmployeeId(), x.getValue(), 1);
					shoudUpVer.add(x.getKey());
				}
			});
		}
		
//		if(!shoudUpVer.isEmpty()){
//			shoudUpVer.stream().forEach(pair -> {
//				workInfo.updated(pair.getKey(), pair.getValue());
//			});
//		}
		return insertApproval;
	}
	
	public void registerMonApproval(String approverID, List<EmpPerformMonthParamImport> listParamApproval) {
		approvalStatusAdapter.approveMonth(approverID, listParamApproval);;	
	}
	
	public void removeMonApproval(String approverID, List<EmpPerformMonthParamImport> listParamApproval) {
		approvalStatusAdapter.cancelMonth(approverID, listParamApproval);	
	}
}

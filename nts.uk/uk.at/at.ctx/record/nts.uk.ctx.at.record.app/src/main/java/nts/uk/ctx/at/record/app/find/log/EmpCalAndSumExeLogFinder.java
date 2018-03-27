package nts.uk.ctx.at.record.app.find.log;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.find.log.dto.EmpCalAndSumExeLogDto;
import nts.uk.ctx.at.record.app.find.log.dto.InputEmpCalAndSumByDate;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLogRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class EmpCalAndSumExeLogFinder {

	@Inject
	private EmpCalAndSumExeLogRepository empCalAndSumExeLogRepo;
	@Inject
	private ExecutionLogRepository executionLogRepo;

	/**
	 * get All EmpCalAndSumExeLog by Employee and ID DESC
	 * 
	 * @return list EmpCalAndSumExeLog
	 */
	public EmpCalAndSumExeLogDto getEmpCalAndSumExeLogMaxByEmp() {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		Optional<EmpCalAndSumExeLogDto> data = empCalAndSumExeLogRepo
				.getEmpCalAndSumExeLogMaxByEmp(companyID, employeeID).map(c -> EmpCalAndSumExeLogDto.fromDomain(c,this.executionLogRepo.getExecutionLogs(c.getEmpCalAndSumExecLogID())));
		if (data.isPresent())
			return data.get();
		return null;
	}

	/**
	 * get All EmpCalAndSumExeLog
	 * 
	 * @return list EmpCalAndSumExeLog
	 */
	public List<EmpCalAndSumExeLogDto> getAllEmpCalAndSumExeLog() {
		String companyID = AppContexts.user().companyId();
		List<EmpCalAndSumExeLogDto> data = empCalAndSumExeLogRepo.getAllEmpCalAndSumExeLog(companyID).stream()
				.map(c -> EmpCalAndSumExeLogDto.fromDomain(c,this.executionLogRepo.getExecutionLogs(c.getEmpCalAndSumExecLogID()))).collect(Collectors.toList());
		if (data.isEmpty())
			return Collections.emptyList();
		return data;
	}

	/**
	 * get EmpCalAndSumExeLogDto by empCalAndSumExecLogID
	 * 
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	public EmpCalAndSumExeLogDto getByEmpCalAndSumExecLogID(String empCalAndSumExecLogID) {
		Optional<EmpCalAndSumExeLogDto> data = empCalAndSumExeLogRepo.getByEmpCalAndSumExecLogID(empCalAndSumExecLogID)
				.map(c -> EmpCalAndSumExeLogDto.fromDomain(c,this.executionLogRepo.getExecutionLogs(c.getEmpCalAndSumExecLogID())));
		if (data.isPresent())
			return data.get();
		return null;
	}

	/**
	 * get list EmpCalAndSumExeLogDto by startDate and endDate
	 * 
	 * @param inputEmpCalAndSumByDate
	 * @return
	 */
	public List<EmpCalAndSumExeLogDto> getEmpCalAndSumExeLogByDate(InputEmpCalAndSumByDate inputEmpCalAndSumByDate) {
		String companyID = AppContexts.user().companyId();
		List<EmpCalAndSumExeLog> lstDomain = empCalAndSumExeLogRepo
				.getAllEmpCalAndSumExeLogByDate(companyID, inputEmpCalAndSumByDate.getStartDate(),
						inputEmpCalAndSumByDate.getEndDate());
		List<EmpCalAndSumExeLogDto> data = lstDomain.stream().map(c -> EmpCalAndSumExeLogDto.fromDomain(c,this.executionLogRepo.getExecutionLogs(c.getEmpCalAndSumExecLogID()))).collect(Collectors.toList());
		if (data.isEmpty())
			return Collections.emptyList();
		return data;
	}

}

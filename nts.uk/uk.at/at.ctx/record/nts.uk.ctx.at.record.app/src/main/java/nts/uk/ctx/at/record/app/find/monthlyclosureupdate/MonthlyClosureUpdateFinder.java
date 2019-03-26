package nts.uk.ctx.at.record.app.find.monthlyclosureupdate;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.auth.dom.employmentrole.EmployeeReferenceRange;
import nts.uk.ctx.at.record.app.command.monthlyclosureupdate.MonthlyClosureResponse;
import nts.uk.ctx.at.record.app.command.monthlyclosureupdate.OutputParam;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.record.dom.adapter.query.employee.RegulationInfoEmployeeQuery;
import nts.uk.ctx.at.record.dom.adapter.query.employee.RegulationInfoEmployeeQueryAdapter;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureCompleteStatus;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureExecutionStatus;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosurePersonExecutionResult;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosurePersonExecutionStatus;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdateErrorInfor;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdateErrorInforRepository;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdateLog;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdateLogRepository;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdatePersonLog;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdatePersonLogRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureInfor;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class MonthlyClosureUpdateFinder {

	@Inject
	private MonthlyClosureUpdateLogRepository monthlyClosureUpdateRepo;

	@Inject
	private MonthlyClosureUpdatePersonLogRepository closureUpdatePersonLogRepo;

	@Inject
	private ClosureService closureService;

	@Inject
	private MonthlyClosureUpdateErrorInforRepository errorInforRepo;

	@Inject
	private EmployeeRecordAdapter empImportAdapter;

	@Inject
	private ClosureRepository closureRepo;

	@Inject
	private MonthlyClosureUpdatePersonLogRepository monthlyClosurePersonLogRepo;
	
	@Inject
	private MonthlyClosureUpdateLogRepository monthlyClosureUpdateLogRepository;
	
	@Inject
	private RegulationInfoEmployeeQueryAdapter employeeSearch;

	
	public MonthlyClosureUpdateLogDto findById(String id) {
		return domainToDto(monthlyClosureUpdateRepo.getLogById(id).get());
	}

	public Kmw006fResultDto getClosureResult(String monthlyClosureUpdateLogId) {
		List<MonthlyClosureErrorInforDto> listResult = new ArrayList<>();
		MonthlyClosureUpdateLog updateLog = monthlyClosureUpdateRepo.getLogById(monthlyClosureUpdateLogId).get();
		List<MonthlyClosureUpdateErrorInfor> listErrorInfo = errorInforRepo.getAll(monthlyClosureUpdateLogId);
		if (!listErrorInfo.isEmpty()) {
			for (MonthlyClosureUpdateErrorInfor errInfor : listErrorInfo) {
				EmployeeRecordImport empImport = empImportAdapter.getPersonInfor(errInfor.getEmployeeId());
				MonthlyClosureErrorInforDto result = new MonthlyClosureErrorInforDto(empImport.getEmployeeCode(),
						empImport.getPname(), errInfor.getErrorMessage(), errInfor.getAtr().value);
				listResult.add(result);
			}
		}
		listResult.sort((i1, i2) -> i1.getEmployeeCode().compareToIgnoreCase(i2.getEmployeeCode()));
		return new Kmw006fResultDto(listResult, domainToDto(updateLog));
	}

	private MonthlyClosureUpdateLogDto domainToDto(MonthlyClosureUpdateLog domain) {
		return new MonthlyClosureUpdateLogDto(domain.getId(), domain.getCompanyId(), domain.getExecutionStatus().value,
				domain.getCompleteStatus().value, domain.getExecutionDateTime(), domain.getExecutionPeriod().start(),
				domain.getExecutionPeriod().end(), domain.getExecuteEmployeeId(), domain.getTargetYearMonth().v(),
				domain.getClosureId().value, domain.getClosureDate().getClosureDay().v(),
				domain.getClosureDate().getLastDayOfMonth());
	}

	public Kmw006aResultDto getClosureInfors(MonthlyClosureResponse response) {
		String companyId = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();
		OutputParam outputParam = this.checkExecutionStatus(companyId);
		GeneralDateTime currentDT = GeneralDateTime.now();

		if (outputParam.getStatus() == 2) { 
			// open dialog F
			MonthlyClosureUpdateLog log = outputParam.getOutputLog().get();
			if (response.getMonthlyClosureUpdateLogId() != null) {

				List<String> listEmpId = closureUpdatePersonLogRepo.getAll(log.getId()).stream()
						.map(item -> item.getEmployeeId()).collect(Collectors.toList());
				MonthlyClosureResponse resultClosurtLog = new MonthlyClosureResponse(log.getId(), listEmpId,
						response.getClosureId(), response.getStartDT(), response.getEndDT(), response.getCurrentMonth(),
						response.getClosureDay(), response.getIsLastDayOfMonth(),
						response.getPeriodStart(), response.getPeriodEnd(), 2, log.getExecutionStatus().value, currentDT);
				return new Kmw006aResultDto(false, log.getClosureId().value, null, resultClosurtLog);
			} else {
				MonthlyClosureUpdatePersonLog resultLog = new MonthlyClosureUpdatePersonLog(employeeId, log.getId(),
						MonthlyClosureCompleteStatus.INCOMPLETE.value, MonthlyClosureExecutionStatus.RUNNING.value);
				
				List<String> listEmployeeId = closureUpdatePersonLogRepo
						.getAll(resultLog.getMonthlyClosureUpdateLogId()).stream().map(item -> item.getEmployeeId())
						.collect(Collectors.toList());
				
				MonthlyClosureResponse resultCloLog = new MonthlyClosureResponse(log.getId(), listEmployeeId,
						log.getClosureId().value, log.getExecutionDateTime(), response.getEndDT(), log.getTargetYearMonth().v(),
						log.getClosureDate().getClosureDay().v(), log.getClosureDate().getLastDayOfMonth(),
						log.getExecutionPeriod().start(), null, 2, log.getExecutionStatus().value, currentDT);
				return new Kmw006aResultDto(false, log.getClosureId().value, null, resultCloLog);
			}
		} else {
			boolean executable = true;
			if (outputParam.getStatus() == 0) { 
				// not executable
				executable = false;
			}
			return this.getInfor(companyId, executable);
		}
	}
	
	public Kmw006aResultDto getClosureInfor(){
		String companyId = AppContexts.user().companyId();
		boolean executable = true;
		// get data for screen A
		return this.getInfor(companyId, executable);
	}
	
	private Kmw006aResultDto getInfor(String companyId, boolean executable){
		List<ClosureInforDto> listInforDto = new ArrayList<>();
		List<ClosureInfor> listClosureInfor = closureService.getClosureInfo();
		ClosureInfor tmp = listClosureInfor.get(0);
		for (ClosureInfor infor : listClosureInfor) {
			List<MonthlyClosureUpdateLog> listMonthlyLog = monthlyClosureUpdateRepo
					.getAllByClosureId(companyId, infor.getClosureId().value).stream()
					.sorted((o1, o2) -> o2.getExecutionDateTime().compareTo(o1.getExecutionDateTime()))
					.collect(Collectors.toList());
			MonthlyClosureUpdateLog log = null;
			if (!listMonthlyLog.isEmpty())
				log = listMonthlyLog.get(0);
			ClosureInforDto i = new ClosureInforDto(infor.getClosureId().value, infor.getClosureName().v(),
					infor.getClosureMonth().getProcessingYm().v(), infor.getPeriod().start(),
					infor.getPeriod().end(), infor.getClosureDate().getClosureDay().v(),
					infor.getClosureDate().getLastDayOfMonth(), log == null ? null : log.getTargetYearMonth().v(),
					log == null ? null : log.getExecutionDateTime());
			listInforDto.add(i);
			if (infor.getPeriod().end().before(tmp.getPeriod().end())) {
				tmp = infor;
			}
		}
		final GeneralDate end = tmp.getPeriod().end();
		listClosureInfor = listClosureInfor.stream().filter(item -> item.getPeriod().end().beforeOrEquals(end))
				.sorted((o1, o2) -> o1.getClosureId().compareTo(o2.getClosureId())).collect(Collectors.toList());
		tmp = listClosureInfor.get(0);
		return new Kmw006aResultDto(executable, tmp.getClosureId().value, listInforDto, null);
	}

	// 実行状況を確認する
	private OutputParam checkExecutionStatus(String companyId) {
		List<MonthlyClosureUpdateLog> list = monthlyClosureUpdateRepo.getAll(companyId).stream()
				.filter(item -> item.getExecutionStatus() == MonthlyClosureExecutionStatus.RUNNING
						|| item.getExecutionStatus() == MonthlyClosureExecutionStatus.COMPLETED_NOT_CONFIRMED)
				.collect(Collectors.toList());
		if (list.isEmpty())
			// return executable
			return new OutputParam(1, Optional.empty());
		String empId = AppContexts.user().employeeId();
		List<MonthlyClosureUpdateLog> newList = new ArrayList<>();
		for (MonthlyClosureUpdateLog log : list) {
			if (log.getExecuteEmployeeId().equals(empId)) {
				newList.add(log);
			}
		}
		if(newList.size() > 0){
			MonthlyClosureUpdateLog logMaxStart = newList.stream().max(Comparator.comparing(MonthlyClosureUpdateLog::getExecutionDateTime)).orElseThrow(NoSuchElementException::new);
			// return running
			return new OutputParam(2, Optional.of(logMaxStart));
		}
		// return not executable
		return new OutputParam(0, Optional.empty());
	}

	public List<Kmw006cDto> getClosureLogInfor() {
		String companyId = AppContexts.user().companyId();
		List<Kmw006cDto> result = new ArrayList<>();
		List<MonthlyClosureUpdateLog> listClosureLog = monthlyClosureUpdateRepo.getAllSortedByExeDate(companyId);
		// アルゴリズム「締めの名称を取得する」を実行する
		for (MonthlyClosureUpdateLog log : listClosureLog) {
			String closureName = getClosureName(companyId, log.getClosureId().value, log.getTargetYearMonth().v());
			List<MonthlyClosureUpdatePersonLog> listPersonLog = monthlyClosurePersonLogRepo.getAll(log.getId());
			int alarmCount = listPersonLog.stream()
					.filter(l -> l.getExecutionResult() == MonthlyClosurePersonExecutionResult.UPDATED_WITH_ALARM)
					.collect(Collectors.toList()).size();
			int errorCount = listPersonLog.stream()
					.filter(l -> l.getExecutionResult() == MonthlyClosurePersonExecutionResult.NOT_UPDATED_WITH_ERROR)
					.collect(Collectors.toList()).size();
			Kmw006cDto r = new Kmw006cDto(log.getId(), log.getClosureId().value, closureName,
					log.getTargetYearMonth().v(), log.getExecutionDateTime(), listPersonLog.size(), alarmCount,
					errorCount);
			result.add(r);
		}
		return result;
	}

	private String getClosureName(String companyId, int closureId, int targetYm) {
		Optional<Closure> closure = closureRepo.findById(companyId, closureId);
		if (!closure.isPresent()) {
			return null;
		}
		Optional<ClosureHistory> closureHis = closure.get().getHistoryByYearMonth(YearMonth.of(targetYm));
		if (closureHis.isPresent()) {
			return closureHis.get().getClosureName().v();
		} else
			return null;
	}

	public List<String> getListExecutedEmployeeId(String monthlyClosureLogId, int atr) {
		switch (atr) {
		case 0:// all
			return monthlyClosurePersonLogRepo.getAll(monthlyClosureLogId).stream().map(item -> item.getEmployeeId())
					.collect(Collectors.toList());
		case 1:// alarm
			return monthlyClosurePersonLogRepo.getAll(monthlyClosureLogId).stream()
					.filter(item -> item.getExecutionResult() == MonthlyClosurePersonExecutionResult.UPDATED_WITH_ALARM)
					.map(item -> item.getEmployeeId()).collect(Collectors.toList());
		case 2:// error
			return monthlyClosurePersonLogRepo.getAll(monthlyClosureLogId).stream().filter(
					item -> item.getExecutionResult() == MonthlyClosurePersonExecutionResult.NOT_UPDATED_WITH_ERROR)
					.map(item -> item.getEmployeeId()).collect(Collectors.toList());
		default:
			return Collections.emptyList();
		}
	}

	public List<EmployeeRecordImport> getListEmployeeInfo(List<String> listEmpId) {
		List<EmployeeRecordImport> result = new ArrayList<>();
		for (String id : listEmpId) {
			EmployeeRecordImport empImport = empImportAdapter.getPersonInfor(id);
			result.add(empImport);
		}
		result.sort((e1, e2) -> e1.getEmployeeCode().compareToIgnoreCase(e2.getEmployeeCode()));
		return result;
	}

	public List<MonthlyClosureErrorInforDto> getListErrorInfor(String logId, List<String> listEmpId) {
		List<MonthlyClosureErrorInforDto> listResult = new ArrayList<>();
		for (String empId : listEmpId) {
			List<MonthlyClosureUpdateErrorInfor> errInfors = errorInforRepo.getByLogIdAndEmpId(logId, empId);
			EmployeeRecordImport empImport = empImportAdapter.getPersonInfor(empId);
			for (MonthlyClosureUpdateErrorInfor e : errInfors) {
				MonthlyClosureErrorInforDto result = new MonthlyClosureErrorInforDto(empImport.getEmployeeCode(),
						empImport.getPname(), e.getErrorMessage(), e.getAtr().value);
				listResult.add(result);
			}
		}
		listResult.sort((e1, e2) -> e1.getEmployeeCode().compareToIgnoreCase(e2.getEmployeeCode()));
		return listResult;
	}
	
	public List<MonthlyClosureErrorInforDto> getListErrorInforByLogId(String logId) {
		List<MonthlyClosureErrorInforDto> listResult = new ArrayList<>();
		List<MonthlyClosureUpdateErrorInfor> errInfors = errorInforRepo.getAll(logId);
		for (MonthlyClosureUpdateErrorInfor errInfor : errInfors) {
			EmployeeRecordImport empImport = empImportAdapter.getPersonInfor(errInfor.getEmployeeId());
			MonthlyClosureErrorInforDto result = new MonthlyClosureErrorInforDto(empImport.getEmployeeCode(),
					empImport.getPname(), errInfor.getErrorMessage(), errInfor.getAtr().value);
			listResult.add(result);
		}
		listResult.sort((e1, e2) -> e1.getEmployeeCode().compareToIgnoreCase(e2.getEmployeeCode()));
		return listResult;
	}
	
	public Integer getPersonCompleteNo(String id){
		List<MonthlyClosureUpdatePersonLog> listPersonLog = this.closureUpdatePersonLogRepo.getAll(id);
		int countComplete = (int)listPersonLog.stream().filter(x -> x.getExecutionStatus().value == MonthlyClosurePersonExecutionStatus.COMPLETE.value).count();
		return new Integer(countComplete);
	}
	
	public Integer getAllPersonNo(DataToGetPersonNo param){
		DatePeriod closureDP = this.closureService.getClosurePeriodNws(param.getClosureId(), new YearMonth(param.getYearMonth()));
		if(closureDP != null){
			List<String> listEmpId = employeeSearch.search(createQueryToFilterEmployees(closureDP, param.getClosureId())).stream()
					.map(item -> item.getEmployeeId()).collect(Collectors.toList());
			int allPerson = listEmpId.size();
			return new Integer(allPerson);
		}
		return new Integer(0);
	}
	
	public Integer getDurationTime(String id){
		Optional<MonthlyClosureUpdateLog> optMonthlyClosureUpdateLog = this.monthlyClosureUpdateLogRepository.getLogById(id);
		if(optMonthlyClosureUpdateLog.isPresent()){
			GeneralDateTime startTime = optMonthlyClosureUpdateLog.get().getExecutionDateTime();
			GeneralDateTime systemTime = GeneralDateTime.now();
			int diffTime = (int)ChronoUnit.SECONDS.between(startTime.localDateTime(), systemTime.localDateTime());
			
			return new Integer(diffTime);
		}
		return new Integer(0); 
	}
	
	private RegulationInfoEmployeeQuery createQueryToFilterEmployees(DatePeriod closurePeriod, int closureId) {
		RegulationInfoEmployeeQuery query = new RegulationInfoEmployeeQuery();
		query.setBaseDate(closurePeriod.end());
		query.setReferenceRange(EmployeeReferenceRange.ALL_EMPLOYEE.value);
		query.setFilterByEmployment(false);
		query.setFilterByDepartment(false);
		query.setFilterByWorkplace(false);
		query.setFilterByClassification(false);
		query.setFilterByJobTitle(false);
		query.setFilterByWorktype(false);
		query.setPeriodStart(closurePeriod.start());
		query.setPeriodEnd(closurePeriod.end());
		query.setIncludeIncumbents(true);
		query.setIncludeWorkersOnLeave(true);
		query.setIncludeOccupancy(true);
		query.setIncludeRetirees(false);
		query.setRetireStart(GeneralDate.today());
		query.setRetireEnd(GeneralDate.today());
		query.setSortOrderNo(1);
		// query.setNameType(nameType);
		query.setSystemType(2);
		query.setFilterByClosure(true);
		query.setClosureIds(Arrays.asList(closureId));
		return query;
	}
	
}

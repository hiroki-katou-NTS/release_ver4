package nts.uk.ctx.at.record.app.find.monthlyclosureupdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureExecutionStatus;
import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosurePersonExecutionResult;
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
	private ClosureService closureService;

	@Inject
	private MonthlyClosureUpdateErrorInforRepository errorInforRepo;

	@Inject
	private EmployeeRecordAdapter empImportAdapter;

	@Inject
	private ClosureRepository closureRepo;

	@Inject
	private MonthlyClosureUpdatePersonLogRepository monthlyClosurePersonLogRepo;

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

	public Kmw006aResultDto getClosureInfors() {
		String companyId = AppContexts.user().companyId();
		if (checkExecutionStatus() == 2) { // running => open dialog F
			return null;
		} else {
			boolean executable = true;
			if (checkExecutionStatus() == 0) { // not executable
				executable = false;
			}
			List<ClosureInforDto> listInforDto = new ArrayList<>();
			List<ClosureInfor> listClosureInfor = closureService.getClosureInfo();
			ClosureInfor tmp = listClosureInfor.get(0);
			for (ClosureInfor infor : listClosureInfor) {
				List<MonthlyClosureUpdateLog> listMonthlyLog = monthlyClosureUpdateRepo
						.getAllByClosureId(companyId, infor.getClosureId().value).stream()
						.sorted((o1, o2) -> o1.getExecutionDateTime().compareTo(o2.getExecutionDateTime()))
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
			return new Kmw006aResultDto(executable, tmp.getClosureId().value, listInforDto);
		}
	}

	// 実行状況を確認する
	private int checkExecutionStatus() {
		String companyId = AppContexts.user().companyId();
		List<MonthlyClosureUpdateLog> list = monthlyClosureUpdateRepo.getAll(companyId).stream()
				.filter(item -> item.getExecutionStatus() == MonthlyClosureExecutionStatus.RUNNING
						|| item.getExecutionStatus() == MonthlyClosureExecutionStatus.COMPLETED_NOT_CONFIRMED)
				.collect(Collectors.toList());
		if (list.isEmpty())
			// return executable
			return 1;
		String empId = AppContexts.user().employeeId();
		for (MonthlyClosureUpdateLog log : list) {
			if (log.getExecuteEmployeeId().equals(empId)) {
				// return running
				return 2;
			}
		}
		// return not executable
		return 0;
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
			MonthlyClosureUpdateErrorInfor errInfor = errorInforRepo.getById(logId, empId).get();
			EmployeeRecordImport empImport = empImportAdapter.getPersonInfor(empId);
			MonthlyClosureErrorInforDto result = new MonthlyClosureErrorInforDto(empImport.getEmployeeCode(),
					empImport.getPname(), errInfor.getErrorMessage(), errInfor.getAtr().value);
			listResult.add(result);
		}
		return listResult;
	}

}

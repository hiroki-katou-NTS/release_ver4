package nts.uk.ctx.at.record.app.service.workrecord.erroralarm.recordcheck;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.auth.dom.employmentrole.EmployeeReferenceRange;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordWorkFinder;
import nts.uk.ctx.at.record.dom.adapter.query.employee.RegulationInfoEmployeeQuery;
import nts.uk.ctx.at.record.dom.adapter.query.employee.RegulationInfoEmployeeQueryAdapter;
import nts.uk.ctx.at.record.dom.adapter.query.employee.RegulationInfoEmployeeQueryR;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.AlCheckTargetCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.otkcustomize.ContinuousHolCheckSet;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.otkcustomize.repo.ContinuousHolCheckSetRepo;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ErAlWorkRecordCheckService {

	@Inject
	private RegulationInfoEmployeeQueryAdapter employeeSearch;

	@Inject
	private DailyRecordWorkFinder fullFinder;

	@Inject
	private ErrorAlarmWorkRecordRepository errorRecordRepo;

	@Inject
	private ContinuousHolCheckSetRepo checkSetting;

	@Inject
	private WorkInformationRepository workInfo;

	public List<RegulationInfoEmployeeQueryR> filterEmployees(GeneralDate workingDate, Collection<String> employeeIds,
			ErrorAlarmCondition checkCondition) {
		if (checkCondition == null) {
			return new ArrayList<>();
		}
		return filterEmployees(workingDate, employeeIds, checkCondition.getCheckTargetCondtion());
	}

	public List<RegulationInfoEmployeeQueryR> filterEmployees(GeneralDate workingDate, Collection<String> employeeIds,
			String EACheckID) {
		ErrorAlarmCondition checkCondition = errorRecordRepo.findConditionByErrorAlamCheckId(EACheckID).orElse(null);
		return filterEmployees(workingDate, employeeIds, checkCondition);
	}
	
	public List<RegulationInfoEmployeeQueryR> filterEmployees(GeneralDate workingDate, Collection<String> employeeIds,
			AlCheckTargetCondition checkCondition) {
		if (checkCondition == null) {
			return new ArrayList<>();
		}
		return this.employeeSearch.search(createQueryToFilterEmployees(workingDate, checkCondition)).stream()
				.filter(e -> employeeIds.contains(e.getEmployeeId())).collect(Collectors.toList());
	}

	public Map<String, List<RegulationInfoEmployeeQueryR>> filterEmployees(GeneralDate workingDate,
			Collection<String> employeeIds, List<String> EACheckID) {
		List<ErrorAlarmCondition> checkConditions = errorRecordRepo.findConditionByListErrorAlamCheckId(EACheckID);

		if (checkConditions == null) {
			return new HashMap<>();
		}
		return checkConditions.stream().collect(Collectors.toMap(c -> c.getErrorAlarmCheckID(), 
				c -> filterEmployees(workingDate, employeeIds, c)));
	}

	public Map<String, Boolean> check(GeneralDate workingDate, Collection<String> employeeIds,
			ErrorAlarmCondition checkCondition) {
		List<String> filted = this.filterEmployees(workingDate, employeeIds, checkCondition).stream()
				.map(e -> e.getEmployeeId()).collect(Collectors.toList());

		if (filted.isEmpty()) {
			return toEmptyResultMap();
		}
		/** 社員に一致する日別実績を取得する */
		List<DailyRecordDto> record = fullFinder.find(filted, new DatePeriod(workingDate, workingDate));

		if (record.isEmpty()) {
			return toEmptyResultMap();
		}

		return record.stream().collect(Collectors.toMap(c -> c.employeeId(), c -> {
			return checkErrorAlarmCondition(c, checkCondition);
		}));
	}

	public Map<String, Boolean> check(GeneralDate workingDate, Collection<String> employeeIds, String EACheckID) {
		ErrorAlarmCondition checkCondition = errorRecordRepo.findConditionByErrorAlamCheckId(EACheckID).orElse(null);

		if (checkCondition != null) {
			return check(workingDate, employeeIds, checkCondition);
		}

		return toEmptyResultMap();
	}

	public Map<String, Map<String, Boolean>> check(GeneralDate workingDate, Collection<String> employeeIds,
			List<String> EACheckID) {
		List<ErrorAlarmCondition> checkConditions = errorRecordRepo.findConditionByListErrorAlamCheckId(EACheckID);

		if (checkConditions != null) {
			return checkConditions.stream()
					.collect(Collectors.toMap(c -> c.getErrorAlarmCheckID(), c -> check(workingDate, employeeIds, c)));
		}

		return toEmptyResultMap();
	}

	/** 大塚用連続休暇チェック */
	public Map<GeneralDate, Integer> checkContinuousHolidays(String employeeId, DatePeriod range) {
		Optional<ContinuousHolCheckSet> settingOp = checkSetting.find(AppContexts.user().companyId());
		Map<GeneralDate, Integer> result = new HashMap<>();
		settingOp.ifPresent(setting -> {
			if(setting.isUseAtr()){
				processCheckContinuous(range.start(), range, result, setting, employeeId, null, 0, true);
			}
		});

		return result;
	}

	private void processCheckContinuous(GeneralDate endMark, DatePeriod range, Map<GeneralDate, Integer> result,
			ContinuousHolCheckSet setting, String employeeId, GeneralDate markDate, int count,
			boolean markPreviousDate) {

		List<WorkInfoOfDailyPerformance> workInfos = workInfo.findByPeriodOrderByYmd(employeeId, range).stream()
				.sorted((w1, w2) -> w2.getYmd().compareTo(w1.getYmd())).collect(Collectors.toList());

		if (workInfos.isEmpty()) { return; }
		
		for (WorkInfoOfDailyPerformance info : workInfos) {
			WorkTypeCode currentWTC = info.getRecordWorkInformation().getWorkTypeCode();
			if (setting.getTargetWorkType().contains(currentWTC)) {
				if (markPreviousDate) {
					markDate = info.getYmd();
					markPreviousDate = false;
				}
				count++;
			} else if (!setting.getIgnoreWorkType().contains(currentWTC)) {
				if (count >= setting.getMaxContinuousDays()) {
					result.put(markDate, count);
				}
				if (endMark.afterOrEquals(info.getYmd())) {
					break;
				}
				markPreviousDate = true;
				count = 0;
			}
		}

		DatePeriod perviousRange = new DatePeriod(range.start().addDays(-16), range.start().addDays(-1));
		processCheckContinuous(endMark, perviousRange, result, setting, employeeId, markDate, count, markPreviousDate);
	}

	private boolean checkErrorAlarmCondition(DailyRecordDto record, ErrorAlarmCondition condition) {
		WorkInfoOfDailyPerformance workInfo = record.getWorkInfo().toDomain(record.employeeId(), record.getDate());
		return condition.checkWith(workInfo, item -> {
			if (item.isEmpty()) {
				return item;
			}
			return AttendanceItemUtil.toItemValues(record, item).stream().map(iv -> getValue(iv))
					.collect(Collectors.toList());
		});
	}

	private Integer getValue(ItemValue value) {
		if (value.value() == null) {
			return 0;
		}
		return value.getValueType() == ValueType.DECIMAL ? ((BigDecimal) value.value()).intValue()
				: (Integer) value.value();
	}

	private <T> Map<String, T> toEmptyResultMap() {
		return new HashMap<>();
	}

	private RegulationInfoEmployeeQuery createQueryToFilterEmployees(GeneralDate workingDate,
			AlCheckTargetCondition checkCondition) {
		RegulationInfoEmployeeQuery query = new RegulationInfoEmployeeQuery();
		boolean isFixed = checkCondition == null;
		query.setBaseDate(workingDate);
		query.setReferenceRange(EmployeeReferenceRange.ALL_EMPLOYEE.value);
		query.setFilterByEmployment(isFixed ? false : checkCondition.getFilterByEmployment());
		query.setEmploymentCodes(isFixed ? new ArrayList<>() : checkCondition.getLstEmploymentCode().stream().map(c -> c.v())
				.collect(Collectors.toList()));
		query.setFilterByDepartment(false);
		// query.setDepartmentCodes(departmentCodes);
		query.setFilterByWorkplace(false);
		// query.setWorkplaceCodes(workplaceCodes);
		query.setFilterByClassification(isFixed ? false : checkCondition.getFilterByClassification());
		query.setClassificationCodes(isFixed ? new ArrayList<>() : checkCondition.getLstClassificationCode().stream()
				.map(c -> c.v()).collect(Collectors.toList()));
		query.setFilterByJobTitle(isFixed ? false : checkCondition.getFilterByJobTitle());
		query.setJobTitleCodes(isFixed ? new ArrayList<>() : checkCondition.getLstJobTitleId());
		query.setFilterByWorktype(isFixed ? false : checkCondition.getFilterByBusinessType());
		query.setWorktypeCodes(isFixed ? new ArrayList<>() : checkCondition.getLstBusinessTypeCode().stream().map(c -> c.v())
				.collect(Collectors.toList()));
		query.setPeriodStart(workingDate.toString());
		query.setPeriodEnd(workingDate.toString());
		query.setIncludeIncumbents(true);
		query.setIncludeWorkersOnLeave(true);
		query.setIncludeOccupancy(true);
		query.setIncludeAreOnLoan(true);
		query.setIncludeGoingOnLoan(false);
		query.setIncludeRetirees(false);
		// query.setRetireStart(retireStart);
		// query.setRetireEnd(retireEnd);
		return query;
	}
}

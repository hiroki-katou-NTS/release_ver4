package nts.uk.ctx.at.function.app.find.alarm.alarmlist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.AlarmListExtractResult;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.AlarmListExtractResultRepo;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.ExtractEmployeeInfo;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.ExtractExecuteType;
import nts.uk.ctx.at.function.dom.alarm.sendemail.ValueExtractAlarmDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ErAlExtractResultFinder {

	@Inject
	private AlarmListExtractResultRepo resultRepo;
	
	public ErAlExtractViewResult getResult(String processId) {
		return resultRepo.findBy(AppContexts.user().companyId(), AppContexts.user().employeeId(), ExtractExecuteType.MANUAL, processId).map(r -> {
			return new ErAlExtractViewResult(r.getEmpInfos().stream().map(ei -> ei.toList()).collect(Collectors.toList()),
											r.getEmpEralData().stream().map(ei -> ei.toList()).collect(Collectors.toList()));
		}).orElseGet(() -> new ErAlExtractViewResult());
	}
	
	public ErAlExtractViewResult getResultLimitedBy(String processId, int limit) {
		return resultRepo.findByLimited(AppContexts.user().companyId(), AppContexts.user().employeeId(), 
				ExtractExecuteType.MANUAL, processId, limit).map(r -> {
					
			return new ErAlExtractViewResult(r.getEmpInfos().stream().map(ei -> ei.toList()).collect(Collectors.toList()),
											r.getEmpEralData().stream().map(ei -> ei.toList()).collect(Collectors.toList()));
		}).orElseGet(() -> new ErAlExtractViewResult());
	}
	
	public AlarmListExtractResult getResultForExport(String processId) {
		return resultRepo.findBy(AppContexts.user().companyId(), AppContexts.user().employeeId(), ExtractExecuteType.MANUAL, processId)
				.orElseGet(() -> new AlarmListExtractResult(AppContexts.user().companyId(), AppContexts.user().employeeId(), ExtractExecuteType.MANUAL, processId));
	}
	
	public AlarmListExtractResult getResultEmpInfo(String processId) {
		return resultRepo.findResultEmpInfo(AppContexts.user().companyId(), AppContexts.user().employeeId(), ExtractExecuteType.MANUAL, processId)
				.orElseGet(() -> new AlarmListExtractResult(AppContexts.user().companyId(), AppContexts.user().employeeId(), ExtractExecuteType.MANUAL, processId));
	}
	
	public List<ValueExtractAlarmDto> getResultDto(String processId) {
		AlarmListExtractResult result = this.getResultForExport(processId);
		Map<String, List<ExtractEmployeeInfo>> empMap = result.getEmpInfos().stream().collect(Collectors.groupingBy(c -> c.getEmployeeId(), Collectors.toList()));
		
		return result.getEmpEralData().stream().map(eral -> {
			DatePeriod period = getPeriod(eral.getAlarmTime());
			ExtractEmployeeInfo employee = empMap.get(eral.getEmployeeId()).stream().filter(emp -> {
				return (new DatePeriod(emp.getWpWorkStartDate(), emp.getWpWorkEndDate())).contains(period);
			}).sorted((e1, e2) -> e2.getWpWorkStartDate().compareTo(e1.getWpWorkStartDate())).findFirst().orElse(null);
			
			if(employee == null) return null;
			
			return new ValueExtractAlarmDto(eral.getRecordId(), employee.getWorkplaceId(), employee.getHierarchyCode(), employee.getWorkplaceName(),
					employee.getEmployeeId(), employee.getEmployeeCode(), employee.getEmployeeName(), eral.getAlarmTime(),
					eral.getCategoryCode(), eral.getCategoryName(), eral.getAlarmItem(), eral.getAlarmMes(), eral.getComment());
			
		}).sorted((v1, v2) -> {
			int compare1 = v1.getHierarchyCd().compareTo(v2.getHierarchyCd());
			if(compare1 != 0){
				return compare1;
			}
			int compare2 = v1.getEmployeeCode().compareTo(v2.getEmployeeCode());
			if(compare2 != 0){
				return compare2;
			}
			int compare3 = v1.getAlarmValueDate().compareTo(v2.getAlarmValueDate());
			if(compare3 != 0){
				return compare3;
			}
			int compare4 = v1.getCategory().compareTo(v2.getCategory());
			if(compare4 != 0){
				return compare4;
			}
			return v1.getAlarmItem().compareTo(v2.getAlarmItem());
		}).filter(c -> c != null).collect(Collectors.toList());
	}

	private DatePeriod getPeriod(String dateString) {
		DatePeriod period = null;
		if(dateString.indexOf(ErAlConstant.PERIOD_SEPERATOR) > 0){
			period = convertToPeriod(dateString);
		} else {
			GeneralDate date = convertToDate(dateString);
			period = new DatePeriod(date, date);
		}
		return period;
	}
	
	private DatePeriod convertToPeriod(String date) {
		if (date == null || date.isEmpty()) {
			return null;
		}
		String[] parts = date.split(ErAlConstant.PERIOD_SEPERATOR);
		if(parts.length == 2){
			return new DatePeriod(convertToDate(parts[0]), convertToDate(parts[1]));
		}
		
		return null;
	}

	private GeneralDate convertToDate(String date) {
		if (date == null || date.isEmpty()) {
			return null;
		}
		String[] parts = date.split("/");
		if (parts.length == 2) {
			return GeneralDate.localDate(LocalDate.parse(date.trim(), new DateTimeFormatterBuilder()
														                    .appendPattern(ErAlConstant.YM_FORMAT)
														                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
														                    .toFormatter()));
		} else if (parts.length == 3) {
			return GeneralDate.fromString(date.trim(), ErAlConstant.DATE_FORMAT);
		}
		return null;
	}
}

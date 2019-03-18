package nts.uk.ctx.at.function.app.command.alarm.alarmlist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSetting;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSettingRepository;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.AlarmExtraValueWkReDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.ExtractAlarmListService;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.ExtractedAlarmDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.AlarmListExtractResult;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.AlarmListExtractResultRepo;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.ExtractEmployeeErAlData;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.ExtractEmployeeInfo;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.ExtractExecuteType;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.ctx.at.function.dom.alarm.extraprocessstatus.AlarmListExtraProcessStatus;
import nts.uk.ctx.at.function.dom.alarm.extraprocessstatus.AlarmListExtraProcessStatusRepository;
import nts.uk.ctx.at.function.dom.alarm.extraprocessstatus.ExtractionState;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ErrorAlarmListExtractCommandHandler extends AsyncCommandHandler<ErrorAlarmListCommand> {

	// @Inject
	// private ExtractAlarmListFinder extractAlarmFinder;

	@Inject
	private AlarmListExtraProcessStatusRepository repo;

	@Inject
	private AlarmCheckConditionByCategoryRepository erAlByCateRepo;

	@Inject
	private AlarmPatternSettingRepository alPatternSettingRepo;

	@Inject
	private ExtractAlarmListService extractAlarmListService;
	
	@Inject 
	private AlarmListExtractResultRepo extractResultRepo;

	// private static final List<Integer> CHECK_CATEGORY =
	// Arrays.asList(AlarmCategory.DAILY.value,
	// AlarmCategory.SCHEDULE_4WEEK.value,
	// AlarmCategory.AGREEMENT.value, AlarmCategory.MONTHLY.value,
	// AlarmCategory.MULTIPLE_MONTH.value);

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	protected void handle(CommandHandlerContext<ErrorAlarmListCommand> context) {
		val asyncContext = context.asAsync();
		String comId = AppContexts.user().companyId();
		ErrorAlarmListCommand command = context.getCommand();

		// パラメータ．パターンコードをもとにドメインモデル「アラームリストパターン設定」を取得する
		// パラメータ．パターンコードから「アラームリストパターン設定」を取得する
		Optional<AlarmPatternSetting> alarmPatternSetting = this.alPatternSettingRepo.findByAlarmPatternCode(comId,
				command.getAlarmCode());
		if (!alarmPatternSetting.isPresent())
			throw new RuntimeException("「アラームリストパターン設定 」が見つかりません！");

		List<EmployeeSearchDto> listEmpId = command.getListEmployee();

		TaskDataSetter dataSetter = asyncContext.getDataSetter();
		AtomicInteger counter = new AtomicInteger(0);

//		ObjectMapper mapper = new ObjectMapper();
		dataSetter.setData("extracting", false);

		dataSetter.setData("empCount", counter.get());
		List<Integer> listCategory = command.getListPeriodByCategory().stream().map(x -> x.getCategory())
				.collect(Collectors.toList());
		List<CheckCondition> checkConList = alarmPatternSetting.get().getCheckConList().stream()
				.filter(e -> listCategory.contains(e.getAlarmCategory().value)).collect(Collectors.toList());

		List<AlarmCheckConditionByCategory> eralCate = erAlByCateRepo.findByCategoryAndCode(comId, listCategory,
				checkConList.stream().map(c -> c.getCheckConditionList()).flatMap(List::stream)
						.collect(Collectors.toList()));
		int max = listEmpId.size() * eralCate.size();

		ExtractedAlarmDto dto = this.extractAlarmListService.extractAlarmV2(listEmpId,
				command.getListPeriodByCategory(), eralCate, checkConList, finished -> {
					counter.set(counter.get() + finished);
					int completed = calcCompletedEmp(listEmpId, counter, max, finished).intValue();
					dataSetter.updateData("empCount", completed >= max ? max - 1 : completed);
				}, () -> {
					return shouldStop(context, asyncContext);
				});
		dataSetter.updateData("extracting", dto.isExtracting());
		dataSetter.setData("dataWriting", true);
//		try {
		if (CollectionUtil.isEmpty(dto.getExtractedAlarmData())) {
			dataSetter.setData("nullData", true);
			dataSetter.setData("eralRecord", 0);
		} else {
			List<ExtractEmployeeInfo> empData = getEmpData(command.getStatusProcessId(), dto).values()
					.stream().flatMap(List::stream).collect(Collectors.toList());
			List<ExtractEmployeeErAlData> empEralData = dto.getExtractedAlarmData().stream().map(c -> {
				return new ExtractEmployeeErAlData(command.getStatusProcessId(), c.getEmployeeID(), c.getGuid(), 
													c.getAlarmValueDate(), c.getCategory(), c.getCategoryName(), 
													c.getAlarmItem(), c.getAlarmValueMessage(), c.getComment());
			}).collect(Collectors.toList());
			
			extractResultRepo.insert(Arrays.asList(new AlarmListExtractResult(AppContexts.user().employeeId(), 
																		comId, ExtractExecuteType.MANUAL, command.getStatusProcessId(),
																		empData, empEralData)));
			dataSetter.setData("nullData", false);
			dataSetter.setData("eralRecord", empEralData.size());
			/*for (int i = 0; i < empData.size(); i++) {
				*//** Convert to json string *//*
				dataSetter.setData("empDataNo" + i, mapper.writeValueAsString(empData.get(i)));
			}
			for (int i = 0; i < dto.getExtractedAlarmData().size(); i++) {
				*//** Convert to json string *//*
				dataSetter.setData("erAlDataNo" + i, mapper.writeValueAsString(dto.getExtractedAlarmData().get(i).erAlData()));
			}*/
		}
//		} catch (JsonProcessingException e) {
//			throw new RuntimeException(e);
//		}

		dataSetter.updateData("empCount", listEmpId.size());
	}

	private Map<String, List<ExtractEmployeeInfo>> getEmpData(String executeId, ExtractedAlarmDto dto) {
		return dto.getExtractedAlarmData().stream().collect(Collectors.groupingBy(c -> c.getEmployeeID(),
				Collectors.collectingAndThen(Collectors.toList(), list -> {
					Map<String, List<AlarmExtraValueWkReDto>> mappedByWp = list.stream()
							.collect(Collectors.groupingBy(c -> c.getWorkplaceID(), Collectors.toList()));

					return mappedByWp.entrySet().stream().map(c -> {
						AlarmExtraValueWkReDto first = c.getValue().get(0);
						List<GeneralDate> ds = c.getValue().stream().map(ee -> {
							if(ee.getAlarmValueDate().indexOf(ErAlConstant.PERIOD_SEPERATOR) > 0){
								DatePeriod period = convertToPeriod(ee.getAlarmValueDate());
								return period == null ? new ArrayList<GeneralDate>() : period.datesBetween();
							}
							return Arrays.asList(convertToDate(ee.getAlarmValueDate()));
						}).flatMap(List::stream).filter(d -> d != null).distinct().collect(Collectors.toList());
						GeneralDate start = ds.stream().min((c1, c2) -> c1.compareTo(c2)).orElse(null);
						GeneralDate end = ds.stream().max((c1, c2) -> c1.compareTo(c2)).orElse(null);
						
						return new ExtractEmployeeInfo(executeId, first.getEmployeeID(), first.getWorkplaceID(), first.getEmployeeCode(),
								first.getEmployeeName(), first.getWorkplaceName(), first.getHierarchyCd(), start, end);
					}).collect(Collectors.toList());
				})));
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

	private Double calcCompletedEmp(List<EmployeeSearchDto> listEmpId, AtomicInteger counter, int max,
			Integer finished) {
		double completedPercent = Double.valueOf(counter.get()) / max;
		return Math.floor(completedPercent * listEmpId.size());
	}

	private Boolean shouldStop(CommandHandlerContext<ErrorAlarmListCommand> context,
			AsyncCommandHandlerContext<ErrorAlarmListCommand> asyncContext) {
		Optional<AlarmListExtraProcessStatus> alarmListExtraProcessStatus = this.repo
				.getAlListExtaProcessByID(context.getCommand().getStatusProcessId());
		if (alarmListExtraProcessStatus.isPresent()
				&& alarmListExtraProcessStatus.get().getStatus() == ExtractionState.INTERRUPTION) {
			asyncContext.finishedAsCancelled();
			return true;
		}
		return false;
	}
}
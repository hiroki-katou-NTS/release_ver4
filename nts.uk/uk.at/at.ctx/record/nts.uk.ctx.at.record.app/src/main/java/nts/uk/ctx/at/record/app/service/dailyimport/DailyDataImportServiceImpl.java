package nts.uk.ctx.at.record.app.service.dailyimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lombok.SneakyThrows;
import nts.arc.layer.ws.json.serializer.GeneralDateDeserializer;
import nts.arc.layer.ws.json.serializer.GeneralDateSerializer;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.gul.time.minutesbased.MinutesBasedTimeParser;
import nts.gul.time.minutesbased.MinutesBasedTimeParser.Result;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.dailyimport.DailyDataImportTemp;
import nts.uk.ctx.at.record.dom.dailyimport.DailyDataImportTempRepository;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
public class DailyDataImportServiceImpl implements DailyDataImportService {

	private static final String SPLIT_BY_1000_REG = "(?<=\\G.{1000})";

	private static final String ERROR_NAME_FORMAT = "Error_{0}_{1}";
	
	@Inject
	private DailyDataImportTempRepository tempRepo;

	@Inject
	private EmployeeRecordAdapter empAdapter;

	@Inject
	private WorkInformationRepository workInfoRepo;

	@Inject
	private EditStateOfDailyPerformanceRepository editStateRepo;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingRepo;

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakItemRepo;

	@Inject
	private ManagedParallelWithContext parallelManager;
	
	@Resource
	private SessionContext ss;

	private DailyDataImportService self;

	@PostConstruct
	public void getSelf() {
		this.self = ss.getBusinessObject(DailyDataImportService.class);
	}

	public List<RecordImportError> importAt(DatePeriod period, TaskDataSetter dataSetter, Supplier<Boolean> isCanceled) {
		dataSetter.setData("status", "準備中 (1/2)");
		
		List<RecordImportError> errors = Collections.synchronizedList(new ArrayList<>());
		ObjectMapper jsonMapper = initJsonMapper();

		
		List<DailyDataImportTemp> importData = tempRepo.getDataImport(period, AppContexts.user().companyCode());
		List<String> importedEmpCd = importData.stream().map(c -> c.getEmployeeCode()).distinct()
				.collect(Collectors.toList());
		
		dataSetter.setData("totalEmpCount", importedEmpCd.size());
		
		Map<String, String> mappedEmp = empAdapter.mapEmpCodeToId(importedEmpCd);
		List<String> empIds = new ArrayList<>(mappedEmp.values());
		dataSetter.setData("processEmpCount", empIds.size());
		
		List<WorkInfoOfDailyPerformance> workInfos = workInfoRepo.findByListEmployeeId(empIds, period);
		List<TimeLeavingOfDailyPerformance> timeLeaves = timeLeavingRepo.finds(empIds, period);
		List<BreakTimeOfDailyPerformance> breakItems = breakItemRepo.finds(empIds, period);
		
		dataSetter.updateData("status", "準備中 (2/2)");
		
		self.removePreData(period, empIds, timeLeaves);

		dataSetter.updateData("status", "受入データ処理中");
		dataSetter.setData("errorCount", 0);
		dataSetter.setData("processedEmpCount", 0);
		AtomicInteger count = new AtomicInteger(0);
		
		parallelManager.forEach(groupByEmpCode(importData), edi -> {
		//groupByEmpCode(importData).forEach(edi -> {
			if(isCanceled.get()){
				return;
			}
			
			List<RecordImportError> errorsPerEmp = (processOneEmp(mappedEmp, workInfos, timeLeaves, breakItems, edi));
			
			setErrorOnSetter(dataSetter, jsonMapper, edi.getKey(), errorsPerEmp);
			
			errors.addAll(errorsPerEmp);
			dataSetter.updateData("errorCount", errors.size());
			
			dataSetter.updateData("processedEmpCount", count.incrementAndGet());
		});
		
		dataSetter.updateData("status", "受入完了");
		
		return errors;
	}
	
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void removePreData(DatePeriod period, List<String> empIds) {
		List<GeneralDate> processingYmds = period.datesBetween();

		timeLeavingRepo.finds(empIds, period).stream().forEach(tl -> {
			tl.getAttendanceLeavingWork(1).ifPresent(alw -> {
				alw.getAttendanceStamp().ifPresent(as -> {
					as.removeStamp();
				});
				alw.getLeaveStamp().ifPresent(ls -> {
					ls.removeStamp();
				});
			});
			timeLeavingRepo.update(tl);
		});
		// timeLeavingRepo.deleteTimeNoBy(empIds, processingYmds, 1);
		breakItemRepo.deleteRecord1And2By(empIds, processingYmds);
		editStateRepo.deleteBy(empIds, processingYmds,
				Arrays.asList(28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 157, 163, 159, 165));
	}
	
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void removePreData(DatePeriod period, List<String> empIds, List<TimeLeavingOfDailyPerformance> timeLeaves) {
		List<GeneralDate> processingYmds = period.datesBetween();

		timeLeaves.forEach(tl -> {
			tl.getAttendanceLeavingWork(1).ifPresent(alw -> {
				alw.getAttendanceStamp().ifPresent(as -> {
					as.removeStamp();
				});
				alw.getLeaveStamp().ifPresent(ls -> {
					ls.removeStamp();
				});
			});
			timeLeavingRepo.update(tl);
		});
		// timeLeavingRepo.deleteTimeNoBy(empIds, processingYmds, 1);
		breakItemRepo.deleteRecord1And2By(empIds, processingYmds);
		editStateRepo.deleteBy(empIds, processingYmds,
				Arrays.asList(28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 157, 163, 159, 165));
	}

	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void finishUpdate(List<WorkInfoOfDailyPerformance> workInfos, List<TimeLeavingOfDailyPerformance> timeLeaves,
			List<BreakTimeOfDailyPerformance> cBreak) {
		timeLeavingRepo.update(timeLeaves);
		breakItemRepo.updateV2(cBreak);
		List<EditStateOfDailyPerformance> edits = workInfos.stream().map(cWorkInfo -> {
			return Arrays.asList(initEditState(cWorkInfo, 28), initEditState(cWorkInfo, 29),
									initEditState(cWorkInfo, 30), initEditState(cWorkInfo, 31), 
									initEditState(cWorkInfo, 32), initEditState(cWorkInfo, 33), 
									initEditState(cWorkInfo, 34), initEditState(cWorkInfo, 35),
									initEditState(cWorkInfo, 36), initEditState(cWorkInfo, 37), 
									initEditState(cWorkInfo, 38), initEditState(cWorkInfo, 39), 
									initEditState(cWorkInfo, 157), initEditState(cWorkInfo, 163),
									initEditState(cWorkInfo, 159), initEditState(cWorkInfo, 165));
		}).flatMap(List::stream).collect(Collectors.toList());
		workInfoRepo.updateV2(workInfos);
		editStateRepo.updateByKey(edits);
	}

	private ObjectMapper initJsonMapper() {
		ObjectMapper jsonMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(GeneralDate.class, new GeneralDateSerializer());
		module.addDeserializer(GeneralDate.class, new GeneralDateDeserializer());
		jsonMapper.registerModule(module);
		return jsonMapper;
	}

	@SneakyThrows
	private void setErrorOnSetter(TaskDataSetter dataSetter, ObjectMapper jsonMapper,
			String empCode, List<RecordImportError> errorsPerEmp) {
		String jsValue = jsonMapper.writeValueAsString(errorsPerEmp);
		if (jsValue.length() > 1000) {
			String[] splitted = jsValue.split(SPLIT_BY_1000_REG);
			IntStream.range(0, splitted.length).forEach(idx -> {
				dataSetter.setData(convertToTaskDataName(empCode, idx), splitted[idx]);
			});
		} else {
			dataSetter.setData(convertToTaskDataName(empCode, 1), jsValue);
		}
		
	}

	private String convertToTaskDataName(String empCode, int idx) {
		return ERROR_NAME_FORMAT.replace("{0}", empCode).replace("{1}", String.valueOf(idx));
	}

	private Set<Entry<String, List<DailyDataImportTemp>>> groupByEmpCode(List<DailyDataImportTemp> importData) {
		return importData.stream().collect(Collectors.groupingBy(edi -> edi.getEmployeeCode())).entrySet();
	}

	private List<RecordImportError> processOneEmp(Map<String, String> mappedEmp,
			List<WorkInfoOfDailyPerformance> workInfos, List<TimeLeavingOfDailyPerformance> timeLeaves,
			List<BreakTimeOfDailyPerformance> breakItems, Entry<String, List<DailyDataImportTemp>> edi) {
		List<RecordImportError> errors = new ArrayList<>();
		String empId = mappedEmp.get(edi.getKey());
		if (empId == null) {
			errors.add(new RecordImportError(edi.getKey()));
			return errors;
		}

		Map<GeneralDate, WorkInfoOfDailyPerformance> currentInfos = workInfos.stream()
				.filter(wi -> wi.getEmployeeId().equals(empId)).collect(Collectors.toMap(wi -> wi.getYmd(), wi -> wi));
		Map<GeneralDate, TimeLeavingOfDailyPerformance> currentTimeLeaves = timeLeaves.stream()
				.filter(wi -> wi.getEmployeeId().equals(empId)).collect(Collectors.toMap(wi -> wi.getYmd(), wi -> wi));
		Map<GeneralDate, BreakTimeOfDailyPerformance> currentBreaks = breakItems.stream()
				.filter(wi -> wi.getEmployeeId().equals(empId) && wi.getBreakType() == BreakType.REFER_WORK_TIME)
				.collect(Collectors.toMap(wi -> wi.getYmd(), wi -> wi));

		List<WorkInfoOfDailyPerformance> toUpdateWI = new ArrayList<>() ;
		List<TimeLeavingOfDailyPerformance> toUpdateTL = new ArrayList<>();
		List<BreakTimeOfDailyPerformance> toUpdateBK = new ArrayList<>();
		
		edi.getValue().stream().forEach(ddi -> {
			RecordImportError error = processOneDate(edi, currentInfos, currentTimeLeaves, currentBreaks, ddi, toUpdateWI, toUpdateTL, toUpdateBK);
			if(error != null) {
				errors.add(error);
			}
		});
		
		self.finishUpdate(toUpdateWI, toUpdateTL, toUpdateBK);
		
		return errors;
	}

	private RecordImportError processOneDate(Entry<String, List<DailyDataImportTemp>> edi,
			Map<GeneralDate, WorkInfoOfDailyPerformance> currentInfos,
			Map<GeneralDate, TimeLeavingOfDailyPerformance> currentTimeLeaves,
			Map<GeneralDate, BreakTimeOfDailyPerformance> currentBreaks, DailyDataImportTemp ddi,
			List<WorkInfoOfDailyPerformance> toUpdateWI, List<TimeLeavingOfDailyPerformance> toUpdateTL, 
			List<BreakTimeOfDailyPerformance> toUpdateBK) {
		GeneralDate ymd = ddi.getYmd();
		RecordImportError error = new RecordImportError(edi.getKey(), ymd);
		WorkInfoOfDailyPerformance cWorkInfo = currentInfos.get(ddi.getYmd());
		if (cWorkInfo == null) {
			return error;
		}
		TimeLeavingOfDailyPerformance cTimeLeave = getCurrentTimeLeave(edi, currentTimeLeaves, ymd);

		BreakTimeOfDailyPerformance cBreak = getCurrentBreakTime(edi, currentBreaks, ymd);

		processWorkInfo(ddi, error, cWorkInfo);

		processTimeLeaving(ddi, error, cTimeLeave);

		processBreakItem(cBreak, 1, ddi.getBreakStart1(), ddi.getBreakEnd1(), error);
		processBreakItem(cBreak, 2, ddi.getBreakStart2(), ddi.getBreakEnd2(), error);

		toUpdateWI.add(cWorkInfo);
		toUpdateTL.add(cTimeLeave);
		toUpdateBK.add(cBreak);

		if (!error.getItems().isEmpty()) {
			return error;
		}
		return null;
	}

	private EditStateOfDailyPerformance initEditState(WorkInfoOfDailyPerformance cWorkInfo, int itemNo) {
		return new EditStateOfDailyPerformance(cWorkInfo.getEmployeeId(), itemNo, cWorkInfo.getYmd(), EditStateSetting.HAND_CORRECTION_MYSELF);
	}

	private TimeLeavingOfDailyPerformance getCurrentTimeLeave(Entry<String, List<DailyDataImportTemp>> edi,
			Map<GeneralDate, TimeLeavingOfDailyPerformance> currentTimeLeaves, GeneralDate ymd) {
		return currentTimeLeaves.get(ymd) != null ? currentTimeLeaves.get(ymd)
				: new TimeLeavingOfDailyPerformance(edi.getKey(), new WorkTimes(1), new ArrayList<>(), ymd);
	}

	private BreakTimeOfDailyPerformance getCurrentBreakTime(Entry<String, List<DailyDataImportTemp>> edi,
			Map<GeneralDate, BreakTimeOfDailyPerformance> currentBreaks, GeneralDate ymd) {
		return currentBreaks.get(ymd) != null ? currentBreaks.get(ymd)
				: new BreakTimeOfDailyPerformance(edi.getKey(), BreakType.REFER_WORK_TIME, new ArrayList<>(), ymd);
	}

	private void processWorkInfo(DailyDataImportTemp ddi, RecordImportError error,
			WorkInfoOfDailyPerformance cWorkInfo) {
		WorkTypeCode workTypeCode = cWorkInfo.getRecordInfo().getWorkTypeCode();
		WorkTimeCode workTimeCode = cWorkInfo.getRecordInfo().getSiftCode();

		if (ddi.getWorkType().trim().length() != 3) {
			error.getItems().add("勤務種類");
		} else {
			workTypeCode = new WorkTypeCode(ddi.getWorkType());
		}

		if (ddi.getWorkTime().isPresent()) {
			if (ddi.getWorkTime().get().trim().length() != 3) {
				error.getItems().add("就業時間帯");
			} else {
				workTypeCode = new WorkTypeCode(ddi.getWorkTime().get());
			}
		} else {
			workTimeCode = null;
		}

		cWorkInfo.setRecordInfo(new WorkInformation(workTimeCode, workTypeCode));
	}

	private void processTimeLeaving(DailyDataImportTemp ddi, RecordImportError error,
			TimeLeavingOfDailyPerformance cTimeLeave) {
		String start = ddi.getStartTime().orElse("");
		String end = ddi.getEndTime().orElse("");
		if (StringUtil.isNullOrEmpty(start, true) && StringUtil.isNullOrEmpty(end, true)) {
			return;
		}
		cTimeLeave.forceAccessAttendanceLeavingWork(1).ifPresent(tl -> {

			getWorkStamp(error, start, tl, "出勤時刻", nstamp -> {
				if (tl.getAttendanceStamp().isPresent()) {
					tl.getAttendanceStamp().get().updateStamp(nstamp);
				} else {
					tl.setAttendanceStamp(Optional.of(new TimeActualStamp(null, nstamp, 0)));
				}
			});

			getWorkStamp(error, end, tl, "退勤時刻", netamp -> {
				if (tl.getLeaveStamp().isPresent()) {
					tl.getLeaveStamp().get().updateStamp(netamp);
				} else {
					tl.setLeaveStamp(Optional.of(new TimeActualStamp(null, netamp, 0)));
				}
			});
		});
	}

	private void getWorkStamp(RecordImportError error, String time, TimeLeavingWork tl, String errorItem,
			Consumer<WorkStamp> processOnSuccess) {
		if (StringUtil.isNullOrEmpty(time, true)) {
			processOnSuccess.accept(null);
		}
		Result result = MinutesBasedTimeParser.parse(time);
		if (result.isSuccess()) {
			processOnSuccess.accept(new WorkStamp(new TimeWithDayAttr(result.asDuration()),
					new TimeWithDayAttr(result.asDuration()), null, StampSourceInfo.HAND_CORRECTION_BY_MYSELF));
		} else {
			error.getItems().add(errorItem);
		}
	}

	private void processBreakItem(BreakTimeOfDailyPerformance cBreak, int breakNo, Optional<String> breakStart,
			Optional<String> breakEnd, RecordImportError error) {
		if (breakEnd.isPresent() && breakStart.isPresent()) {
			Result bre = MinutesBasedTimeParser.parse(breakEnd.get());
			Result brs = MinutesBasedTimeParser.parse(breakStart.get());
			if (bre.isSuccess() && brs.isSuccess()) {
				int breakTime = bre.asDuration() - brs.asDuration();
				if (breakTime >= 0) {
					cBreak.getBreakTimeSheets()
							.add(new BreakTimeSheet(new BreakFrameNo(breakNo), new TimeWithDayAttr(brs.asDuration()),
									new TimeWithDayAttr(bre.asDuration()), new AttendanceTime(breakTime)));
				} else {
					error.getItems().add("休憩開始時刻" + breakNo);
					error.getItems().add("休憩終了時刻" + breakNo);
				}
			} else if (bre.isSuccess()) {
				error.getItems().add("休憩開始時刻" + breakNo);
			} else if (brs.isSuccess()) {
				error.getItems().add("休憩終了時刻" + breakNo);
			} else {
				error.getItems().add("休憩開始時刻" + breakNo);
				error.getItems().add("休憩終了時刻" + breakNo);
			}
		} else if (breakEnd.isPresent()) {
			error.getItems().add("休憩開始時刻" + breakNo);
		} else if (breakStart.isPresent()) {
			error.getItems().add("休憩終了時刻" + breakNo);
		}
	}

}

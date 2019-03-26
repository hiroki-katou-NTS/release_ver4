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

import org.apache.commons.lang3.StringUtils;

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
import nts.gul.util.value.MutableValue;
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

	private static final String EMPLOYEE_NOT_EXSIST_MES = "社員が存在しません。";

	private static final String PARAM2 = "{1}";

	private static final String PARAM1 = "{0}";

	private static final String WORK_TYPE_ITEM_NAME = "勤務種類";

	private static final String WORK_TIME_ITEM_NAME = "就業時間帯";

	private static final String LEAVING_TIME_ITEM_NAME = "退勤時刻";

	private static final String ATTENDANCE_TIME_ITEM_NAME = "出勤時刻";

	private static final String BREAKTIME_END_ITEM_NAME = "休憩終了時刻";

	private static final String BREAKTIME_START_ITEM_NAME = "休憩開始時刻";

	private static final String ITEM_ERROR_MES = "項目エラー。";

	private static final String DAILY_RECORD_NOT_EXSIST_MES = "日次データが存在しません。";

	private static final String PROCESS_STATUS = "status";

	private static final String ACTUAL_PROCESS_EMPLOYEE_NUMBER = "processEmpCount";

	private static final String TOTAL_PROCESS_RECORDS = "processRecords";

	private static final String TOTAL_EMPLOYEE_NUMBER = "totalEmpCount";

	private static final String SUCCESSED_EMP_COUNT = "processedEmpCount";

	private static final String ERROR_NUMBER = "errorCount";

	private static final String SUCCESSED_RECORDS_NUMBER = "successRecords";

	//private static final List<Integer> PROCESS_ITEMS = Arrays.asList(28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 157, 163, 159, 165);

	private static final String SPLIT_BY_1000_REG = "(?<=\\G.{1000})";

	private static final String ERROR_NAME_FORMAT = StringUtils.join(new String[]{"Error", PARAM1, PARAM2}, "_");
	
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
		dataSetter.setData(PROCESS_STATUS, ImportState.DATA_COLLECTING.description);
		
		List<RecordImportError> errors = Collections.synchronizedList(new ArrayList<>());
		ObjectMapper jsonMapper = initJsonMapper();

		
		List<DailyDataImportTemp> importData = tempRepo.getDataImport(period, AppContexts.user().companyCode());
		List<String> importedEmpCd = importData.stream().map(c -> c.getEmployeeCode()).distinct()
				.collect(Collectors.toList());
		
		dataSetter.setData(TOTAL_EMPLOYEE_NUMBER, importedEmpCd.size());
		dataSetter.setData(TOTAL_PROCESS_RECORDS, importData.size());
		dataSetter.setData(SUCCESSED_RECORDS_NUMBER, 0);
		dataSetter.setData(ERROR_NUMBER, 0);
		dataSetter.setData(SUCCESSED_EMP_COUNT, 0);
		
		if(!importData.isEmpty()){
			Map<String, String> mappedEmp = empAdapter.mapEmpCodeToId(importedEmpCd);
			List<String> empIds = new ArrayList<>(mappedEmp.values());
			dataSetter.setData(ACTUAL_PROCESS_EMPLOYEE_NUMBER, empIds.size());
			
			
			dataSetter.updateData(PROCESS_STATUS, ImportState.BEFORE_DATA_DELETING.description);
			
			self.removePreData(period, empIds);
				
			List<WorkInfoOfDailyPerformance> workInfos = workInfoRepo.findByPeriodOrderByYmdAndEmps(empIds, period);
			List<TimeLeavingOfDailyPerformance> timeLeaves = timeLeavingRepo.finds(empIds, period);
			List<BreakTimeOfDailyPerformance> breakItems = breakItemRepo.finds(empIds, period);

			dataSetter.updateData(PROCESS_STATUS, ImportState.DATA_PROCESSING.description);
			//successEmpCount.set(0);
			AtomicInteger successEmpCount = new AtomicInteger(0);
			AtomicInteger successRecordCount = new AtomicInteger(0);
			
			parallelManager.forEach(groupByEmpCode(importData), edi -> {
			//groupByEmpCode(importData).forEach(edi -> {
				if(isCanceled.get()){
					return;
				}
				List<RecordImportError> errorsPerEmp = new ArrayList<>();
				String empId = mappedEmp.get(edi.getKey());
				if (empId == null) {
					edi.getValue().stream().forEach(ddit -> {
						errorsPerEmp.add(new RecordImportError(edi.getKey(), ddit.getYmd(), EMPLOYEE_NOT_EXSIST_MES));
					});
					
				} else {
					errorsPerEmp.addAll(processOneEmp(empId, workInfos, timeLeaves, breakItems, edi));
				}
				
				
				setErrorOnSetter(dataSetter, jsonMapper, edi.getKey(), errorsPerEmp);
				errors.addAll(errorsPerEmp);
				dataSetter.updateData(SUCCESSED_EMP_COUNT, successEmpCount.incrementAndGet());
				dataSetter.updateData(SUCCESSED_RECORDS_NUMBER, successRecordCount.addAndGet(edi.getValue().size() - errorsPerEmp.size()));
				dataSetter.updateData(ERROR_NUMBER, errors.size());
				
			});
			
			dataSetter.updateData(SUCCESSED_RECORDS_NUMBER, successRecordCount.get());
			dataSetter.updateData(SUCCESSED_EMP_COUNT, successEmpCount.get());
			dataSetter.updateData(ERROR_NUMBER, errors.size());
		}

		dataSetter.updateData(PROCESS_STATUS, ImportState.PROCESS_COMPLETE.description);
		
		return errors;
	}
	
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void removePreData(DatePeriod period, List<String> empIds) {
		List<GeneralDate> processingYmds = period.datesBetween();
		
		timeLeavingRepo.deleteScheStamp(empIds, processingYmds);
		breakItemRepo.deleteRecord1And2By(empIds, processingYmds);
		editStateRepo.deleteBy(empIds, processingYmds);
	}

	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void finishUpdate(List<WorkInfoOfDailyPerformance> workInfos, List<TimeLeavingOfDailyPerformance> timeLeaves,
			List<BreakTimeOfDailyPerformance> breakTimes, List<EditStateOfDailyPerformance> editStates) {
		
		timeLeavingRepo.update(timeLeaves);
		breakItemRepo.updateV2(breakTimes);
		workInfoRepo.updateV2(workInfos);
		editStateRepo.updateByKey(editStates);
	}

	private List<RecordImportError> processOneEmp(String empId, List<WorkInfoOfDailyPerformance> workInfos, 
			List<TimeLeavingOfDailyPerformance> timeLeaves, List<BreakTimeOfDailyPerformance> breakItems, 
			Entry<String, List<DailyDataImportTemp>> edi) {
		List<RecordImportError> errors = new ArrayList<>();

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
		List<EditStateOfDailyPerformance> toAddEditStates = new ArrayList<>();
		
		edi.getValue().stream().forEach(ddi -> {
			RecordImportError error = processOneDate(edi, currentInfos, toAddEditStates, 
					currentTimeLeaves, currentBreaks, ddi, toUpdateWI, toUpdateTL, toUpdateBK, empId);
			if(error != null) {
				errors.add(error);
			}
		});
		
		self.finishUpdate(toUpdateWI, toUpdateTL, toUpdateBK, toAddEditStates);
		
		return errors;
	}

	private RecordImportError processOneDate(Entry<String, List<DailyDataImportTemp>> edi,
			Map<GeneralDate, WorkInfoOfDailyPerformance> currentInfos, 
			List<EditStateOfDailyPerformance> toAddEditStates,
			Map<GeneralDate, TimeLeavingOfDailyPerformance> currentTimeLeaves,
			Map<GeneralDate, BreakTimeOfDailyPerformance> currentBreaks, DailyDataImportTemp importData,
			List<WorkInfoOfDailyPerformance> toUpdateWI, List<TimeLeavingOfDailyPerformance> toUpdateTL, 
			List<BreakTimeOfDailyPerformance> toUpdateBK, String empId) {
		GeneralDate currentDate = importData.getYmd();
		WorkInfoOfDailyPerformance currentDateWorkInfo = currentInfos.get(importData.getYmd());
		if (currentDateWorkInfo == null) {
			return new RecordImportError(edi.getKey(), currentDate, DAILY_RECORD_NOT_EXSIST_MES);
		}
		List<Integer> updatedItems = new ArrayList<>();
		RecordImportError error = new RecordImportError(edi.getKey(), currentDate, ITEM_ERROR_MES);
		
		TimeLeavingOfDailyPerformance currentDateTimeLeave = getCurrentTimeLeave(edi, currentTimeLeaves, currentDate, empId);

		BreakTimeOfDailyPerformance currentDateBreak = getCurrentBreakTime(edi, currentBreaks, currentDate, empId);

		if(processWorkInfo(importData, error, currentDateWorkInfo, updatedItems)){
			toUpdateWI.add(currentDateWorkInfo);
		}

		if(processTimeLeaving(importData, error, currentDateTimeLeave, updatedItems)){
			toUpdateTL.add(currentDateTimeLeave);
		}

		boolean break1Error = processBreakItem(currentDateBreak, 1, importData.getBreakStart1(), importData.getBreakEnd1(), error, updatedItems),
				break2Error = processBreakItem(currentDateBreak, 2, importData.getBreakStart2(), importData.getBreakEnd2(), error, updatedItems);
		if(break1Error || break2Error){
			toUpdateBK.add(currentDateBreak);
		}

		if(!updatedItems.isEmpty()){
			toAddEditStates.addAll(updatedItems.stream().map(ui -> initEditState(currentDateWorkInfo, ui)).collect(Collectors.toList()));
		}
		
		if (!error.getItems().isEmpty()) {
			return error;
		}
		return null;
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
		return ERROR_NAME_FORMAT.replace(PARAM1, empCode).replace(PARAM2, String.valueOf(idx));
	}

	private Set<Entry<String, List<DailyDataImportTemp>>> groupByEmpCode(List<DailyDataImportTemp> importData) {
		return importData.stream().collect(Collectors.groupingBy(edi -> edi.getEmployeeCode())).entrySet();
	}

	private EditStateOfDailyPerformance initEditState(WorkInfoOfDailyPerformance cWorkInfo, int itemNo) {
		return new EditStateOfDailyPerformance(cWorkInfo.getEmployeeId(), itemNo, cWorkInfo.getYmd(), EditStateSetting.HAND_CORRECTION_MYSELF);
	}

	private TimeLeavingOfDailyPerformance getCurrentTimeLeave(Entry<String, List<DailyDataImportTemp>> edi,
			Map<GeneralDate, TimeLeavingOfDailyPerformance> currentTimeLeaves, GeneralDate ymd, String empId) {
		return currentTimeLeaves.get(ymd) != null ? currentTimeLeaves.get(ymd)
				: new TimeLeavingOfDailyPerformance(empId, new WorkTimes(1), new ArrayList<>(), ymd);
	}

	private BreakTimeOfDailyPerformance getCurrentBreakTime(Entry<String, List<DailyDataImportTemp>> edi,
			Map<GeneralDate, BreakTimeOfDailyPerformance> currentBreaks, GeneralDate ymd, String empId) {
		return currentBreaks.get(ymd) != null ? currentBreaks.get(ymd)
				: new BreakTimeOfDailyPerformance(empId, BreakType.REFER_WORK_TIME, new ArrayList<>(), ymd);
	}

	private boolean processWorkInfo(DailyDataImportTemp ddi, RecordImportError error,
			WorkInfoOfDailyPerformance cWorkInfo, List<Integer> updatedItems) {
		boolean isProcessed = false;
		WorkTypeCode workTypeCode = cWorkInfo.getRecordInfo().getWorkTypeCode();
		WorkTimeCode workTimeCode = cWorkInfo.getRecordInfo().getSiftCode();

		if (ddi.getWorkType().trim().length() != 3) {
			error.getItems().add(WORK_TYPE_ITEM_NAME);
		} else {
			workTypeCode = new WorkTypeCode(ddi.getWorkType());
			updatedItems.add(28);
			isProcessed = true;
		}

		if (ddi.getWorkTime().isPresent()) {
			if (ddi.getWorkTime().get().trim().length() != 3) {
				error.getItems().add(WORK_TIME_ITEM_NAME);
			} else {
				workTimeCode = new WorkTimeCode(ddi.getWorkTime().get());
				updatedItems.add(29);
				isProcessed = true;
			}
		} else {
			workTimeCode = null;
		}

		cWorkInfo.setRecordInfo(new WorkInformation(workTimeCode, workTypeCode));
		return isProcessed;
	}

	private boolean processTimeLeaving(DailyDataImportTemp ddi, RecordImportError error,
			TimeLeavingOfDailyPerformance cTimeLeave, List<Integer> updatedItems) {
		String start = ddi.getStartTime().orElse(null);
		String end = ddi.getEndTime().orElse(null);
		MutableValue<Boolean> isProcessed = new MutableValue<Boolean>(false);
		
		if (StringUtil.isNullOrEmpty(start, true) && StringUtil.isNullOrEmpty(end, true)) {
			return isProcessed.get();
		}
		
		
		cTimeLeave.forceAccessAttendanceLeavingWork(1).ifPresent(tl -> {
			
			if (!StringUtil.isNullOrEmpty(start, true) && !StringUtil.isNullOrEmpty(end, true)) {
				Result attendance = MinutesBasedTimeParser.parse(start.trim());
				Result leaving = MinutesBasedTimeParser.parse(end.trim());
				if (attendance.isSuccess() && leaving.isSuccess()) {
					int workingTime = leaving.asDuration() - attendance.asDuration();
					if (workingTime >= 0) {
						WorkStamp ats = new WorkStamp(new TimeWithDayAttr(attendance.asDuration()),
								new TimeWithDayAttr(attendance.asDuration()), null, StampSourceInfo.HAND_CORRECTION_BY_MYSELF);
						WorkStamp lvs = new WorkStamp(new TimeWithDayAttr(leaving.asDuration()),
								new TimeWithDayAttr(leaving.asDuration()), null, StampSourceInfo.HAND_CORRECTION_BY_MYSELF);
						if (tl.getAttendanceStamp().isPresent()) {
							tl.getAttendanceStamp().get().updateStamp(ats);
						} else {
							tl.setAttendanceStamp(Optional.of(new TimeActualStamp(null, ats, 0)));
						}
						if (tl.getLeaveStamp().isPresent()) {
							tl.getLeaveStamp().get().updateStamp(lvs);
						} else {
							tl.setLeaveStamp(Optional.of(new TimeActualStamp(null, lvs, 0)));
						}
						updatedItems.addAll(Arrays.asList(30, 31, 32, 33, 34, 35, 36, 37, 38, 39));
						isProcessed.set(true);
					} else {
						error.getItems().add(ATTENDANCE_TIME_ITEM_NAME);
						error.getItems().add(LEAVING_TIME_ITEM_NAME);
					}
				} else if (attendance.isSuccess()) {
					error.getItems().add(LEAVING_TIME_ITEM_NAME);
				} else if (leaving.isSuccess()) {
					error.getItems().add(ATTENDANCE_TIME_ITEM_NAME);
				} else {
					error.getItems().add(ATTENDANCE_TIME_ITEM_NAME);
					error.getItems().add(LEAVING_TIME_ITEM_NAME);
				}
			} else if (!StringUtil.isNullOrEmpty(start, true)) {
				error.getItems().add(LEAVING_TIME_ITEM_NAME);
			} else if (!StringUtil.isNullOrEmpty(end, true)) {
				error.getItems().add(ATTENDANCE_TIME_ITEM_NAME);
			}
		});
		
		return isProcessed.get();
	}

	private boolean processBreakItem(BreakTimeOfDailyPerformance cBreak, int breakNo, Optional<String> breakStart,
			Optional<String> breakEnd, RecordImportError error, List<Integer> updatedItems) {
		if (breakEnd.isPresent() && breakStart.isPresent()) {
			Result bre = MinutesBasedTimeParser.parse(breakEnd.get().trim());
			Result brs = MinutesBasedTimeParser.parse(breakStart.get().trim());
			if (bre.isSuccess() && brs.isSuccess()) {
				int breakTime = bre.asDuration() - brs.asDuration();
				if (breakTime >= 0) {
					cBreak.getBreakTimeSheets().add(new BreakTimeSheet(new BreakFrameNo(breakNo), new TimeWithDayAttr(brs.asDuration()),
																		new TimeWithDayAttr(bre.asDuration()), new AttendanceTime(breakTime)));
					updatedItems.addAll(breakNo == 1 ? Arrays.asList(157, 159) : Arrays.asList(163, 165));
					return true;
				} else {
					error.getItems().add(BREAKTIME_START_ITEM_NAME + breakNo);
					error.getItems().add(BREAKTIME_END_ITEM_NAME + breakNo);
				}
			} else if (bre.isSuccess()) {
				error.getItems().add(BREAKTIME_START_ITEM_NAME + breakNo);
			} else if (brs.isSuccess()) {
				error.getItems().add(BREAKTIME_END_ITEM_NAME + breakNo);
			} else {
				error.getItems().add(BREAKTIME_START_ITEM_NAME + breakNo);
				error.getItems().add(BREAKTIME_END_ITEM_NAME + breakNo);
			}
		} else if (breakEnd.isPresent()) {
			error.getItems().add(BREAKTIME_START_ITEM_NAME + breakNo);
		} else if (breakStart.isPresent()) {
			error.getItems().add(BREAKTIME_END_ITEM_NAME + breakNo);
		}
		
		return false;
	}

}

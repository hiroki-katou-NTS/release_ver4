package nts.uk.ctx.at.function.dom.alarm.w4d4alarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
//import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.task.AsyncTask;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.workrecord.alarmlist.fourweekfourdayoff.W4D4CheckAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.ErAlWorkRecordCheckAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.RegulationInfoEmployeeResult;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckTargetCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.fourweekfourdayoff.AlarmCheckCondition4W4D;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class W4D4AlarmService {
	
	@Inject
	private AlarmCheckConditionByCategoryRepository alarmCheckConditionByCategoryRepository;	
	
	@Inject
	private W4D4CheckAdapter w4D4CheckAdapter;
	
	@Inject
	private ErAlWorkRecordCheckAdapter erAlWorkRecordCheckAdapter;
	
	@Inject
	private ManagedParallelWithContext parallelManager;
	
		
	public List<ValueExtractAlarm> calculateTotal4W4D(EmployeeSearchDto employee, DatePeriod period, String checkConditionCode) {
		String companyID = AppContexts.user().companyId();
		List<ValueExtractAlarm> result = new ArrayList<ValueExtractAlarm>();
		
		Optional<AlarmCheckConditionByCategory> optAlarmCheckConditionByCategory = alarmCheckConditionByCategoryRepository.find(companyID, AlarmCategory.SCHEDULE_4WEEK.value, checkConditionCode);
		if (!optAlarmCheckConditionByCategory.isPresent())
			throw new RuntimeException("Can't find AlarmCheckConditionByCategory with category: 4W4D and code: " + checkConditionCode);
		
		// TODO: Narrow down the target audience
		List<RegulationInfoEmployeeResult> listTarget = erAlWorkRecordCheckAdapter.filterEmployees(period.end(), Arrays.asList(employee.getId()), optAlarmCheckConditionByCategory.get().getExtractTargetCondition());
		if(!listTarget.isEmpty()) {
			List<String> listEmps = new ArrayList<>();
			listEmps.add(employee.getId());
			List<RecordWorkInfoFunAdapterDto> listWorkInfoOfDailyPerformance = recordWorkInfoFunAdapter.findByPeriodOrderByYmdAndEmps(listEmps, period);
			
			val listHolidayWorkType = workTypeRepository.findWorkOneDay(companyID, DeprecateClassification.NotDeprecated.value, WorkTypeUnit.OneDay.value, WorkTypeClassification.Holiday.value);
			List<String> listHolidayWorkTypeCode = listHolidayWorkType.stream().map(c -> c.getWorkTypeCode().v()).collect(Collectors.toList());
			
			for(RegulationInfoEmployeeResult target : listTarget) {
				if(target.getEmployeeId().equals(employee.getId())) {
					AlarmCheckConditionByCategory alarmCheckConditionByCategory = optAlarmCheckConditionByCategory.get();
					AlarmCheckCondition4W4D fourW4DCheckCond = (AlarmCheckCondition4W4D) alarmCheckConditionByCategory.getExtractionCondition();
					
					if (fourW4DCheckCond.isForActualResultsOnly()) {
						Optional<ValueExtractAlarm> optAlarm = this.checkWithActualResults(employee, period,listHolidayWorkTypeCode,listWorkInfoOfDailyPerformance);
						if (optAlarm.isPresent())
							result.add(optAlarm.get());
					}
					break;
				}
			}
		}
	
		return result;

	}
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private RecordWorkInfoFunAdapter recordWorkInfoFunAdapter;
	
	public List<ValueExtractAlarm> calculateTotal4W4D(List<EmployeeSearchDto> employees, DatePeriod period, List<String> checkConditionCodes) {
		
		List<String> empIds = employees.stream().map( e->e.getId()).collect(Collectors.toList());
		
		String companyID = AppContexts.user().companyId();
		List<ValueExtractAlarm> result = Collections.synchronizedList(new ArrayList<ValueExtractAlarm>());
		
		List<AlarmCheckConditionByCategory> optAlarmCheckConditionByCategory = alarmCheckConditionByCategoryRepository.findByCategoryAndCode(companyID, AlarmCategory.SCHEDULE_4WEEK.value, checkConditionCodes);
		if (optAlarmCheckConditionByCategory.isEmpty())
			throw new RuntimeException("Can't find AlarmCheckConditionByCategory with category: 4W4D and code");
		List<AlarmCheckTargetCondition> listExtractionCondition = optAlarmCheckConditionByCategory.stream().map(c->c.getExtractTargetCondition()).collect(Collectors.toList());
		
		
		//List<RegulationInfoEmployeeResult> listTarget = erAlWorkRecordCheckAdapter.filterEmployees(period.end(), empIds, optAlarmCheckConditionByCategory.get().getExtractTargetCondition());
		
		val listHolidayWorkType = workTypeRepository.findWorkOneDay(companyID, DeprecateClassification.NotDeprecated.value, WorkTypeUnit.OneDay.value, WorkTypeClassification.Holiday.value);
		List<String> listHolidayWorkTypeCode = listHolidayWorkType.stream().map(c -> c.getWorkTypeCode().v()).collect(Collectors.toList());
		
		List<RecordWorkInfoFunAdapterDto> listWorkInfoOfDailyPerformance = recordWorkInfoFunAdapter.findByPeriodOrderByYmdAndEmps(empIds, period);
		
		// TODO: Narrow down the target audience
		Map<String, List<RegulationInfoEmployeeResult>> listTargetMap = erAlWorkRecordCheckAdapter.filterEmployees(period, empIds, listExtractionCondition);
		optAlarmCheckConditionByCategory.forEach(c -> {
			List<RegulationInfoEmployeeResult> value = listTargetMap.get(c.getExtractTargetCondition().getId());
			AlarmCheckCondition4W4D fourW4DCheckCond = (AlarmCheckCondition4W4D) c.getExtractionCondition();
			if(!value.isEmpty()) {
				
				/** 並列処理、AsyncTask */
				// Create thread pool.
				Map<String, List<RegulationInfoEmployeeResult>> valueMap = value.stream().collect(Collectors.groupingBy(v -> v.getEmployeeId(), Collectors.toList()));
				ExecutorService executorService = Executors.newFixedThreadPool(5);
				CountDownLatch countDownLatch = new CountDownLatch(valueMap.keySet().size());
				valueMap.entrySet().forEach(v -> {
					AsyncTask task = AsyncTask.builder()
							.withContexts()
							.keepsTrack(false)
							.threadName(this.getClass().getName())
							.build(() -> {
								Optional<EmployeeSearchDto> emOp = employees.stream().filter(e -> e.getId().equals(v.getKey())).findFirst();
								if(emOp.isPresent()) {
									
									List<RecordWorkInfoFunAdapterDto> listWorkInfoOfDailyPerByID = new ArrayList<>();
									for(RecordWorkInfoFunAdapterDto recordWorkInfoFunAdapterDto :listWorkInfoOfDailyPerformance) {
										if(recordWorkInfoFunAdapterDto.getEmployeeId().equals(emOp.get().getId())) {
											listWorkInfoOfDailyPerByID.add(recordWorkInfoFunAdapterDto);
										}
									}
									
									if (fourW4DCheckCond.isForActualResultsOnly()) {
										Optional<ValueExtractAlarm> optAlarm = this.checkWithActualResults(emOp.get(), period, listHolidayWorkTypeCode,listWorkInfoOfDailyPerByID);
										if (optAlarm.isPresent()) // thắc mắc đoạn này, sao check null r mà vẫn add dc 1 gtri null nhỉ?
											result.add(optAlarm.get());
									}
									
								}
								// Count down latch.
								countDownLatch.countDown();
							});
					executorService.submit(task);
				});
				// Wait for latch until finish.
				try {
					countDownLatch.await();
				} catch (InterruptedException ie) {
					throw new RuntimeException(ie);
				} finally {
					// Force shut down executor services.
					executorService.shutdown();
				}
//				value.stream().collect(Collectors.groupingBy(v -> v.getEmployeeId(), Collectors.toList())).entrySet().stream()
//				.forEach(v -> {
//					Optional<EmployeeSearchDto> emOp = employees.stream().filter(e -> e.getId().equals(v.getKey())).findFirst();
//					if(emOp.isPresent()) {
//						
//						List<RecordWorkInfoFunAdapterDto> listWorkInfoOfDailyPerByID = new ArrayList<>();
//						for(RecordWorkInfoFunAdapterDto recordWorkInfoFunAdapterDto :listWorkInfoOfDailyPerformance) {
//							if(recordWorkInfoFunAdapterDto.getEmployeeId().equals(emOp.get().getId())) {
//								listWorkInfoOfDailyPerByID.add(recordWorkInfoFunAdapterDto);
//							}
//						}
//						
//						if (fourW4DCheckCond.isForActualResultsOnly()) {
//							Optional<ValueExtractAlarm> optAlarm = this.checkWithActualResults(emOp.get(), period, listHolidayWorkTypeCode,listWorkInfoOfDailyPerByID);
//							if (optAlarm.isPresent())
//								result.add(optAlarm.get());
//						}
//						
//					}
//				});
				
			}
		});
		
		
	
		return result;

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ValueExtractAlarm> calculateTotal4W4D(String companyID, List<EmployeeSearchDto> employees, DatePeriod period, List<AlarmCheckConditionByCategory> w4d4ErAl, 
			Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		List<ValueExtractAlarm> result = Collections.synchronizedList(new ArrayList<ValueExtractAlarm>());
		List<String> empIds = employees.stream().map(e-> e.getId()).collect(Collectors.toList());
		
		List<AlarmCheckTargetCondition> listExtractionCondition = w4d4ErAl.stream().map(c-> c.getExtractTargetCondition()).collect(Collectors.toList());
		
		List<String> workTypes = workTypeRepository.findWorkTypeCodeOneDay(companyID, DeprecateClassification.NotDeprecated.value, WorkTypeUnit.OneDay.value, WorkTypeClassification.Holiday.value);
		
		List<RecordWorkInfoFunAdapterDto> workInfos = recordWorkInfoFunAdapter.findByPeriodOrderByYmdAndEmps(empIds, period);

		Map<String, List<RegulationInfoEmployeeResult>> listTargetMap = erAlWorkRecordCheckAdapter.filterEmployees(period, empIds, listExtractionCondition);
		
		w4d4ErAl.forEach(c -> {
			AlarmCheckCondition4W4D fourW4DCheckCond = (AlarmCheckCondition4W4D) c.getExtractionCondition();
			if (fourW4DCheckCond.isForActualResultsOnly()) {
				List<RegulationInfoEmployeeResult> targetEmps = listTargetMap.get(c.getExtractTargetCondition().getId());
				if(!targetEmps.isEmpty()) {
					Map<String, List<RegulationInfoEmployeeResult>> valueMap = targetEmps.stream().collect(Collectors.groupingBy(v -> v.getEmployeeId(), Collectors.toList()));
					parallelManager.forEach(CollectionUtil.partitionBySize(employees, 100), emps -> {
						
						synchronized (this) {
							if(shouldStop.get()) {
								return;
							}
						}
						emps.stream().forEach(emp -> {
							if(valueMap.containsKey(emp.getId())){
								List<RecordWorkInfoFunAdapterDto> currentWorkInfos = workInfos.stream().filter(wi -> emp.getId().equals(wi.getEmployeeId())).collect(Collectors.toList());
								this.checkWithActualResults(emp, period, workTypes, currentWorkInfos).ifPresent(er -> {
									result.add(er);
								});
							}
						});
						
						synchronized (this) {
							counter.accept(emps.size());
						}
						
					});
				}
			}
		});
		
		return result;

	}
	
	public Optional<ValueExtractAlarm> checkWithActualResults(EmployeeSearchDto employee, DatePeriod period,List<String> listHolidayWorkTypeCode,List<RecordWorkInfoFunAdapterDto> listWorkInfoOfDailyPerByID) {
		return w4D4CheckAdapter.checkHoliday(employee.getWorkplaceId(), employee.getId(), period,listHolidayWorkTypeCode,listWorkInfoOfDailyPerByID);
	}
}
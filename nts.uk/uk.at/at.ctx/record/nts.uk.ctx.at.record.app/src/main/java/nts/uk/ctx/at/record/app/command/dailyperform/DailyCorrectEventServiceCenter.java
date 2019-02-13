package nts.uk.ctx.at.record.app.command.dailyperform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.app.command.dailyperform.breaktime.UpdateBreakTimeByTimeLeaveChangeCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.breaktime.UpdateBreakTimeByTimeLeaveChangeHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.checkdata.DailyModifyRCResult;
import nts.uk.ctx.at.record.app.command.dailyperform.correctevent.EventCorrectResult;
import nts.uk.ctx.at.record.app.command.dailyperform.workrecord.TimeLeaveUpdateByWorkInfoChangeCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.workrecord.TimeLeaveUpdateByWorkInfoChangeHandler;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.service.event.common.CorrectEventConts;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult.EventHandleAction;
import nts.uk.ctx.at.record.dom.service.event.overtime.OvertimeOfDailyService;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.enu.DailyDomainGroup;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DailyCorrectEventServiceCenter {

	@Inject
	private TimeLeaveUpdateByWorkInfoChangeHandler timeLeaveCorrectHandler;
	
	@Inject
	private UpdateBreakTimeByTimeLeaveChangeHandler breakTimeCorrectHandler;

	@Inject
	private WorkTypeRepository workTypeRepo;

	@Inject
	private WorkingConditionItemRepository workConditionRepo;

	@Inject
	private OptionalItemRepository optionalMasterRepo;
	
	/** TODO: create handler (?) (Tin!!!)*/
	@Inject
	private OvertimeOfDailyService overtimeCorrectService;

	
	public CorrectResult correctTimeLeaveAndBreakTime(List<DailyRecordWorkCommand> sources, String companyId){
		EventTrigger eventBus = EventTrigger.builder().isTriggerRelatedEvent(false).triggerBreakTime(true).triggerTimeLeave(true).build();
		return prepareSourceAndFireEvent(mapShouldTriggerSources(sources, eventBus), 
				companyId, eventBus);
	}
	
	public CorrectResult correctTimeLeave(List<DailyRecordWorkCommand> sources, String companyId){
		EventTrigger eventBus = EventTrigger.builder().isTriggerRelatedEvent(true).triggerBreakTime(false).triggerTimeLeave(true).build();
		return prepareSourceAndFireEvent(mapShouldTriggerSources(sources, eventBus), companyId, eventBus);
	}
	
	public CorrectResult correctBreakTime(List<DailyRecordWorkCommand> sources, String companyId){
		EventTrigger eventBus = EventTrigger.builder().isTriggerRelatedEvent(true).triggerBreakTime(true).triggerTimeLeave(false).build();
		return prepareSourceAndFireEvent(mapShouldTriggerSources(sources, eventBus), companyId, eventBus);
	}
	
	public EventCorrectResult correctRunTime(DailyRecordDto baseDto, DailyModifyRCResult updated, String companyId){
		EventTrigger eventBus = EventTrigger.builder().isTriggerRelatedEvent(false).triggerBreakTime(true).triggerTimeLeave(true).build();
		EventTriggerBus triggerBus = judgeEventTriggerType(updated.getItems());
		List<DailyDomainGroup> correctedType = new ArrayList<>();
		
		WorkType workType = getFirstOrDefault(workTypeRepo.getPossibleWorkType(companyId, 
				Arrays.asList(baseDto.getWorkInfo().getActualWorkInfo().getWorkTypeCode())), null);
		WorkingConditionItem workCondition = getWorkCondition(eventBus, updated.getEmployeeId(), updated.getDate());
		
		IntegrationOfDaily domain = baseDto.toDomain(updated.getEmployeeId(), updated.getDate());
		
		triggerTimeLeave(companyId, workType, workCondition, 
				eventBus, triggerBus, domain.getWorkInformation(), domain.getEditState(),
				domain.getAttendanceLeave(), e -> {
					if(e.getAction() == EventHandleAction.ABORT){
						return;
					}
					
					domain.setAttendanceLeave(Optional.ofNullable(e.getData()));
					
					correctedType.add(DailyDomainGroup.ATTENDACE_LEAVE);
				});
		
		triggerBreakTime(companyId, workType, eventBus, triggerBus, domain.getWorkInformation(),
				domain.getEditState(), domain.getBreakTime(), domain.getAttendanceLeave(), e -> {
					if(e.getAction() == EventHandleAction.ABORT){
						return;
					}
					if(e.getAction() == EventHandleAction.DELETE){
						
						domain.getBreakTime().removeIf(b -> b.getBreakType() == BreakType.REFER_WORK_TIME);
						
					} else if(e.getAction() == EventHandleAction.INSERT || e.getAction() == EventHandleAction.UPDATE) {
						
						domain.getBreakTime().removeIf(b -> b.getBreakType() == BreakType.REFER_WORK_TIME);
						domain.getBreakTime().add(e.getData());
						domain.getBreakTime().sort((c1, c2) -> c1.getBreakType().compareTo(c2.getBreakType()));
					}
					correctedType.add(DailyDomainGroup.BREAK_TIME);
				});
		//updated.getItems().addAll(Arrays.asList(a));
		DailyRecordDto correctted = AttendanceItemUtil.fromItemValues(
				DailyRecordDto.from(overtimeCorrectService.correct(domain, Optional.of(workType))), 
				updated.getItems());
		correctedType.add(DailyDomainGroup.ATTENDANCE_TIME);
		
		EventCorrectResult result = new EventCorrectResult(setOptionalItemAtr(baseDto), setOptionalItemAtr(correctted), updated, correctedType);
		
		result.removeEditStatesForCorrectedItem();
		
		return result;
	}
	
	
	private DailyRecordDto setOptionalItemAtr(DailyRecordDto dto){
		dto.getOptionalItem().ifPresent(optional -> {
			Map<Integer, OptionalItem> optionalMaster = optionalMasterRepo
					.findAll(AppContexts.user().companyId()).stream()
					.collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c));
			
			optional.correctItems(optionalMaster);
		});
		return dto;
	}
	
	private EventTriggerBus judgeEventTriggerType(List<ItemValue> s) {
		boolean workInfoChanged = s.stream().anyMatch(i -> CorrectEventConts.WORK_INFO_ITEMS.contains(i.itemId()));
		boolean timeLeaveChanged = s.stream().anyMatch(i -> CorrectEventConts.ATTENDANCE_LEAVE_ITEMS.contains(i.itemId()));
		return EventTriggerBus.builder().workInfoChanged(workInfoChanged).timeLeaveChanged(timeLeaveChanged).build();
	}
	
	private WorkingConditionItem getWorkCondition(EventTrigger eventBus,
			String empId, GeneralDate date) {
		if(!eventBus.triggerTimeLeave){
			return null;
		}
		
		Map<String, Set<GeneralDate>> employeeIds = new HashMap<>();
		employeeIds.put(empId,  new HashSet<>(Arrays.asList(date)));
		
		Map<String, Map<GeneralDate, WorkingConditionItem>> result = workConditionRepo.getBySidAndPeriod(employeeIds);
		
		return result.containsKey(empId) ? result.get(empId).get(date) : null;
	}
	
	private void triggerBreakTime(String companyId, WorkType workType, EventTrigger eventTriggerBus,
			EventTriggerBus eventBus, WorkInfoOfDailyPerformance wi, List<EditStateOfDailyPerformance> editStates,
			List<BreakTimeOfDailyPerformance> breakTimes, Optional<TimeLeavingOfDailyPerformance> timeLeave,
			Consumer<EventHandleResult<BreakTimeOfDailyPerformance>> actionAfterComplete) {
		if(eventBus.shouldCorreactBreakTime() && eventTriggerBus.triggerBreakTime){
			UpdateBreakTimeByTimeLeaveChangeCommand breakTimeEvent = (UpdateBreakTimeByTimeLeaveChangeCommand) UpdateBreakTimeByTimeLeaveChangeCommand
					.builder()
					.cachedBreackTime(breakTimes.stream().filter(b -> b.getBreakType() == BreakType.REFER_WORK_TIME).findFirst().orElse(null))
					.employeeId(wi.getEmployeeId())
					.targetDate(wi.getYmd())
					.companyId(companyId)
					.cachedEditState(editStates)
					.cachedWorkInfo(wi)
					.actionOnCache(true)
					.cachedTimeLeave(timeLeave.orElse(null))
					.cachedWorkType(workType)
					.isTriggerRelatedEvent(eventTriggerBus.isTriggerRelatedEvent)
					.build();
			
			EventHandleResult<BreakTimeOfDailyPerformance>  breakTimeCorrected = breakTimeCorrectHandler.handle(breakTimeEvent);
			
			if(breakTimeCorrected.getAction() == EventHandleAction.ABORT){
				actionAfterComplete.accept(EventHandleResult.onFail());
				return;
			}
			
			actionAfterComplete.accept(breakTimeCorrected);
		}

		actionAfterComplete.accept(EventHandleResult.onFail());
	}
	
	private void triggerTimeLeave(String companyId, WorkType workType,
			WorkingConditionItem workCondition, EventTrigger eventTriggerBus,
			EventTriggerBus eventBus,
			WorkInfoOfDailyPerformance wi, List<EditStateOfDailyPerformance> editStates,
			Optional<TimeLeavingOfDailyPerformance> timeLeave, 
			Consumer<EventHandleResult<TimeLeavingOfDailyPerformance>> actionAfterComplete) {
		if(eventBus.shouldCorrectTimeLeave() && eventTriggerBus.triggerTimeLeave){
			TimeLeaveUpdateByWorkInfoChangeCommand timeLeaveEvent = (TimeLeaveUpdateByWorkInfoChangeCommand) TimeLeaveUpdateByWorkInfoChangeCommand
					.builder()
					.cachedWorkCondition(workCondition)
					.employeeId(wi.getEmployeeId())
					.targetDate(wi.getYmd())
					.companyId(companyId)
					.cachedEditState(editStates)
					.cachedWorkInfo(wi)
					.cachedTimeLeave(timeLeave.orElse(null))
					.actionOnCache(true)
					.isTriggerRelatedEvent(eventTriggerBus.isTriggerRelatedEvent)
					.cachedWorkType(workType).build();
	
			EventHandleResult<TimeLeavingOfDailyPerformance> timeLeaveCorrected = timeLeaveCorrectHandler.handle(timeLeaveEvent);
	
			if(timeLeaveCorrected.getAction() == EventHandleAction.ABORT){
			actionAfterComplete.accept(EventHandleResult.onFail());
			return;
		}

		actionAfterComplete.accept(timeLeaveCorrected);
		}

		actionAfterComplete.accept(EventHandleResult.onFail());
	}
	
	private <T> T getFirstOrDefault(List<T> source, T defaultV){
		return CollectionUtil.isEmpty(source) ? defaultV : source.get(0);
	}

	private Map<DailyRecordWorkCommand, EventTriggerBus> mapShouldTriggerSources(List<DailyRecordWorkCommand> sources, EventTrigger eventBus) {
		List<Integer> timeLeaveItems = mergeItems(CorrectEventConts.ATTENDANCE_ITEMS, CorrectEventConts.LEAVE_ITEMS);
		return sources.stream().collect(Collectors.toMap(s -> s, s -> {
			boolean workInfoChanged = s.itemValues().stream().anyMatch(i -> CorrectEventConts.WORK_INFO_ITEMS.contains(i.itemId()));
			boolean timeLeaveChanged = s.itemValues().stream().anyMatch(i -> timeLeaveItems.contains(i.itemId()));
			return EventTriggerBus.builder().workInfoChanged(workInfoChanged).timeLeaveChanged(timeLeaveChanged).build();
		}));
	}
	
	private CorrectResult prepareSourceAndFireEvent(Map<DailyRecordWorkCommand, EventTriggerBus> sources, 
			String companyId, EventTrigger eventBus){
		if(sources.isEmpty()){
			return CorrectResult.builder().build();
		}
		if(!sources.values().stream().anyMatch(c -> c.shouldTriggerEvent())){
			return CorrectResult.builder().build(); 
		}
		Set<String> workTypeCode = new HashSet<>();
		sources.keySet().stream().forEach(s -> {
			workTypeCode.add(s.getWorkInfo().getData().getRecordInfo().getWorkTypeCode().v());
		});
		Map<String, Set<GeneralDate>> employeeIds = sources.keySet().stream().collect(Collectors.groupingBy(c -> c.getEmployeeId(), 
					Collectors.collectingAndThen(Collectors.toList(), 
						c -> c.stream().map(q -> q.getWorkDate()).collect(Collectors.toSet()))));
		
		Map<WorkTypeCode, WorkType> workTypes = workTypeRepo.getPossibleWorkTypeV2(companyId, new ArrayList<>(workTypeCode))
				.stream().collect(Collectors.toMap(wt -> wt.getWorkTypeCode(), wt -> wt));
		
		CorrectResult result = CorrectResult.builder().workType(workTypes).workCondition(getWorkCondition(eventBus, employeeIds)).build();

		result.setData(triggerEvent(sources, companyId, workTypes, result.getWorkCondition(), eventBus));
		
		return result;
	}

	private Map<String, Map<GeneralDate, WorkingConditionItem>> getWorkCondition(EventTrigger eventBus,
			Map<String, Set<GeneralDate>> employeeIds) {
		return !eventBus.triggerTimeLeave ? null : workConditionRepo.getBySidAndPeriod(employeeIds);
	}

	private List<DailyRecordWorkCommand> triggerEvent(Map<DailyRecordWorkCommand, EventTriggerBus> sources, String companyId,
			Map<WorkTypeCode, WorkType> workTypes, Map<String, Map<GeneralDate, WorkingConditionItem>> workCondition,
			EventTrigger eventTriggerBus) {
		return sources.entrySet().stream().map(c -> {
			DailyRecordWorkCommand dailyRecord = c.getKey();
			WorkInfoOfDailyPerformance wi = dailyRecord.getWorkInfo().getData();
			
			triggerTimeLeave(companyId, workTypes.get(wi.getRecordInfo().getWorkTypeCode()), getBySidAndDate(workCondition, wi), 
					eventTriggerBus, c.getValue(), wi, dailyRecord.getEditState().getData(),
					dailyRecord.getTimeLeaving().getData(), e -> {
						dailyRecord.getTimeLeaving().updateDataO(Optional.ofNullable(e.getData()));
						dailyRecord.getTimeLeaving().shouldDeleteIfNull();
					});
			
			triggerBreakTime(companyId, workTypes.get(wi.getRecordInfo().getWorkTypeCode()), eventTriggerBus, c.getValue(), wi,
					dailyRecord.getEditState().getData(), dailyRecord.getBreakTime().getData(), dailyRecord.getTimeLeaving().getData(), e -> {
						if(e.getAction() == EventHandleAction.DELETE){
							dailyRecord.getBreakTime().getData().removeIf(b -> b.getBreakType() == BreakType.REFER_WORK_TIME);
							dailyRecord.getBreakTime().shouldDeleteIfNull();
						} else if(e.getAction() == EventHandleAction.INSERT || e.getAction() == EventHandleAction.UPDATE) {
							dailyRecord.getBreakTime().updateData(e.getData());
						}
					});
			
			/** TODO: need test (Tin!!!) */
			IntegrationOfDaily domainForCorrect = dailyRecord.toDomain();
			domainForCorrect = overtimeCorrectService.correct(domainForCorrect, Optional.ofNullable(workTypes.get(wi.getRecordInfo().getWorkTypeCode())));
			dailyRecord.getAttendanceTime().updateData(domainForCorrect.getAttendanceTimeOfDailyPerformance());
			
			return dailyRecord;
		}).collect(Collectors.toList());
	}
	
	@SafeVarargs
	private final List<Integer> mergeItems(List<Integer>... sources) {
		return Stream.of(sources).flatMap(List::stream).collect(Collectors.toList());
	}

	private WorkingConditionItem getBySidAndDate(Map<String, Map<GeneralDate, WorkingConditionItem>> workCondition,
			WorkInfoOfDailyPerformance wi) {
		if(workCondition.get(wi.getEmployeeId()) == null){
			return null;
		}
		return workCondition.get(wi.getEmployeeId()).get(wi.getYmd());
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CorrectResult {
		
		private Map<WorkTypeCode, WorkType> workType;
		
		private List<DailyRecordWorkCommand> data;
		
		private Map<String, Map<GeneralDate, WorkingConditionItem>> workCondition;
	}
	
	@Builder
	public static class EventTrigger {
		
		boolean isTriggerRelatedEvent;
		
		boolean triggerTimeLeave;
		
		boolean triggerBreakTime;
	}
	
	@Builder
	private static class EventTriggerBus {
		
		boolean workInfoChanged;
		
		boolean timeLeaveChanged;
		
		boolean shouldCorreactBreakTime() { 
			return timeLeaveChanged || shouldCorrectTimeLeave();
		}
		
		boolean shouldCorrectTimeLeave() { 
			return workInfoChanged;
		}
		
		boolean shouldTriggerEvent() {
			return shouldCorreactBreakTime();
		}
	}
	
}

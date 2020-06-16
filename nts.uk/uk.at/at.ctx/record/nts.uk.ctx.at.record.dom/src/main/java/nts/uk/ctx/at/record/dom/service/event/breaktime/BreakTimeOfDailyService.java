package nts.uk.ctx.at.record.dom.service.event.breaktime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectBreakTimeOfDailyDomainService;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.service.event.common.CorrectEventConts;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult.EventHandleAction;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.dailyattdcal.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

@Stateless
public class BreakTimeOfDailyService {
	
	@Inject
	private AttendanceItemConvertFactory attendanceItemConvertFactory;

	@Inject
	private WorkTypeRepository workTypeRepo;

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeRepo;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeaveRepo;

	@Inject
	private ReflectBreakTimeOfDailyDomainService reflectBreakTimeService;

	@Inject
	private EditStateOfDailyPerformanceRepository editStateRepo;
	
	@Inject
	private AttendanceItemConvertFactory convertFactory;

	public EventHandleResult<IntegrationOfDaily> correct(String companyId, IntegrationOfDaily working,
			Optional<WorkType> cachedWorkType, boolean directToDB) {

		WorkInfoOfDailyPerformance wi = working.getWorkInformation();
		if (wi == null) {
			return EventHandleResult.withResult(EventHandleAction.ABORT, working);
		}

		WorkType wt = getWithDefaul(cachedWorkType,
				() -> getDefaultWorkType(wi.getRecordInfo().getWorkTypeCode().v(), companyId));
		if (wt == null) {
			return EventHandleResult.withResult(EventHandleAction.ABORT, working);
		}

		BreakTimeOfDailyPerformance breakTimeRecord = getWithDefaul(
							working.getBreakTime().stream().filter(b -> b.getBreakType() == BreakType.REFER_WORK_TIME).findFirst(),
							() -> getBreakTimeDefault(wi.getEmployeeId(), wi.getYmd()));

		DailyRecordToAttendanceItemConverter converter = convertFactory.createDailyConverter()
				.employeeId(wi.getEmployeeId())
				.workingDate(wi.getYmd())
				.withBreakTime(breakTimeRecord);

		List<ItemValue> beforeCorrectItemValues = converter.convert(CorrectEventConts.BREAK_TIME_ITEMS);
		
		if (!wt.isWokingDay()) {
			return deleteBreakTime(working, directToDB, CorrectEventConts.BREAK_TIME_ITEMS, breakTimeRecord, converter, beforeCorrectItemValues);
		}

		BreakTimeOfDailyPerformance breakTime = getUpdateBreakTime(working.getAttendanceLeave(), breakTimeRecord, wi, companyId, working.getEditState());
		if (breakTime != null) {
			return updateBreakTime(working, directToDB, converter, beforeCorrectItemValues, breakTime);
		}

		/** 「日別実績の休憩時間帯」を削除する */
		return deleteBreakTime(working, directToDB, CorrectEventConts.BREAK_TIME_ITEMS, breakTimeRecord, converter, beforeCorrectItemValues);
	}

	private EventHandleResult<IntegrationOfDaily> updateBreakTime(IntegrationOfDaily working, boolean directToDB,
			DailyRecordToAttendanceItemConverter converter, List<ItemValue> beforeCorrectItemValues,
			BreakTimeOfDailyPerformance breakTime) {
		/** 「日別実績の休憩時間帯」を更新する */
		working.getBreakTime().removeIf(b -> b.getBreakType() == BreakType.REFER_WORK_TIME);
		working.getBreakTime().add(breakTime);
		
		List<ItemValue> afterCorrectItemValues = converter.withBreakTime(breakTime).convert(CorrectEventConts.BREAK_TIME_ITEMS);
		
		afterCorrectItemValues.removeAll(beforeCorrectItemValues);
		List<Integer> correctedItemIds = afterCorrectItemValues.stream().map(iv -> iv.getItemId()).collect(Collectors.toList());
		working.getEditState().removeIf(es -> correctedItemIds.contains(es.getAttendanceItemId()));
		
		if (directToDB) {
			this.breakTimeRepo.update(breakTime);
			if(!correctedItemIds.isEmpty()){
				this.editStateRepo.deleteByListItemId(working.getWorkInformation().getEmployeeId(), working.getWorkInformation().getYmd(), correctedItemIds);
			}
		}
		return EventHandleResult.withResult(EventHandleAction.UPDATE, working);
	}

	private EventHandleResult<IntegrationOfDaily> deleteBreakTime(IntegrationOfDaily working, boolean directToDB, List<Integer> canBeUpdatedItemIds, 
			BreakTimeOfDailyPerformance breakTimeRecord, DailyRecordToAttendanceItemConverter converter, List<ItemValue> beforeCorrectItemValues) {
		String empId = working.getWorkInformation().getEmployeeId();
		GeneralDate workingDate = working.getWorkInformation().getYmd();
		BreakTimeOfDailyPerformance deleted = mergeWithEditStates(empId, workingDate, 
				working.getEditState(), new BreakTimeOfDailyPerformance(empId, BreakType.REFER_WORK_TIME, new ArrayList<>(), workingDate), breakTimeRecord);
		if(!deleted.getBreakTimeSheets().isEmpty()){
			return updateBreakTime(working, directToDB, converter, beforeCorrectItemValues, deleted);
		}
		
		working.getBreakTime().removeIf(c -> c.getBreakType() == BreakType.REFER_WORK_TIME);
		working.getEditState().removeIf(es -> canBeUpdatedItemIds.contains(es.getAttendanceItemId()));
		
		/** 「日別実績の休憩時間帯」を削除する */
		if (directToDB) {
			this.breakTimeRepo.delete(working.getWorkInformation().getEmployeeId(), working.getWorkInformation().getYmd());
			this.editStateRepo.deleteByListItemId(working.getWorkInformation().getEmployeeId(), working.getWorkInformation().getYmd(), canBeUpdatedItemIds);
		}
		return EventHandleResult.withResult(EventHandleAction.DELETE, working);
	}

	/** 「補正した休憩時間帯」を取得する */
	private BreakTimeOfDailyPerformance getUpdateBreakTime(Optional<TimeLeavingOfDailyPerformance> timeLeaveO,
			BreakTimeOfDailyPerformance breakTimeRecord, WorkInfoOfDailyPerformance wi, String companyId,
			List<EditStateOfDailyPerformance> editState) {
		TimeLeavingOfDailyPerformance timeLeave = getWithDefaul(timeLeaveO,
				() -> getTimeLeaveDefault(wi.getEmployeeId(), wi.getYmd()));

		BreakTimeOfDailyPerformance breakTime = reflectBreakTimeService.reflectBreakTime(companyId, wi.getEmployeeId(),
				wi.getYmd(), null, timeLeave, wi);

// 		if (breakTimeRecord != null) {
			return mergeWithEditStates(wi.getEmployeeId(), wi.getYmd(), editState, breakTime, breakTimeRecord);
// 		}
// 		return breakTime;
	}
	

	/** 取得したドメインモデル「編集状態」を見て、マージする */
	private BreakTimeOfDailyPerformance mergeWithEditStates(String empId, GeneralDate targetDate,
			List<EditStateOfDailyPerformance> cachedEditState, BreakTimeOfDailyPerformance breakTime,
			BreakTimeOfDailyPerformance breakTimeRecord) {
		List<EditStateOfDailyPerformance> editStates = getEditStateByItems(empId, targetDate,
				cachedEditState.isEmpty() ? Optional.empty() : Optional.of(cachedEditState));

		if (editStates.isEmpty() && (breakTime == null || breakTime.getBreakTimeSheets().isEmpty())) {
			return breakTime;
		}

		List<Integer> itemsToMerge = getItemsToMerge(editStates);

		if (!itemsToMerge.isEmpty()) {
			DailyRecordToAttendanceItemConverter converter = attendanceItemConvertFactory.createDailyConverter()
																	.employeeId(empId).workingDate(targetDate).withBreakTime(breakTimeRecord);
			
			List<ItemValue> ipByHandValues = converter.convert(itemsToMerge);
			
			converter.withBreakTime(new ArrayList<>(Arrays.asList(breakTime)));
			
//			List<ItemValue> recordVal = converter.convert(itemsToMerge);
			
//			ipByHandValues.removeAll(recordVal);
			
			converter.merge(ipByHandValues);

			return converter.breakTime().get(0);
		}

		return breakTime;
	}

	private List<Integer> getItemsToMerge(List<EditStateOfDailyPerformance> editStates) {
		List<Integer> handEditItems = editStates.stream().filter(es -> isInputByHands(es.getEditStateSetting()))
															.map(es -> es.getAttendanceItemId()).collect(Collectors.toList());

		List<Integer> result = new ArrayList<>();

		for (int i = 0; i < CorrectEventConts.START_BREAK_TIME_CLOCK_ITEMS.size(); i++) {
			int startItem = CorrectEventConts.START_BREAK_TIME_CLOCK_ITEMS.get(i);
			int endItem = CorrectEventConts.END_BREAK_TIME_CLOCK_ITEMS.get(i);
			if (handEditItems.contains(startItem) || handEditItems.contains(endItem)) {
				result.add(startItem);
				result.add(endItem);
			}
		}

		return result;
	}

	/** 手修正の勤怠項目を判断する */
	private boolean isInputByHands(EditStateSetting es) {

		//return es == EditStateSetting.HAND_CORRECTION_MYSELF || es == EditStateSetting.HAND_CORRECTION_OTHER;
		return true;
	}

	private List<EditStateOfDailyPerformance> getEditStateByItems(String empId, GeneralDate targetDate,
			Optional<List<EditStateOfDailyPerformance>> cachedEditState) {
		val needCheckItems = getBreakTimeClockItems();
		return getWithDefaul(cachedEditState, () -> getDefaultEditStates(empId, targetDate, needCheckItems)).stream()
				.filter(e -> needCheckItems.contains(e.getAttendanceItemId())).collect(Collectors.toList());
	}

	private List<Integer> getBreakTimeClockItems() {
		return Stream.concat(CorrectEventConts.START_BREAK_TIME_CLOCK_ITEMS.stream(),
				CorrectEventConts.END_BREAK_TIME_CLOCK_ITEMS.stream()).collect(Collectors.toList());
	}

	private BreakTimeOfDailyPerformance getBreakTimeDefault(String employeeId, GeneralDate workingDate) {
		return breakTimeRepo.find(employeeId, workingDate, BreakType.REFER_WORK_TIME.value).orElse(null);
	}

	private TimeLeavingOfDailyPerformance getTimeLeaveDefault(String empId, GeneralDate targetDate) {
		return timeLeaveRepo.findByKey(empId, targetDate).orElse(null);
	}

	private WorkType getDefaultWorkType(String workTypeCode, String companyId) {
		return workTypeRepo.findByPK(companyId, workTypeCode).orElse(null);
	}

	private List<EditStateOfDailyPerformance> getDefaultEditStates(String empId, GeneralDate targetDate,
			List<Integer> needCheckItems) {
		return this.editStateRepo.findByItems(empId, targetDate, needCheckItems);
	}

	private <T> T getWithDefaul(Optional<T> target, Supplier<T> defaultVal) {
		if (target.isPresent()) {
			return target.get();
		}
		return defaultVal.get();
	}
}

package nts.uk.ctx.at.record.dom.service.event.breaktime;

import java.util.ArrayList;
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
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
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

	public EventHandleResult<IntegrationOfDaily> correct(String companyId, IntegrationOfDaily working,
			Optional<WorkType> cachedWorkType) {

		WorkInfoOfDailyPerformance wi = working.getWorkInformation();
		if (wi == null) {
			return EventHandleResult.withResult(EventHandleAction.ABORT, null);
		}

		WorkType wt = getWithDefaul(cachedWorkType,
				() -> getDefaultWorkType(wi.getRecordInfo().getWorkTypeCode().v(), companyId));
		if (wt == null) {
			return EventHandleResult.withResult(EventHandleAction.ABORT, null);
		}

		if (!wt.isWokingDay()) {
			/** 「日別実績の休憩時間帯」を削除する */
			return EventHandleResult.withResult(EventHandleAction.DELETE, null);
		}

		BreakTimeOfDailyPerformance breakTime = getUpdateBreakTime(working.getAttendanceLeave(),
				working.getBreakTime().stream().filter(b -> b.getBreakType() == BreakType.REFER_WORK_TIME).findFirst(),
				wi, companyId, working.getEditState());
		if (breakTime != null) {
			/** 「日別実績の休憩時間帯」を更新する */
			working.getBreakTime().removeIf(b -> b.getBreakType() == BreakType.REFER_WORK_TIME);
			working.getBreakTime().add(breakTime);
			return EventHandleResult.withResult(EventHandleAction.UPDATE, working);
		}

		/** 「日別実績の休憩時間帯」を削除する */
		return EventHandleResult.withResult(EventHandleAction.DELETE, null);
	}

	/** 「補正した休憩時間帯」を取得する */
	private BreakTimeOfDailyPerformance getUpdateBreakTime(Optional<TimeLeavingOfDailyPerformance> timeLeaveO,
			Optional<BreakTimeOfDailyPerformance> cachedBreakTime, WorkInfoOfDailyPerformance wi, String companyId,
			List<EditStateOfDailyPerformance> editState) {
		TimeLeavingOfDailyPerformance timeLeave = getWithDefaul(timeLeaveO,
				() -> getTimeLeaveDefault(wi.getEmployeeId(), wi.getYmd()));

		BreakTimeOfDailyPerformance breakTime = reflectBreakTimeService.reflectBreakTime(companyId, wi.getEmployeeId(),
				wi.getYmd(), null, timeLeave, wi);

		BreakTimeOfDailyPerformance breakTimeRecord = getWithDefaul(cachedBreakTime,
				() -> getBreakTimeDefault(wi.getEmployeeId(), wi.getYmd()));

		if (breakTimeRecord != null) {
			return mergeWithEditStates(wi.getEmployeeId(), wi.getYmd(), editState, breakTime, breakTimeRecord);
		}
		return breakTime;
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
																	.employeeId(empId).workingDate(targetDate).withBreakTime(breakTime);
			
			List<ItemValue> ipByHandValues = converter.convert(itemsToMerge);
			
			converter.withBreakTime(breakTimeRecord);
			
			List<ItemValue> recordVal = converter.convert(itemsToMerge);
			
			ipByHandValues.removeAll(recordVal);
			
			converter.merge(ipByHandValues);

			return converter.breakTime().get(0);
		}

		return breakTimeRecord;
	}

	private List<Integer> getItemsToMerge(List<EditStateOfDailyPerformance> editStates) {
		List<Integer> endItemsToMerge = getItemBys(editStates, CorrectEventConts.END_BREAK_TIME_CLOCK_ITEMS).stream()
				.sorted().collect(Collectors.toList());
		List<Integer> startItemsToMerge = getItemBys(editStates, CorrectEventConts.START_BREAK_TIME_CLOCK_ITEMS)
				.stream().sorted().collect(Collectors.toList());

		List<Integer> result = new ArrayList<>();

		for (int i = 0; i < startItemsToMerge.size(); i++) {
			int itemNo = CorrectEventConts.START_BREAK_TIME_CLOCK_ITEMS.indexOf(startItemsToMerge.get(i));
			int itemEndNo = endItemsToMerge.indexOf(CorrectEventConts.END_BREAK_TIME_CLOCK_ITEMS.get(itemNo));
			if (itemEndNo >= 0) {
				result.add(startItemsToMerge.get(i));
				result.add(endItemsToMerge.get(itemEndNo));
			}
		}

		return result;
	}

	private List<Integer> getItemBys(List<EditStateOfDailyPerformance> editStates, List<Integer> toGet) {
		if (editStates.isEmpty()) {
			return toGet;
		}

		List<Integer> edited = editStates.stream()
				.filter(e -> toGet.contains(e.getAttendanceItemId()) && isInputByHands(e.getEditStateSetting()))
				.map(c -> c.getAttendanceItemId()).collect(Collectors.toList());

		return toGet.stream().filter(i -> !edited.contains(i)).collect(Collectors.toList());
	}

	/** 手修正の勤怠項目を判断する */
	private boolean isInputByHands(EditStateSetting es) {

		return es == EditStateSetting.HAND_CORRECTION_MYSELF || es == EditStateSetting.HAND_CORRECTION_OTHER;
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

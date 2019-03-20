package nts.uk.ctx.at.record.dom.service.event.timeleave;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectWorkInforDomainService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.service.event.common.CorrectEventConts;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult.EventHandleAction;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemIdContainer;
import nts.uk.ctx.at.shared.dom.attendance.util.enu.DailyDomainGroup;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workingcondition.NotUseAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSetCheck;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;

@Stateless
public class TimeLeavingOfDailyService {

	@Inject
	private WorkTypeRepository workTypeRepo;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeaveRepo;

	@Inject
	private WorkingConditionItemRepository workConditionRepo;

	@Inject
	private ReflectWorkInforDomainService reflectService;

	@Inject
	private EditStateOfDailyPerformanceRepository editStateRepo;
	
	@Inject
	private AttendanceItemConvertFactory convertFactory;

	public EventHandleResult<IntegrationOfDaily> correct(String companyId, IntegrationOfDaily working,
			Optional<WorkingConditionItem> cachedWorkCondition, Optional<WorkType> cachedWorkType, boolean directToDB) {

		WorkInfoOfDailyPerformance wi = working.getWorkInformation();
		if(wi == null) {
			return EventHandleResult.withResult(EventHandleAction.ABORT, working);
		}
		
		WorkType wt = getWithDefaul(cachedWorkType,
				() -> getDefaultWorkType(wi.getRecordInfo().getWorkTypeCode().v(), companyId));
		if (wt == null) {
			return EventHandleResult.withResult(EventHandleAction.ABORT, working);
		}
		TimeLeavingOfDailyPerformance tlo = getWithDefaul(working.getAttendanceLeave(),
				() -> getTimeLeaveDefault(wi.getEmployeeId(), wi.getYmd()));

		 DailyRecordToAttendanceItemConverter converter = convertFactory.createDailyConverter()
																		.employeeId(wi.getEmployeeId())
																		.workingDate(wi.getYmd())
																		.withTimeLeaving(tlo);
		
		List<Integer> canbeCorrectedItem = AttendanceItemIdContainer.getItemIdByDailyDomains(DailyDomainGroup.ATTENDACE_LEAVE);
		List<ItemValue> beforeCorrectItemValues = converter.convert(canbeCorrectedItem);
	
		List<EditStateOfDailyPerformance> editStates = getEditStateByItems(working.getEditState(), wi.getEmployeeId(), wi.getYmd());
		
		if(isPairUpdatedByHand(editStates, 1) && isPairUpdatedByHand(editStates, 2)){
			return EventHandleResult.withResult(EventHandleAction.ABORT, working);
		}

		if (wt.getDailyWork() == null) {
			return EventHandleResult.withResult(EventHandleAction.ABORT, working);
		}
		if(tlo != null){
			if(tlo.getTimeLeavingWorks().stream().anyMatch(tl -> isSpr(tl.getAttendanceStamp()) || isSpr(tl.getLeaveStamp()))){
				return EventHandleResult.withResult(EventHandleAction.ABORT, working);
			}
		}
		TimeLeavingOfDailyPerformance correctedTlo = null;
		
		/** 取得したドメインモデル「勤務種類．一日の勤務．勤務区分」をチェックする */
		WorkAtr dayAtr = isWokingDay(wt);
		if (dayAtr != null) {
			val wts = wt.getWorkTypeSetByAtr(dayAtr).get();
			Optional<WorkingConditionItem>  workCondition = getWorkConditionOrDefault(cachedWorkCondition, wi.getEmployeeId(), wi.getYmd());
			if ((workCondition.isPresent() && workCondition.get().getAutoStampSetAtr() == NotUseAtr.USE) 
					|| wts.getAttendanceTime() == WorkTypeSetCheck.CHECK 
					|| wts.getTimeLeaveWork() == WorkTypeSetCheck.CHECK) {
				TimeLeavingOfDailyPerformance tl = null;
				if (tlo != null) {
					tl = mergeWithEditStates(working.getEditState(), tlo, wts);
				}
				WorkInfoOfDailyPerformance clonedWI = new WorkInfoOfDailyPerformance(wi.getEmployeeId(), wi.getRecordInfo(), 
						wi.getScheduleInfo(), wi.getCalculationState(), 
						wts.getAttendanceTime() == WorkTypeSetCheck.CHECK ? NotUseAttribute.Use : NotUseAttribute.Not_use, 
						wts.getTimeLeaveWork() == WorkTypeSetCheck.CHECK ? NotUseAttribute.Use : NotUseAttribute.Not_use, 
						wi.getYmd(), wi.getDayOfWeek(), wi.getScheduleTimeSheets()) ;
				
				correctedTlo = updateTimeLeave(companyId, clonedWI, tl, workCondition, wi.getEmployeeId(), wi.getYmd());
			} else {
				return EventHandleResult.withResult(EventHandleAction.ABORT, working);
			}
		} else {
			/** どちらか一方が 年休 or 特別休暇 の場合 */
			if (wt.getDailyWork().isAnnualOrSpecialHoliday()) {
				//return EventHandleResult.withResult(EventHandleAction.ABORT, working);
				correctedTlo = deleteTimeLeave(true, tlo, editStates);
			} else {
				correctedTlo = deleteTimeLeave(false, tlo, editStates);
			}
		}
		
		return updated(working, correctedTlo, directToDB, converter, canbeCorrectedItem, beforeCorrectItemValues);
	}

	private boolean isSpr(Optional<TimeActualStamp> stamp) {
		AtomicBoolean flag = new AtomicBoolean(false);
		
		stamp.ifPresent(s -> s.getStamp().ifPresent(ss -> {
			if(ss.getStampSourceInfo() == StampSourceInfo.SPR){
				flag.set(true);
			}
		}));
		
		return flag.get();
	}

	private boolean isPairUpdatedByHand(List<EditStateOfDailyPerformance> editStates, int no) {
		int idx = no - 1;
		return isItemUpdatedByHand(editStates, CorrectEventConts.LEAVE_ITEMS.get(idx)) 
				|| isItemUpdatedByHand(editStates, CorrectEventConts.ATTENDANCE_ITEMS.get(idx));
	}

	private boolean isItemUpdatedByHand(List<EditStateOfDailyPerformance> editStates, int itemId) {
		Optional<EditStateOfDailyPerformance> itemState = editStates.stream().filter(es -> es.getAttendanceItemId() == itemId).findFirst();
		
		if(!itemState.isPresent()){
			return false;
		}
		
		return itemState.get().getEditStateSetting() != EditStateSetting.REFLECT_APPLICATION;
	}

	private EventHandleResult<IntegrationOfDaily> updated(IntegrationOfDaily working, 
			TimeLeavingOfDailyPerformance correctedTlo, boolean directToDB,
			DailyRecordToAttendanceItemConverter converter, List<Integer> canbeCorrectedItem, 
			List<ItemValue> beforeCorrectItemValues) {
		working.setAttendanceLeave(Optional.ofNullable(correctedTlo));

		List<ItemValue> afterCorrectItemValues = converter.withTimeLeaving(correctedTlo).convert(canbeCorrectedItem);
		
		afterCorrectItemValues.removeAll(beforeCorrectItemValues);
		List<Integer> correctedItemIds = afterCorrectItemValues.stream().map(iv -> iv.getItemId()).collect(Collectors.toList());
		working.getEditState().removeIf(es -> correctedItemIds.contains(es.getAttendanceItemId()));
		
		if(directToDB){
			this.timeLeaveRepo.update(working.getAttendanceLeave().orElse(null));
			this.editStateRepo.deleteByListItemId(working.getWorkInformation().getEmployeeId(), 
													working.getWorkInformation().getYmd(), correctedItemIds);
		}
		
		return EventHandleResult.withResult(EventHandleAction.UPDATE, working);
	}

	/** 取得したドメインモデル「勤務種類．一日の勤務．一日」をチェックする */
	private WorkAtr isWokingDay(WorkType wt) {
		if (wt.getDailyWork() == null) {
			return null;
		}
		if (wt.getDailyWork().getWorkTypeUnit() == WorkTypeUnit.OneDay) {
			if (isWorkingType(wt.getDailyWork().getOneDay())) {
				return WorkAtr.OneDay;
			} else {
				return null;
			}
		}

		if (isWorkingType(wt.getDailyWork().getMorning())) {
			return WorkAtr.Monring;
		}

		return isWorkingType(wt.getDailyWork().getAfternoon()) ? WorkAtr.Afternoon : null;
	}

	/** 出勤系かチェックする */
	private boolean isWorkingType(WorkTypeClassification wt) {
		return wt == WorkTypeClassification.Attendance || wt == WorkTypeClassification.Shooting
				|| wt == WorkTypeClassification.HolidayWork;
	}

	/** 取得したドメインモデル「編集状態」を見て、マージする */
	private TimeLeavingOfDailyPerformance mergeWithEditStates(List<EditStateOfDailyPerformance> editStates,
			TimeLeavingOfDailyPerformance timeLeave, WorkTypeSet wts) {
		List<Integer> inputByReflect = editStates.stream()
				.filter(es -> isInputByReflect(es.getEditStateSetting())).map(c -> c.getAttendanceItemId())
				.collect(Collectors.toList());
		if (wts.getAttendanceTime() == WorkTypeSetCheck.CHECK) {
			/** 「所定勤務の設定．打刻の扱い方．出勤時刻を直行とする」＝ TRUE */
			inputByReflect.removeIf(id -> CorrectEventConts.ATTENDANCE_ITEMS.contains(id));
		}
		if (wts.getTimeLeaveWork() == WorkTypeSetCheck.CHECK) {
			/** 「所定勤務の設定．打刻の扱い方．退勤時刻を直行とする」＝ TRUE */
			inputByReflect.removeIf(id -> CorrectEventConts.LEAVE_ITEMS.contains(id));
		}
		if (!inputByReflect.isEmpty()) {
			removeAttendanceLeave(timeLeave, inputByReflect, 0);
			removeAttendanceLeave(timeLeave, inputByReflect, 1);
		}
		return timeLeave;
	}

	private List<EditStateOfDailyPerformance> getEditStateByItems(List<EditStateOfDailyPerformance> cached, String empId, GeneralDate target) {
		List<Integer> needCheckItems = mergeItems();
		return getWithDefaul(cached.isEmpty() ? Optional.empty() : Optional.of(cached), 
							() -> getDefaultEditStates(empId, target, needCheckItems)).stream()
								.filter(e -> needCheckItems.contains(e.getAttendanceItemId())).collect(Collectors.toList());
	}

	/** 「日別実績の出退勤．出退勤の打刻」を削除する */
	private void removeAttendanceLeave(TimeLeavingOfDailyPerformance timeLeave, List<Integer> inputByReflect,
			int index) {
		if (inputByReflect.contains(CorrectEventConts.LEAVE_ITEMS.get(index))
				|| inputByReflect.contains(CorrectEventConts.ATTENDANCE_ITEMS.get(index))) {
			timeLeave.getAttendanceLeavingWork(index + 1).ifPresent(alw -> {
				alw.getAttendanceStamp().ifPresent(as -> {
					if (inputByReflect.contains(CorrectEventConts.ATTENDANCE_ITEMS.get(index))
							&& as.getStamp().isPresent() && !as.getStamp().get().isFromSPR()) {
						as.removeStamp();
					}
				});
				alw.getLeaveStamp().ifPresent(ls -> {
					if (inputByReflect.contains(CorrectEventConts.LEAVE_ITEMS.get(index)) && ls.getStamp().isPresent()
							&& !ls.getStamp().get().isFromSPR()) {
						ls.removeStamp();
					}
				});
			});
		}
	}

	private List<Integer> mergeItems() {
		return Stream.of(CorrectEventConts.LEAVE_ITEMS, CorrectEventConts.ATTENDANCE_ITEMS).flatMap(List::stream)
				.collect(Collectors.toList());
	}

	/** 申請反映の勤怠項目を判断する */
	private boolean isInputByReflect(EditStateSetting es) {
		return es == EditStateSetting.REFLECT_APPLICATION;
	}

	/** 日別実績の出退勤を更新する */
	private TimeLeavingOfDailyPerformance updateTimeLeave(String companyId, WorkInfoOfDailyPerformance workInfo,
			TimeLeavingOfDailyPerformance timeLeave, Optional<WorkingConditionItem> workConditionItem, String empId,
			GeneralDate target) {
		/** 自動打刻セットする */
		timeLeave = reflectService.createStamp(companyId, workInfo, workConditionItem, timeLeave, empId, target, null);
		if (timeLeave != null) {
			timeLeave.setWorkTimes(new WorkTimes(countTime(timeLeave)));
		}
		return timeLeave;
	}

	/** 出退勤回数の計算 */
	private int countTime(TimeLeavingOfDailyPerformance timeLeave) {
		return timeLeave.getTimeLeavingWorks().stream()
				.filter(tl -> tl.getAttendanceStamp().isPresent()
						&& tl.getAttendanceStamp().get().getStamp().isPresent() && tl.getLeaveStamp().isPresent()
						&& tl.getLeaveStamp().get().getStamp().isPresent())
				.mapToInt(tl -> 1).sum();
	}

	/** 日別実績の出退勤を削除する */
	private TimeLeavingOfDailyPerformance deleteTimeLeave(boolean isSPR, TimeLeavingOfDailyPerformance tl, List<EditStateOfDailyPerformance> editState) {
		if (tl != null) {
			if(isSPR) {
				if(shouldSaveStamp(tl)){
					return tl;
				}
			}
			
			tl.getTimeLeavingWorks().stream().forEach(tlw -> {
				boolean cantRemove = editState.stream().anyMatch(es -> isHandUpdatePair(tlw.getWorkNo().v(), es));
				if(!cantRemove){
					tlw.getAttendanceStamp().ifPresent(as -> {
						as.getStamp().ifPresent(ass -> {
							if (isRemoveStamp(ass)) {
								as.removeStamp();
							}
						});
					});

					tlw.getLeaveStamp().ifPresent(as -> {
						as.getStamp().ifPresent(ass -> {
							if (isRemoveStamp(ass)) {
								as.removeStamp();
							}
						});
					});
				}
			});
		}
		return tl;
	}

	private boolean isHandUpdatePair(int workNo, EditStateOfDailyPerformance es) {
		return es.getAttendanceItemId() == CorrectEventConts.ATTENDANCE_ITEMS.get(workNo - 1) 
				|| es.getAttendanceItemId() == CorrectEventConts.LEAVE_ITEMS.get(workNo - 1);
	}

	private boolean shouldSaveStamp(TimeLeavingOfDailyPerformance tl) {
		return tl.getTimeLeavingWorks().stream().anyMatch(tlx -> {
			AtomicBoolean flag = new AtomicBoolean(false);
			
			tlx.getAttendanceStamp().ifPresent(tlxa -> {
				tlxa.getStamp().ifPresent(tlxas -> {
					if(tlxas.getStampSourceInfo() == StampSourceInfo.SPR){
						flag.set(true);
					}
				});
			});
			
			tlx.getLeaveStamp().ifPresent(tlxl -> {
				tlxl.getStamp().ifPresent(tlxls -> {
					if(tlxls.getStampSourceInfo() == StampSourceInfo.SPR){
						flag.set(true);
					}
				});
			});
			
			return flag.get();
		});
	}

	private boolean isRemoveStamp(WorkStamp ass) {
		return ass.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT
				|| ass.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT_APPLICATION
				|| ass.getStampSourceInfo() == StampSourceInfo.GO_STRAIGHT_APPLICATION_BUTTON
				|| ass.getStampSourceInfo() == StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO
				|| ass.getStampSourceInfo() == StampSourceInfo.SPR;
	}

	private Optional<WorkingConditionItem> getWorkConditionOrDefault(Optional<WorkingConditionItem> cached,
			String empId, GeneralDate targetDate) {
		if (cached.isPresent()) {
			return cached;
		}
		return workConditionRepo.getBySidAndStandardDate(empId, targetDate);
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

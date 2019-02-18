package nts.uk.ctx.at.record.dom.attendanceitem;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.medical.MedicalCareTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workingtime.StayingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workschedule.WorkScheduleTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.daily.ExcessOverTimeWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculationMinusExist;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnNo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemNo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemTime;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemTimes;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValue;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyTimesMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkTimeNightShift;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class StoredProcdureProcessing implements StoredProcdureProcess {
	
	/**Iﾜｰｸ休*/
	private static final WorkTypeCode DEFAULT_WORK_TYPE = new WorkTypeCode("100");
	
	/**Iﾜｰｸ*/
	private static final List<WorkTimeCode> DEFAULT_WORK_TIME = Arrays.asList(new WorkTimeCode("100"), new WorkTimeCode("101"));
	
	private static final List<Integer> MONTHKY_ANYITEM_TO_PROCESS = Arrays.asList(20, 22, 24, 46, 1, 2);
	
	@Inject
	private WorkTypeRepository workTypeRepo;
	
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthly;
	
	@Inject
	private AnyItemValueOfDailyRepo dailyOptionalItem;
	
	@Inject
	private AttendanceTimeRepository dailyAttendanceTime;
	
	@Inject
	private AnyItemOfMonthlyRepository monthlyOptionalItem;
	
	private final BigDecimal COUNT_ON = BigDecimal.ONE;
	private final BigDecimal COUNT_OFF = BigDecimal.ZERO;
	private final BigDecimal V100 = BigDecimal.valueOf(100);
	
	@Override
	public List<IntegrationOfDaily> dailyProcessing(List<IntegrationOfDaily> dailies) {
		return dailyProcessing(dailies, null);
	}

	public List<IntegrationOfDaily> dailyProcessing(List<IntegrationOfDaily> dailies, Map<WorkTypeCode, WorkType> workTypeMap) {
		String companyId = AppContexts.user().companyId();
		//勤怠時間更新
		Set<String> workTypeCode = dailies.stream().map(s -> s.getWorkInformation().getRecordInfo().getWorkTypeCode().v())
												.collect(Collectors.toSet());
		
		Map<WorkTypeCode, WorkType> workTypes = getWorkType(companyId, workTypeCode, workTypeMap);
		
		dailies.stream().forEach(d -> {
			
			/** データ準備スタート　*/
			if(!d.getAnyItemValue().isPresent()){
				d.setAnyItemValue(Optional.of(new AnyItemValueOfDaily(d.getWorkInformation().getEmployeeId(), d.getWorkInformation().getYmd(), new ArrayList<>())));
			}
			
			AnyItemValueOfDaily optionalItem = d.getAnyItemValue().get();
			
			Integer logonTime, logoffTime,
					leaveGateStartTime = null, leaveGateEndTime = null, 
					startTime, endTime = null; 
			
			WorkType workType = workTypes.get(d.getWorkInformation().getRecordInfo().getWorkTypeCode());
			if(workType == null){
				return;
			}
			WorkTypeUnit atr = workType.getDailyWork().getWorkTypeUnit();
			DailyWork dailyWork = workType.getDailyWork();
			
			if(d.getAttendanceLeave().isPresent()){
				Optional<TimeLeavingWork> timeLeaveNo1 = d.getAttendanceLeave().get().getAttendanceLeavingWork(1);
				if(timeLeaveNo1.isPresent()){
					startTime = getTimeStamp(timeLeaveNo1.get().getAttendanceStamp());
					endTime =  getTimeStamp(timeLeaveNo1.get().getLeaveStamp());
				} else {
					startTime = null;
				}
			} else {
				startTime = null;
			}
			
			if(d.getPcLogOnInfo().isPresent()){
				LogOnInfo logonInfo = d.getPcLogOnInfo().get().getLogOnInfo(new PCLogOnNo(1)).orElse(null);
				logonTime = logonInfo != null && logonInfo.getLogOn().isPresent() ? logonInfo.getLogOn().get().v() : null;
				logoffTime = logonInfo != null &&  logonInfo.getLogOff().isPresent() ? logonInfo.getLogOff().get().v() : null;
			} else {
				logonTime = logoffTime = null;
			}
			
			if(d.getAttendanceLeavingGate().isPresent()){
				Optional<AttendanceLeavingGate> gateNo1 = d.getAttendanceLeavingGate().get().getAttendanceLeavingGate(new WorkNo(1));
				if(gateNo1.isPresent()){
					leaveGateStartTime = getWorkStamp(gateNo1.get().getAttendance());
					leaveGateEndTime = getWorkStamp(gateNo1.get().getLeaving());
				}
			}
			
			Integer timeOff = null, timeOn = leaveGateStartTime, divergenceTime = 0;
			List<Integer> overTime = new ArrayList<>(), preOver = new ArrayList<>();
			boolean isGotoWork = isGotoWork(atr, dailyWork.getOneDay(), dailyWork.getMorning(), dailyWork.getAfternoon());
			/** データ準備エンド　*/

			/** 任意項目3: 出勤時刻が入っており、PCログオンログオフがない事が条件 */
			processOptionalItem(() -> startTime != null && logonTime == null && logoffTime == null, 
					optionalItem, COUNT_ON, COUNT_OFF, 3);
			
			/** 任意項目4: その日にPCログオン = null and ログオフ <> null が条件 */
			processOptionalItem(() -> startTime != null && logonTime == null && logoffTime != null, 
					optionalItem, COUNT_ON, COUNT_OFF, 4);
			
			/** 任意項目5: その日にPCログオン = null and ログオフ <> null が条件 */
			processOptionalItem(() -> startTime != null && logonTime != null && logoffTime == null, 
					optionalItem, COUNT_ON, COUNT_OFF, 5);
			
			/** 任意項目14: その日にPCログオン = null が条件 */
			processOptionalItem(() -> startTime != null && logonTime == null, optionalItem, COUNT_ON, COUNT_OFF, 14);
			
			/** 任意項目15: その日にPCログオフ = null が条件 */
			processOptionalItem(() -> startTime != null && logoffTime == null, optionalItem, COUNT_ON, COUNT_OFF, 15);
			
			/** TODO: update*/
			/** 任意項目16: 出勤時刻が入っており、PCログオンログオフのどちらかが無い事が条件 */
			processOptionalItem(() -> startTime != null && (logonTime == null || logoffTime == null), 
					optionalItem, COUNT_ON, COUNT_OFF, 16);
			
			/** 任意項目7: 出勤の判断 */
			processOptionalItem(() -> isGotoWork, optionalItem, timeOn, timeOff, 7);
			
			timeOn = leaveGateEndTime;
			
			/** 任意項目9: 出勤の判断 */
			processOptionalItem(() -> isGotoWork, optionalItem, timeOn, timeOff, 9);
			
			timeOff = 0;
			/** 任意項目8: 両方時刻が入っている場合のみ乖離時間を計算する */
			if(startTime != null && leaveGateStartTime != null){
				divergenceTime = timeOn = startTime < leaveGateStartTime ? 0 : startTime - leaveGateStartTime;
				mergeOptionalItemWithNo(optionalItem, timeOn, 8);
			} else {
				updateOptionalItemWithNo(optionalItem, timeOff, 8);
			}
			
			/** 任意項目10 */
			if(endTime != null && leaveGateEndTime != null){
				timeOn = leaveGateEndTime - endTime; 
				mergeOptionalItemWithNo(optionalItem, timeOn < 0 ? 0 : timeOn, 10);
			} else {
				updateOptionalItemWithNo(optionalItem, timeOff, 10);
			}
			
			timeOn = 1; timeOff = 0;
			Optional<OverTimeOfDaily> overTimeD = Optional.empty();
			if(d.getAttendanceTimeOfDailyPerformance().isPresent()){
				
				/** 任意項目11: 実働就業時間 = 0 かつ 勤務種類が(年休、特休、代休) である事が条件 */
				processOptionalItem(() -> isNotActualWork(d, atr, dailyWork.getOneDay(), dailyWork.getMorning(), dailyWork.getAfternoon()), 
						optionalItem, COUNT_ON, COUNT_OFF, 11);
				
				overTimeD = getOverTime(d.getAttendanceTimeOfDailyPerformance().get());
				overTimeD.ifPresent(ot -> {
					ot.getOverTimeWorkFrameTime().stream().forEach(o -> {
						overTime.add(sumActualOvertime(o) - getAttendanceTime(o.getBeforeApplicationTime()));
						preOver.add(getAttendanceTime(o.getBeforeApplicationTime()));
					});
				});
				FlexTime flex = getFlexTime(overTimeD);
				
				int timePreFlex = getAttendanceTime(flex.getBeforeApplicationTime()),
						flexTime = flex.getFlexTime().getTime().valueAsMinutes(), 
						timeFlex = flexTime > 0 ? flexTime - timePreFlex : 0,
						timePre = preOver.stream().mapToInt(t -> t).sum() + timePreFlex,
						time = overTime.stream().mapToInt(t -> t).sum() + preOver.stream().mapToInt(t -> t).sum() + flexTime;
				
				/** 任意項目18, 任意項目28: 事前残業時間 > 0 である事が条件 */
				processOptionalItem(() -> timePre > 0, optionalItem, COUNT_ON, COUNT_OFF, 18, 28);
				
				/** 任意項目19: 事前残業1~10 > 0 かつ　乖離時間が発生していない事が条件 */
				processOptionalItem(() -> timePre > 0 && overTime.stream().allMatch(t -> t <= 0) && timeFlex <= 0, 
						optionalItem, COUNT_ON, COUNT_OFF, 19);
				
				/** 任意項目21: 事前残業1~10 > 0 かつ　乖離時間が発生している事が条件 */
				processOptionalItem(() -> timePre > 0 && (timeFlex > 0 || overTime.stream().anyMatch(t -> t > 0)),
						optionalItem, COUNT_ON, COUNT_OFF, 21);
				
				/** 任意項目23: 残業あり かつ 事前残業なし　が条件 */
				processOptionalItem(() -> (timePreFlex <= 0 && timeFlex > 0) || 
								checkOnPair(overTime, preOver, (ot, pot) -> ot > 0 && pot <= 0),
						optionalItem, COUNT_ON, COUNT_OFF, 23);
				
				/** 任意項目27: 残業あり かつ 事前残業なし　が条件 */
				processOptionalItem(() -> time > 0, optionalItem, COUNT_ON, COUNT_OFF, 27);
				
				/** 任意項目40: フレックス時間がマイナスである事が条件 */
				processOptionalItem(() -> flexTime < 0, optionalItem, flexTime * -1, timeOff, 40);
				
				/** 任意項目41: フレックス時間がプラスである事が条件 */
				processOptionalItem(() -> flexTime >= 0, optionalItem, flexTime, timeOff, 41);
			} else {
				/** 任意項目18, 19, 21, 23, 28 */
				updateOptionalItemWithNo(optionalItem, COUNT_OFF, 18, 19, 21, 23, 27, 28);

				/** 任意項目40, 41 */
				updateOptionalItemWithNo(optionalItem, timeOff, 40, 41);
			}
			
			if(d.getPcLogOnInfo().isPresent()){
				
				/** 任意項目1: ログオン時刻がnullではない事が条件 */
				processOptionalItem(() -> logonTime != null, optionalItem, COUNT_ON, COUNT_OFF, 1);

				/** 任意項目2: ログオフ時刻 > 0 である事が条件 */
				processOptionalItem(() -> logoffTime != null, optionalItem, COUNT_ON, COUNT_OFF, 2);
				
				/** 任意項目6: ログオン、ログオフが両方入っている事が条件 */
				processOptionalItem(() -> logonTime != null && logoffTime != null, optionalItem, COUNT_ON, COUNT_OFF, 6);
			} else {
				/** 任意項目1, 2, 6 */
				updateOptionalItemWithNo(optionalItem, COUNT_OFF, 1, 2, 6);
			}
			
			/** 平日か*/
			timeOn = divergenceTime;
			if(isWeekday(atr, dailyWork.getOneDay(), dailyWork.getMorning(), dailyWork.getAfternoon())){
				/** 任意項目12 */
				mergeOptionalItemWithNo(optionalItem, COUNT_ON, 12);
				
				/** 任意項目17 */
				mergeOptionalItemWithNo(optionalItem, timeOn, 17);
			} else {
				/** 任意項目12 */
				updateOptionalItemWithNo(optionalItem, COUNT_OFF, 12);
				
				/** 任意項目17 */
				updateOptionalItemWithNo(optionalItem, timeOff, 17);
			}
			
			if(startTime != null){
				/** 任意項目89 */
				mergeOptionalItemWithNo(optionalItem, startTime, 89);
				
				/** 任意項目90 */
				mergeOptionalItemWithNo(optionalItem, COUNT_ON, 90);
			} else {
				/** 任意項目89 */
				updateOptionalItemWithNo(optionalItem, timeOff, 89);

				/** 任意項目90 */
				updateOptionalItemWithNo(optionalItem, COUNT_OFF, 90);
			}
			
			if(d.getAnyItemValue().get().getItems().isEmpty()){
				d.setAnyItemValue(Optional.empty());
			}
			
//			if(atdStampTime != null){
				if(DEFAULT_WORK_TYPE.equals(d.getWorkInformation().getRecordInfo().getWorkTypeCode())  
						|| DEFAULT_WORK_TIME.contains(d.getWorkInformation().getRecordInfo().getWorkTimeCode())){
					if(!d.getAttendanceTimeOfDailyPerformance().isPresent()){
						d.setAttendanceTimeOfDailyPerformance(Optional.of(createDefaultAttendanceTime(d)));
					}
					Optional<OverTimeOfDaily>  ot = d.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
						.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork();
					if(ot.isPresent()){
						processOverTime(ot.get(), 4, startTime == null ? 0 : 30);
						
						processOverTime(ot.get(), 5, startTime == null ? 0 : 60);
					} else {
						OverTimeOfDaily otn = new OverTimeOfDaily(new ArrayList<>(), new ArrayList<>(), 
								Finally.of(new ExcessOverTimeWorkMidNightTime(TimeDivergenceWithCalculation.sameTime(AttendanceTime.ZERO))));
						
						processOverTime(otn, 4, startTime == null ? 0 : 30);
						
						processOverTime(otn, 5, startTime == null ? 0 : 60);
						
						d.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
								.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().updateOverTime(otn);
					}
				}
//			}
			
		});
		
		return dailies;
	}

	private AttendanceTimeOfDailyPerformance createDefaultAttendanceTime(IntegrationOfDaily d) {
		return new AttendanceTimeOfDailyPerformance(d.getWorkInformation().getEmployeeId(), 
				d.getWorkInformation().getYmd(), WorkScheduleTimeOfDaily.defaultValue(), 
				ActualWorkingTimeOfDaily.defaultValue(), 
				new StayingTimeOfDaily(AttendanceTime.ZERO, AttendanceTime.ZERO, AttendanceTime.ZERO, AttendanceTime.ZERO, AttendanceTime.ZERO), 
				AttendanceTimeOfExistMinus.ZERO, AttendanceTimeOfExistMinus.ZERO, 
				new MedicalCareTimeOfDaily(WorkTimeNightShift.DAY_SHIFT, AttendanceTime.ZERO, AttendanceTime.ZERO, AttendanceTime.ZERO));
	}
	
	@Override
	public List<AnyItemOfMonthly> monthlyProcessing(String companyId, String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		return monthlyProcessing(companyId, employeeId, yearMonth, closureId, closureDate, Optional.empty(), new ArrayList<>());
	}
	
	@Override
	public List<AnyItemOfMonthly> monthlyProcessing(String companyId, String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, Optional<AttendanceTimeOfMonthly> attendanceTime) {
		return monthlyProcessing(companyId, employeeId, yearMonth, closureId, closureDate, attendanceTime, new ArrayList<>());
	}
	
	@Override
	public List<AnyItemOfMonthly> monthlyProcessing(String companyId, String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, Optional<AttendanceTimeOfMonthly> attendanceTime, List<AnyItemOfMonthly> monthlyOptionalItems) {
		/** 任意項目の件数を取得 */
		if(!attendanceTime.isPresent()){
			attendanceTime = attendanceTimeOfMonthly.find(employeeId, yearMonth, closureId, closureDate);
		}
		if(!attendanceTime.isPresent()){
			return new ArrayList<>(monthlyOptionalItems);
		}
		
		/** 任意項目の件数を取得 */
		List<AnyItemValueOfDaily> optionalItemsInMonth = dailyOptionalItem.finds(Arrays.asList(attendanceTime.get().getEmployeeId()), attendanceTime.get().getDatePeriod());
		List<AnyItemValue> items = optionalItemsInMonth.stream().map(o -> o.getItems()).flatMap(List::stream).collect(Collectors.toList());
		double count18 = sumCountItem(items, 18), count19 = sumCountItem(items, 19), 
				count21 = sumCountItem(items, 21), count23 = sumCountItem(items, 23);
		
		/** 残業日数を取得 */
		int countOver = countOverTime(employeeId, attendanceTime);
		
		List<AnyItemOfMonthly> dataToProcess = new ArrayList<>(monthlyOptionalItems);

		if(dataToProcess.isEmpty()){
			dataToProcess = monthlyOptionalItem.find(employeeId, yearMonth, closureId, closureDate, MONTHKY_ANYITEM_TO_PROCESS);
		} else {
			List<Integer> notExist = new ArrayList<>(MONTHKY_ANYITEM_TO_PROCESS);
			
			notExist.removeAll(dataToProcess.stream().map(c -> c.getAnyItemId()).distinct().collect(Collectors.toList()));
			
			if(!notExist.isEmpty()){
				dataToProcess.addAll(monthlyOptionalItem.find(employeeId, yearMonth, closureId, closureDate, notExist));
			}
		}
		
		/** 任意項目20: 割合を計算 */
		accessMonthlyItem(employeeId, yearMonth, closureId, closureDate, 20, getOnDefault(count18, count19), dataToProcess);
		
		/** 任意項目22: 割合を計算 */
		accessMonthlyItem(employeeId, yearMonth, closureId, closureDate, 22, getOnDefault(count18, count21), dataToProcess);
		
		/** 任意項目24: 割合を計算 */
		accessMonthlyItem(employeeId, yearMonth, closureId, closureDate, 24, getOnDefault(countOver, count23), dataToProcess);
		
		/** 任意項目46: 割合を計算 */
		accessMonthlyItem(employeeId, yearMonth, closureId, closureDate, 46, sumFor46(dataToProcess), dataToProcess);
		
		monthlyOptionalItem.persistAndUpdate(dataToProcess);
		
		return dataToProcess;
	}

	private int countOverTime(String employeeId, Optional<AttendanceTimeOfMonthly> attendanceTime) {
		List<AttendanceTimeOfDailyPerformance> dailyAttendance = dailyAttendanceTime.finds(Arrays.asList(employeeId), attendanceTime.get().getDatePeriod());
		return dailyAttendance.stream().mapToInt(at -> {
			Optional<OverTimeOfDaily>  ot = getOverTime(at);
			if(ot.isPresent()){
				return ot.get().getOverTimeWorkFrameTime().stream().mapToInt(otf -> sumActualOvertime(otf)).sum() > 0 ? 1 : 0;
			}
			return 0;
		}).sum();
	}
	
	private void accessMonthlyItem(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, int itemNo, double val, List<AnyItemOfMonthly> monthlyOptionalItems){
		AnyTimesMonth value = new AnyTimesMonth(val);
		if(monthlyOptionalItems.stream().anyMatch(m -> m.getAnyItemId() == itemNo)){
			monthlyOptionalItems.stream().filter(mi -> mi.getAnyItemId() == itemNo).findFirst().ifPresent(mi -> {
				mi.updateTimes(value);
			});
		} else {
			monthlyOptionalItems.add(AnyItemOfMonthly.of(employeeId, yearMonth, closureId, 
					closureDate, itemNo, Optional.empty(), Optional.of(value), Optional.empty()));
		}
	}
	
	private double sumFor46(List<AnyItemOfMonthly> monthlyOptionalItems){
		return monthlyOptionalItems.stream().filter(mo -> mo.getAnyItemId() == 1 || mo.getAnyItemId() == 2)
				.mapToInt(mo -> mo.getTimes().orElseGet(() -> new AnyTimesMonth(0d)).v().intValue()).sum();
	}

	private double sumCountItem(List<AnyItemValue> items, int itemNo) {
		return items.stream().filter(o -> o.getItemNo().v() == itemNo)
					.mapToDouble(ai -> ai.getTimes().orElseGet(() -> new AnyItemTimes(COUNT_OFF)).v().doubleValue())
					.sum();
	}

	private int sumActualOvertime(OverTimeFrameTime otf) {
		return getTimefrom(otf.getOverTimeWork()) + getTimefrom(otf.getTransferTime());
	}

	private void processOverTime(OverTimeOfDaily ot, int no, int val){
		Optional<OverTimeFrameTime> frame = ot.getOverTimeWorkFrameTime().stream()
				.filter(otf -> otf.getOverWorkFrameNo().v() == no).findFirst();
		if(frame.isPresent()){
			frame.get().getOverTimeWork().setTime(new AttendanceTime(val));
		} else {
			ot.getOverTimeWorkFrameTime().add(new OverTimeFrameTime(new OverTimeFrameNo(no), 
					TimeDivergenceWithCalculation.sameTime(new AttendanceTime(val)), 
					TimeDivergenceWithCalculation.sameTime(AttendanceTime.ZERO), 
					AttendanceTime.ZERO, AttendanceTime.ZERO));
		}
	}

	private Map<WorkTypeCode, WorkType> getWorkType(String companyId, Set<String> workTypeCode, Map<WorkTypeCode, WorkType> workTypeMap) {
		if(workTypeMap != null && !workTypeMap.isEmpty()){
			return workTypeMap;
		}
		return workTypeRepo.getPossibleWorkTypeV2(companyId, new ArrayList<>(workTypeCode))
							.stream().collect(Collectors.toMap(wt -> wt.getWorkTypeCode(), wt -> wt));
	}	
	
	private double getOnDefault(double downer, double upper) {
		if(downer == 0 || upper == 0){
			return 0;
		}
		return BigDecimal.valueOf(upper).divide(BigDecimal.valueOf(downer), new MathContext(5, RoundingMode.HALF_UP)).multiply(V100).setScale(0, RoundingMode.HALF_UP).doubleValue();
	}
	
	private boolean isWeekday(WorkTypeUnit atr, WorkTypeClassification oneDay, WorkTypeClassification morning,
			WorkTypeClassification afternoon) {
		return (atr ==WorkTypeUnit.OneDay && (oneDay == WorkTypeClassification.Attendance || oneDay == WorkTypeClassification.Shooting))
				|| (atr == WorkTypeUnit.MonringAndAfternoon && (morning == WorkTypeClassification.Attendance || morning == WorkTypeClassification.Shooting
															|| afternoon == WorkTypeClassification.Attendance || afternoon == WorkTypeClassification.Shooting));
	}

	private Integer getTimeStamp(Optional<TimeActualStamp> stamp){
		if(stamp.isPresent()){
			return getWorkStamp(stamp.get().getStamp());
		}
		return null;
	}
	
	private Integer getWorkStamp(Optional<WorkStamp> stamp){
		if(stamp.isPresent() && stamp.get().getTimeWithDay() != null){
			return stamp.get().getTimeWithDay().valueAsMinutes();
		}
		return null;
	}
	
	private int getAttendanceTime(AttendanceTime time) {
		return time == null ? 0 : time.valueAsMinutes();
	}
	
	private int getTimefrom(TimeDivergenceWithCalculation time){
		return time == null ? 0 : getAttendanceTime(time.getTime());
	}
	
	private FlexTime getFlexTime(Optional<OverTimeOfDaily> overTimeD){ 
		if(overTimeD.isPresent() && overTimeD.get().getFlexTime() != null ){
				return overTimeD.get().getFlexTime();
		}
		return new FlexTime(TimeDivergenceWithCalculationMinusExist.sameTime(AttendanceTimeOfExistMinus.ZERO), 
							AttendanceTime.ZERO);
	}
	
	private Optional<OverTimeOfDaily> getOverTime(AttendanceTimeOfDailyPerformance attendanceTime){
		ActualWorkingTimeOfDaily actual = attendanceTime.getActualWorkingTimeOfDaily();
		if(actual == null || actual.getTotalWorkingTime() == null
			|| actual.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily() == null){
					return Optional.empty();
		}
		return actual.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork();
	}

	private boolean isGotoWork(WorkTypeUnit atr, WorkTypeClassification oneDay, WorkTypeClassification morning,
			WorkTypeClassification afternoon) {
		return (atr == WorkTypeUnit.OneDay && oneDay == WorkTypeClassification.Attendance) || 
				(atr == WorkTypeUnit.MonringAndAfternoon && (morning == WorkTypeClassification.Attendance || afternoon == WorkTypeClassification.Attendance));
	}

	private boolean isNotActualWork(IntegrationOfDaily d, WorkTypeUnit atr, WorkTypeClassification oneDay,
			WorkTypeClassification morning, WorkTypeClassification afternoon) {
		AttendanceTime actualWork = d.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
				.getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getActualWorkTime();
		return ((atr == WorkTypeUnit.OneDay && (oneDay == WorkTypeClassification.AnnualHoliday 
				|| oneDay == WorkTypeClassification.SpecialHoliday || oneDay == WorkTypeClassification.SubstituteHoliday))
			|| (atr == WorkTypeUnit.MonringAndAfternoon && 
						((morning == WorkTypeClassification.AnnualHoliday && afternoon == WorkTypeClassification.SpecialHoliday) ||
						(morning == WorkTypeClassification.AnnualHoliday && afternoon == WorkTypeClassification.SubstituteHoliday) ||
						(morning == WorkTypeClassification.SpecialHoliday && afternoon == WorkTypeClassification.AnnualHoliday) ||
						(morning == WorkTypeClassification.SpecialHoliday && afternoon == WorkTypeClassification.SubstituteHoliday) ||
						(morning == WorkTypeClassification.SubstituteHoliday && afternoon == WorkTypeClassification.AnnualHoliday) ||
						(morning == WorkTypeClassification.SubstituteHoliday && afternoon == WorkTypeClassification.SpecialHoliday))))
			&&  (actualWork == null || actualWork.valueAsMinutes() == 0);
	}

	private void processOptionalItem(Supplier<Boolean> condition, AnyItemValueOfDaily optionalItem,
			BigDecimal valueOn, BigDecimal valueOff, int... itemNo){
		if(condition.get()){
			mergeOptionalItemWithNo(optionalItem, valueOn, itemNo);
		} else {
			updateOptionalItemWithNo(optionalItem, valueOff, itemNo);
		}
	}
	
	private void processOptionalItem(Supplier<Boolean> condition, AnyItemValueOfDaily optionalItem,
			Integer valueOn, Integer valueOff, int... itemNo){
		if(condition.get()){
			mergeOptionalItemWithNo(optionalItem, valueOn, itemNo);
		} else {
			updateOptionalItemWithNo(optionalItem, valueOff, itemNo);
		}
	}
	
	private void mergeOptionalItemWithNo(AnyItemValueOfDaily d, BigDecimal count, int... itemNo) {
		AnyItemTimes val = count == null ? null : new AnyItemTimes(count);
		for (int no : itemNo) {
			if(d.getNo(no).isPresent()){
				d.getNo(no).get().updateTimes(val);
			} else {
				d.getItems().add(new AnyItemValue(new AnyItemNo(no), Optional.ofNullable(val), Optional.empty(), Optional.empty()));
			}
		}
	}
	
	private void updateOptionalItemWithNo(AnyItemValueOfDaily d, BigDecimal count, int... itemNo) {
		AnyItemTimes val = count == null ? null : new AnyItemTimes(count);
		for (int no : itemNo) {
			d.getNo(no).ifPresent(oi -> oi.updateTimes(val));
		}
	}
	
	private void mergeOptionalItemWithNo(AnyItemValueOfDaily d, Integer time, int... itemNo) {
		AnyItemTime val = time == null ? null : new AnyItemTime(time);
		for (int no : itemNo) {
			if(d.getNo(no).isPresent()){
				d.getNo(no).get().updateTime(val);
			} else {
				d.getItems().add(new AnyItemValue(new AnyItemNo(no), Optional.empty(), Optional.empty(), Optional.ofNullable(val)));
			}
		}
	}
	
	private void updateOptionalItemWithNo(AnyItemValueOfDaily d, Integer time, int... itemNo) {
		AnyItemTime val = time == null ? null : new AnyItemTime(time);
		for (int no : itemNo) {
			d.getNo(no).ifPresent(oi -> oi.updateTime(val));
		}
	}
	
	private boolean checkOnPair(List<Integer> list1, List<Integer> list2, BiFunction<Integer, Integer, Boolean> onCheck){
		for(int i = 0; i < list1.size(); i++){
			int val1 = getOnDefault(list1, i);
			int val2 = getOnDefault(list2, i);
			if(onCheck.apply(val1, val2)){
				return true;
			}
		}
		return false;
	}

	private Integer getOnDefault(List<Integer> overTime, int idx) {
		return idx >= overTime.size() || overTime.get(idx) == null ? 0 : overTime.get(idx);
	}

}


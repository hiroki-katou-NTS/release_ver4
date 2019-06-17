package nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyKey;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.converter.MonthlyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkCheckResult;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.AttendanceItemCondition;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class PerTimeMonActualResultDefault implements PerTimeMonActualResultService {

	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	
	@Inject
	private AttendanceItemConvertFactory attendanceItemConvertFactory;
	
	@Inject 
	private AnyItemOfMonthlyRepository anyItemOfMonthlyRepo;
	
	
	@Override
	public boolean checkPerTimeMonActualResult(YearMonth yearMonth, int closureID, ClosureDate closureDate,
			String employeeID,AttendanceItemCondition attendanceItemCondition) {
		
		//社員に一致する月別実績を取得する
		Optional<AttendanceTimeOfMonthly> attendanceTimeOfMonthly = attendanceTimeOfMonthlyRepo.find(
				employeeID, 
				yearMonth,
				EnumAdaptor.valueOf(closureID,ClosureId.class),
				closureDate);
		//存在しない場合
		if(!attendanceTimeOfMonthly.isPresent()) {
			return false;
		}
		
		MonthlyRecordToAttendanceItemConverter monthly = attendanceItemConvertFactory.createMonthlyConverter();
		monthly.withAttendanceTime(attendanceTimeOfMonthly.get());
		
		//勤怠項目をチェックする
		return attendanceItemCondition.check(item->{
			if (item.isEmpty()) {
				return new ArrayList<>();
			}
			return monthly.convert(item).stream().map(iv -> getValue(iv))
					.collect(Collectors.toList());
			
		}) == WorkCheckResult.ERROR;
	}
	//HoiDD No.257
	@Override
	public Map<String, Integer> checkPerTimeMonActualResult(YearMonth yearMonth, String employeeID,
			AttendanceItemCondition attendanceItemCondition) {
		Map<String, Integer> results = new HashMap<String,Integer>();
	/*	List<MonthlyRecordValueImport> monthlyRecords = new ArrayList<>();
		MonthlyRecordValueImports monthlyRecord=null;*/
		List<AttendanceTimeOfMonthly> attendanceTimeOfMonthlys = new ArrayList<>();
		List<AnyItemOfMonthly> anyItems = new ArrayList<>();
		//締めをチェックする
			attendanceTimeOfMonthlys = attendanceTimeOfMonthlyRepo.findByYearMonthOrderByStartYmd(employeeID,
					yearMonth);
			anyItems = anyItemOfMonthlyRepo.findByMonthly(employeeID, yearMonth);

		if (!CollectionUtil.isEmpty(attendanceTimeOfMonthlys)) {
			for (AttendanceTimeOfMonthly attendanceTimeOfMonthly : attendanceTimeOfMonthlys) {
				AttendanceTimeOfMonthlyKey key = new AttendanceTimeOfMonthlyKey(
						attendanceTimeOfMonthly.getEmployeeId(),
						attendanceTimeOfMonthly.getYearMonth(),
						attendanceTimeOfMonthly.getClosureId(),
						attendanceTimeOfMonthly.getClosureDate());
				MonthlyRecordToAttendanceItemConverter monthly = attendanceItemConvertFactory.createMonthlyConverter();
					monthly.withAttendanceTime(attendanceTimeOfMonthly);
				if (!CollectionUtil.isEmpty(anyItems)){
					Map<AttendanceTimeOfMonthlyKey, List<AnyItemOfMonthly>> anyItemsMap = new HashMap<>();
					for (AnyItemOfMonthly anyItem : anyItems){
						AttendanceTimeOfMonthlyKey key2 = new AttendanceTimeOfMonthlyKey(
								anyItem.getEmployeeId(),
								anyItem.getYearMonth(),
								anyItem.getClosureId(),
								anyItem.getClosureDate());
						if(anyItemsMap.containsKey(key2)){
							anyItemsMap.get(key2).add(anyItem);
						}else {
							List<AnyItemOfMonthly> anyItemsType = new ArrayList<>();
							anyItemsType.add(anyItem);
							anyItemsMap.put(key2, anyItemsType);
						}
					}
					if(anyItemsMap.containsKey(key)){
						monthly.withAnyItem(anyItemsMap.get(key));
					}
				}			
					boolean check = attendanceItemCondition.check(item->{
						if (item.isEmpty()) {
							return new ArrayList<>();
						}
						return monthly.convert(item).stream().map(iv -> getValueNew(iv))
								.collect(Collectors.toList());
					}) == WorkCheckResult.ERROR;
					if (check == true) {
						results.put(employeeID + yearMonth.toString() +  attendanceTimeOfMonthly.getClosureId().toString(),1);
					}
				
				
			}
		}
		
		return results;
	}
	
	@Inject
	private TimeOfMonthlyRepository timeRepo;
	
	@Inject
	private RemainMergeRepository remainRepo;
	
	@Override
	public Map<String, Map<YearMonth, Map<String, Integer>>> checkPerTimeMonActualResult(YearMonthPeriod yearMonth, List<String> employeeID, Map<String, AttendanceItemCondition> checkConditions,
			Map<String, Map<YearMonth, Map<String,String>>> resultsData) {
		Map<String, Map<YearMonth, Map<String, Integer>>> results = new HashMap<>();
		//締めをチェックする
		List<YearMonth> yearmonths = yearMonth.yearMonthsBetween();
//		List<AttendanceTimeOfMonthly> attendanceTimeOfMonthlys = attendanceTimeOfMonthlyRepo.findBySidsAndYearMonths(employeeID, yearmonths);
		List<AnyItemOfMonthly> anyItems = anyItemOfMonthlyRepo.findBySidsAndMonths(employeeID, yearmonths);
//		MonthlyRecordToAttendanceItemConverter monthly = attendanceItemConvertFactory.createMonthlyConverter();
		
		// 月別実績の所属情報を取得する
		List<TimeOfMonthly> times = timeRepo.findBySidsAndYearMonths(employeeID, yearmonths);
		List<RemainMerge>  remains = remainRepo.findBySidsAndYearMonths(employeeID, yearmonths);
		
		for (TimeOfMonthly attendanceTime : times){
			String employeeId = attendanceTime.getEmployeeId();
			
			Optional<RemainMerge> currentRemain = remains.stream().filter(c -> {
				return c.getMonthMergeKey().getEmployeeId().equals(employeeId)
						&& c.getMonthMergeKey().getClosureDate().equals(attendanceTime.getClosureDate())
						&& c.getMonthMergeKey().getClosureId().equals(attendanceTime.getClosureId())
						&& c.getMonthMergeKey().getYearMonth().equals(attendanceTime.getYearMonth());
			}).findFirst();
			List<AnyItemOfMonthly> currentAnyItems = anyItems.stream().filter(c -> {
				return c.getEmployeeId().equals(employeeId)
						&& c.getClosureDate().equals(attendanceTime.getClosureDate())
						&& c.getClosureId().equals(attendanceTime.getClosureId())
						&& c.getYearMonth().equals(attendanceTime.getYearMonth());
			}).collect(Collectors.toList());
			// 勤怠項目値リストに変換する準備をする
			MonthlyRecordToAttendanceItemConverter monthlyConverter = this.attendanceItemConvertFactory.createMonthlyConverter();
			
			attendanceTime.getAffiliation().ifPresent(af -> {
				monthlyConverter.withAffiliation(af);
			});
			attendanceTime.getAttendanceTime().ifPresent(at -> {
				monthlyConverter.withAttendanceTime(at);
			});
			monthlyConverter.withAnyItem(currentAnyItems);
			
			currentRemain.ifPresent(remain -> {
				monthlyConverter.withAnnLeave(remain.getAnnLeaRemNumEachMonth());
				monthlyConverter.withRsvLeave(remain.getRsvLeaRemNumEachMonth());
				monthlyConverter.withAbsenceLeave(remain.getAbsenceLeaveRemainData());
				monthlyConverter.withDayOff(remain.getMonthlyDayoffRemainData());
				monthlyConverter.withSpecialLeave(remain.getSpecialHolidayRemainList());
				monthlyConverter.withMonCareHd(remain.getMonCareHdRemain());
				monthlyConverter.withMonChildHd(remain.getMonChildHdRemain());
			});
			
			checkConditions.entrySet().stream().forEach(con -> {
				List<Double> listData = con.getValue().getGroup1().getLstErAlAtdItemCon().stream().map(c->c.sumCheckTarget(item ->{
					if (item.isEmpty()) {
						return new ArrayList<>();
					}
					return monthlyConverter.convert(item).stream().map(iv -> getValueNew(iv))
							.collect(Collectors.toList());
				})).collect(Collectors.toList());
				boolean check = con.getValue().check(item->{
					if (item.isEmpty()) {
						return new ArrayList<>();
					}
					return monthlyConverter.convert(item).stream().map(iv -> getValueNew(iv))
							.collect(Collectors.toList());
				}) == WorkCheckResult.ERROR;
				
				if (check) {
					if(!results.containsKey(employeeId)){
						results.put(employeeId, new HashMap<>());
						resultsData.put(employeeId, new HashMap<>());
					}
					if(!results.get(employeeId).containsKey(attendanceTime.getYearMonth())){
						results.get(employeeId).put(attendanceTime.getYearMonth(), new HashMap<>());
						resultsData.get(employeeId).put(attendanceTime.getYearMonth(), new HashMap<>());
					}
					results.get(employeeId).get(attendanceTime.getYearMonth()).put(con.getKey(), 1);
					resultsData.get(employeeId).get(attendanceTime.getYearMonth()).put(con.getKey(), listData.isEmpty()?null:listData.get(0).toString());
				}
			});
		}
		
		
		
		
		
//		if (!CollectionUtil.isEmpty(attendanceTimeOfMonthlys)) {
//			attendanceTimeOfMonthlys.stream().forEach(atm -> {
//				
//				List<AnyItemOfMonthly> aims = anyItems.stream().filter(aim -> {
//					return aim.getEmployeeId().equals(atm.getEmployeeId()) && aim.getYearMonth().equals(atm.getYearMonth())
//							&& aim.getClosureId() == atm.getClosureId() && aim.getClosureDate().equals(atm.getClosureDate());
//				}).collect(Collectors.toList());
//				
//				monthly.withAttendanceTime(atm).withAnyItem(aims);
//				
//				checkConditions.entrySet().stream().forEach(con -> {
//
//					boolean check = con.getValue().check(item->{
//						if (item.isEmpty()) {
//							return new ArrayList<>();
//						}
//						return monthly.convert(item).stream().map(iv -> getValueNew(iv))
//								.collect(Collectors.toList());
//					}) == WorkCheckResult.ERROR;
//					
//					if (check) {
//						if(!results.containsKey(atm.getEmployeeId())){
//							results.put(atm.getEmployeeId(), new HashMap<>());
//						}
//						if(!results.get(atm.getEmployeeId()).containsKey(atm.getYearMonth())){
//							results.get(atm.getEmployeeId()).put(atm.getYearMonth(), new HashMap<>());
//						}
//						results.get(atm.getEmployeeId()).get(atm.getYearMonth()).put(con.getKey(), 1);
//					}
//				});
//			});
//		}
		
		return results;
	}
	//HoiDD
	private Double getValueNew(ItemValue value) {
		if(value.getValueType()==ValueType.DATE){
			return 0d;
		}
		if (value.value() == null) {
			return 0d;
		}
		
		return value.getValueType().isDouble() ? ((Double) value.value()) : Double.valueOf((Integer) value.value());
	}
	
	private Double getValue(ItemValue value) {
		if (value.value() == null) {
			return 0d;
		}
		return value.getValueType().isDouble() ? ((Double) value.value())
				: Double.valueOf((Integer) value.value());
	}
}

package nts.uk.ctx.at.record.pubimp.monthly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthlyRepository;
import nts.uk.ctx.at.record.pub.monthly.GetMonthlyRecordPub;
import nts.uk.ctx.at.record.pub.monthly.MonthlyRecordValuesExport;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.monthlyattditem.DisplayMonthResultsMethod;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItem;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItemRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.MonthlyDayoffRemainData;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.service.AttendanceItemConvertFactory;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.affiliation.AffiliationInfoOfMonthlyRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.remainmerge.RemainMergeRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.absenceleave.AbsenceLeaveRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.absenceleave.AbsenceLeaveRemainDataRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.MonthlyDayoffRemainDataRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.RsvLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialHolidayRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialHolidayRemainDataRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * ?????????????????????????????????????????????
 * @author shuichu_ishida
 */
@Stateless
public class GetMonthlyRecordPubImpl implements GetMonthlyRecordPub {

	/** ??????????????????????????? */
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeRepo;
	/** ??????????????????????????? */
	@Inject
	private AffiliationInfoOfMonthlyRepository affiliationInfoRepo;
	/** ??????????????????????????? */
	@Inject
	private AnyItemOfMonthlyRepository anyItemRepo;
	/** ??????????????????????????? */
	@Inject
	private AnnLeaRemNumEachMonthRepository annualLeaveRepo;
	/** ????????????????????????????????? */
	@Inject
	private RsvLeaRemNumEachMonthRepository reserveLeaveRepo;
	/** ??????????????????????????? */
	@Inject
	private AbsenceLeaveRemainDataRepository absLeaRemRepo;
	/** ??????????????????????????? */
	@Inject
	private MonthlyDayoffRemainDataRepository monDayoffRemRepo;
	/** ????????????????????????????????? */
	@Inject
	private SpecialHolidayRemainDataRepository spcLeaRemRepo;
	/** ?????????????????? */
	@Inject
	private AttendanceItemConvertFactory attendanceItemConverterFact;
	
	@Inject
	private TimeOfMonthlyRepository timeRepo;
	
	@Inject
	private RemainMergeRepository remainRepo;
	
	@Inject
	private MonthlyAttendanceItemRepository monthAtdRepo;
	
	/** ???????????????????????????????????? */
	@Override
	public List<MonthlyRecordValuesExport> algorithm(String employeeId, YearMonthPeriod period, List<Integer> itemIds) {
		
		// ???????????????????????????
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		
		// ????????????????????????????????????
		val results = this.algorithm(employeeIds, period, itemIds);
		if (results.containsKey(employeeId)) return results.get(employeeId);
		
		return new ArrayList<>();
	}
	
	/** ???????????????????????????????????? */
	@Override
	public Map<String, List<MonthlyRecordValuesExport>> algorithm(List<String> employeeIds, YearMonthPeriod period,
			List<Integer> itemIds) {
		
		Map<String, List<MonthlyRecordValuesExport>> results = new HashMap<>();

		// ???????????????????????????
		val yearMonths = ConvertHelper.yearMonthsBetween(period);
		
		// ??????????????????????????????????????????
		val times = timeRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		val remains = remainRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		
		// ??????????????????????????????????????????
		val anyItems = this.anyItemRepo.findBySidsAndMonths(employeeIds, yearMonths);
		for (val attendanceTime : times){
			val employeeId = attendanceTime.getEmployeeId();
			
			val currentRemain = remains.stream().filter(c -> {
				return c.getMonthMergeKey().getEmployeeId().equals(employeeId)
						&& c.getMonthMergeKey().getClosureDate().equals(attendanceTime.getClosureDate())
						&& c.getMonthMergeKey().getClosureId().equals(attendanceTime.getClosureId())
						&& c.getMonthMergeKey().getYearMonth().equals(attendanceTime.getYearMonth());
			}).findFirst();
			val currentAnyItems = anyItems.stream().filter(c -> {
				return c.getEmployeeId().equals(employeeId)
						&& c.getClosureDate().equals(attendanceTime.getClosureDate())
						&& c.getClosureId().equals(attendanceTime.getClosureId())
						&& c.getYearMonth().equals(attendanceTime.getYearMonth());
			}).collect(Collectors.toList());
			
			// ??????????????????????????????????????????????????????
			val monthlyConverter = this.attendanceItemConverterFact.createMonthlyConverter();
			
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
				monthlyConverter.withSpecialLeave(remain.getSpecialHolidayRemainData());
				monthlyConverter.withMonCareHd(remain.getMonCareHdRemain());
				monthlyConverter.withMonChildHd(remain.getMonChildHdRemain());
			});
			
			// ????????????????????????????????????????????????
			results.putIfAbsent(employeeId, new ArrayList<>());
			results.get(employeeId).add(MonthlyRecordValuesExport.of(
					attendanceTime.getYearMonth(),
					attendanceTime.getClosureId(),
					attendanceTime.getClosureDate(),
					monthlyConverter.convert(itemIds)));
		}
		
		return results;
	}
	
	/** ?????????????????????????????????????????? */
	@Override
	public Optional<AttendanceTimeOfMonthly> getAttendanceTime(String employeeId, YearMonth yearMonth) {
		
		val attendanceTimes = this.attendanceTimeRepo.findByYearMonthOrderByStartYmd(employeeId, yearMonth);
		AttendanceTimeOfMonthly result = null;
		for (val attendanceTime : attendanceTimes){
			if (result == null){
				result = attendanceTime;
				continue;
			}
			attendanceTime.sum(result);
			result = attendanceTime;
		}
		return Optional.ofNullable(result);
	}
	
	/** ????????????????????????????????? */
	@Override
	public Map<String, List<MonthlyRecordValuesExport>> getRecordValues(List<String> employeeIds,
			YearMonthPeriod period, List<Integer> itemIds) {
		
		Map<String, List<MonthlyRecordValuesExport>> results = new HashMap<>();

		// ???????????????????????????
		val yearMonths = ConvertHelper.yearMonthsBetween(period);
		
		// ??????????????????????????????????????????
		val attendanceTimes = this.attendanceTimeRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		Map<String, Map<YearMonth, List<AttendanceTimeOfMonthly>>> attendanceTimeMap = new HashMap<>();
		for (val attendanceTime : attendanceTimes) {
			val employeeId = attendanceTime.getEmployeeId();
			val yearMonth = attendanceTime.getYearMonth();
			attendanceTimeMap.putIfAbsent(employeeId, new HashMap<>());
			if (attendanceTimeMap.get(employeeId).containsKey(yearMonth)) {
				attendanceTimeMap.get(employeeId).get(yearMonth).add(attendanceTime);
				continue;
			}
			List<AttendanceTimeOfMonthly> lstAttendanceTimeOfMonthlies = new ArrayList<>();
			lstAttendanceTimeOfMonthlies.add(attendanceTime);
			attendanceTimeMap.get(employeeId).putIfAbsent(yearMonth, lstAttendanceTimeOfMonthlies);
		}

		// ??????????????????????????????????????????
		val anyItems = this.anyItemRepo.findBySidsAndMonths(employeeIds, yearMonths);
		Map<String, Map<YearMonth, List<AnyItemOfMonthly>>> anyItemMap = new HashMap<>();
		for (val anyItem : anyItems) {
			val employeeId = anyItem.getEmployeeId();
			val yearMonth = anyItem.getYearMonth();
			anyItemMap.putIfAbsent(employeeId, new HashMap<>());
			anyItemMap.get(employeeId).putIfAbsent(yearMonth, new ArrayList<>());
			ListIterator<AnyItemOfMonthly> itrAnyItem = anyItemMap.get(employeeId).get(yearMonth).listIterator();
			boolean isSum = false;
			while (itrAnyItem.hasNext()){
				val outAnyItem = itrAnyItem.next();
				if (outAnyItem.getAnyItemId() == anyItem.getAnyItemId()){
					outAnyItem.sum(anyItem);
					itrAnyItem.set(outAnyItem);
					isSum = true;
					break;
				}
			}
			if (!isSum) anyItemMap.get(employeeId).get(yearMonth).add(anyItem);
		}
	
		// ??????.????????????????????????????????????
		// ?????????????????????????????????????????? 
		val affiliationInfos = this.affiliationInfoRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		Map<String, Map<YearMonth, AffiliationInfoOfMonthly>> affiliationInfoMap = new HashMap<>();
		for (val affiliationInfo : affiliationInfos){
			val employeeId = affiliationInfo.getEmployeeId();
			val yearMonth = affiliationInfo.getYearMonth();
			affiliationInfoMap.putIfAbsent(employeeId, new HashMap<>());
			affiliationInfoMap.get(employeeId).putIfAbsent(yearMonth, affiliationInfo);
		}

		// ??????????????????????????????????????????
		val annualLeaves = this.annualLeaveRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		Map<String, Map<YearMonth, AnnLeaRemNumEachMonth>> annualLeaveMap = new HashMap<>();
		for (val annualLeave : annualLeaves){
			val employeeId = annualLeave.getEmployeeId();
			val yearMonth = annualLeave.getYearMonth();
			annualLeaveMap.putIfAbsent(employeeId, new HashMap<>());
			annualLeaveMap.get(employeeId).put(yearMonth, annualLeave);
		}
		
		// ????????????????????????????????????????????????
		val reserveLeaves = this.reserveLeaveRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		Map<String, Map<YearMonth, RsvLeaRemNumEachMonth>> reserveLeaveMap = new HashMap<>();
		for (val reserveLeave : reserveLeaves){
			val employeeId = reserveLeave.getEmployeeId();
			val yearMonth = reserveLeave.getYearMonth();
			reserveLeaveMap.putIfAbsent(employeeId, new HashMap<>());
			reserveLeaveMap.get(employeeId).put(yearMonth, reserveLeave);
		}
		
		// ??????????????????????????????????????????
		val absenceLeaves = this.absLeaRemRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		Map<String, Map<YearMonth, AbsenceLeaveRemainData>> absenceLeaveMap = new HashMap<>();
		for (val absenceLeave : absenceLeaves){
			val employeeId = absenceLeave.getSId();
			val yearMonth = absenceLeave.getYm();
			absenceLeaveMap.putIfAbsent(employeeId, new HashMap<>());
			absenceLeaveMap.get(employeeId).put(yearMonth, absenceLeave);
		}
		
		// ??????????????????????????????????????????
		val monDayoffs = this.monDayoffRemRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		Map<String, Map<YearMonth, MonthlyDayoffRemainData>> monDayoffMap = new HashMap<>();
		for (val monDayoff : monDayoffs){
			val employeeId = monDayoff.getSId();
			val yearMonth = monDayoff.getYm();
			monDayoffMap.putIfAbsent(employeeId, new HashMap<>());
			monDayoffMap.get(employeeId).put(yearMonth, monDayoff);
		}
		
		// ????????????????????????????????????????????????
		val specialLeaves = this.spcLeaRemRepo.findBySidsAndYearMonths(employeeIds, yearMonths);
		Map<String, Map<YearMonth, List<SpecialHolidayRemainData>>> specialLeaveMap = new HashMap<>();
		for (val specialLeave : specialLeaves){
			val employeeId = specialLeave.getSid();
			val yearMonth = specialLeave.getYm();
			specialLeaveMap.putIfAbsent(employeeId, new HashMap<>());
			specialLeaveMap.get(employeeId).putIfAbsent(yearMonth, new ArrayList<>());
			ListIterator<SpecialHolidayRemainData> itrSpecialLeave =
					specialLeaveMap.get(employeeId).get(yearMonth).listIterator();
			boolean isNotExist = false;
			while (itrSpecialLeave.hasNext()){
				val outSpecialLeave = itrSpecialLeave.next();
				if (outSpecialLeave.getSpecialHolidayCd() == specialLeave.getSpecialHolidayCd()){
					itrSpecialLeave.set(outSpecialLeave);
					isNotExist = true;
					break;
				}
			}
			if (!isNotExist) specialLeaveMap.get(employeeId).get(yearMonth).add(specialLeave);
		}

		// ?????????????????????????????????????????????????????????
		List<MonthlyAttendanceItem> lstAttendanceItems = this.monthAtdRepo.findByAttendanceItemId(AppContexts.user().companyId(), itemIds);
		
		Map<Integer, DisplayMonthResultsMethod> mapAtdCalMethod = lstAttendanceItems.stream()
																					.collect(Collectors.toMap(
																							MonthlyAttendanceItem::getAttendanceItemId,
																							MonthlyAttendanceItem::getTwoMonthlyDisplay));

		for (val employeeId : employeeIds) {
			for (val yearMonth : yearMonths) {
				if (!attendanceTimeMap.containsKey(employeeId)) continue;
				if (!attendanceTimeMap.get(employeeId).containsKey(yearMonth)) continue;
				if (attendanceTimeMap.get(employeeId).get(yearMonth).isEmpty()) continue;
				val attendanceTime = attendanceTimeMap.get(employeeId).get(yearMonth);

				// ??????????????????????????????????????????????????????
				val monthlyConverter = this.attendanceItemConverterFact.createMonthlyConverter();
				val monthlyConverter2nd = this.attendanceItemConverterFact.createMonthlyConverter();
				monthlyConverter.withAttendanceTime(attendanceTime.get(0));

				if (attendanceTime.size() >= 2) {
					monthlyConverter2nd.withAttendanceTime(attendanceTime.get(1));
				}

				if (anyItemMap.containsKey(employeeId)){
					if (anyItemMap.get(employeeId).containsKey(yearMonth)){
						monthlyConverter.withAnyItem(anyItemMap.get(employeeId).get(yearMonth));
					}
				}

				if (affiliationInfoMap.containsKey(employeeId)){
					if (affiliationInfoMap.get(employeeId).containsKey(yearMonth)){
						monthlyConverter.withAffiliation(affiliationInfoMap.get(employeeId).get(yearMonth));
					}
				}

				if (annualLeaveMap.containsKey(employeeId)){
					if (annualLeaveMap.get(employeeId).containsKey(yearMonth)){
						monthlyConverter.withAnnLeave(annualLeaveMap.get(employeeId).get(yearMonth));
					}
				}
				if (reserveLeaveMap.containsKey(employeeId)){
					if (reserveLeaveMap.get(employeeId).containsKey(yearMonth)){
						monthlyConverter.withRsvLeave(reserveLeaveMap.get(employeeId).get(yearMonth));
					}
				}
				if (absenceLeaveMap.containsKey(employeeId)){
					if (absenceLeaveMap.get(employeeId).containsKey(yearMonth)){
						monthlyConverter.withAbsenceLeave(absenceLeaveMap.get(employeeId).get(yearMonth));
					}
				}
				if (monDayoffMap.containsKey(employeeId)){
					if (monDayoffMap.get(employeeId).containsKey(yearMonth)){
						monthlyConverter.withDayOff(monDayoffMap.get(employeeId).get(yearMonth));
					}
				}
				if (specialLeaveMap.containsKey(employeeId)){
					if (specialLeaveMap.get(employeeId).containsKey(yearMonth)){
						monthlyConverter.withSpecialLeave(specialLeaveMap.get(employeeId).get(yearMonth));
					}
				}
				
				// ????????????????????????????????????????????????
				results.putIfAbsent(employeeId, new ArrayList<>());
				results.get(employeeId).add(MonthlyRecordValuesExport.of(
						yearMonth,
						attendanceTime.get(0).getClosureId(),
						attendanceTime.get(0).getClosureDate(),
						this.convertByDisplayMonthResult(
								monthlyConverter.convert(itemIds),
								monthlyConverter2nd.convert(itemIds),
								mapAtdCalMethod,
								itemIds)
						)
				);
			}
		}
		
		return results;
	}
	
	
	/**
	 * converter??????????????????
	 * 
	 * @param month1stResult: 1?????????????????????
	 * @param month2ndResult: 2?????????????????????
	 * @param mapAtdCalMethod 
	 * @param itemIds
	 * @return
	 */
	private List<ItemValue> convertByDisplayMonthResult(List<ItemValue> month1stResult
													  , List<ItemValue> month2ndResult
													  , Map<Integer, DisplayMonthResultsMethod> mapAtdCalMethod
													  , List<Integer> itemIds) {
		List<ItemValue> result = new ArrayList<>();

		for (Integer atdId : itemIds) {
			DisplayMonthResultsMethod displayMonthResultsMethod = mapAtdCalMethod.get(atdId);
			if (displayMonthResultsMethod != null) {
				switch (displayMonthResultsMethod) {
					// 1?????????????????????
					case DISPLAY_FIRST_ITEM:
						// ????????????????????????.????????????1?????????????????????????????????????????????????????????????????????ID???;
						Optional<ItemValue> actualValue = month1stResult.stream().filter(t -> t.getItemId() == atdId).findFirst();
						if (actualValue.isPresent()) result.add(actualValue.get());
						break;

					// 2?????????????????????
					case DISPLAY_SECOND_ITEM:
						// ????????????????????????.????????????2?????????????????????????????????????????????????????????????????????ID???;
						Optional<ItemValue> actualValue2nd = month2ndResult.stream().filter(t -> t.getItemId() == atdId).findFirst();
						if (actualValue2nd.isPresent()) result.add(actualValue2nd.get());		
						break;
					
					// ??????????????????????????????
					case DISPLAY_TOTAL_VALUE:
						// 1?????????????????????1?????????????????????????????????????????????????????????????????????ID???;
						Optional<ItemValue> actualValue1 = month1stResult.stream().filter(t -> t.getItemId() == atdId).findFirst();
						// 2?????????????????????2?????????????????????????????????????????????????????????????????????ID???;
						Optional<ItemValue> actualValue2 = month2ndResult.stream().filter(t -> t.getItemId() == atdId).findFirst();
						if (actualValue1.isPresent() && actualValue2.isPresent()) {
							String value;
							if (actualValue1.get().getValueType().isDouble() || actualValue1.get().getValueType().isDoubleCountable()) {
								Double doubleValue = (actualValue1.get().getValue() != null ? Double.parseDouble(actualValue1.get().getValue()) : 0d)
												   + (actualValue2.get().getValue() != null ? Double.parseDouble(actualValue2.get().getValue()) : 0d);
								value = doubleValue.toString();
							} else if (actualValue1.get().getValueType().isInteger() || actualValue1.get().getValueType().isIntegerCountable()) {
								Integer integerValue = (actualValue1.get().getValue() != null ? Integer.parseInt(actualValue1.get().getValue()) : 0)
													 + (actualValue2.get().getValue() != null ? Integer.parseInt(actualValue2.get().getValue()) : 0);
								value = integerValue.toString();
							} else {
								value = actualValue1.get().getValue();
							}
							
							// ????????????????????????.????????????1???????????? + 2????????????;
							result.add(new ItemValue(value, actualValue1.get().getValueType(), actualValue1.get().getLayoutCode(), actualValue1.get().getItemId()));
						}
						break;
					default:
						break;
				}
			}
		}
		
		return result;
	}
}

package nts.uk.ctx.at.function.ac.workrecord.erroralarm.condition.multimonth;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth.CheckActualResultMulMonth;
import nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth.MonthlyRecordValueImport;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlAtdItemConAdapterDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.daily.dailyaggregationprocess.ConditionType;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.multimonth.doevent.MulMonCheckCondDomainEventDto;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.SingleValueCompareType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.MulMonthCheckCond;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.MulMonthCheckCondContinue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.MulMonthCheckCondCosp;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.AttendanceItemId;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValueDay;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class CheckResultMulMonCondition implements CheckActualResultMulMonth{

	@Override
	public boolean checkMulMonthCheckCond(DatePeriod period, String companyId, String employeeId,
			List<MonthlyRecordValueImport> results, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto) {
		String errorAlarmCode = "";
		boolean check = false;
		MulMonthCheckCond mulMonthCheckCond = new MulMonthCheckCond(
				erAlAtdItemConAdapterDto.getErrorAlarmCheckID(),
				erAlAtdItemConAdapterDto.isUseAtr(),
				convertAtdIemConToDomain(erAlAtdItemConAdapterDto.getErAlAtdItem(),companyId,errorAlarmCode)
				) ;
		List<ItemValue> itemValues = new ArrayList<ItemValue>();
			for(MonthlyRecordValueImport result :results ){
				itemValues.addAll(result.getItemValues());
				check = mulMonthCheckCond.getErAlAttendanceItemCondition().checkTarget(item->{
						if (item.isEmpty()) {
							return new ArrayList<>();
						}
						return itemValues.stream().map(iv -> getValue(iv))
								.collect(Collectors.toList());
					});
			}

		return check;
	}

	@Override
	public boolean checkMulMonthCheckCondContinue(DatePeriod period, String companyId, String employeeId,
			List<MonthlyRecordValueImport> results, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto) {
		List<YearMonth> lstYearMonth = period.yearMonthsBetween();
		String errorAlarmCode = "";
		boolean check = false;
		MulMonthCheckCondContinue mulMonthCheckCondContinue = new MulMonthCheckCondContinue(
				erAlAtdItemConAdapterDto.getErrorAlarmCheckID(),
				erAlAtdItemConAdapterDto.isUseAtr(),
				erAlAtdItemConAdapterDto.getContinuousMonths(),
				convertAtdIemConToDomain(erAlAtdItemConAdapterDto.getErAlAtdItem(),companyId,errorAlarmCode)
				) ;
		for (YearMonth yearMonth : lstYearMonth) {
			int countContinus = 0;
			for (MonthlyRecordValueImport result : results) {
				if (result.getYearMonth().equals(yearMonth)) {
					boolean checkPerMonth = false;
					 checkPerMonth = mulMonthCheckCondContinue.getErAlAttendanceItemCondition()
							.checkTarget(item -> {
								if (item.isEmpty()) {
									return new ArrayList<>();
								}
								return result.getItemValues().stream().map(iv -> getValue(iv))
										.collect(Collectors.toList());
							});
					countContinus = checkPerMonth ? countContinus++ : 0; 
					if(countContinus>=mulMonthCheckCondContinue.getContinuousMonths()){
						check = true;
					}
				}
			}
			
		}
		return check;
	}

	@Override
	public ArrayList<Integer> checkMulMonthCheckCondCosp(DatePeriod period, String companyId, String employeeId,
			List<MonthlyRecordValueImport> results, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto) {
		String errorAlarmCode = "";
		ArrayList<Integer> listMonthNumber = new ArrayList<>();
		MulMonthCheckCondCosp mulMonthCheckCondCosp = new MulMonthCheckCondCosp(
				erAlAtdItemConAdapterDto.getErrorAlarmCheckID(),
				erAlAtdItemConAdapterDto.isUseAtr(),
				erAlAtdItemConAdapterDto.getTimes(),
				erAlAtdItemConAdapterDto.getCompareOperator(),	
				convertAtdIemConToDomain(erAlAtdItemConAdapterDto.getErAlAtdItem(),companyId,errorAlarmCode)
				) ;
			int countCosp = 0 ;
			for(MonthlyRecordValueImport result :results ){
				boolean checkPerMonth = false;
				checkPerMonth = mulMonthCheckCondCosp.getErAlAttendanceItemCondition().checkTarget(item->{
						if (item.isEmpty()) {
							return new ArrayList<>();
						}
						return result.getItemValues().stream().map(iv -> getValue(iv))
								.collect(Collectors.toList());
					});
				if(checkPerMonth){
					countCosp ++; 
					listMonthNumber.add(result.getYearMonth().month());
				}
				if(!checkFixedValue(countCosp,EnumAdaptor.valueOf(mulMonthCheckCondCosp.getCompareOperator(), SingleValueCompareType.class),mulMonthCheckCondCosp.getTimes())) {
					listMonthNumber.clear();
				}
			}
			
		return listMonthNumber;
	}

	@Override
	public boolean checkMulMonthCheckCondAverage(DatePeriod period, String companyId, String employeeId,
			List<MonthlyRecordValueImport> results, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto) {
		List<YearMonth> lstYearMonth = period.yearMonthsBetween();
//		String errorAlarmCode = "";
		float sum = 0 ;
		float avg = 0 ;
		boolean check = false;
//		MulMonthCheckCondAverage mulMonthCheckCondAverage = new MulMonthCheckCondAverage(
//				erAlAtdItemConAdapterDto.getErrorAlarmCheckID(),
//				erAlAtdItemConAdapterDto.isUseAtr(),
//				convertAtdIemConToDomain(erAlAtdItemConAdapterDto.getErAlAtdItem(),companyId,errorAlarmCode)
//				) ;
		for(MonthlyRecordValueImport result :results ){
			List<ItemValue> listValue = result.getItemValues();
			for (ItemValue itemValue : listValue) {
				sum+=getValue(itemValue);
			}
			
		}	
		avg = sum/lstYearMonth.size();
		BigDecimal bdAVG = new BigDecimal(avg);
		check = CompareDouble(bdAVG, erAlAtdItemConAdapterDto.getErAlAtdItem().getCompareStartValue(),
				erAlAtdItemConAdapterDto.getErAlAtdItem().getCompareEndValue(),
				erAlAtdItemConAdapterDto.getErAlAtdItem().getCompareOperator());
		return check;
	}

	@SuppressWarnings("unchecked")
	private <V> ErAlAttendanceItemCondition<V> convertAtdIemConToDomain(ErAlAtdItemConAdapterDto atdItemCon, String companyId, String errorAlarmCode) {

		ErAlAttendanceItemCondition<V> atdItemConDomain = new ErAlAttendanceItemCondition<V>(
				companyId, 
				errorAlarmCode,
				atdItemCon.getTargetNO(), 
				atdItemCon.getConditionAtr(), 
				atdItemCon.isUseAtr(),
				atdItemCon.getConditionType());
		// Set Target
		if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value) {
			atdItemConDomain.setUncountableTarget(atdItemCon.getUncountableAtdItem());
		} else {
			atdItemConDomain.setCountableTarget(atdItemCon.getCountableAddAtdItems(), atdItemCon.getCountableSubAtdItems());
		}
		// Set Compare
		if (atdItemCon.getCompareOperator() > 5) {
			if (atdItemCon.getConditionAtr() == ConditionAtr.AMOUNT_VALUE.value) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(), (V) new CheckedAmountValue(atdItemCon.getCompareStartValue().intValue()), (V) new CheckedAmountValue(atdItemCon.getCompareEndValue().intValue()));
			} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION.value) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(), (V) new CheckedTimeDuration(atdItemCon.getCompareStartValue().intValue()), (V) new CheckedTimeDuration(atdItemCon.getCompareEndValue().intValue()));
			} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(), (V) new TimeWithDayAttr(atdItemCon.getCompareStartValue().intValue()), (V) new TimeWithDayAttr(atdItemCon.getCompareEndValue().intValue()));
			} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIMES.value ) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(), (V) new CheckedTimesValue(atdItemCon.getCompareStartValue().intValue()), (V) new CheckedTimesValue(atdItemCon.getCompareEndValue().intValue()));
			} else if (atdItemCon.getConditionAtr() == ConditionAtr.DAYS.value) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(), (V) new CheckedTimesValueDay(atdItemCon.getCompareStartValue().doubleValue()), (V) new CheckedTimesValueDay(atdItemCon.getCompareEndValue().doubleValue()));
			}
		} else {
			if (atdItemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
				if (atdItemCon.getConditionAtr() == ConditionAtr.AMOUNT_VALUE.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new CheckedAmountValue(atdItemCon.getCompareStartValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new CheckedTimeDuration(atdItemCon.getCompareStartValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new TimeWithDayAttr(atdItemCon.getCompareStartValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIMES.value ) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new CheckedTimesValue(atdItemCon.getCompareStartValue().intValue()));
				} else if ( atdItemCon.getConditionAtr() == ConditionAtr.DAYS.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new CheckedTimesValueDay(atdItemCon.getCompareStartValue().doubleValue()));
				}
			} else {
				atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new AttendanceItemId(atdItemCon.getSingleAtdItem()));
			}
		}
		return atdItemConDomain;
	}
	
	private Double getValue(ItemValue value) {
		if(value.getValueType()==ValueType.DATE){
			return 0d;
		}
		if (value.value() == null) {
			return 0d;
		}
		else if (value.getValueType().isDouble()||value.getValueType().isInteger()) {
			return value.getValueType().isDouble() ? ((Double) value.value()) : Double.valueOf((Integer) value.value());
		}
		return 0d;
	}
	
	private boolean checkFixedValue(Integer target,SingleValueCompareType compareType, Integer compare) {
		if(target == null) {
			return false;
		}
		switch (compareType) {
		case EQUAL:
			return target.compareTo(compare) == 0;
		case GREATER_OR_EQUAL:
			return target.compareTo(compare) >= 0;
		case GREATER_THAN:
			return target.compareTo(compare) > 0;
		case LESS_OR_EQUAL:
			return target.compareTo(compare) <= 0;
		case LESS_THAN:
			return target.compareTo(compare) < 0;
		case NOT_EQUAL:
			return target.compareTo(compare) != 0;
		default:
			throw new RuntimeException("invalid compareOpertor: " + compareType);
		}
	}
	private boolean CompareDouble(BigDecimal value, BigDecimal valueAgreementStart, BigDecimal valueAgreementEnd,
			int compare) {
		boolean check = false;
		switch (compare) {
		/* 範囲の間（境界値を含まない）（＜＞） */
		case 6:
			if (value.compareTo(valueAgreementStart) > 0 && value.compareTo(valueAgreementEnd) < 0) {
				check = true;
			}
			break;
		/* 範囲の間（境界値を含む）（≦≧） */
		case 7:
			if (value.compareTo(valueAgreementStart) >= 0 && value.compareTo(valueAgreementEnd) <= 0) {
				check = true;
			}
			break;
		/* 範囲の外（境界値を含まない）（＞＜） */
		case 8:
			if (value.compareTo(valueAgreementStart) < 0 || value.compareTo(valueAgreementEnd) > 0) {
				check = true;
			}
			break;
		/* 範囲の外（境界値を含む）（≧≦） */
		default:
			if (value.compareTo(valueAgreementStart) <= 0 || value.compareTo(valueAgreementEnd) >= 0) {
				check = true;
			}
			break;
		}
		return check;
	}
	
}

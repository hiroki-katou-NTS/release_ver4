package nts.uk.ctx.at.record.pubimp.workrecord.erroralarm.condition.monthlycheckcondition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.AttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlConditionsAttendanceItem;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.SingleValueCompareType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.AgreementCheckCon36;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.Check36AgreementValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.Checking36AgreementCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.CheckingPublicHolidayService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.ErrorAlarmRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.PerTimeMonActualResultService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.SpecHolidayCheckCon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.AttendanceItemId;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValueDay;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.AttendanceItemConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.ErAlAtdItemConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.AgreementCheckCon36PubEx;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.CheckResultMonthlyPub;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.SpecHolidayCheckConPubEx;
import nts.uk.ctx.at.shared.dom.common.days.MonthlyDays;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class CheckResultMonthlyPubImpl implements CheckResultMonthlyPub {

	@Inject
	private CheckingPublicHolidayService checkingPublicHoliday;
	
	@Inject
	private Checking36AgreementCondition checking36AgreementCondition;
	
	@Inject
	private PerTimeMonActualResultService perTimeMonActualResult;
	
	@Override
	public boolean checkPublicHoliday(String companyId, String employeeCd, String employeeId, String workplaceId,
			boolean isManageComPublicHd, YearMonth yearMonth, SpecHolidayCheckConPubEx specHolidayCheckCon) {
		boolean check = checkingPublicHoliday.checkPublicHoliday(
				companyId, 
				employeeCd, 
				employeeId, 
				workplaceId, 
				isManageComPublicHd, 
				yearMonth, 
				convertToSpecHolidayCheckConDto(specHolidayCheckCon));
		return check;
	}

	@Override
	public Check36AgreementValue check36AgreementCondition(String employeeId,YearMonth yearMonth,int closureID,ClosureDate closureDate, AgreementCheckCon36PubEx agreementCheckCon36) {
		Check36AgreementValue check36AgreementValue =  checking36AgreementCondition.check36AgreementCondition(
				employeeId,
				yearMonth, 
				closureID, 
				closureDate,
				convertToAgreementCheckCon36AdapterPubDto(agreementCheckCon36));
				
		return check36AgreementValue;
	}

	@Override
	public boolean checkPerTimeMonActualResult(YearMonth yearMonth, int closureID, ClosureDate closureDate,
			String employeeID, AttendanceItemConditionPubExport attendanceItemCondition) {
		boolean check = perTimeMonActualResult.checkPerTimeMonActualResult(
				yearMonth, 
				closureID, 
				closureDate, 
				employeeID, 
				convertToExport(attendanceItemCondition));
		return check;
	}
	
	private SpecHolidayCheckCon convertToSpecHolidayCheckConDto(SpecHolidayCheckConPubEx dto) {
		return new SpecHolidayCheckCon(
				dto.getErrorAlarmCheckID(), dto.getCompareOperator(), 
				new MonthlyDays(dto.getNumberDayDiffHoliday1().doubleValue()),
				dto.getNumberDayDiffHoliday2() == null ? null : new MonthlyDays(dto.getNumberDayDiffHoliday2().doubleValue()));

	}
	
	private AgreementCheckCon36 convertToAgreementCheckCon36AdapterPubDto(AgreementCheckCon36PubEx dto) {
		return new AgreementCheckCon36(dto.getErrorAlarmCheckID(), EnumAdaptor.valueOf(dto.getClassification(), ErrorAlarmRecord.class), EnumAdaptor.valueOf(dto.getCompareOperator(), SingleValueCompareType.class), dto.getEralBeforeTime());
	}
	
	private AttendanceItemCondition convertToExport(AttendanceItemConditionPubExport toDto) {
		String companyId = "";
		String errorAlarmCode = "";
		AttendanceItemCondition attendanceItemCon = null;
		if (toDto != null) {
			List<ErAlAttendanceItemCondition<?>> conditionsGroup1 = toDto.getGroup1().getLstErAlAtdItemCon()
					.stream().filter(item -> item.getCompareStartValue() != null)
					.map(atdItemCon -> convertAtdIemConToDomain(atdItemCon, companyId, errorAlarmCode)).collect(Collectors.toList());
			List<ErAlAttendanceItemCondition<?>> conditionsGroup2 = null;
			if(toDto.getGroup2()!=null) {
				conditionsGroup2 = toDto.getGroup2().getLstErAlAtdItemCon().stream().filter(item -> item.getCompareStartValue() != null)
					.map(atdItemCon -> convertAtdIemConToDomain(atdItemCon, companyId, errorAlarmCode)).collect(Collectors.toList());
			}
			attendanceItemCon = AttendanceItemCondition.init(toDto.getOperatorBetweenGroups(), toDto.isGroup2UseAtr());
			attendanceItemCon.setGroup1(setErAlConditionsAttendanceItem(toDto.getGroup1().getConditionOperator(), conditionsGroup1));
			if(toDto.getGroup2()!=null) {
				attendanceItemCon.setGroup2(setErAlConditionsAttendanceItem(toDto.getGroup2().getConditionOperator(), conditionsGroup2));
			}
		}
		
		return  attendanceItemCon;
		
	}
	
	@SuppressWarnings("unchecked")
	private <V> ErAlAttendanceItemCondition<V> convertAtdIemConToDomain(ErAlAtdItemConditionPubExport atdItemCon, String companyId, String errorAlarmCode) {

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
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIMES.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new CheckedTimesValue(atdItemCon.getCompareStartValue().intValue()));
				}else if (atdItemCon.getConditionAtr() == ConditionAtr.DAYS.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new CheckedTimesValueDay(atdItemCon.getCompareStartValue().doubleValue()));
				}
			} else {
				atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(), (V) new AttendanceItemId(atdItemCon.getSingleAtdItem()));
			}
		}
		return atdItemConDomain;
	}
	
	private ErAlConditionsAttendanceItem setErAlConditionsAttendanceItem(int conditionOperator, List<ErAlAttendanceItemCondition<?>> conditions) {
		ErAlConditionsAttendanceItem group = ErAlConditionsAttendanceItem.init(conditionOperator);
		group.addAtdItemConditions(conditions);

		return group;

	}
	//Hoidd No.257
	@Override
	public Map<String, Integer> checkPerTimeMonActualResult(YearMonth yearMonth, String employeeID, AttendanceItemConditionPubExport attendanceItemCondition) {
		Map<String, Integer> result = perTimeMonActualResult.checkPerTimeMonActualResult(
				yearMonth, 
				employeeID, 
				convertToExport(attendanceItemCondition)
				);
		return result;
	}
	
	//Hoidd No.257
	@Override
	public Map<String, Map<YearMonth,Map<String,Integer>>> checkPerTimeMonActualResult(YearMonthPeriod yearMonth, List<String> employeeID, Map<String, AttendanceItemConditionPubExport> attendanceItemCondition) {
		Map<String,Map<YearMonth,Map<String,Integer>>> result = perTimeMonActualResult.checkPerTimeMonActualResult(yearMonth,  employeeID, 
				attendanceItemCondition.entrySet().stream().collect(Collectors.toMap(c -> c.getKey(), c -> convertToExport(c.getValue()))));
		return result;
	}
}

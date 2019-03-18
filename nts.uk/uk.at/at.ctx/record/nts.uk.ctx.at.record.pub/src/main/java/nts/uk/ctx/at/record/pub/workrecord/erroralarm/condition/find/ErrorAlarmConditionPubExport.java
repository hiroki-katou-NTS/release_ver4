package nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlConditionsAttendanceItem;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.PlanActualWorkTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.SingleWorkTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.PlanActualWorkType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.SingleWorkType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ErrorAlarmConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.AttendanceItemId;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValueDay;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Getter
@NoArgsConstructor
public class ErrorAlarmConditionPubExport {

	/* ID */
	@Setter
	private String errorAlarmCheckID;
	/* チェック条件*/
	private AlarmCheckTargetConditionPubExport alarmCheckTargetCondition;
	/* 勤務種類の条件 */
	private WorkTypeConditionPubExport workTypeCondition;
	/* 勤怠項目の条件 */
	private AttendanceItemConditionPubExport attendanceItemCondition;
	/* 就業時間帯の条件 */
	private WorkTimeConditionPubExport workTimeCondition;
	/* 表示メッセージ */
	private String displayMessage;

	/* 連続期間 */
	private int continuousPeriod;
	
	public ErrorAlarmConditionPubExport(String errorAlarmCheckID,
			AlarmCheckTargetConditionPubExport alarmCheckTargetCondition,
			WorkTypeConditionPubExport workTypeCondition, 
			AttendanceItemConditionPubExport attendanceItemCondition,
			WorkTimeConditionPubExport workTimeCondition,
			String displayMessage,
			int continuousPeriod
			) {
		this.errorAlarmCheckID = errorAlarmCheckID;
		this.alarmCheckTargetCondition = alarmCheckTargetCondition;
		this.workTypeCondition = workTypeCondition;
		this.attendanceItemCondition = attendanceItemCondition;
		this.workTimeCondition = workTimeCondition;
		this.displayMessage = displayMessage;
		this.continuousPeriod = continuousPeriod;
	}
	
	public ErrorAlarmCondition toConditionDomain() {
		/* EA is worng, don't need these fields (companyId and errorAlarmCode */
		String companyId = "";
		String errorAlarmCode = "";
		ErrorAlarmCondition condition = ErrorAlarmCondition.init();
		condition.setCheckId(this.errorAlarmCheckID);
		condition.setDisplayMessage(this.displayMessage);
		condition.setContinuousPeriod(this.continuousPeriod);
		//if (fixedAtr != 1) {
			// Set AlCheckTargetCondition
			condition.createAlCheckTargetCondition(this.alarmCheckTargetCondition.isFilterByBusinessType(),
					this.alarmCheckTargetCondition.isFilterByJobTitle(), this.alarmCheckTargetCondition.isFilterByEmployment(),
					this.alarmCheckTargetCondition.isFilterByClassification(), this.alarmCheckTargetCondition.getLstBusinessType(),
					this.alarmCheckTargetCondition.getLstJobTitle(), this.alarmCheckTargetCondition.getLstEmployment(),
					this.alarmCheckTargetCondition.getLstClassification());
			// Set WorkTypeCondition
			condition.createWorkTypeCondition(this.workTypeCondition.isUseAtr(),
					this.workTypeCondition.getComparePlanAndActual());
			
			if (this.workTypeCondition.getComparePlanAndActual() != FilterByCompare.EXTRACT_SAME.value) {
				condition.setWorkTypePlan(this.workTypeCondition.isPlanFilterAtr(), this.workTypeCondition.getPlanLstWorkType());
				condition.setWorkTypeActual(this.workTypeCondition.isActualFilterAtr(),
						this.workTypeCondition.getActualLstWorkType());
				condition.chooseWorkTypeOperator(0); //set deault value
			} else {
				condition.setWorkTypeSingle(this.workTypeCondition.isPlanFilterAtr(),
						this.workTypeCondition.getPlanLstWorkType());
			}
			
			// Set WorkTimeCondtion
			condition.createWorkTimeCondition(this.workTimeCondition.isUseAtr(),
					this.workTimeCondition.getComparePlanAndActual());
			if (this.workTimeCondition.getComparePlanAndActual() != FilterByCompare.EXTRACT_SAME.value) {
				condition.setWorkTimePlan(this.workTimeCondition.isPlanFilterAtr(), this.workTimeCondition.getPlanLstWorkTime());
				condition.setWorkTimeActual(this.workTimeCondition.isActualFilterAtr(),
						this.workTimeCondition.getActualLstWorkTime());
				condition.chooseWorkTimeOperator(0); //set deault value
			} else {
				condition.setWorkTimeSingle(this.workTimeCondition.isPlanFilterAtr(),
						this.workTimeCondition.getPlanLstWorkTime());
			}
			// Set AttendanceItemCondition
			
			List<ErAlAttendanceItemCondition<?>> conditionsGroup1 = this.attendanceItemCondition.getGroup1().getLstErAlAtdItemCon().stream()
					.filter(item -> item.getCompareStartValue() != null)
					.map(atdItemCon -> convertAtdIemConToDomain(atdItemCon, companyId, errorAlarmCode)).collect(Collectors.toList());
			List<ErAlAttendanceItemCondition<?>> conditionsGroup2 = this.attendanceItemCondition.getGroup2().getLstErAlAtdItemCon().stream()
					.filter(item -> item.getCompareStartValue() != null)
					.map(atdItemCon -> convertAtdIemConToDomain(atdItemCon, companyId, errorAlarmCode)).collect(Collectors.toList());
			
			condition.createAttendanceItemCondition(this.attendanceItemCondition.getOperatorBetweenGroups(),
						this.attendanceItemCondition.isGroup2UseAtr())
					.setAttendanceItemConditionGroup1(this.attendanceItemCondition.getGroup1().getConditionOperator(), conditionsGroup1)
					.setAttendanceItemConditionGroup2(this.attendanceItemCondition.getGroup2().getConditionOperator(), conditionsGroup2);
		//}
		return condition;
	}
	
	@SuppressWarnings("unchecked")
	private <V> ErAlAttendanceItemCondition<V> convertAtdIemConToDomain(
			ErAlAtdItemConditionPubExport atdItemCon, String companyId, String errorAlarmCode) {

		ErAlAttendanceItemCondition<V> atdItemConDomain = new ErAlAttendanceItemCondition<V>(companyId, errorAlarmCode,
				atdItemCon.getTargetNO(), atdItemCon.getConditionAtr(), atdItemCon.isUseAtr(),
				atdItemCon.getConditionType());
		// Set Target
		if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value || atdItemCon.getConditionType() == ErrorAlarmConditionType.INPUT_CHECK.value) {
			atdItemConDomain.setUncountableTarget(atdItemCon.getUncountableAtdItem());
		} else {
			atdItemConDomain.setCountableTarget(atdItemCon.getCountableAddAtdItems(),
					atdItemCon.getCountableSubAtdItems());
		}
		// Set Compare
		if (atdItemCon.getConditionType() < 2) {
			if (atdItemCon.getCompareOperator() > 5) {
				if (atdItemCon.getConditionAtr() == ConditionAtr.AMOUNT_VALUE.value) {
					atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
							(V) new CheckedAmountValue(atdItemCon.getCompareStartValue().intValue()),
							(V)new CheckedAmountValue(atdItemCon.getCompareEndValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION.value) {
					atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
							(V)new CheckedTimeDuration(atdItemCon.getCompareStartValue().intValue()),
							(V)new CheckedTimeDuration(atdItemCon.getCompareEndValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value) {
					atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
							(V)new TimeWithDayAttr(atdItemCon.getCompareStartValue().intValue()),
							(V)new TimeWithDayAttr(atdItemCon.getCompareEndValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIMES.value) {
					atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
							(V)new CheckedTimesValue(atdItemCon.getCompareStartValue().intValue()),
							(V)new CheckedTimesValue(atdItemCon.getCompareEndValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.DAYS.value) {
					atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
							(V)new CheckedTimesValueDay(atdItemCon.getCompareStartValue().doubleValue()),
							(V)new CheckedTimesValueDay(atdItemCon.getCompareEndValue().doubleValue()));
				}
			} else {
				if (atdItemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
					if (atdItemCon.getConditionAtr() == ConditionAtr.AMOUNT_VALUE.value) {
						atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
								atdItemCon.getConditionType(),
								(V)new CheckedAmountValue(atdItemCon.getCompareStartValue().intValue()));
					} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION.value) {
						atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
								atdItemCon.getConditionType(),
								(V)new CheckedTimeDuration(atdItemCon.getCompareStartValue().intValue()));
					} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value) {
						atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
								atdItemCon.getConditionType(),
								(V)new TimeWithDayAttr(atdItemCon.getCompareStartValue().intValue()));
					} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIMES.value) {
						atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
								atdItemCon.getConditionType(),
								(V)new CheckedTimesValue(atdItemCon.getCompareStartValue().intValue()));
					} else if (atdItemCon.getConditionAtr() == ConditionAtr.DAYS.value) {
						atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
								atdItemCon.getConditionType(),
								(V)new CheckedTimesValueDay(atdItemCon.getCompareStartValue().doubleValue()));
					}
				} else {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(),
							(V) new AttendanceItemId(atdItemCon.getSingleAtdItem()));
				}
			}
		} else {
			atdItemConDomain.setInputCheck(atdItemCon.getInputCheckCondition().intValue());
		}
		return atdItemConDomain;
	}

	/**
	 * Build DTO from Dom
	 * @param conditionDomain
	 */
	public void convertFromDomain(ErrorAlarmCondition conditionDomain) {
		
		//if (!domain.getFixedAtr()) {
			// Set AlarmCheckTargetConditionDto
		this.errorAlarmCheckID = conditionDomain.getErrorAlarmCheckID();
		this.displayMessage = conditionDomain.getDisplayMessage().v();
		this.continuousPeriod = conditionDomain.getContinuousPeriod().v();
		this.alarmCheckTargetCondition = new AlarmCheckTargetConditionPubExport(
					conditionDomain.getCheckTargetCondtion().getFilterByBusinessType(),
					conditionDomain.getCheckTargetCondtion().getFilterByJobTitle(),
					conditionDomain.getCheckTargetCondtion().getFilterByEmployment(),
					conditionDomain.getCheckTargetCondtion().getFilterByClassification(),
					conditionDomain.getCheckTargetCondtion().getLstBusinessTypeCode().stream()
							.map(lstCode -> lstCode.v()).collect(Collectors.toList()),
					conditionDomain.getCheckTargetCondtion().getLstJobTitleId(),
					conditionDomain.getCheckTargetCondtion().getLstEmploymentCode().stream()
							.map(lstCode -> lstCode.v()).collect(Collectors.toList()),
					conditionDomain.getCheckTargetCondtion().getLstClassificationCode().stream()
							.map(lstCode -> lstCode.v()).collect(Collectors.toList()));
			this.workTypeCondition = new WorkTypeConditionPubExport();
			this.workTimeCondition = new WorkTimeConditionPubExport();
			// Set WorkTypeConditionDto
			this.workTypeCondition.setComparePlanAndActual(conditionDomain.getWorkTypeCondition().getComparePlanAndActual().value);
			if (conditionDomain.getWorkTypeCondition()
					.getComparePlanAndActual() != FilterByCompare.EXTRACT_SAME) {
				PlanActualWorkType wtypeConditionDomain = (PlanActualWorkType) conditionDomain
						.getWorkTypeCondition();
				this.workTypeCondition.setActualFilterAtr(wtypeConditionDomain.getWorkTypeActual().isUse());
				this.workTypeCondition.setActualLstWorkType(wtypeConditionDomain.getWorkTypeActual().getLstWorkType()
						.stream().map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
				this.workTypeCondition.setUseAtr(wtypeConditionDomain.isUse());
				this.workTypeCondition.setPlanFilterAtr(wtypeConditionDomain.getWorkTypePlan().isUse());
				this.workTypeCondition.setPlanLstWorkType(wtypeConditionDomain.getWorkTypePlan().getLstWorkType().stream()
						.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
				this.workTypeCondition.setComparePlanAndActual(wtypeConditionDomain.getComparePlanAndActual().value);
						
			} else {
				SingleWorkType wtypeConditionDomain = (SingleWorkType) conditionDomain
						.getWorkTypeCondition();
				this.workTypeCondition.setUseAtr(wtypeConditionDomain.isUse());
				this.workTypeCondition.setPlanFilterAtr(wtypeConditionDomain.getTargetWorkType().isUse());
				this.workTypeCondition.setPlanLstWorkType(wtypeConditionDomain.getTargetWorkType().getLstWorkType().stream()
						.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
			}
			//errorAlarmWorkRecordDto.setWorkTypeCondition(wtypeConditionDto);
			// Set WorkTimeConditionDto
			this.workTimeCondition.setComparePlanAndActual(conditionDomain.getWorkTimeCondition().getComparePlanAndActual().value);
			if (conditionDomain.getWorkTimeCondition()
					.getComparePlanAndActual() != FilterByCompare.EXTRACT_SAME) {
				PlanActualWorkTime wtimeConditionDomain = (PlanActualWorkTime) conditionDomain
						.getWorkTimeCondition();
				this.workTimeCondition.setActualFilterAtr(wtimeConditionDomain.getWorkTimeActual().isUse());
				this.workTimeCondition.setActualLstWorkTime(wtimeConditionDomain.getWorkTimeActual().getLstWorkTime()
						.stream().map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
				this.workTimeCondition.setUseAtr(wtimeConditionDomain.isUse());
				this.workTimeCondition.setPlanFilterAtr(wtimeConditionDomain.getWorkTimePlan().isUse());
				this.workTimeCondition.setPlanLstWorkTime(wtimeConditionDomain.getWorkTimePlan().getLstWorkTime().stream()
						.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
			} else {
				SingleWorkTime wtimeConditionDomain = (SingleWorkTime) conditionDomain
						.getWorkTimeCondition();
				this.workTimeCondition.setUseAtr(wtimeConditionDomain.isUse());
				this.workTimeCondition.setPlanFilterAtr(wtimeConditionDomain.getTargetWorkTime().isUse());
				this.workTimeCondition.setPlanLstWorkTime(wtimeConditionDomain.getTargetWorkTime().getLstWorkTime().stream()
						.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
			}
			//errorAlarmWorkRecordDto.setWorkTimeCondition(wtimeConditionDto);
			// Set ErAlAtdItemConditionDto
			
			List<ErAlAtdItemConditionPubExport> erAlAtdItemConditionGroup2 = new ArrayList<>();
			
			//errorAlarmWorkRecordDto.setErAlAtdItemConditionGroup1(erAlAtdItemConditionGroup1);
			List<ErAlAttendanceItemCondition<?>> atdItemConditionDomain2 = conditionDomain
					.getAtdItemCondition().getGroup2().getLstErAlAtdItemCon();
			for (ErAlAttendanceItemCondition<?> itemDomain : atdItemConditionDomain2) {
				ErAlAtdItemConditionPubExport erAlAtdItemConditionDto = convertItemDomainToDto(itemDomain);
				erAlAtdItemConditionGroup2.add(erAlAtdItemConditionDto);
			}
			ErAlConditionsAttendanceItemPubExport group1 = convertItemDomainToDto(
					conditionDomain.getAtdItemCondition().getGroup1());
			ErAlConditionsAttendanceItemPubExport group2 = convertItemDomainToDto(
					conditionDomain.getAtdItemCondition().getGroup2());
			
			this.attendanceItemCondition = new AttendanceItemConditionPubExport(
					conditionDomain.getAtdItemCondition().getOperatorBetweenGroups().value,
					group1,	group2, conditionDomain.getAtdItemCondition().isUseGroup2());
		//}
	}
	
	/**
	 * convert ErAlConditionsAttendanceItem to ErAlConditionsAttendanceItemPubExport
	 * @param erAlConditionsAttendanceItem
	 * @return
	 */
	private ErAlConditionsAttendanceItemPubExport convertItemDomainToDto(ErAlConditionsAttendanceItem erAlConditionsAttendanceItem) {

		List<ErAlAttendanceItemCondition<?>> atdItemConditionDomain = erAlConditionsAttendanceItem.getLstErAlAtdItemCon();
		List<ErAlAtdItemConditionPubExport> erAlAtdItemConditionGroup = new ArrayList<>();
		for (ErAlAttendanceItemCondition<?> itemDomain : atdItemConditionDomain) {
			ErAlAtdItemConditionPubExport erAlAtdItemConditionDto = convertItemDomainToDto(itemDomain);
			erAlAtdItemConditionGroup.add(erAlAtdItemConditionDto);
		}
		return new ErAlConditionsAttendanceItemPubExport(erAlConditionsAttendanceItem.getAtdItemConGroupId(),
				erAlConditionsAttendanceItem.getConditionOperator().value, erAlAtdItemConditionGroup);
	}
	
	/**
	 * Convert ErAlAttendanceItemCondition to ErAlAtdItemConditionPubExport
	 * @param itemDomain
	 * @return
	 */
	public static ErAlAtdItemConditionPubExport convertItemDomainToDto(ErAlAttendanceItemCondition<?> itemDomain) {
		ErAlAtdItemConditionPubExport erAlAtdItemConditionDto = new ErAlAtdItemConditionPubExport();
		erAlAtdItemConditionDto.setTargetNO(itemDomain.getTargetNO());
		erAlAtdItemConditionDto.setConditionAtr(itemDomain.getConditionAtr().value);
		erAlAtdItemConditionDto.setUseAtr(itemDomain.isUse());
		// Check Target
		// チェック対象
		if (itemDomain.getConditionAtr() == ConditionAtr.TIME_WITH_DAY || itemDomain.getType() == ErrorAlarmConditionType.INPUT_CHECK) {
			erAlAtdItemConditionDto.setUncountableAtdItem(itemDomain.getUncountableTarget().getAttendanceItem());
		} else {
			erAlAtdItemConditionDto.setCountableAddAtdItems(
					itemDomain.getCountableTarget().getAddSubAttendanceItems().getAdditionAttendanceItems());
			erAlAtdItemConditionDto.setCountableSubAtdItems(
					itemDomain.getCountableTarget().getAddSubAttendanceItems().getSubstractionAttendanceItems());
		}
		// Check Condition
		// チェック条件
		if (itemDomain.getCompareRange() != null) {
			switch (itemDomain.getConditionAtr()) {
			case AMOUNT_VALUE:
				erAlAtdItemConditionDto.setCompareStartValue(
						new BigDecimal(((CheckedAmountValue) itemDomain.getCompareRange().getStartValue()).v()));
				erAlAtdItemConditionDto.setCompareEndValue(
						new BigDecimal(((CheckedAmountValue) itemDomain.getCompareRange().getEndValue()).v()));
				break;
			case TIME_DURATION:
				erAlAtdItemConditionDto.setCompareStartValue(
						new BigDecimal(((CheckedTimeDuration) itemDomain.getCompareRange().getStartValue()).v()));
				erAlAtdItemConditionDto.setCompareEndValue(
						new BigDecimal(((CheckedTimeDuration) itemDomain.getCompareRange().getEndValue()).v()));
				break;
			case TIME_WITH_DAY:
				erAlAtdItemConditionDto.setCompareStartValue(
						new BigDecimal(((TimeWithDayAttr) itemDomain.getCompareRange().getStartValue()).v()));
				erAlAtdItemConditionDto.setCompareEndValue(
						new BigDecimal(((TimeWithDayAttr) itemDomain.getCompareRange().getEndValue()).v()));
				break;
			case TIMES:
				erAlAtdItemConditionDto.setCompareStartValue(
						new BigDecimal(((CheckedTimesValue) itemDomain.getCompareRange().getStartValue()).v()));
				erAlAtdItemConditionDto.setCompareEndValue(
						new BigDecimal(((CheckedTimesValue) itemDomain.getCompareRange().getEndValue()).v()));
				break;
			case DAYS:
				erAlAtdItemConditionDto.setCompareStartValue(
						new BigDecimal(((CheckedTimesValueDay) itemDomain.getCompareRange().getStartValue()).v()));
						/*new BigDecimal( ( Double.valueOf(itemDomain.getCompareRange().getStartValue().toString()) ).intValue()));*/
				
				erAlAtdItemConditionDto.setCompareEndValue(
						
						new BigDecimal(((CheckedTimesValueDay) itemDomain.getCompareRange().getEndValue()).v()));
						/*new BigDecimal( ( Double.valueOf(itemDomain.getCompareRange().getEndValue().toString()) ).intValue()));*/
				break;
			}
			erAlAtdItemConditionDto.setCompareOperator(itemDomain.getCompareRange().getCompareOperator().value);
		} else if (itemDomain.getCompareSingleValue() != null && itemDomain.getCompareSingleValue().getValue() !=null ) {
			if (itemDomain.getCompareSingleValue().getConditionType() == ConditionType.FIXED_VALUE) {
				switch (itemDomain.getConditionAtr()) {
				case AMOUNT_VALUE:
					erAlAtdItemConditionDto.setCompareStartValue(
							new BigDecimal(((CheckedAmountValue) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				case TIME_DURATION:
					erAlAtdItemConditionDto.setCompareStartValue(
							new BigDecimal(((CheckedTimeDuration) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				case TIME_WITH_DAY:
					erAlAtdItemConditionDto.setCompareStartValue(
							new BigDecimal(((TimeWithDayAttr) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				case TIMES:
					erAlAtdItemConditionDto.setCompareStartValue(							
							new BigDecimal(((CheckedTimesValue) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				case DAYS:
					erAlAtdItemConditionDto.setCompareStartValue(
						/*	new BigDecimal( ( Double.valueOf(itemDomain.getCompareSingleValue().getValue().toString()) ).intValue()));*/
						   new BigDecimal(((CheckedTimesValueDay) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				}
			} else {
				erAlAtdItemConditionDto.setSingleAtdItem(((AttendanceItemId) itemDomain.getCompareSingleValue().getValue()).v());
			}
			erAlAtdItemConditionDto.setConditionType(itemDomain.getCompareSingleValue().getConditionType().value);
			erAlAtdItemConditionDto.setCompareOperator(itemDomain.getCompareSingleValue().getCompareOpertor().value);
		} else if (itemDomain.getInputCheck() != null) {
			erAlAtdItemConditionDto.setInputCheckCondition(itemDomain.getInputCheck().getInputCheckCondition().value);
		}
		if (erAlAtdItemConditionDto.getCompareStartValue() == null) erAlAtdItemConditionDto.setCompareStartValue(new BigDecimal(0));
		if (erAlAtdItemConditionDto.getCompareEndValue() == null) erAlAtdItemConditionDto.setCompareEndValue(new BigDecimal(0));
		return erAlAtdItemConditionDto;
	}
	
}

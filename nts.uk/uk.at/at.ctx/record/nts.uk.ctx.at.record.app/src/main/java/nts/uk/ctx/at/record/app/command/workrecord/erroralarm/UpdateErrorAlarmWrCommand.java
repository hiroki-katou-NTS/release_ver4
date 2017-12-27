/**
 * 9:44:13 AM Jul 24, 2017
 */
package nts.uk.ctx.at.record.app.command.workrecord.erroralarm;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.app.find.workrecord.erroralarm.condition.AlarmCheckTargetConditionDto;
import nts.uk.ctx.at.record.app.find.workrecord.erroralarm.condition.ErAlAtdItemConditionDto;
import nts.uk.ctx.at.record.app.find.workrecord.erroralarm.condition.WorkTimeConditionDto;
import nts.uk.ctx.at.record.app.find.workrecord.erroralarm.condition.WorkTypeConditionDto;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValue;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * @author hungnm
 *
 */
@Getter
@Setter
public class UpdateErrorAlarmWrCommand {

	/* 会社ID */
	private String companyId;
	/* コード */
	private String code;
	/* 名称 */
	private String name;
	/* システム固定とする */
	private int fixedAtr;
	/* 使用する */
	private int useAtr;
	/* 区分 */
	private int typeAtr;
	/* 表示メッセージ */
	private String displayMessage;
	/* メッセージを太字にする */
	private int boldAtr;
	/* メッセージの色 */
	private String messageColor;
	/* エラーアラームを解除できる */
	private int cancelableAtr;
	/* エラー表示項目 */
	private int errorDisplayItem;
	/* チェック条件 */
	private AlarmCheckTargetConditionDto alCheckTargetCondition;
	/* 勤務種類の条件 */
	private WorkTypeConditionDto workTypeCondition;
	/* 就業時間帯の条件 */
	private WorkTimeConditionDto workTimeCondition;
	private int operatorBetweenPlanActual;
	private List<Integer> lstApplicationTypeCode;
	private int operatorBetweenGroups;
	private int operatorGroup1;
	private int operatorGroup2;
	private List<ErAlAtdItemConditionDto> erAlAtdItemConditionGroup1;
	private List<ErAlAtdItemConditionDto> erAlAtdItemConditionGroup2;

	public UpdateErrorAlarmWrCommand() {
		super();
	}

	public UpdateErrorAlarmWrCommand(String companyId, String code, String name, int fixedAtr, int useAtr, int typeAtr,
			String displayMessage, int boldAtr, String messageColor, int cancelableAtr, int errorDisplayItem,
			AlarmCheckTargetConditionDto alCheckTargetCondition, WorkTypeConditionDto workTypeCondition,
			WorkTimeConditionDto workTimeCondition, int operatorBetweenPlanActual, List<Integer> lstApplicationTypeCode,
			int operatorBetweenGroups, int operatorGroup1, int operatorGroup2,
			List<ErAlAtdItemConditionDto> erAlAtdItemConditionGroup1,
			List<ErAlAtdItemConditionDto> erAlAtdItemConditionGroup2) {
		super();
		this.companyId = companyId;
		this.code = code;
		this.name = name;
		this.fixedAtr = fixedAtr;
		this.useAtr = useAtr;
		this.typeAtr = typeAtr;
		this.displayMessage = displayMessage;
		this.boldAtr = boldAtr;
		this.messageColor = messageColor;
		this.cancelableAtr = cancelableAtr;
		this.errorDisplayItem = errorDisplayItem;
		this.alCheckTargetCondition = alCheckTargetCondition;
		this.workTypeCondition = workTypeCondition;
		this.workTimeCondition = workTimeCondition;
		this.operatorBetweenPlanActual = operatorBetweenPlanActual;
		this.lstApplicationTypeCode = lstApplicationTypeCode;
		this.operatorBetweenGroups = operatorBetweenGroups;
		this.operatorGroup1 = operatorGroup1;
		this.operatorGroup2 = operatorGroup2;
		this.erAlAtdItemConditionGroup1 = erAlAtdItemConditionGroup1;
		this.erAlAtdItemConditionGroup2 = erAlAtdItemConditionGroup2;
	}

	private ErAlAttendanceItemCondition<?> convertAtdIemConToDomain(ErAlAtdItemConditionDto atdItemCon) {
		ErAlAttendanceItemCondition<Object> atdItemConDomain = new ErAlAttendanceItemCondition<Object>(companyId, code,
				atdItemCon.getTargetNO(), atdItemCon.getConditionAtr(), atdItemCon.isUseAtr());
		// Set Target
		if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value) {
			atdItemConDomain.setUncountableTarget(atdItemCon.getUncountableAtdItem());
		} else {
			atdItemConDomain.setCountableTarget(atdItemCon.getCountableAddAtdItems(),
					atdItemCon.getCountableSubAtdItems());
		}
		// Set Compare
		if (atdItemCon.getCompareOperator() > 5) {
			if (atdItemCon.getConditionAtr() == ConditionAtr.AMOUNT_VALUE.value) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
						new CheckedAmountValue(atdItemCon.getCompareStartValue().intValue()),
						new CheckedAmountValue(atdItemCon.getCompareEndValue().intValue()));
			} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION.value) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
						new CheckedTimeDuration(atdItemCon.getCompareStartValue().intValue()),
						new CheckedTimeDuration(atdItemCon.getCompareEndValue().intValue()));
			} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
						new TimeWithDayAttr(atdItemCon.getCompareStartValue().intValue()),
						new TimeWithDayAttr(atdItemCon.getCompareEndValue().intValue()));
			} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIMES.value) {
				atdItemConDomain.setCompareRange(atdItemCon.getCompareOperator(),
						new CheckedTimesValue(atdItemCon.getCompareStartValue().intValue()),
						new CheckedTimesValue(atdItemCon.getCompareEndValue().intValue()));
			}
		} else {
			if (atdItemCon.getConditionType() == ConditionType.FIXED_VALUE.value) {
				if (atdItemCon.getConditionAtr() == ConditionAtr.AMOUNT_VALUE.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
							atdItemCon.getConditionType(),
							new CheckedAmountValue(atdItemCon.getCompareStartValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
							atdItemCon.getConditionType(),
							new CheckedTimeDuration(atdItemCon.getCompareStartValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
							atdItemCon.getConditionType(),
							new TimeWithDayAttr(atdItemCon.getCompareStartValue().intValue()));
				} else if (atdItemCon.getConditionAtr() == ConditionAtr.TIMES.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(),
							atdItemCon.getConditionType(),
							new CheckedTimesValue(atdItemCon.getCompareStartValue().intValue()));
				}
			} else {
				atdItemConDomain.setCompareSingleValue(atdItemCon.getCompareOperator(), atdItemCon.getConditionType(),
						atdItemCon.getSingleAtdItem());
			}
		}
		return atdItemConDomain;
	}

	public ErrorAlarmWorkRecord toDomain() {
		ErrorAlarmWorkRecord domain = ErrorAlarmWorkRecord.init(companyId, code, name, fixedAtr == 1, useAtr == 1,
				typeAtr, boldAtr == 1, messageColor, cancelableAtr == 1, new BigDecimal(errorDisplayItem),
				lstApplicationTypeCode);
		ErrorAlarmCondition condition = ErrorAlarmCondition.init();
		condition.setDisplayMessage(displayMessage);
		// Set AlCheckTargetCondition
		condition.createAlCheckTargetCondition(alCheckTargetCondition.isFilterByBusinessType(),
				alCheckTargetCondition.isFilterByJobTitle(), alCheckTargetCondition.isFilterByEmployment(),
				alCheckTargetCondition.isFilterByClassification(), alCheckTargetCondition.getLstBusinessType(),
				alCheckTargetCondition.getLstJobTitle(), alCheckTargetCondition.getLstEmployment(),
				alCheckTargetCondition.getLstClassification());
		// Set WorkTypeCondition
		condition.createWorkTypeCondition(workTypeCondition.isUseAtr(), workTypeCondition.getComparePlanAndActual());
		if (workTypeCondition.getComparePlanAndActual() != FilterByCompare.EXTRACT_SAME.value) {
			condition.setWorkTypePlan(workTypeCondition.isPlanFilterAtr(), workTypeCondition.getPlanLstWorkType());
			condition.setWorkTypeActual(workTypeCondition.isActualFilterAtr(),
					workTypeCondition.getActualLstWorkType());
			condition.chooseWorkTypeOperator(operatorBetweenPlanActual);
		} else {
			condition.setWorkTypeSingle(workTypeCondition.isPlanFilterAtr(), workTypeCondition.getPlanLstWorkType());
		}
		// Set WorkTimeCondtion
		condition.createWorkTimeCondition(workTimeCondition.isUseAtr(), workTimeCondition.getComparePlanAndActual());
		if (workTimeCondition.getComparePlanAndActual() != FilterByCompare.EXTRACT_SAME.value) {
			condition.setWorkTimePlan(workTimeCondition.isPlanFilterAtr(), workTimeCondition.getPlanLstWorkTime());
			condition.setWorkTimeActual(workTimeCondition.isActualFilterAtr(),
					workTimeCondition.getActualLstWorkTime());
			condition.chooseWorkTimeOperator(operatorBetweenPlanActual);
		} else {
			condition.setWorkTimeSingle(workTimeCondition.isPlanFilterAtr(), workTimeCondition.getPlanLstWorkTime());
		}
		// Set AttendanceItemCondition
		List<ErAlAttendanceItemCondition<?>> conditionsGroup1 = erAlAtdItemConditionGroup1.stream()
				.map(atdItemCon -> convertAtdIemConToDomain(atdItemCon)).collect(Collectors.toList());
		List<ErAlAttendanceItemCondition<?>> conditionsGroup2 = erAlAtdItemConditionGroup2.stream()
				.map(atdItemCon -> convertAtdIemConToDomain(atdItemCon)).collect(Collectors.toList());
		condition.createAttendanceItemCondition(operatorBetweenGroups)
				.setAttendanceItemConditionGroup1(operatorGroup1, conditionsGroup1)
				.setAttendanceItemConditionGroup2(operatorGroup2, conditionsGroup2);
		domain.setCondition(condition);
		return domain;
	}

}

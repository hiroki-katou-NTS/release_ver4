/**
 * 4:55:17 PM Jul 21, 2017
 */
package nts.uk.ctx.at.record.app.find.workrecord.erroralarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.app.find.workrecord.erroralarm.condition.AlarmCheckTargetConditionDto;
import nts.uk.ctx.at.record.app.find.workrecord.erroralarm.condition.ErAlAtdItemConditionDto;
import nts.uk.ctx.at.record.app.find.workrecord.erroralarm.condition.WorkTimeConditionDto;
import nts.uk.ctx.at.record.app.find.workrecord.erroralarm.condition.WorkTypeConditionDto;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.PlanActualWorkTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.SingleWorkTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.PlanActualWorkType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.SingleWorkType;
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
@Setter
@Getter
public class ErrorAlarmWorkRecordDto {

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
	
	private static ErAlAtdItemConditionDto convertItemDomainToDto(ErAlAttendanceItemCondition<?> itemDomain){
		ErAlAtdItemConditionDto erAlAtdItemConditionDto = new ErAlAtdItemConditionDto();
		erAlAtdItemConditionDto.setTargetNO(itemDomain.getTargetNO());
		erAlAtdItemConditionDto.setConditionAtr(itemDomain.getConditionAtr().value);
		erAlAtdItemConditionDto.setUseAtr(true);
		// Check Target
		// チェック対象
		if (itemDomain.getConditionAtr() == ConditionAtr.TIME_WITH_DAY) {
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
						new BigDecimal(((CheckedAmountValue) itemDomain.getCompareRange().getStartValue()).v()));
				break;
			case TIME_DURATION:
				erAlAtdItemConditionDto.setCompareStartValue(
						new BigDecimal(((CheckedTimeDuration) itemDomain.getCompareRange().getStartValue()).v()));
				erAlAtdItemConditionDto.setCompareEndValue(
						new BigDecimal(((CheckedTimeDuration) itemDomain.getCompareRange().getStartValue()).v()));
				break;
			case TIME_WITH_DAY:
				erAlAtdItemConditionDto.setCompareStartValue(
						new BigDecimal(((TimeWithDayAttr) itemDomain.getCompareRange().getStartValue()).v()));
				erAlAtdItemConditionDto.setCompareEndValue(
						new BigDecimal(((TimeWithDayAttr) itemDomain.getCompareRange().getStartValue()).v()));
				break;
			case TIMES:
				erAlAtdItemConditionDto.setCompareStartValue(
						new BigDecimal(((CheckedTimesValue) itemDomain.getCompareRange().getStartValue()).v()));
				erAlAtdItemConditionDto.setCompareEndValue(
						new BigDecimal(((CheckedTimesValue) itemDomain.getCompareRange().getStartValue()).v()));
				break;
			}
			erAlAtdItemConditionDto.setCompareOperator(itemDomain.getCompareRange().getCompareOperator().value);
		} else if (itemDomain.getCompareSingleValue() != null) {
			if (itemDomain.getCompareSingleValue().getConditionType() == ConditionType.FIXED_VALUE) {
				switch (itemDomain.getConditionAtr()) {
				case AMOUNT_VALUE:
					erAlAtdItemConditionDto.setCompareStartValue(new BigDecimal(
							((CheckedAmountValue) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				case TIME_DURATION:
					erAlAtdItemConditionDto.setCompareStartValue(new BigDecimal(
							((CheckedTimeDuration) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				case TIME_WITH_DAY:
					erAlAtdItemConditionDto.setCompareStartValue(
							new BigDecimal(((TimeWithDayAttr) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				case TIMES:
					erAlAtdItemConditionDto.setCompareStartValue(new BigDecimal(
							((CheckedTimesValue) itemDomain.getCompareSingleValue().getValue()).v()));
					break;
				}
			} else {
				erAlAtdItemConditionDto.setSingleAtdItem((Integer) itemDomain.getCompareSingleValue().getValue());
			}
			erAlAtdItemConditionDto.setConditionType(itemDomain.getCompareSingleValue().getConditionType().value);
			erAlAtdItemConditionDto.setCompareOperator(itemDomain.getCompareSingleValue().getCompareOpertor().value);
		}
		return erAlAtdItemConditionDto;
	}

	public ErrorAlarmWorkRecordDto() {
		super();
	}

	public ErrorAlarmWorkRecordDto(String companyId, String code, String name, int fixedAtr, int useAtr, int typeAtr,
			String displayMessage, int boldAtr, String messageColor, int cancelableAtr, int errorDisplayItem,
			int operatorBetweenPlanActual, List<Integer> lstApplicationTypeCode, int operatorBetweenGroups,
			int operatorGroup1, int operatorGroup2) {
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
		this.operatorBetweenPlanActual = operatorBetweenPlanActual;
		this.lstApplicationTypeCode = lstApplicationTypeCode;
		this.operatorBetweenGroups = operatorBetweenGroups;
		this.operatorGroup1 = operatorGroup1;
		this.operatorGroup2 = operatorGroup2;
	}
	
	public ErrorAlarmWorkRecordDto(String companyId, String code, String name, int fixedAtr, int useAtr, int typeAtr,
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
	
	public static ErrorAlarmWorkRecordDto fromDomain(ErrorAlarmWorkRecord domain) {
		// Create to DTO root
		ErrorAlarmWorkRecordDto errorAlarmWorkRecordDto = new ErrorAlarmWorkRecordDto(domain.getCompanyId(),
				domain.getCode().v(), domain.getName().v(), domain.getFixedAtr() ? 1 : 0, domain.getUseAtr() ? 1 : 0,
				domain.getTypeAtr().value, domain.getErrorAlarmCondition().getDisplayMessage().v(),
				domain.getMessage().getBoldAtr() ? 1 : 0, domain.getMessage().getMessageColor().v(),
				domain.getCancelableAtr() ? 1 : 0, domain.getErrorDisplayItem().intValue(), 0,
				domain.getLstApplication(),
				domain.getErrorAlarmCondition().getAtdItemCondition().getOperatorBetweenGroups().value,
				domain.getErrorAlarmCondition().getAtdItemCondition().getGroup1().getConditionOperator().value,
				domain.getErrorAlarmCondition().getAtdItemCondition().getGroup2().getConditionOperator().value);
		// Set AlarmCheckTargetConditionDto
		errorAlarmWorkRecordDto.setAlCheckTargetCondition(new AlarmCheckTargetConditionDto(
				domain.getErrorAlarmCondition().getCheckTargetCondtion().getFilterByBusinessType(),
				domain.getErrorAlarmCondition().getCheckTargetCondtion().getFilterByJobTitle(),
				domain.getErrorAlarmCondition().getCheckTargetCondtion().getFilterByEmployment(),
				domain.getErrorAlarmCondition().getCheckTargetCondtion().getFilterByClassification(),
				domain.getErrorAlarmCondition().getCheckTargetCondtion().getLstBusinessTypeCode().stream()
						.map(lstCode -> lstCode.v()).collect(Collectors.toList()),
				domain.getErrorAlarmCondition().getCheckTargetCondtion().getLstJobTitleId(),
				domain.getErrorAlarmCondition().getCheckTargetCondtion().getLstEmploymentCode().stream()
						.map(lstCode -> lstCode.v()).collect(Collectors.toList()),
				domain.getErrorAlarmCondition().getCheckTargetCondtion().getLstClassificationCode().stream()
						.map(lstCode -> lstCode.v()).collect(Collectors.toList())));
		WorkTypeConditionDto wtypeConditionDto = new WorkTypeConditionDto();
		WorkTimeConditionDto wtimeConditionDto = new WorkTimeConditionDto();
		// Set WorkTypeConditionDto
		if (domain.getErrorAlarmCondition().getWorkTypeCondition()
				.getComparePlanAndActual() == FilterByCompare.DO_NOT_COMPARE) {
			PlanActualWorkType wtypeConditionDomain = (PlanActualWorkType) domain.getErrorAlarmCondition()
					.getWorkTypeCondition();
			wtypeConditionDto.setActualFilterAtr(wtypeConditionDomain.getWorkTypeActual().getFilterAtr());
			wtypeConditionDto.setActualLstWorkType(wtypeConditionDomain.getWorkTypeActual().getLstWorkType().stream()
					.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
			wtypeConditionDto.setUseAtr(wtypeConditionDomain.getUseAtr());
			wtypeConditionDto.setPlanFilterAtr(wtypeConditionDomain.getWorkTypePlan().getFilterAtr());
			wtypeConditionDto.setPlanLstWorkType(wtypeConditionDomain.getWorkTypePlan().getLstWorkType().stream()
					.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
			errorAlarmWorkRecordDto
					.setOperatorBetweenPlanActual(wtypeConditionDomain.getOperatorBetweenPlanActual().value);
		} else {
			SingleWorkType wtypeConditionDomain = (SingleWorkType) domain.getErrorAlarmCondition()
					.getWorkTypeCondition();
			wtypeConditionDto.setUseAtr(wtypeConditionDomain.getUseAtr());
			wtypeConditionDto.setPlanFilterAtr(wtypeConditionDomain.getTargetWorkType().getFilterAtr());
			wtypeConditionDto.setPlanLstWorkType(wtypeConditionDomain.getTargetWorkType().getLstWorkType().stream()
					.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
		}
		errorAlarmWorkRecordDto.setWorkTypeCondition(wtypeConditionDto);
		// Set WorkTimeConditionDto
		if (domain.getErrorAlarmCondition().getWorkTimeCondition()
				.getComparePlanAndActual() == FilterByCompare.DO_NOT_COMPARE) {
			PlanActualWorkTime wtimeConditionDomain = (PlanActualWorkTime) domain.getErrorAlarmCondition()
					.getWorkTimeCondition();
			wtimeConditionDto.setActualFilterAtr(wtimeConditionDomain.getWorkTimeActual().getFilterAtr());
			wtimeConditionDto.setActualLstWorkTime(wtimeConditionDomain.getWorkTimeActual().getLstWorkTime().stream()
					.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
			wtimeConditionDto.setUseAtr(wtimeConditionDomain.getUseAtr());
			wtimeConditionDto.setPlanFilterAtr(wtimeConditionDomain.getWorkTimePlan().getFilterAtr());
			wtimeConditionDto.setPlanLstWorkTime(wtimeConditionDomain.getWorkTimePlan().getLstWorkTime().stream()
					.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
			errorAlarmWorkRecordDto
					.setOperatorBetweenPlanActual(((PlanActualWorkType) domain.getErrorAlarmCondition()
							.getWorkTypeCondition()).getOperatorBetweenPlanActual().value);
		} else {
			SingleWorkTime wtimeConditionDomain = (SingleWorkTime) domain.getErrorAlarmCondition()
					.getWorkTimeCondition();
			wtimeConditionDto.setUseAtr(wtimeConditionDomain.getUseAtr());
			wtimeConditionDto.setPlanFilterAtr(wtimeConditionDomain.getTargetWorkTime().getFilterAtr());
			wtimeConditionDto.setPlanLstWorkTime(wtimeConditionDomain.getTargetWorkTime().getLstWorkTime().stream()
					.map(wtypeCode -> wtypeCode.v()).collect(Collectors.toList()));
		}
		errorAlarmWorkRecordDto.setWorkTimeCondition(wtimeConditionDto);
		// Set ErAlAtdItemConditionDto
		List<ErAlAtdItemConditionDto> erAlAtdItemConditionGroup1 = new ArrayList<>();
		List<ErAlAtdItemConditionDto> erAlAtdItemConditionGroup2 = new ArrayList<>();
		List<ErAlAttendanceItemCondition<?>> atdItemConditionDomain1 = domain.getErrorAlarmCondition()
				.getAtdItemCondition().getGroup1().getLstErAlAtdItemCon();
		for (ErAlAttendanceItemCondition<?> itemDomain : atdItemConditionDomain1) {
			ErAlAtdItemConditionDto erAlAtdItemConditionDto = convertItemDomainToDto(itemDomain);
			erAlAtdItemConditionGroup1.add(erAlAtdItemConditionDto);
		}
		errorAlarmWorkRecordDto.setErAlAtdItemConditionGroup1(erAlAtdItemConditionGroup1);
		List<ErAlAttendanceItemCondition<?>> atdItemConditionDomain2 = domain.getErrorAlarmCondition()
				.getAtdItemCondition().getGroup2().getLstErAlAtdItemCon();
		for (ErAlAttendanceItemCondition<?> itemDomain : atdItemConditionDomain2) {
			ErAlAtdItemConditionDto erAlAtdItemConditionDto = convertItemDomainToDto(itemDomain);
			erAlAtdItemConditionGroup2.add(erAlAtdItemConditionDto);
		}
		errorAlarmWorkRecordDto.setErAlAtdItemConditionGroup2(erAlAtdItemConditionGroup2);
		return errorAlarmWorkRecordDto;
	}

}

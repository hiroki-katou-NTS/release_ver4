/**
 * 10:08:35 AM Nov 2, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.AttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CompareRange;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlConditionsAttendanceItem;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.PlanActualWorkTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.SingleWorkTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.WorkTimeCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.PlanActualWorkType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.SingleWorkType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.WorkTypeCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.TypeCheckWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ContinuousPeriod;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.DisplayMessage;

/**
 * @author hungnm
 *
 */
// 勤務実績のエラーアラームチェック
@Getter
public class ErrorAlarmCondition extends AggregateRoot {
	
	// Check ID
	private String errorAlarmCheckID;

	/* 表示メッセージ */
	private DisplayMessage displayMessage;

	// チェック条件 : アラームチェック対象者の条件
	private AlCheckTargetCondition checkTargetCondtion;

	// 勤務種類の条件
	private WorkTypeCondition workTypeCondition;

	// 就業時間帯の条件
	private WorkTimeCondition workTimeCondition;

	// 勤怠項目の条件
	private AttendanceItemCondition atdItemCondition;
	
	private ContinuousPeriod continuousPeriod;

	private ErrorAlarmCondition() {
		super();
		this.displayMessage = new DisplayMessage("");
	}
	
	public void referenceTo(ErrorAlarmCondition target) {
		this.errorAlarmCheckID = target.errorAlarmCheckID;

		this.displayMessage = target.displayMessage;

		this.checkTargetCondtion = target.checkTargetCondtion;

		this.workTypeCondition = target.workTypeCondition;

		this.workTimeCondition = target.workTimeCondition;

		this.atdItemCondition = target.atdItemCondition;
		
		this.continuousPeriod = target.continuousPeriod;
	}
	
	/**
	 * Init
	 * 
	 * @return instance of ErrorAlarmCondition
	 */
	public static ErrorAlarmCondition init() {
		return new ErrorAlarmCondition();
	}

	public ErrorAlarmCondition(String errorAlarmCheckID, String displayMessage) {
		this.errorAlarmCheckID = errorAlarmCheckID;
		this.displayMessage = new DisplayMessage(displayMessage);
	}
	/**
	 * Set display message
	 * 
	 * @param message
	 * @return itself
	 */
	public ErrorAlarmCondition setDisplayMessage(String message) {
		this.displayMessage = new DisplayMessage(message);
		return this;
	}

	public void clearDuplicate(){
		this.checkTargetCondtion.clearDuplicate();
		this.workTimeCondition.clearDuplicate();
		this.workTypeCondition.clearDuplicate();
	}
	
	/**
	 * 
	 * @param filterByBusinessType
	 * @param filterByJobTitle
	 * @param filterByEmployment
	 * @param filterByClassification
	 * @param lstBusinessTypeCode
	 * @param lstJobTitleId
	 * @param lstEmploymentCode
	 * @param lstClassificationCode
	 * @return itself
	 */
	public ErrorAlarmCondition createAlCheckTargetCondition(boolean filterByBusinessType, boolean filterByJobTitle,
			boolean filterByEmployment, boolean filterByClassification, List<String> lstBusinessTypeCode,
			List<String> lstJobTitleId, List<String> lstEmploymentCode, List<String> lstClassificationCode) {
		this.checkTargetCondtion = new AlCheckTargetCondition(filterByBusinessType, filterByJobTitle,
				filterByEmployment, filterByClassification, lstBusinessTypeCode, lstJobTitleId, lstEmploymentCode,
				lstClassificationCode);
		return this;
	}

	/**
	 * Set WorkTypeCondtion: PlanActualWorkType or SingleWorkType
	 * 
	 * @param useAtr
	 * @param comparePlanAndActual
	 */
	public void createWorkTypeCondition(boolean useAtr, int comparePlanAndActual) {
		if (comparePlanAndActual != FilterByCompare.EXTRACT_SAME.value) {
			this.workTypeCondition = PlanActualWorkType.init(useAtr, comparePlanAndActual);
		} else {
			this.workTypeCondition = SingleWorkType.init(useAtr, comparePlanAndActual);
		}
	}

	/**
	 * Set WorkTypePlan
	 * 
	 * @param filterAtr
	 * @param lstWorkType
	 */
	public void setWorkTypePlan(boolean filterAtr, List<String> lstWorkType) {
		((PlanActualWorkType) this.workTypeCondition).setWorkTypePlan(filterAtr, lstWorkType);
	}
	
	public void setWorkType(boolean usePlan, boolean useActual) {
		this.workTypeCondition.setupWorkType(usePlan, useActual);
	}
	
	public void setWorkTime(boolean usePlan, boolean useActual) {
		this.workTimeCondition.setupWorkTime(usePlan, useActual);
	}

	/**
	 * Set WorkTypeActual
	 * 
	 * @param filterAtr
	 * @param lstWorkType
	 */
	public void setWorkTypeActual(boolean filterAtr, List<String> lstWorkType) {
		((PlanActualWorkType) this.workTypeCondition).setworkTypeActual(filterAtr, lstWorkType);
	}

	/**
	 * 
	 * @param filterAtr
	 * @param lstWorkType
	 */
	public void setWorkTypeSingle(boolean filterAtr, List<String> lstWorkType) {
		((SingleWorkType) this.workTypeCondition).setTargetWorkType(filterAtr, lstWorkType);
	}

	public void chooseWorkTypeOperator(Integer operator) {
		this.workTypeCondition.chooseOperator(operator);
	}

	/**
	 * Set WorkTimeCondtion: PlanActualWorkTime or SingleWorkTime
	 * 
	 * @param useAtr
	 * @param comparePlanAndActual
	 */
	public void createWorkTimeCondition(boolean useAtr, int comparePlanAndActual) {
		if (comparePlanAndActual != FilterByCompare.EXTRACT_SAME.value) {
			this.workTimeCondition = PlanActualWorkTime.init(useAtr, comparePlanAndActual);
		} else {
			this.workTimeCondition = SingleWorkTime.init(useAtr, comparePlanAndActual);
		}
	}

	/**
	 * Set WorkTimePlan
	 * 
	 * @param filterAtr
	 * @param lstWorkTime
	 */
	public void setWorkTimePlan(boolean filterAtr, List<String> lstWorkTime) {
		((PlanActualWorkTime) this.workTimeCondition).setWorkTimePlan(filterAtr, lstWorkTime);
	}

	/**
	 * Set WorkTimeActual
	 * 
	 * @param filterAtr
	 * @param lstWorkTime
	 */
	public void setWorkTimeActual(boolean filterAtr, List<String> lstWorkTime) {
		((PlanActualWorkTime) this.workTimeCondition).setworkTimeActual(filterAtr, lstWorkTime);
	}

	/**
	 * 
	 * @param filterAtr
	 * @param lstWorkType
	 */
	public void setWorkTimeSingle(boolean filterAtr, List<String> lstWorkTime) {
		((SingleWorkTime) this.workTimeCondition).setTargetWorkTime(filterAtr, lstWorkTime);
	}

	public void chooseWorkTimeOperator(int operator) {
		this.workTimeCondition.chooseOperator(operator);
	}

	/**
	 * Create AttendanceItemCondition
	 * 
	 * @param operatorBetweenGroups
	 * @return itself
	 */
	public ErrorAlarmCondition createAttendanceItemCondition(int operatorBetweenGroups, boolean group2UseAtr) {
		this.atdItemCondition = AttendanceItemCondition.init(operatorBetweenGroups, group2UseAtr);
		return this;
	}

	/**
	 * Set group 1
	 * 
	 * @param conditionOperator
	 * @param conditions
	 * @return
	 */
	public ErrorAlarmCondition setAttendanceItemConditionGroup1(int conditionOperator,
			List<ErAlAttendanceItemCondition<?>> conditions) {
		ErAlConditionsAttendanceItem group = ErAlConditionsAttendanceItem.init(conditionOperator);
		group.addAtdItemConditions(conditions);
		this.atdItemCondition.setGroup1(group);
		return this;
	}

	/**
	 * Set group 2
	 * 
	 * @param conditionOperator
	 * @param conditions
	 * @return
	 */
	public ErrorAlarmCondition setAttendanceItemConditionGroup2(int conditionOperator,
			List<ErAlAttendanceItemCondition<?>> conditions) {
		ErAlConditionsAttendanceItem group = ErAlConditionsAttendanceItem.init(conditionOperator);
		group.addAtdItemConditions(conditions);
		this.atdItemCondition.setGroup2(group);
		return this;
	}
	
	public void setCheckId(String errorAlarmCheckID) {
		this.errorAlarmCheckID = errorAlarmCheckID;
	}

	public void setGroupId1(String groupId) {
		this.atdItemCondition.setGroupId1(groupId);
	}

	public void setGroupId2(String groupId) {
		this.atdItemCondition.setGroupId2(groupId);
	}
	
	public void setContinuousPeriod(int continuousPeriod){
		this.continuousPeriod = new ContinuousPeriod(continuousPeriod);
	}
	
	public boolean checkWith(WorkInfoOfDailyPerformance workInfo, Function<List<Integer>, List<Double>> getValueFromItemIds){
		/** 勤務種類をチェックする */
		// TODO: uncomment
		// if (condition.getWorkTypeCondition().isUse() &&
		// !condition.getWorkTypeCondition().checkWorkType(workInfo)) {
		WorkCheckResult  workTypeCheck = WorkCheckResult.NOT_CHECK;
		if (this.workTypeCondition != null) {
			workTypeCheck = this.workTypeCondition.checkWorkType(workInfo);
		}
		/** 就業時間帯をチェックする */
		// TODO: uncomment
		// if (condition.getWorkTimeCondition().isUse() &&
		// !condition.getWorkTimeCondition().checkWorkTime(workInfo)) {
		WorkCheckResult workTimeCheck = WorkCheckResult.NOT_CHECK;
		if (this.workTimeCondition != null) {
			workTimeCheck = this.workTimeCondition.checkWorkTime(workInfo);
		}
		/** 勤怠項目をチェックする */
		WorkCheckResult atdCheck = WorkCheckResult.NOT_CHECK;
		if(this.atdItemCondition != null) { 
			atdCheck = this.atdItemCondition.check(getValueFromItemIds);
		}
		
		return evaluate(workTypeCheck, workTimeCheck, atdCheck);
	}
	
	private boolean evaluate(WorkCheckResult workTypeCheck, WorkCheckResult workTimeCheck, WorkCheckResult atdCheck){
		List<WorkCheckResult> result = Stream.of(workTypeCheck, workTimeCheck, atdCheck).filter(c -> c != WorkCheckResult.NOT_CHECK).collect(Collectors.toList());
		if(result.isEmpty()){
			return false;
		}
		return result.stream().allMatch(c -> c == WorkCheckResult.ERROR);
	}
	
	/**
	 * Process setting data for insert, update base on checkItem
	 * @param checkItem
	 * @param errorAlarmCondition
	 */
	public void setByCheckItem(int checkItem, ErrorAlarmCondition errorAlarmCondition) {
		if (checkItem == TypeCheckWorkRecord.TIME.value
			|| checkItem == TypeCheckWorkRecord.TIMES.value
			|| checkItem == TypeCheckWorkRecord.AMOUNT_OF_MONEY.value
			|| checkItem == TypeCheckWorkRecord.TIME_OF_DAY.value) {
			this.workTypeCondition 	= errorAlarmCondition.workTypeCondition;
			this.atdItemCondition 	= errorAlarmCondition.atdItemCondition;
			this.displayMessage 	= errorAlarmCondition.displayMessage;

			processCompareRange();
		} else if (checkItem == TypeCheckWorkRecord.CONTINUOUS_TIME.value) {
			this.workTypeCondition 	= errorAlarmCondition.workTypeCondition;
			this.atdItemCondition 	= errorAlarmCondition.atdItemCondition;
			this.displayMessage 	= errorAlarmCondition.displayMessage;
			this.continuousPeriod	= errorAlarmCondition.continuousPeriod;

			processCompareRange();
		} else if (checkItem == TypeCheckWorkRecord.CONTINUOUS_WORK.value) {
			this.workTimeCondition 	= errorAlarmCondition.workTimeCondition;
			this.displayMessage 	= errorAlarmCondition.displayMessage;
			this.continuousPeriod	= errorAlarmCondition.continuousPeriod;

		} else if (checkItem == TypeCheckWorkRecord.CONTINUOUS_TIME_ZONE.value) {
			this.workTypeCondition 	= errorAlarmCondition.workTypeCondition;
			this.workTimeCondition 	= errorAlarmCondition.workTimeCondition;
			this.displayMessage 	= errorAlarmCondition.displayMessage;
			this.continuousPeriod	= errorAlarmCondition.continuousPeriod;
			
		} else if (checkItem == TypeCheckWorkRecord.CONTINUOUS_CONDITION.value) {
			this.atdItemCondition 	= errorAlarmCondition.atdItemCondition;
			this.displayMessage 	= errorAlarmCondition.displayMessage;
			//validate range
			try{
				List<ErAlAttendanceItemCondition<?>> lstErAlAttendanceItemCondition = this.atdItemCondition.getGroup1().getLstErAlAtdItemCon();
				if (lstErAlAttendanceItemCondition != null && !lstErAlAttendanceItemCondition.isEmpty()) {
					for(ErAlAttendanceItemCondition<?> data : lstErAlAttendanceItemCondition) {
						validRangeOfErAlCondition(data.getCompareRange());
					}
				}
				if (this.atdItemCondition.isUseGroup2()) {
					List<ErAlAttendanceItemCondition<?>> lstErAlAttendanceItemCondition2 = this.atdItemCondition.getGroup2().getLstErAlAtdItemCon();
					if (lstErAlAttendanceItemCondition2 != null && !lstErAlAttendanceItemCondition2.isEmpty()) {
						for(ErAlAttendanceItemCondition<?> data : lstErAlAttendanceItemCondition2) {
							validRangeOfErAlCondition(data.getCompareRange());
						}
					}
				} else {
					List<ErAlAttendanceItemCondition<?>> lstErAlAttendanceItemCondition2 = this.atdItemCondition.getGroup2().getLstErAlAtdItemCon();
					if (lstErAlAttendanceItemCondition2 != null && !lstErAlAttendanceItemCondition2.isEmpty()) {
						this.atdItemCondition.getGroup2().getLstErAlAtdItemCon().clear();
					}
				}
			}catch(Exception e) {
				throw new RuntimeErrorException(new Error(), "Data is invalid");
			}
		} else {
			throw new RuntimeErrorException(new Error(), "Item Check is invalid!");
		}
	}
	
	private void validRangeOfErAlCondition(CompareRange<?> compareRange) {
		if (compareRange == null) {
			return;
		}

		int comOper = compareRange.getCompareOperator().value;
		int minValue = Integer.valueOf(compareRange.getStartValue().toString()); 
		int maxValue = Integer.valueOf(compareRange.getEndValue().toString());
		
		boolean valid = true;
		switch (comOper) {
	        case 6: // 範囲の間（境界値を含まない）（＜＞）
	        case 8: // 範囲の外（境界値を含まない）（＞＜）
	        	valid = !(minValue >= maxValue);
	        case 7: // 範囲の間（境界値を含む）（≦≧）
	        case 9: // 範囲の外（境界値を含む）（≧≦）
	            valid = !(minValue > maxValue);
	        default:
	            break;
	    }
	    if (!valid) {
	    	throw new BusinessException("Msg_927");
	    }
	}
	/**
	 * process in case Compare Range
	 */
	private void processCompareRange() {
		try{
			List<ErAlAttendanceItemCondition<?>> lstErAlAttendanceItemCondition = this.atdItemCondition.getGroup1().getLstErAlAtdItemCon();
			if (lstErAlAttendanceItemCondition != null && !lstErAlAttendanceItemCondition.isEmpty()) {
				int len = lstErAlAttendanceItemCondition.size();
				for(int i = len - 1; i > 0; i--) {
					this.atdItemCondition.getGroup1().getLstErAlAtdItemCon().remove(i);
				}
			}
			List<ErAlAttendanceItemCondition<?>> lstErAlAttendanceItemCondition2 = this.atdItemCondition.getGroup2().getLstErAlAtdItemCon();
			if (lstErAlAttendanceItemCondition2 != null && !lstErAlAttendanceItemCondition2.isEmpty()) {
				this.atdItemCondition.getGroup2().getLstErAlAtdItemCon().clear();
			}
		}catch(Exception e) {
			throw new RuntimeErrorException(new Error(), "Data is invalid");
		}
		
		//validate data range
		if (this.atdItemCondition.getGroup1().getLstErAlAtdItemCon().isEmpty()) {
			throw new RuntimeErrorException(new Error(), "Data is invalid");
		}
		CompareRange<?> compareRange = this.atdItemCondition.getGroup1().getLstErAlAtdItemCon().get(0).getCompareRange();
		validRangeOfErAlCondition(compareRange);
	}
}

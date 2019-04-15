/**
 * 11:15:00 AM Nov 8, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkCheckResult;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.LogicalOperator;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;

/**
 * @author hungnm
 *
 */
// 就業時間帯（予実）
@Getter
public class PlanActualWorkTime extends WorkTimeCondition {

	// 予定と実績の間の演算子
	private LogicalOperator operatorBetweenPlanActual;

	// 予定
	private TargetWorkTime workTimePlan;

	// 実績
	private TargetWorkTime workTimeActual;

	/**
	 * Constructor from Superclass
	 * 
	 * @param useAtr
	 * @param comparePlanAndActual
	 */
	private PlanActualWorkTime(boolean useAtr, FilterByCompare comparePlanAndActual) {
		super(useAtr, comparePlanAndActual);
	}

	/* Initial from java type */
	public static PlanActualWorkTime init(boolean useAtr, int comparePlanAndActual) {
		return new PlanActualWorkTime(useAtr, EnumAdaptor.valueOf(comparePlanAndActual, FilterByCompare.class));
	}

	/**
	 * Set logical operator
	 * 
	 * @param: operator
	 *             0: AND 1: OR
	 * @return itself
	 */
	@Override
	public PlanActualWorkTime chooseOperator(int operator) {
		this.operatorBetweenPlanActual = EnumAdaptor.valueOf(operator, LogicalOperator.class);
		return this;
	}

	/**
	 * Set WorkTime plan
	 * 
	 * @param: filterAtr
	 * @param lstWorkTime
	 * @return itself
	 */
	public PlanActualWorkTime setWorkTimePlan(boolean filterAtr, List<String> lstWorkType) {
		this.workTimePlan = TargetWorkTime.createFromJavaType(filterAtr, lstWorkType);
		return this;
	}

	/**
	 * Set WorkTime actual
	 * 
	 * @param filterAtr
	 * @param lstWorkTime
	 * @return itself
	 */
	public PlanActualWorkTime setworkTimeActual(boolean filterAtr, List<String> lstWorkType) {
		this.workTimeActual = TargetWorkTime.createFromJavaType(filterAtr, lstWorkType);
		return this;
	}

	@Override
	public void clearDuplicate() {
		if(this.workTimePlan != null){
			this.workTimePlan.clearDuplicate();
		}
		if(this.workTimeActual != null){
			this.workTimeActual.clearDuplicate();
		}
	}
	
	@Override
	public void addWorkTime(WorkTimeCode plan, WorkTimeCode actual) {
		if(this.workTimePlan != null && plan != null){
			this.workTimePlan.getLstWorkTime().add(plan);
		}
		
		if(this.workTimeActual != null && actual != null){
			this.workTimeActual.getLstWorkTime().add(actual);
		}
	}
	
	@Override
	public void addWorkTime(List<WorkTimeCode> plan, List<WorkTimeCode> actual) {
		if(this.workTimePlan != null && plan != null){
			this.workTimePlan.getLstWorkTime().addAll(plan);
		}
		
		if(this.workTimeActual != null && actual != null){
			this.workTimeActual.getLstWorkTime().addAll(actual);
		}
	}
	
	@Override
	public void setupWorkTime(boolean usePlan, boolean useActual) { 
		this.workTimePlan = TargetWorkTime.createFromJavaType(usePlan, new ArrayList<>());
		this.workTimeActual = TargetWorkTime.createFromJavaType(useActual, new ArrayList<>());
	}
	
	@Override
	public WorkCheckResult checkWorkTime(WorkInfoOfDailyPerformance workInfo) {
		WorkCheckResult compareTypeError = WorkCheckResult.NOT_CHECK;
		if(this.getComparePlanAndActual() == FilterByCompare.EXTRACT_DIFFERENT){
			if(workInfo.getRecordInfo().getWorkTimeCode() == null && workInfo.getScheduleInfo().getWorkTimeCode() == null) {
				compareTypeError = WorkCheckResult.NOT_ERROR;
			} else if(workInfo.getRecordInfo().getWorkTimeCode() == null || workInfo.getScheduleInfo().getWorkTimeCode() == null) {
				compareTypeError = WorkCheckResult.ERROR;
			} else if(workInfo.getRecordInfo().getWorkTimeCode().equals(workInfo.getScheduleInfo().getWorkTimeCode())){
				compareTypeError = WorkCheckResult.NOT_ERROR;
			} else {
				compareTypeError = WorkCheckResult.ERROR;
			}
		}
		
		WorkCheckResult planCheck = WorkCheckResult.NOT_CHECK;
		if (this.workTimePlan != null) {
			if(this.workTimePlan.isUse() && !this.workTimePlan.getLstWorkTime().isEmpty()){
				planCheck = this.workTimePlan.contains(workInfo.getScheduleInfo().getWorkTimeCode()) 
						? WorkCheckResult.ERROR : WorkCheckResult.NOT_ERROR;
			}
		}
		WorkCheckResult actualCheck = WorkCheckResult.NOT_CHECK;
		if (this.workTimeActual != null) {
			if(this.workTimeActual.isUse() && !this.workTimeActual.getLstWorkTime().isEmpty()){
				actualCheck = this.workTimeActual.contains(workInfo.getRecordInfo().getWorkTimeCode()) 
						? WorkCheckResult.ERROR : WorkCheckResult.NOT_ERROR;
			}
		}
		
		return comparePlanAndActual(compareTypeError, 
									comparePlanAndActual(planCheck, actualCheck, this.operatorBetweenPlanActual == LogicalOperator.AND), true);
	}
	
	private WorkCheckResult comparePlanAndActual(WorkCheckResult plan, WorkCheckResult actual, boolean same){
		if(plan == WorkCheckResult.NOT_CHECK && actual == WorkCheckResult.NOT_CHECK){
			return WorkCheckResult.NOT_CHECK;
		}
		
		if(plan == WorkCheckResult.NOT_CHECK) {
			if(WorkCheckResult.ERROR == actual){
				return WorkCheckResult.ERROR;
			}
			return WorkCheckResult.NOT_ERROR;
		}
		if(actual == WorkCheckResult.NOT_CHECK) {
			if(WorkCheckResult.ERROR == plan){
				return WorkCheckResult.ERROR;
			}
			return WorkCheckResult.NOT_ERROR;
		}
		if(same){
			if(WorkCheckResult.ERROR == actual && WorkCheckResult.ERROR == plan){
				return WorkCheckResult.ERROR;
			}
			return WorkCheckResult.NOT_ERROR;
		}

		if(WorkCheckResult.ERROR == actual || WorkCheckResult.ERROR == plan){
			return WorkCheckResult.ERROR;
		}
		return WorkCheckResult.NOT_ERROR;
	}
}

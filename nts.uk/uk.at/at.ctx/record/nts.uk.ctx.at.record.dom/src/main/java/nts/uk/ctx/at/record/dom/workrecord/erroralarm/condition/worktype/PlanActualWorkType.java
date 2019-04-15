/**
 * 6:40:38 PM Nov 6, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkCheckResult;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.LogicalOperator;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * @author hungnm
 *
 */
// 勤務種類（予実）
@Getter
public class PlanActualWorkType extends WorkTypeCondition {

	// 予定と実績の間の演算子
	private LogicalOperator operatorBetweenPlanActual;

	// 予定
	private TargetWorkType workTypePlan;

	// 実績
	private TargetWorkType workTypeActual;

	/* Constructor from Superclass */
	private PlanActualWorkType(boolean useAtr, FilterByCompare comparePlanAndActual) {
		super(useAtr, comparePlanAndActual);
	}

	/* Initial from java type */
	public static PlanActualWorkType init(boolean useAtr, int comparePlanAndActual) {
		return new PlanActualWorkType(useAtr, EnumAdaptor.valueOf(comparePlanAndActual, FilterByCompare.class));
	}

	/**
	 * Set logical operator
	 * 
	 * @param: operator
	 *             0: AND 1: OR
	 * @return itself
	 */
	@Override
	public PlanActualWorkType chooseOperator(Integer operator) {
		this.operatorBetweenPlanActual = EnumAdaptor.valueOf(operator, LogicalOperator.class);
		return this;
	}

	/**
	 * Set WorkType plan
	 * 
	 * @param: filterAtr
	 * @param lstWorkType
	 * @return itself
	 */
	public PlanActualWorkType setWorkTypePlan(boolean filterAtr, List<String> lstWorkType) {
		this.workTypePlan = TargetWorkType.createFromJavaType(filterAtr, lstWorkType);
		return this;
	}

	@Override
	public void clearDuplicate() {
		if(this.workTypePlan != null){
			this.workTypePlan.clearDuplicate();
		}
		if(this.workTypeActual != null){
			this.workTypeActual.clearDuplicate();
		}
	}

	@Override
	public void addWorkType(WorkTypeCode plan, WorkTypeCode actual){ 
		if (this.workTypePlan != null && plan != null) {
			this.workTypePlan.getLstWorkType().add(plan);
		}
		if (this.workTypeActual != null && actual != null) {
			this.workTypeActual.getLstWorkType().add(actual);
		}
	}
	
	public void addWorkType(List<WorkTypeCode> plan, List<WorkTypeCode> actual){
		if (this.workTypePlan != null && plan != null) {
			this.workTypePlan.getLstWorkType().addAll(plan);
		}
		if (this.workTypeActual != null && actual != null) {
			this.workTypeActual.getLstWorkType().addAll(actual);
		}
	}

	@Override
	public void setupWorkType(boolean usePlan, boolean useActual){
		this.workTypePlan = TargetWorkType.createFromJavaType(usePlan, new ArrayList<>());
		this.workTypeActual = TargetWorkType.createFromJavaType(useActual, new ArrayList<>());
	}

	/**
	 * Set WorkType actual
	 * 
	 * @param filterAtr
	 * @param lstWorkType
	 * @return itself
	 */
	public PlanActualWorkType setworkTypeActual(boolean filterAtr, List<String> lstWorkType) {
		this.workTypeActual = TargetWorkType.createFromJavaType(filterAtr, lstWorkType);
		return this;
	}

	@Override
	public WorkCheckResult checkWorkType(WorkInfoOfDailyPerformance workInfo) {
		WorkCheckResult compareTypeError = WorkCheckResult.NOT_CHECK;
		if(this.getComparePlanAndActual() == FilterByCompare.EXTRACT_DIFFERENT){
			if(workInfo.getRecordInfo().getWorkTypeCode().equals(workInfo.getScheduleInfo().getWorkTypeCode())){
				compareTypeError = WorkCheckResult.NOT_ERROR;
			} else {
				compareTypeError = WorkCheckResult.ERROR;
			}
		}
		
		WorkCheckResult planCheck = WorkCheckResult.NOT_CHECK;
		if (this.workTypePlan != null) {
			if(this.workTypePlan.isUse() && !this.workTypePlan.getLstWorkType().isEmpty()){
				planCheck = this.workTypePlan.contains(workInfo.getScheduleInfo().getWorkTypeCode()) 
						? WorkCheckResult.ERROR : WorkCheckResult.NOT_ERROR;
			}
		}
		WorkCheckResult actualCheck = WorkCheckResult.NOT_CHECK;
		if (this.workTypeActual != null) {
			if(this.workTypeActual.isUse() && !this.workTypeActual.getLstWorkType().isEmpty()){
				actualCheck = this.workTypeActual.contains(workInfo.getRecordInfo().getWorkTypeCode()) 
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

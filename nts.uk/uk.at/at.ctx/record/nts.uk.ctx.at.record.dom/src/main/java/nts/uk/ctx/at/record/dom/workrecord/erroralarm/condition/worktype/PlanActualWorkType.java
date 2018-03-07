/**
 * 6:40:38 PM Nov 6, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype;

import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.LogicalOperator;

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
	private PlanActualWorkType(Boolean useAtr, FilterByCompare comparePlanAndActual) {
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
	public PlanActualWorkType chooseOperator(int operator) {
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
	public boolean checkWorkType(WorkInfoOfDailyPerformance workInfo) {
		if (this.getUseAtr() != null && this.getUseAtr()) {
			boolean planCheck = false;
			if (this.workTypePlan != null) {
				if (this.workTypePlan.getFilterAtr() != null && this.workTypePlan.getFilterAtr()) {
					planCheck = this.workTypePlan.getLstWorkType()
							.contains(workInfo.getScheduleWorkInformation().getWorkTypeCode());
				}
			}
			boolean actualCheck = false;
			if (this.workTypeActual != null) {
				if (this.workTypeActual.getFilterAtr() != null && this.workTypeActual.getFilterAtr()) {
					planCheck = this.workTypeActual.getLstWorkType()
							.contains(workInfo.getRecordWorkInformation().getWorkTypeCode());
				} else {
					return planCheck;
				}
			}
			if (this.operatorBetweenPlanActual != null) {
				switch (this.operatorBetweenPlanActual) {
				case AND:
					return planCheck && actualCheck;
				case OR:
					return planCheck || actualCheck;
				default:
					break;
				}
			}
		}
		return false;
	}
}

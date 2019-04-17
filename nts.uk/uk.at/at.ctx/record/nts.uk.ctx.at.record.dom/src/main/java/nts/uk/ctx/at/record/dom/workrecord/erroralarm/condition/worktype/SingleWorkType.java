/**
 * 6:48:51 PM Nov 6, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkCheckResult;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * @author hungnm
 *
 */
// 勤務種類（単一）
@Getter
public class SingleWorkType extends WorkTypeCondition {

	// 勤務種類
	private TargetWorkType targetWorkType;

	/* Constructor from Superclass */
	private SingleWorkType(boolean useAtr, FilterByCompare comparePlanAndActual) {
		super(useAtr, comparePlanAndActual);
	}

	/* Initial from java type */
	public static SingleWorkType init(boolean useAtr, int comparePlanAndActual) {
		return new SingleWorkType(useAtr, EnumAdaptor.valueOf(comparePlanAndActual, FilterByCompare.class));
	}

	/**
	 * Set WorkType target
	 * 
	 * @param filterAtr
	 * @param lstWorkType
	 * @return itself
	 */
	public SingleWorkType setTargetWorkType(boolean filterAtr, List<String> lstWorkType) {
		this.targetWorkType = TargetWorkType.createFromJavaType(filterAtr, lstWorkType);
		return this;
	}

	@Override
	public WorkCheckResult checkWorkType(WorkInfoOfDailyPerformance workInfo) {
		if (this.targetWorkType != null) {
			if(this.targetWorkType.isUse() && !this.targetWorkType.getLstWorkType().isEmpty()){
				if(workInfo.getRecordInfo().getWorkTypeCode().equals(workInfo.getScheduleInfo().getWorkTypeCode()) && 
						this.targetWorkType.contains(workInfo.getRecordInfo().getWorkTypeCode())){
					return WorkCheckResult.ERROR;
				} 
				return WorkCheckResult.NOT_ERROR;
			}
		}
		return WorkCheckResult.NOT_CHECK;
	}

	@Override
	public void clearDuplicate() {
		if (this.targetWorkType != null) {
			this.targetWorkType.clearDuplicate();
		}
	}

	@Override
	public void addWorkType(WorkTypeCode plan, WorkTypeCode actual){ 
		if (this.targetWorkType != null && plan != null) {
			this.targetWorkType.getLstWorkType().add(plan);
		}
	}
	
	public void addWorkType(List<WorkTypeCode> plan, List<WorkTypeCode> actual){
		if (this.targetWorkType != null && plan != null) {
			this.targetWorkType.getLstWorkType().addAll(plan);
		}
	}

	@Override
	public void setupWorkType(boolean usePlan, boolean useActual){
		this.targetWorkType = TargetWorkType.createFromJavaType(usePlan, new ArrayList<>());
	}
	
	@Override
	public SingleWorkType chooseOperator(Integer operator) {
		return this;
	}
}

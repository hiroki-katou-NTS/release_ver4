/**
 * 11:14:44 AM Nov 8, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkCheckResult;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;

/**
 * @author hungnm
 *
 */
// 就業時間帯（単一）
@Getter
public class SingleWorkTime extends WorkTimeCondition {

	// 就業時間帯
	private TargetWorkTime targetWorkTime;

	/**
	 * Constructor from Superclass
	 * 
	 * @param useAtr
	 * @param comparePlanAndActual
	 */
	private SingleWorkTime(boolean useAtr, FilterByCompare comparePlanAndActual) {
		super(useAtr, comparePlanAndActual);
	}

	/* Initial from java type */
	public static SingleWorkTime init(boolean useAtr, int comparePlanAndActual) {
		return new SingleWorkTime(useAtr, EnumAdaptor.valueOf(comparePlanAndActual, FilterByCompare.class));
	}

	/**
	 * Set WorkTime target
	 * 
	 * @param filterAtr
	 * @param lstWorkTime
	 * @return itself
	 */
	public SingleWorkTime setTargetWorkTime(boolean filterAtr, List<String> lstWorkType) {
		this.targetWorkTime = TargetWorkTime.createFromJavaType(filterAtr, lstWorkType);
		return this;
	}

	@Override
	public WorkCheckResult checkWorkTime(WorkInfoOfDailyPerformance workInfo) {
//		if(workInfo.getScheduleInfo().getWorkTimeCode() != null && 
//				!workInfo.getScheduleInfo().getWorkTimeCode().equals(workInfo.getRecordInfo().getWorkTimeCode())){
//			return true;
//		}
		if (this.targetWorkTime != null) {
			if(this.targetWorkTime.isUse() && !this.targetWorkTime.getLstWorkTime().isEmpty()){
				if(workInfo.getRecordInfo().getWorkTimeCode().equals(workInfo.getScheduleInfo().getWorkTimeCode()) && 
						this.targetWorkTime.contains(workInfo.getRecordInfo().getWorkTimeCode())){
					return WorkCheckResult.ERROR;
				}
				return WorkCheckResult.NOT_ERROR;
			}
		}
		return WorkCheckResult.NOT_CHECK;

	}
	
	@Override
	public void clearDuplicate() {
		if(this.targetWorkTime != null){
			this.targetWorkTime.clearDuplicate();
		}
	}
	
	@Override
	public void addWorkTime(WorkTimeCode plan, WorkTimeCode actual) {
		if(this.targetWorkTime != null && plan != null){
			this.targetWorkTime.getLstWorkTime().add(plan);
		}
	}
	
	@Override
	public void addWorkTime(List<WorkTimeCode> plan, List<WorkTimeCode> actual) {
		if(this.targetWorkTime != null && plan != null){
			this.targetWorkTime.getLstWorkTime().addAll(plan);
		}
	}
	
	@Override
	public void setupWorkTime(boolean usePlan, boolean useActual) { 
		this.targetWorkTime = TargetWorkTime.createFromJavaType(usePlan, new ArrayList<>());
	}
	
	@Override
	public SingleWorkTime chooseOperator(int operator) {
		return this;
	}
}

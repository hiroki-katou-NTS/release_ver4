/**
 * 11:14:44 AM Nov 8, 2017
 */
package nts.uk.ctx.at.shared.dom.alarmList.extractionResult.worktime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.enums.WorkCheckResult;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.enums.FilterByCompare;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.snapshot.SnapShot;
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
	public WorkCheckResult checkWorkTime(WorkInfoOfDailyPerformance workInfo, Optional<SnapShot> snapshot) {
		if (this.targetWorkTime != null) {
			
			val scheWorkTime = snapshot.flatMap(c -> c.getWorkInfo().getWorkTimeCodeNotNull()).orElse(null);
			
			if(this.targetWorkTime.isUse() && !this.targetWorkTime.getLstWorkTime().isEmpty()){
				if(workInfo.getWorkInformation().getRecordInfo().getWorkTimeCode().equals(scheWorkTime) && 
						this.targetWorkTime.contains(workInfo.getWorkInformation().getRecordInfo().getWorkTimeCode())){
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

/**
 * 11:38:58 AM Nov 2, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkCheckResult;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;

/**
 * @author hungnm
 *
 */
// 就業時間帯の条件
public class WorkTimeCondition extends DomainObject {

	// 勤務種類の条件を使用する
	private boolean useAtr;

	// 予実比較による絞り込み方法
	@Getter
	private FilterByCompare comparePlanAndActual;

	/* Constructor */
	protected WorkTimeCondition(Boolean useAtr, FilterByCompare comparePlanAndActual) {
		super();
		this.useAtr = useAtr;
		this.comparePlanAndActual = comparePlanAndActual;
	}

	/** 就業時間帯をチェックする */
	public WorkCheckResult checkWorkTime(WorkInfoOfDailyPerformance workInfo) {
		return WorkCheckResult.NOT_CHECK;
	}
	
	public boolean isUse() {
		return this.useAtr;
	}
	
	public void clearDuplicate() { }
	
	public void addWorkTime(WorkTimeCode plan, WorkTimeCode actual) { } 
	
	public void addWorkTime(List<WorkTimeCode> plan, List<WorkTimeCode> actual) { }
	
	public void setupWorkTime(boolean usePlan, boolean useActual) { }
	
	public WorkTimeCondition chooseOperator(int operator) {
		return this;
	}
}

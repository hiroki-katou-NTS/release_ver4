/**
 * 11:38:35 AM Nov 2, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkCheckResult;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * @author hungnm
 *
 */
// 勤務種類の条件
public class WorkTypeCondition extends DomainObject {

	// 勤務種類の条件を使用する
	private boolean useAtr;

	// 予実比較による絞り込み方法
	@Getter
	private FilterByCompare comparePlanAndActual;

	/* Constructor */
	protected WorkTypeCondition(boolean useAtr, FilterByCompare comparePlanAndActual) {
		super();
		this.useAtr = useAtr;
		this.comparePlanAndActual = comparePlanAndActual;
	}

	/** 勤務種類をチェックする */
	public WorkCheckResult checkWorkType(WorkInfoOfDailyPerformance workInfo) {
		return WorkCheckResult.NOT_CHECK;
	}
	
	public boolean isUse() {
		return this.useAtr;
	}
	
	public void clearDuplicate() { }
	
	public void addWorkType(WorkTypeCode plan, WorkTypeCode actual){ }
	
	public void addWorkType(List<WorkTypeCode> plan, List<WorkTypeCode> actual){ }
	
	public void setupWorkType(boolean usePlan, boolean useActual){ }
	
	public WorkTypeCondition chooseOperator(Integer operator) {
		return this;
	}
}

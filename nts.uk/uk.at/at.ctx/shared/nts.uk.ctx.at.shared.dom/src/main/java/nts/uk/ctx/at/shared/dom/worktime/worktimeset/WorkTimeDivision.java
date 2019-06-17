/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.worktimeset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.color.ColorCode;
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.shr.com.primitive.Memo;

//就業時間帯勤務区分
/**
 * The Class WorkTimeDivision.
 */
@Getter
@NoArgsConstructor
public class WorkTimeDivision extends WorkTimeDomainObject implements Cloneable{

	/** The work time daily atr. */
	// 勤務形態区分
	private WorkTimeDailyAtr workTimeDailyAtr;

	/** The work time method set. */
	// 就業時間帯の設定方法
	private WorkTimeMethodSet workTimeMethodSet;
	
	
	/**
	 * フレックス勤務or流動勤務のどちらかに該当するか判定する
	 * @return　フレックスか流動である
	 */
	public boolean isfluidorFlex() {
		return workTimeDailyAtr.isFlex() || workTimeMethodSet.isFluidWork();
	}
	
	public boolean isFlow() {
		return workTimeDailyAtr.isRegular() && workTimeMethodSet.isFluidWork();
	}

	/**
	 * Instantiates a new work time division.
	 *
	 * @param workTimeDailyAtr the work time daily atr
	 * @param workTimeMethodSet the work time method set
	 */
	public WorkTimeDivision(WorkTimeDailyAtr workTimeDailyAtr, WorkTimeMethodSet workTimeMethodSet) {
		super();
		this.workTimeDailyAtr = workTimeDailyAtr;
		this.workTimeMethodSet = workTimeMethodSet;
	}
	
	@Override
	public WorkTimeDivision clone() {
		WorkTimeDivision cloned = new WorkTimeDivision();
		try {
			cloned.workTimeDailyAtr = WorkTimeDailyAtr.valueOf(this.workTimeDailyAtr.value);
			cloned.workTimeMethodSet= WorkTimeMethodSet.valueOf(this.workTimeMethodSet.value);
		}	
		catch (Exception e){
			throw new RuntimeException("WorkTimeDivision clone error.");
		}
		return cloned;
	}
}

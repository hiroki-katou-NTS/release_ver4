package nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CumulativeAtr {
	/** 0- 累計する **/
	ACCUMULATE(0),
	/** 1- 累計しない **/
	NOT_ACCUMULATE(1);
	
	public final int value;
}

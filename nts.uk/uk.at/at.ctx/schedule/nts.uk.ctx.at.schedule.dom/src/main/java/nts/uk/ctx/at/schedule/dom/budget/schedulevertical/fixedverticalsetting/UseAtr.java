package nts.uk.ctx.at.schedule.dom.budget.schedulevertical.fixedverticalsetting;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UseAtr {
	/** 0- 利用する **/
	USE(0),
	/** 1- 利用しない **/
	DO_NOT_USE(1);
	
	public final int value;
}

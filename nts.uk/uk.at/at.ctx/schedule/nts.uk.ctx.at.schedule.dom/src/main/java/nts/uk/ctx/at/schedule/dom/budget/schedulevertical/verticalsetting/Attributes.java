package nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Attributes {
	/** 0- 金額 **/
	AMOUNT(0),
	/** 1- 時間 **/
	TIME(1),
	/** 2- 人数 **/
	NUMBER_OF_PEOPLE(2),
	/** 3- 数値 **/
	NUMBER(3),
	/** 4- 平均単価 **/
	AVERAGE_PRICE(4);
	
	public final int value;
}

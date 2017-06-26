package nts.uk.ctx.pr.core.dom.paymentdata;

import lombok.AllArgsConstructor;

/**
 * 在職区分
 * 
 * @author vunv
 *
 */
@AllArgsConstructor
public enum TenureAtr {
	/** 0:在職 */
	TENURE(0),

	/** 1:休職 */
	LEAVE_OF_ABSENCR(1),

	/** 2:休業(育児休業以外) */
	CLOSED(2),

	/** 3:育児休業 */
	CHILDCARE_LEAVE(3),

	/** 7:海外出向 */
	OVERSEAS_DEPARTURE(7),
	
	/** 8:死亡退職 */
	RETIREMENT_OF_DEATH(8),
	
	/** 9:退職 */
	RETIREMENT(9);

	public int value;

}

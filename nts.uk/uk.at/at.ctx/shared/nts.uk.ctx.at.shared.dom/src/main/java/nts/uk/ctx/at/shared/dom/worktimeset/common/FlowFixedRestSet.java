/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktimeset.common;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class FlowFixedRestSet.
 */
//流動固定休憩設定
@Getter 
public class FlowFixedRestSet extends DomainObject{
	
	/** The is refer rest time. */
	//休憩時刻がない場合はマスタから休憩時刻を参照する
	private boolean isReferRestTime;
	
	/** The use private go out rest. */
	//私用外出を休憩として扱う
	private boolean usePrivateGoOutRest;
	
	/** The use asso go out rest. */
	//組合外出を休憩として扱う
	private boolean useAssoGoOutRest;
	
	/** The calculate method. */
	//計算方法
	private FlowFixedRestCalcMethod calculateMethod;
}

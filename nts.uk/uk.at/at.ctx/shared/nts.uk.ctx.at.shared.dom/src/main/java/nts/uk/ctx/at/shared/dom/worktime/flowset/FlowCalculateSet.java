/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class FlowCalculateSet.
 */
// 流動計算設定
@Getter
public class FlowCalculateSet extends DomainObject {
	
	/** The calc start time set. */
	// 計算開始時刻を決める設定
	private PrePlanWorkTimeCalcMethod calcStartTimeSet;
}

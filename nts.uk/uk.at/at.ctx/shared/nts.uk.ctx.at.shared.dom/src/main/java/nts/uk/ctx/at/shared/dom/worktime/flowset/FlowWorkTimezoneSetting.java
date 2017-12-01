/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;

/**
 * The Class FlowWorkTimezoneSetting.
 */
//流動勤務時間帯設定
@Getter
public class FlowWorkTimezoneSetting extends DomainObject {

	/** The work time rounding. */
	//就業時間丸め
	private TimeRoundingSetting workTimeRounding;
	
	/** The OT timezone. */
	//残業時間帯
	private List<FlowOTTimezone> lstOTTimezone;
}

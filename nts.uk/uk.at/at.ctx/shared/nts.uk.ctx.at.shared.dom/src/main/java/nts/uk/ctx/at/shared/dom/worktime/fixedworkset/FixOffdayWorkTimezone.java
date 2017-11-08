/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedworkset;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class FixOffdayWorkTimezone.
 */
@Getter
// 固定勤務の休日出勤用勤務時間帯
public class FixOffdayWorkTimezone extends DomainObject {

	/** The rest timezone. */
	// 休憩時間帯
	private FixRestTimezoneSet restTimezone;

	/** The work timezone. */
	// 勤務時間帯
	private List<BreakTimezoneSet> workTimezone;

}

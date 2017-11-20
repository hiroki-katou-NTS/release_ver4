/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktimeset.fixed;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class BreakTimezoneSet.
 */
// 休出時間の時間帯設定
@Getter
public class HolidayWorkTimeSheetSet extends DomainObject {
	// TODO 時間帯
	// private timezone;

	/** The is legal holiday constraint time. */
	// 法定内休出を拘束時間として扱う
	private boolean isLegalHolidayConstraintTime;

	/** The in legal break frame no. */
	// 法定内休出枠NO
	private BreakFrameNo inLegalBreakFrameNo;

	/** The is non statutory dayoff constraint time. */
	// 法定外休出を拘束時間として扱う
	private boolean isNonStatutoryDayoffConstraintTime;

	/** The out legal break frame no. */
	// 法定外休出枠NO
	private BreakFrameNo outLegalBreakFrameNo;

	/** The is non statutory holiday constraint time. */
	// 法定外祝日を拘束時間として扱う
	private boolean isNonStatutoryHolidayConstraintTime;

	/** The out legal pub hol frame no. */
	// 法定外祝日枠NO
	private BreakFrameNo outLegalPubHolFrameNo;
}

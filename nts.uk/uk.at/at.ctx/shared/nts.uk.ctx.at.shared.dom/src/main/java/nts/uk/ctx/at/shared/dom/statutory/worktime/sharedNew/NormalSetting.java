/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * The Class NormalSetting.
 */
@Getter
public class NormalSetting extends AggregateRoot {

	/** The year. */
	// 年
	protected Year year;

	/** The statutory setting. */
	// 法定時間
	protected List<MonthlyUnit> statutorySetting;
}
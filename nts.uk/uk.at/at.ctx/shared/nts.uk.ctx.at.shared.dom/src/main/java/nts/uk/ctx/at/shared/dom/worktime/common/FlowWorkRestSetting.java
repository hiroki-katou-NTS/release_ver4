/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class FlowWorkRestSetting.
 */
//流動勤務の休憩設定
@Getter
public class FlowWorkRestSetting extends DomainObject{

	/** The common rest setting. */
	// 共通の休憩設定
	private CommonRestSetting commonRestSetting;

	/** The flow rest setting. */
	// 流動休憩設定
	private FlowWorkRestSettingDetail flowRestSetting;
}

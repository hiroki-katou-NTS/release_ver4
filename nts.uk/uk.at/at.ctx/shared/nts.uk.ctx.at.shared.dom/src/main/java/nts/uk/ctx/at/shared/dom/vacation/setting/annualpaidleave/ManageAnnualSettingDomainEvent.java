/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.layer.dom.event.DomainEvent;

/*
 * 年休管理設定．半日年休管理区分変更
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ManageAnnualSettingDomainEvent extends DomainEvent {
	/** 半日年休管理区分 */
	private boolean parameter;
}

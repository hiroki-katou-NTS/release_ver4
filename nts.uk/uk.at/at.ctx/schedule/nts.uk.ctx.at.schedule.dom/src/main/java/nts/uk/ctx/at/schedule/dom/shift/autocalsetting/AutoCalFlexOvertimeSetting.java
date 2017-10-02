/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.shift.autocalsetting;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class AutoCalFlexOvertimeSetting.
 */
// フレックス超過時間の自動計算設定

/**
 * Gets the flex ot night time.
 *
 * @return the flex ot night time
 */

/**
 * Gets the flex ot time.
 *
 * @return the flex ot time
 */
@Getter
public class AutoCalFlexOvertimeSetting extends DomainObject {

	/** The flex ot time. */
	// フレックス超過時間
	private AutoCalSetting flexOtTime;

	/**
	 * Instantiates a new auto cal flex overtime setting.
	 *
	 * @param memento the memento
	 */
	public AutoCalFlexOvertimeSetting(AutoCalFlexOvertimeSettingGetMemento memento) {
		super();
		this.flexOtTime = memento.getFlexOtTime();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(AutoCalFlexOvertimeSettingSetMemento memento) {
		memento.setFlexOtTime(this.flexOtTime);
	}

}

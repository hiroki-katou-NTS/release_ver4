/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flexset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.personallaborcondition.UseAtr;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class FlexCalcSetting.
 */
// フレックス計算設定
@Getter
@NoArgsConstructor
public class FlexCalcSetting extends WorkTimeDomainObject implements Cloneable{

	/** The remove from work time. */
	// コアタイム内の外出を就業時間から控除する
	private UseAtr removeFromWorkTime;

	/** The calculate sharing. */
	// コアタイム内外の外出を分けて計算する
	private UseAtr calculateSharing;

	/**
	 * Instantiates a new flex calc setting.
	 *
	 * @param memento the memento
	 */
	public FlexCalcSetting(FlexCalcSettingGetMemento memento) {
		this.removeFromWorkTime = memento.getRemoveFromWorkTime();
		this.calculateSharing = memento.getCalculateSharing();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlexCalcSettingSetMemento memento){
		memento.setRemoveFromWorkTime(this.removeFromWorkTime);
		memento.setCalculateSharing(this.calculateSharing);
	}
	
	@Override
	public FlexCalcSetting clone() {
		FlexCalcSetting cloned = new FlexCalcSetting();
		try {
			cloned.removeFromWorkTime = UseAtr.valueOf(this.removeFromWorkTime.value);
			cloned.calculateSharing = UseAtr.valueOf(this.calculateSharing.value);
		}
		catch (Exception e){
			throw new RuntimeException("FlexCalcSetting clone error.");
		}
		return cloned;
	}
}

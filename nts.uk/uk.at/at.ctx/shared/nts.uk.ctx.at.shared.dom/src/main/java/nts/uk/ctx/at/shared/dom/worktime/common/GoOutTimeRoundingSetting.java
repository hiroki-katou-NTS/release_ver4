/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class GoOutTimeRoundingSetting.
 */
//外出時間の丸め設定
@Getter
public class GoOutTimeRoundingSetting extends DomainObject {

	/** The rounding method. */
	//丸め方法
	private GoOutTimeRoundingMethod roundingMethod;
	
	/** The rounding setting. */
	//丸め設定
	private TimeRoundingSetting roundingSetting;
	
	/**
	 * Instantiates a new go out time rounding setting.
	 *
	 * @param roundingMethod the rounding method
	 * @param roundingSetting the rounding setting
	 */
	public GoOutTimeRoundingSetting(GoOutTimeRoundingMethod roundingMethod,
			TimeRoundingSetting roundingSetting) {
		super();
		this.roundingMethod = roundingMethod;
		this.roundingSetting = roundingSetting;
	}
	
	/**
	 * Instantiates a new go out time rounding setting.
	 *
	 * @param memento the memento
	 */
	public GoOutTimeRoundingSetting(GoOutTimeRoundingSettingGetMemento memento) {
		this.roundingMethod = memento.getRoundingMethod();
		this.roundingSetting = memento.getRoundingSetting();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(GoOutTimeRoundingSettingSetMemento memento){
		memento.setRoundingMethod(this.roundingMethod);
		memento.setRoundingSetting(this.roundingSetting);
	}
	
	/**
	 * Restore data.
	 *
	 * @param screenMode the screen mode
	 * @param oldDomain the old domain
	 */
	public void restoreData(ScreenMode screenMode, GoOutTimeRoundingSetting oldDomain) {
		// Simple mode
		if (screenMode == ScreenMode.SIMPLE) {
			this.roundingMethod = oldDomain.getRoundingMethod();
			this.roundingSetting.restoreData(oldDomain.getRoundingSetting());			
			return;
		} 
		
		// Detail mode
		switch (this.roundingMethod) {		
			case TOTAL_AND_ROUNDING:
				this.roundingSetting.restoreData(oldDomain.getRoundingSetting());
				break;
	
			case ROUNDING_AND_TOTAL:
				// Nothing change
				break;
	
			default:
				throw new RuntimeException("GoOutTimeRoundingMethod not found.");
		}
	}
}

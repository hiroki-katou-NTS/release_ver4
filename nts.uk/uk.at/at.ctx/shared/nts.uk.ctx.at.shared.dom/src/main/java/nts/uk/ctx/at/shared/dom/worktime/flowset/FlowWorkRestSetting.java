/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.CommonRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class FlowWorkRestSetting.
 */
// 流動勤務の休憩設定
@Getter
@NoArgsConstructor
public class FlowWorkRestSetting extends WorkTimeDomainObject implements Cloneable{

	/** The common rest setting. */
	// 共通の休憩設定
	private CommonRestSetting commonRestSetting;

	/** The flow rest setting. */
	// 流動休憩設定
	private FlowWorkRestSettingDetail flowRestSetting;

	/**
	 * Instantiates a new flow work rest setting.
	 *
	 * @param memento the memento
	 */
	public FlowWorkRestSetting(FlowWorkRestSettingGetMemento memento) {
		this.commonRestSetting = memento.getCommonRestSetting();
		this.flowRestSetting = memento.getFlowRestSetting();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlowWorkRestSettingSetMemento memento) {
		memento.setCommonRestSetting(this.commonRestSetting);
		memento.setFlowRestSetting(this.flowRestSetting);
	}

	public void correctData(ScreenMode screenMode, FlowWorkRestSetting restSetting,boolean fixRestTime) {
		this.flowRestSetting.correctData(screenMode,restSetting.getFlowRestSetting(),fixRestTime);
	}

	public void correctDefaultData(ScreenMode screenMode,boolean fixRestTime) {
		this.flowRestSetting.correctDefaultData(screenMode,fixRestTime);
	}
	
	@Override
	public FlowWorkRestSetting clone() {
		FlowWorkRestSetting cloned = new FlowWorkRestSetting();
		try {
			cloned.commonRestSetting = this.commonRestSetting.clone();
			cloned.flowRestSetting = this.flowRestSetting.clone();
		}
		catch (Exception e){
			throw new RuntimeException("FlowWorkRestSetting clone error.");
		}
		return cloned;
	}
}

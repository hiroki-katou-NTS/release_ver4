/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class FlowWorkRestSettingDetail.
 */
// 流動勤務の休憩設定詳細
@Getter
@NoArgsConstructor
public class FlowWorkRestSettingDetail extends WorkTimeDomainObject implements Cloneable{

	/** The flow rest setting. */
	// 流動休憩設定
	private FlowRestSet flowRestSetting;

	/** The flow fixed rest setting. */
	// 流動固定休憩設定
	private FlowFixedRestSet flowFixedRestSetting;

	/** The use plural work rest time. */
	// 複数回勤務の間を休憩時間として扱う
	private boolean usePluralWorkRestTime;

	/** The rounding break multiple work. */
	// 複数回勤務の間を休憩時間として扱う場合の丸め
	private TimeRoundingSetting roundingBreakMultipleWork;

	/**
	 * Instantiates a new flow work rest setting detail.
	 *
	 * @param memento
	 *            the memento
	 */
	public FlowWorkRestSettingDetail(FlowWorkRestSettingDetailGetMemento memento) {
		this.flowRestSetting = memento.getFlowRestSetting();
		this.flowFixedRestSetting = memento.getFlowFixedRestSetting();
		this.usePluralWorkRestTime = memento.getUsePluralWorkRestTime();
		this.roundingBreakMultipleWork = memento.getRoundingBreakMultipleWork();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(FlowWorkRestSettingDetailSetMemento memento) {
		memento.setFlowRestSetting(this.flowRestSetting);
		memento.setFlowFixedRestSetting(this.flowFixedRestSetting);
		memento.setUsePluralWorkRestTime(this.usePluralWorkRestTime);
		memento.setRoundingBreakMultipleWork(this.roundingBreakMultipleWork);
	}

	public void correctData(ScreenMode screenMode, FlowWorkRestSettingDetail flowRestSetting,boolean fixRestTime) {
		this.flowFixedRestSetting.correctData(screenMode,flowRestSetting.getFlowFixedRestSetting(),fixRestTime);
	}

	public void correctDefaultData(ScreenMode screenMode,boolean fixRestTime) {
		this.flowFixedRestSetting.correctDefaultData(screenMode,fixRestTime);
	}
	
	public FlowWorkRestSettingDetail clone() {
		FlowWorkRestSettingDetail cloned = new FlowWorkRestSettingDetail();
		try {
			cloned.flowRestSetting = this.flowRestSetting.clone();
			cloned.flowFixedRestSetting = this.flowFixedRestSetting.clone();
			cloned.usePluralWorkRestTime = this.usePluralWorkRestTime ? true : false;
			cloned.roundingBreakMultipleWork = this.roundingBreakMultipleWork.clone();
		}
		catch (Exception e){
			throw new RuntimeException("FlowWorkRestSettingDetail clone error.");
		}
		return cloned;
	}
}

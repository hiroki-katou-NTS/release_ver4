/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class FlowFixedRestSet.
 */
//流動固定休憩設定
@Getter
@NoArgsConstructor
public class FlowFixedRestSet extends WorkTimeDomainObject implements Cloneable{

	/** The calculate method. */
	// 計算方法
	private FlowFixedRestCalcMethod calculateMethod;

	/** The calculate from schedule. */
	// 予定から休憩を計算
	private ScheduleBreakCalculation calculateFromSchedule;

	/** The calculate from stamp. */
	// 打刻から休憩を計算
	private StampBreakCalculation calculateFromStamp;

	/**
	 * Instantiates a new flow fixed rest set.
	 *
	 * @param isReferRestTime
	 *            the is refer rest time
	 * @param usePrivateGoOutRest
	 *            the use private go out rest
	 * @param useAssoGoOutRest
	 *            the use asso go out rest
	 * @param calculateMethod
	 *            the calculate method
	 */
	public FlowFixedRestSet(boolean isReferRestTime, boolean usePrivateGoOutRest, boolean useAssoGoOutRest,
			FlowFixedRestCalcMethod calculateMethod) {
		super();
		this.calculateMethod = calculateMethod;
		this.calculateFromSchedule = new ScheduleBreakCalculation(isReferRestTime);
		this.calculateFromStamp = new StampBreakCalculation(usePrivateGoOutRest, useAssoGoOutRest);
	}

	/**
	 * Instantiates a new flow fixed rest set.
	 *
	 * @param memento
	 *            the memento
	 */
	public FlowFixedRestSet(FlowFixedRestSetGetMemento memento) {
		this.calculateMethod = memento.getCalculateMethod();
		this.calculateFromSchedule = memento.getCalculateFromSchedule();
		this.calculateFromStamp = memento.getCalculateFromStamp();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(FlowFixedRestSetSetMemento memento) {
		memento.setCalculateMethod(this.calculateMethod);
		memento.setCalculateFromSchedule(this.calculateFromSchedule);
		memento.setCalculateFromStamp(this.calculateFromStamp);
	}

	/**
	 * Change calc method to schedule.
	 */
	/*
	 * : 流動固定休憩の計算方法を「予定を参照する」に変更
	 */
	public void changeCalcMethodToSchedule() {
		this.calculateMethod = FlowFixedRestCalcMethod.REFER_SCHEDULE;
	}

	/**
	 * Checks if is use private go out rest.
	 *
	 * @return true, if is use private go out rest
	 */
	public boolean isUsePrivateGoOutRest() {
		return this.getCalculateFromStamp().isUsePrivateGoOutRest();
	}

	/**
	 * Checks if is use asso go out rest.
	 *
	 * @return true, if is use asso go out rest
	 */
	public boolean isUseAssoGoOutRest() {
		return this.getCalculateFromStamp().isUseAssoGoOutRest();
	}

	/**
	 * Checks if is refer rest time.
	 *
	 * @return true, if is refer rest time
	 */
	public boolean isReferRestTime() {
		return this.getCalculateFromSchedule().isReferRestTime();
	}

	public void correctData(ScreenMode screenMode, FlowFixedRestSet flowFixedRestSetting,boolean fixRestTime) {
		if (ScreenMode.DETAIL.equals(screenMode) && fixRestTime) {
			switch (this.calculateMethod) {
			case REFER_MASTER:
				this.calculateFromSchedule = flowFixedRestSetting.getCalculateFromSchedule();
				this.calculateFromStamp = flowFixedRestSetting.getCalculateFromStamp();
				break;
			case REFER_SCHEDULE:
				this.calculateFromStamp = flowFixedRestSetting.getCalculateFromStamp();
				break;
			case STAMP_WHITOUT_REFER:
				this.calculateFromSchedule = flowFixedRestSetting.getCalculateFromSchedule();
				break;
			default:
				break;
			}
		}
	}

	public void correctDefaultData(ScreenMode screenMode,boolean fixRestTime) {
		if (ScreenMode.DETAIL.equals(screenMode) && fixRestTime) {
			switch (this.calculateMethod) {
			case REFER_MASTER:
				this.calculateFromSchedule.setDefaultValue();
				this.calculateFromStamp.setDefaultValue();
				break;
			case REFER_SCHEDULE:
				this.calculateFromStamp.setDefaultValue();
				break;
			case STAMP_WHITOUT_REFER:
				this.calculateFromSchedule.setDefaultValue();
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public FlowFixedRestSet clone() {
		FlowFixedRestSet cloned = new FlowFixedRestSet();
		try {
			cloned.calculateMethod = FlowFixedRestCalcMethod.valueOf(this.calculateMethod.value);
			cloned.calculateFromSchedule = this.calculateFromSchedule.clone();
			cloned.calculateFromStamp = this.calculateFromStamp.clone();
		}
		catch (Exception e){
			throw new RuntimeException("FlowFixedRestSet clone error.");
		}
		return cloned;
	}
}

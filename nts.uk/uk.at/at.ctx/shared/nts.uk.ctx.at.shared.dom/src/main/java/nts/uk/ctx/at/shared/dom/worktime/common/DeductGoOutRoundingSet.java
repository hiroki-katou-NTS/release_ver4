/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class DeductGoOutRoundingSet.
 */
// 計上控除別外出丸め設定
@Getter
@NoArgsConstructor
public class DeductGoOutRoundingSet extends WorkTimeDomainObject implements Cloneable{

	/** The deduct time rounding setting. */
	// 控除時間の丸め設定
	private GoOutTimeRoundingSetting deductTimeRoundingSetting;

	/** The appro time rounding setting. */
	// 計上時間の丸め設定
	private GoOutTimeRoundingSetting approTimeRoundingSetting;

	/**
	 * Instantiates a new deduct go out rounding set.
	 *
	 * @param deductTimeRoundingSetting
	 *            the deduct time rounding setting
	 * @param approTimeRoundingSetting
	 *            the appro time rounding setting
	 */
	public DeductGoOutRoundingSet(GoOutTimeRoundingSetting deductTimeRoundingSetting,
			GoOutTimeRoundingSetting approTimeRoundingSetting) {
		super();
		this.deductTimeRoundingSetting = deductTimeRoundingSetting;
		this.approTimeRoundingSetting = approTimeRoundingSetting;
	}

	/**
	 * Instantiates a new deduct go out rounding set.
	 *
	 * @param memento
	 *            the memento
	 */
	public DeductGoOutRoundingSet(DeductGoOutRoundingSetGetMemento memento) {
		this.deductTimeRoundingSetting = memento.getDeductTimeRoundingSetting();
		this.approTimeRoundingSetting = memento.getApproTimeRoundingSetting();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(DeductGoOutRoundingSetSetMemento memento) {
		memento.setDeductTimeRoundingSetting(this.deductTimeRoundingSetting);
		memento.setApproTimeRoundingSetting(this.approTimeRoundingSetting);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, DeductGoOutRoundingSet oldDomain) {
		this.deductTimeRoundingSetting.correctData(screenMode, oldDomain.getDeductTimeRoundingSetting());
		this.approTimeRoundingSetting.correctData(screenMode, oldDomain.getApproTimeRoundingSetting());
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		this.deductTimeRoundingSetting.correctDefaultData(screenMode);
		this.approTimeRoundingSetting.correctDefaultData(screenMode);
	}
	
	public DeductGoOutRoundingSet clone() {
		DeductGoOutRoundingSet cloned = new DeductGoOutRoundingSet();
		try {
			cloned.deductTimeRoundingSetting = this.deductTimeRoundingSetting.clone();
			cloned.approTimeRoundingSetting = this.approTimeRoundingSetting.clone();
		}
		catch (Exception e){
			throw new RuntimeException("DeductGoOutRoundingSet clone error.");
		}
		return cloned;
	}
}

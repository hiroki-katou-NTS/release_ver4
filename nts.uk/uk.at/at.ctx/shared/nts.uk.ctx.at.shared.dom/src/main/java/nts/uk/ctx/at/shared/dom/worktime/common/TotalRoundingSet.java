/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class TotalRoundingSet.
 */
// 合計丸め設定
@Getter
@NoArgsConstructor
public class TotalRoundingSet extends WorkTimeDomainObject implements Cloneable{

	/** The set same frame rounding. */
	// 同じ枠内での丸め設定
	private GoOutTimeRoundingMethod setSameFrameRounding;

	/** The frame stradd rounding set. */
	// 枠を跨る場合の丸め設定
	private GoOutTimeRoundingMethod frameStraddRoundingSet;

	/**
	 * Instantiates a new total rounding set.
	 *
	 * @param setSameFrameRounding
	 *            the set same frame rounding
	 * @param frameStraddRoundingSet
	 *            the frame stradd rounding set
	 */
	public TotalRoundingSet(int setSameFrameRounding, int frameStraddRoundingSet) {
		super();
		this.setSameFrameRounding = GoOutTimeRoundingMethod.valueOf(setSameFrameRounding);
		this.frameStraddRoundingSet = GoOutTimeRoundingMethod.valueOf(frameStraddRoundingSet);
	}

	/**
	 * Instantiates a new total rounding set.
	 *
	 * @param setSameFrameRounding
	 *            the set same frame rounding
	 * @param frameStraddRoundingSet
	 *            the frame stradd rounding set
	 */
	public TotalRoundingSet(GoOutTimeRoundingMethod setSameFrameRounding,
			GoOutTimeRoundingMethod frameStraddRoundingSet) {
		super();
		this.setSameFrameRounding = setSameFrameRounding;
		this.frameStraddRoundingSet = frameStraddRoundingSet;
	}

	/**
	 * Instantiates a new total rounding set.
	 *
	 * @param memento
	 *            the memento
	 */
	public TotalRoundingSet(TotalRoundingSetGetMemento memento) {
		this.setSameFrameRounding = memento.getSetSameFrameRounding();
		this.frameStraddRoundingSet = memento.getFrameStraddRoundingSet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(TotalRoundingSetSetMemento memento) {
		memento.setSetSameFrameRounding(this.setSameFrameRounding);
		memento.setFrameStraddRoundingSet(this.frameStraddRoundingSet);
	}

	/**
	 * Correct default data.
	 */
	public void correctDefaultData() {
		this.setSameFrameRounding = GoOutTimeRoundingMethod.TOTAL_AND_ROUNDING;
		this.frameStraddRoundingSet = GoOutTimeRoundingMethod.TOTAL_AND_ROUNDING;
	}

	@Override
	public TotalRoundingSet clone() {
		TotalRoundingSet cloned = new TotalRoundingSet();
		try {
			cloned.setSameFrameRounding = GoOutTimeRoundingMethod.valueOf(this.setSameFrameRounding.value);
			cloned.frameStraddRoundingSet = GoOutTimeRoundingMethod.valueOf(this.frameStraddRoundingSet.value);
		}
		catch (Exception e){
			throw new RuntimeException("TotalRoundingSet clone error.");
		}
		return cloned;
	}
}

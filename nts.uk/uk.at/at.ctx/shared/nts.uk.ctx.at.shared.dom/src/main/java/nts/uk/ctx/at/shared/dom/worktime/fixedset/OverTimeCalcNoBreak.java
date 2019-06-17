/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodNoBreak;
import nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class OverTimeCalcNoBreak.
 */
@Getter
@NoArgsConstructor
// 休憩未取得時の残業計算
public class OverTimeCalcNoBreak extends WorkTimeDomainObject implements Cloneable{

	/** The calc method. */
	// 計算方法
	private CalcMethodNoBreak calcMethod;

	/** The in law OT. */
	// 法内残業
	private OTFrameNo inLawOT;

	/** The not in law OT. */
	// 法外残業
	private OTFrameNo notInLawOT;

	/**
	 * Instantiates a new over time calc no break.
	 *
	 * @param memento
	 *            the memento
	 */
	public OverTimeCalcNoBreak(OverTimeCalcNoBreakGetMemento memento) {
		this.calcMethod = memento.getCalcMethod();
		this.inLawOT = memento.getInLawOT();
		this.notInLawOT = memento.getNotInLawOT();
	}

	public OverTimeCalcNoBreak(CalcMethodNoBreak calcMethod, OTFrameNo inLawOT, OTFrameNo notInLawOT) {
		super();
		this.calcMethod = calcMethod;
		this.inLawOT = inLawOT;
		this.notInLawOT = notInLawOT;
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(OverTimeCalcNoBreakSetMemento memento) {
		memento.setCalcMethod(this.calcMethod);
		memento.setInLawOT(this.inLawOT);
		memento.setNotInLawOT(this.notInLawOT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject#validate()
	 */
	@Override
	public void validate() {

		// Msg_889
		if (CalcMethodNoBreak.CALC_AS_WORKING.equals(this.calcMethod)) {
			if (this.inLawOT == null || this.notInLawOT == null) {
				this.bundledBusinessExceptions.addMessage("Msg_889");
			}
		}

		super.validate();
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, OverTimeCalcNoBreak oldDomain) {
		if (CalcMethodNoBreak.CALC_AS_WORKING.equals(this.calcMethod)) {
			this.inLawOT = oldDomain.getInLawOT();
			this.notInLawOT = oldDomain.getNotInLawOT();
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		if (CalcMethodNoBreak.CALC_AS_WORKING.equals(this.calcMethod)) {
			this.inLawOT = new OTFrameNo(1);
			this.notInLawOT = new OTFrameNo(1);
		}
	}
	
	@Override
	public OverTimeCalcNoBreak clone() {
		OverTimeCalcNoBreak cloned = new OverTimeCalcNoBreak();
		try {
			cloned.calcMethod = CalcMethodNoBreak.valueOf(this.calcMethod.value);
			cloned.inLawOT = new OTFrameNo(this.inLawOT.v());
			cloned.notInLawOT = new OTFrameNo(this.notInLawOT.v());
		}
		catch (Exception e){
			throw new RuntimeException("OverTimeCalcNoBreak clone error.");
		}
		return cloned;
	}
}

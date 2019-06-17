/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodExceededPredAddVacation;
import nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class ExceededPredAddVacationCalc.
 */
@Getter
@NoArgsConstructor
// 休暇加算時間が所定を超過した場合の計算
public class ExceededPredAddVacationCalc extends WorkTimeDomainObject implements Cloneable{

	/** The calc method. */
	// 計算方法
	private CalcMethodExceededPredAddVacation calcMethod;

	/** The ot frame no. */
	// 残業枠
	private OTFrameNo otFrameNo;

	/**
	 * Instantiates a new exceeded pred add vacation calc.
	 *
	 * @param memento
	 *            the memento
	 */
	public ExceededPredAddVacationCalc(ExceededPredAddVacationCalcGetMemento memento) {
		this.calcMethod = memento.getCalcMethod();
		this.otFrameNo = memento.getOtFrameNo();
	}
	
	public ExceededPredAddVacationCalc(CalcMethodExceededPredAddVacation calcMethod, OTFrameNo otFrameNo) {
		super();
		this.calcMethod = calcMethod;
		this.otFrameNo = otFrameNo;
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(ExceededPredAddVacationCalcSetMemento memento) {
		memento.setCalcMethod(this.calcMethod);
		memento.setOtFrameNo(this.otFrameNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject#validate()
	 */
	@Override
	public void validate() {

		// Msg_890
		if (CalcMethodExceededPredAddVacation.CALC_AS_WORKING.equals(this.calcMethod)) {
			if (this.otFrameNo == null) {
				this.bundledBusinessExceptions.addMessage("Msg_890");
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
	public void correctData(ScreenMode screenMode, ExceededPredAddVacationCalc oldDomain) {
		if (CalcMethodExceededPredAddVacation.CALC_AS_WORKING.equals(this.calcMethod)) {
			this.otFrameNo = oldDomain.getOtFrameNo();
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		if (CalcMethodExceededPredAddVacation.CALC_AS_WORKING.equals(this.calcMethod)) {
			this.otFrameNo = new OTFrameNo(1);
		}
	}
	
	@Override
	public ExceededPredAddVacationCalc clone() {
		ExceededPredAddVacationCalc cloned = new ExceededPredAddVacationCalc();
		try {
			cloned.calcMethod = CalcMethodExceededPredAddVacation.valueOf(this.calcMethod.value);
			cloned.otFrameNo = new OTFrameNo(this.otFrameNo.v());
		}
		catch (Exception e){
			throw new RuntimeException("ExceededPredAddVacationCalc clone error.");
		}
		return cloned;
	}
}

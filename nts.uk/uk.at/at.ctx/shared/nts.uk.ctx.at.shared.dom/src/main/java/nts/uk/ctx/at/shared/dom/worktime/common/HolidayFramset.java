/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class HolidayFramset.
 */
// 臨時勤務時の休出枠設定
@Getter
@NoArgsConstructor
public class HolidayFramset extends WorkTimeDomainObject implements Cloneable{

	/** The in legal breakout frame no. */
	// 法定内休出枠NO
	private BreakoutFrameNo inLegalBreakoutFrameNo;

	/** The out legal breakout frame no. */
	// 法定外休出枠NO
	private BreakoutFrameNo outLegalBreakoutFrameNo;

	/** The out legal pub hol frame no. */
	// 法定外祝日枠NO
	private BreakoutFrameNo outLegalPubHolFrameNo;

	/**
	 * Instantiates a new holiday framset.
	 *
	 * @param inLegalBreakoutFrameNo
	 *            the in legal breakout frame no
	 * @param outLegalBreakoutFrameNo
	 *            the out legal breakout frame no
	 * @param outLegalPubHolFrameNo
	 *            the out legal pub hol frame no
	 */
	public HolidayFramset(BreakoutFrameNo inLegalBreakoutFrameNo,
			BreakoutFrameNo outLegalBreakoutFrameNo, BreakoutFrameNo outLegalPubHolFrameNo) {
		super();
		this.inLegalBreakoutFrameNo = inLegalBreakoutFrameNo;
		this.outLegalBreakoutFrameNo = outLegalBreakoutFrameNo;
		this.outLegalPubHolFrameNo = outLegalPubHolFrameNo;
	}

	/**
	 * Instantiates a new holiday framset.
	 *
	 * @param memento
	 *            the memento
	 */
	public HolidayFramset(HolidayFramsetGetMemento memento) {
		this.inLegalBreakoutFrameNo = memento.getInLegalBreakoutFrameNo();
		this.outLegalBreakoutFrameNo = memento.getInLegalBreakoutFrameNo();
		this.outLegalPubHolFrameNo = memento.getInLegalBreakoutFrameNo();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(HolidayFramsetSetMemento memento) {
		memento.setInLegalBreakoutFrameNo(this.inLegalBreakoutFrameNo);
		memento.setOutLegalBreakoutFrameNo(this.outLegalBreakoutFrameNo);
		memento.setOutLegalPubHolFrameNo(this.outLegalPubHolFrameNo);
	}

	@Override
	public HolidayFramset clone() {
		HolidayFramset cloned = new HolidayFramset();
		try {
			cloned.inLegalBreakoutFrameNo = new BreakoutFrameNo(this.inLegalBreakoutFrameNo.v());
			cloned.outLegalBreakoutFrameNo = new BreakoutFrameNo(this.outLegalBreakoutFrameNo.v());
			cloned.outLegalPubHolFrameNo = new BreakoutFrameNo(this.outLegalPubHolFrameNo.v());
		}
		catch (Exception e){
			throw new RuntimeException("HolidayFramset clone error.");
		}
		return cloned;
	}
}

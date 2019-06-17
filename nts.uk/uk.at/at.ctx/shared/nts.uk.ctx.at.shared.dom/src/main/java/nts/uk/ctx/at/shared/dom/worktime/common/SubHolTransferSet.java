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
 * The Class SubHolTransferSet.
 */
// 代休振替設定
@Getter
@NoArgsConstructor
public class SubHolTransferSet extends WorkTimeDomainObject implements Cloneable{

	/** The certain time. */
	// 一定時間
	private OneDayTime certainTime;

	/** The use division. */
	// 使用区分
	private boolean useDivision;

	/** The designated time. */
	// 指定時間
	private DesignatedTime designatedTime;

	/** The sub hol transfer set atr. */
	// 振替区分
	private SubHolTransferSetAtr subHolTransferSetAtr;

	/**
	 * Instantiates a new sub hol transfer set.
	 *
	 * @param memento
	 *            the memento
	 */
	public SubHolTransferSet(SubHolTransferSetGetMemento memento) {
		this.certainTime = memento.getCertainTime();
		this.useDivision = memento.getUseDivision();
		this.designatedTime = memento.getDesignatedTime();
		this.subHolTransferSetAtr = memento.getSubHolTransferSetAtr();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(SubHolTransferSetSetMemento memento) {
		memento.setCertainTime(this.certainTime);
		memento.setUseDivision(this.useDivision);
		memento.setDesignatedTime(this.designatedTime);
		memento.setSubHolTransferSetAtr(this.subHolTransferSetAtr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		// check use
		if (this.useDivision) {
			// one day >= half day
			if (this.getDesignatedTime().getOneDayTime().lessThan(this.getDesignatedTime().getHalfDayTime())) {
				this.bundledBusinessExceptions.addMessage("Msg_782");
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
	public void correctData(ScreenMode screenMode, SubHolTransferSet oldDomain,CompensatoryOccurrenceDivision originAtr) {
		// Simple mode
		if (screenMode == ScreenMode.SIMPLE) {
			// Only designatedTime not get restore
			this.certainTime = oldDomain.getCertainTime();
			this.designatedTime = oldDomain.getDesignatedTime();
			if (originAtr.equals(CompensatoryOccurrenceDivision.FromOverTime)) {
				this.useDivision = false;
			} else {
				this.useDivision = true;
			}
			this.subHolTransferSetAtr = SubHolTransferSetAtr.SPECIFIED_TIME_SUB_HOL;
			return;
		}

		// Detail mode
		// Setting not use - restore old data
		if (!this.useDivision) {
			this.certainTime = oldDomain.getCertainTime();
			this.subHolTransferSetAtr = oldDomain.getSubHolTransferSetAtr();
			this.designatedTime.restoreData(oldDomain.getDesignatedTime());
			return;
		}

		// Setting being used
		switch (this.subHolTransferSetAtr) {
		case SPECIFIED_TIME_SUB_HOL:
			this.certainTime = oldDomain.getCertainTime();
			break;

		case CERTAIN_TIME_EXC_SUB_HOL:
			this.designatedTime.restoreData(oldDomain.getDesignatedTime());
			break;

		default:
			throw new RuntimeException("SubHolTransferType not found.");
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode,CompensatoryOccurrenceDivision originAtr) {
		// Simple mode
		if (screenMode == ScreenMode.SIMPLE) {
			// Only designatedTime not get restore
			this.certainTime = new OneDayTime(0);
			this.designatedTime.restoreDefaultData();
			if (originAtr.equals(CompensatoryOccurrenceDivision.FromOverTime)) {
				this.useDivision = false;
			} else {
				this.useDivision = true;
			}
			this.subHolTransferSetAtr = SubHolTransferSetAtr.SPECIFIED_TIME_SUB_HOL;
			return;
		}

		// Detail mode
		// Setting not use - restore old data
		if (!this.useDivision) {
			this.certainTime = new OneDayTime(0);
			this.subHolTransferSetAtr = SubHolTransferSetAtr.SPECIFIED_TIME_SUB_HOL;
			this.designatedTime.restoreDefaultData();
			return;
		}

		// Setting being used
		switch (this.subHolTransferSetAtr) {
		case SPECIFIED_TIME_SUB_HOL:
			this.certainTime = new OneDayTime(0);
			break;

		case CERTAIN_TIME_EXC_SUB_HOL:
			this.designatedTime.restoreDefaultData();
			break;

		default:
			throw new RuntimeException("SubHolTransferType not found.");
		}
	}
	
	@Override
	public SubHolTransferSet clone() {
		SubHolTransferSet cloned = new SubHolTransferSet();
		try {
			cloned.certainTime = new OneDayTime(this.certainTime.v());
			cloned.useDivision = this.useDivision ? true : false ;
			cloned.designatedTime = this.designatedTime.clone();
			cloned.subHolTransferSetAtr = SubHolTransferSetAtr.valueOf(this.subHolTransferSetAtr.value);
		}
		catch (Exception e){
			throw new RuntimeException("SubHolTransferSet clone error.");
		}
		return cloned;
	}
}

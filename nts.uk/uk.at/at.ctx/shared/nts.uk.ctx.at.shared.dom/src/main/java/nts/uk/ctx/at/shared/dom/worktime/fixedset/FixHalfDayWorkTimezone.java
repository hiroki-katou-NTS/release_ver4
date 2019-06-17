/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedWorkTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class FixHalfDayWorkTimezone.
 */
@Getter
@NoArgsConstructor
// 固定勤務の平日出勤用勤務時間帯
public class FixHalfDayWorkTimezone extends WorkTimeDomainObject implements Cloneable{

	/** The rest timezone. */
	// 休憩時間帯
	private FixRestTimezoneSet restTimezone;

	/** The work timezone. */
	// 勤務時間帯
	private FixedWorkTimezoneSet workTimezone;

	/** The day atr. */
	// 午前午後区分
	private AmPmAtr dayAtr;

	/**
	 * Instantiates a new fix half day work timezone.
	 *
	 * @param memento the memento
	 */
	public FixHalfDayWorkTimezone(FixHalfDayWorkTimezoneGetMemento memento) {
		this.restTimezone = memento.getRestTimezone();
		this.workTimezone = memento.getWorkTimezone();
		this.dayAtr = memento.getDayAtr();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FixHalfDayWorkTimezoneSetMemento memento) {
		memento.setRestTimezone(this.restTimezone);
		memento.setWorkTimezone(this.workTimezone);
		memento.setDayAtr(this.dayAtr);
	}
	
	/**
	 * Restore data.
	 *
	 * @param screenMode the screen mode
	 * @param fixedWorkSet the fixed work set
	 * @param other the other
	 */
	public void correctData(ScreenMode screenMode, FixedWorkSetting fixedWorkSet,
			FixHalfDayWorkTimezone other) {
		switch (screenMode) {
		case SIMPLE:
			this.restoreSimpleMode(other);
			break;
		case DETAIL:
			this.restoreDetailMode(fixedWorkSet, other);;
			break;
		default:
			throw new RuntimeException("ScreenMode not found.");
		}
	}
	
	/**
	 * Restore simple mode.
	 *
	 * @param other the other
	 */
	private void restoreSimpleMode(FixHalfDayWorkTimezone other) {
		if (other.getDayAtr() != AmPmAtr.ONE_DAY) {
			this.restTimezone.restoreData(other.getRestTimezone());
			this.workTimezone.restoreData(other.getWorkTimezone());
		}
	}
	
	/**
	 * Restore detail mode.
	 *
	 * @param fixedWorkSet the fixed work set
	 * @param other the other
	 */
	private void restoreDetailMode(FixedWorkSetting fixedWorkSet, FixHalfDayWorkTimezone other) {
		// restore data of dayAtr = AM, PM
		if (!fixedWorkSet.getUseHalfDayShift() && other.getDayAtr() != AmPmAtr.ONE_DAY) {
			this.restTimezone.restoreData(other.getRestTimezone());
			this.workTimezone.restoreData(other.getWorkTimezone());
		}
	}
	
	/**
	 * Restore default data.
	 *
	 * @param screenMode the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		if (screenMode.equals(ScreenMode.SIMPLE) && this.getDayAtr() != AmPmAtr.ONE_DAY) {
			this.restTimezone.restoreDefaultData();
			this.workTimezone.restoreDefaultData();
		}
	}
	
	/**
	 * Correct default data.
	 */
	public void correctDefaultData() {
		this.workTimezone.correctDefaultData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {				
		//validate Msg_770 for list work
		this.workTimezone.getLstWorkingTimezone().stream().forEach(item->{
			item.getTimezone().validateRange("KMK003_86");
		});
		
		// validate Msg_770 for list ot
		this.workTimezone.getLstOTTimezone().stream().forEach(item -> {
			item.getTimezone().validateRange("KMK003_89");
		});
		
		// validate Msg_770 for rest time
		this.restTimezone.getLstTimezone().stream().forEach(item -> {
			item.validateRange("KMK003_20");
		});

		//validate Msg_515 for rest time
		this.restTimezone.validOverlap("KMK003_20");
		
		super.validate();
	}

	/**
	 * Checks if is in fixed work.
	 *
	 * @return true, if is in fixed work
	 */
	public boolean isInFixedWork() {
		return this.restTimezone.getLstTimezone().stream().allMatch(
				dedTime -> this.workTimezone.isInEmTimezone(dedTime) || this.workTimezone.isInOverTimezone(dedTime));
	}
	
	@Override
	public FixHalfDayWorkTimezone clone() {
		FixHalfDayWorkTimezone cloned = new FixHalfDayWorkTimezone();
		try {
			cloned.restTimezone = this.restTimezone.clone();
			cloned.workTimezone = this.workTimezone.clone();
			cloned.dayAtr = AmPmAtr.valueOf(this.dayAtr.value);
		}
		catch (Exception e){
			throw new RuntimeException("FixHalfDayWorkTimezone clone error.");
		}
		return cloned;
	}

}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flexset;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedWorkTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class FlexHalfDayWorkTime.
 */
//フレックス勤務の平日出勤用勤務時間帯
@Getter
@NoArgsConstructor
public class FlexHalfDayWorkTime extends WorkTimeDomainObject implements Cloneable{

	/** The rest timezone. */
	// 休憩時間帯
	private FlowWorkRestTimezone restTimezone;
	
	/** The work timezone. */
	// 勤務時間帯
	private FixedWorkTimezoneSet workTimezone;
	
	/** The ampm atr. */
	// 午前午後区分
	private AmPmAtr ampmAtr;

	/* (non-Javadoc)
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		// set hidden data = null (tab3)
		workTimezone.correctDefaultData();

		if (!this.restTimezone.isFixRestTime() && this.hasNoNo1()) {
			this.bundledBusinessExceptions.addMessage("Msg_847");
		}
		
		// validate 770 for list work
		this.workTimezone.getLstWorkingTimezone().stream().forEach(item -> {
			item.getTimezone().validateRange("KMK003_86");
		});
		
		// validate 770 for list ot
		this.workTimezone.getLstOTTimezone().stream().forEach(item -> {
			item.getTimezone().validateRange("KMK003_89");
		});
		
		
		if (this.restTimezone.isFixRestTime()) {
			// validate Msg_770 for rest time
			this.restTimezone.getFixedRestTimezone().getTimezones().stream().forEach(item -> {
				item.validateRange("KMK003_20");
			});
			
			// validate Msg_515 for rest time
			this.restTimezone.getFixedRestTimezone().checkOverlap("KMK003_20");
		}
		
		super.validate();
	}

	/**
	 * Checks if is in fixed work.
	 *
	 * @param flowRest the flow rest
	 * @return true, if is in fixed work
	 */
	public boolean isInFixedWork(FlowWorkRestTimezone flowRest) {
		return flowRest.getFixedRestTimezone().getTimezones().stream().allMatch(
				dedTime -> this.workTimezone.isInEmTimezone(dedTime) || this.workTimezone.isInOverTimezone(dedTime));
	}

	/**
	 * Checks for no no 1.
	 *
	 * @return true, if successful
	 */
	private boolean hasNoNo1() {
		val EM_TIME_NO_1 = 1;
		return this.workTimezone.getLstWorkingTimezone().stream()
				.anyMatch(item -> item.getEmploymentTimeFrameNo().v() != EM_TIME_NO_1);
	}

	/**
	 * Instantiates a new flex half day work time.
	 *
	 * @param memento the memento
	 */
	public FlexHalfDayWorkTime(FlexHalfDayWorkTimeGetMemento memento) {
		this.ampmAtr = memento.getAmpmAtr();
		this.restTimezone = memento.getRestTimezone();
		this.workTimezone = memento.getWorkTimezone();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlexHalfDayWorkTimeSetMemento memento){
		memento.setAmpmAtr(this.ampmAtr);
		memento.setRestTimezone(this.restTimezone);
		memento.setWorkTimezone(this.workTimezone);
	}
	
	/**
	 * Restore data.
	 *
	 * @param screenMode the screen mode
	 * @param workTimeType the work time type
	 * @param fixedWorkSet the fixed work set
	 * @param other the other
	 */
	public void restoreData(ScreenMode screenMode, FlexWorkSetting flexWorkSet,
			FlexHalfDayWorkTime oldDomain) {
		switch (screenMode) {
		case SIMPLE:
			this.restoreSimpleMode(oldDomain);
			break;
		case DETAIL:
			this.restoreDetailMode(screenMode,flexWorkSet, oldDomain);;
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
	private void restoreSimpleMode(FlexHalfDayWorkTime oldDomain) {
		if (oldDomain.getAmpmAtr() != AmPmAtr.ONE_DAY) {
			this.workTimezone.restoreData(oldDomain.getWorkTimezone());
		}
	}
	
	/**
	 * Restore detail mode.
	 *
	 * @param flexWorkSet the flex work set
	 * @param other the other
	 */
	private void restoreDetailMode(ScreenMode screenMode,FlexWorkSetting flexWorkSet, FlexHalfDayWorkTime oldDomain) {
		// restore worktime data of dayAtr = AM, PM
		if (!flexWorkSet.isUseHalfDayShift() && oldDomain.getAmpmAtr() != AmPmAtr.ONE_DAY) {
			this.workTimezone.restoreData(oldDomain.getWorkTimezone());
		}
		// restore rest time data of dayAtr = AM, PM
		if (!flexWorkSet.isUseHalfDayShift() && oldDomain.getAmpmAtr() != AmPmAtr.ONE_DAY) {
			this.restTimezone = oldDomain.getRestTimezone();
			Optional<FlexHalfDayWorkTime> oneDay = flexWorkSet.getLstHalfDayWorkTimezone().stream()
					.filter(half -> half.ampmAtr.equals(AmPmAtr.ONE_DAY)).findFirst();
			if (oneDay.isPresent()) {
				this.restTimezone.restoreFixRestTime(oneDay.get().getRestTimezone().isFixRestTime());
			}
		} else {
			this.restTimezone.correctData(screenMode, oldDomain.getRestTimezone());
		}
	}
	
	@Override
	public FlexHalfDayWorkTime clone() {
		FlexHalfDayWorkTime cloned = new FlexHalfDayWorkTime();
		try {
			cloned.restTimezone = this.restTimezone.clone();
			cloned.workTimezone = this.workTimezone.clone();
			cloned.ampmAtr = AmPmAtr.valueOf(this.ampmAtr.value);
		}
		catch (Exception e){
			throw new RuntimeException("FlexHalfDayWorkTime clone error.");
		}
		return cloned;
	}
}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flexset;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class FlexOffdayWorkTime.
 */
@Getter
@NoArgsConstructor
// フレックス勤務の休日出勤用勤務時間帯
public class FlexOffdayWorkTime extends WorkTimeDomainObject implements Cloneable{

	/** The lst work timezone. */
	// 勤務時間帯
	private List<HDWorkTimeSheetSetting> lstWorkTimezone;

	/** The rest timezone. */
	// 休憩時間帯
	private FlowWorkRestTimezone restTimezone;

	/**
	 * Instantiates a new flex offday work time.
	 *
	 * @param memento
	 *            the memento
	 */
	public FlexOffdayWorkTime(FlexOffdayWorkTimeGetMemento memento) {
		this.lstWorkTimezone = memento.getLstWorkTimezone();
		this.restTimezone = memento.getRestTimezone();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(FlexOffdayWorkTimeSetMemento memento) {
		memento.setLstWorkTimezone(this.lstWorkTimezone);
		memento.setRestTimezone(this.restTimezone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		if (this.restTimezone.isFixRestTime() && !this.isRestTzInHolidayTz()) {
			this.bundledBusinessExceptions.addMessage("Msg_756");
		}

		// validate 770
		this.lstWorkTimezone.stream().forEach(item -> {
			item.getTimezone().validateRange("KMK003_90");
		});

		// validate 770 for rest
		if (this.restTimezone.isFixRestTime()) {
			this.restTimezone.getFixedRestTimezone().getTimezones().stream().forEach(item -> {
				item.validateRange("KMK003_21");
			});
		}

		this.validOverlap();

		super.validate();
	}

	/**
	 * Valid overlap.
	 */
	private void validOverlap() {
		// sort asc by start time
		this.lstWorkTimezone = this.lstWorkTimezone.stream()
				.sorted((obj1, obj2) -> obj1.getTimezone().getStart().compareTo(obj2.getTimezone().getStart()))
				.collect(Collectors.toList());

		Iterator<HDWorkTimeSheetSetting> iterator = this.lstWorkTimezone.iterator();
		while (iterator.hasNext()) {
			TimeZoneRounding current = iterator.next().getTimezone();

			if (!iterator.hasNext()) {
				break;
			}
			TimeZoneRounding next = iterator.next().getTimezone();
			if (current.getEnd().greaterThan(next.getStart())) {
				this.bundledBusinessExceptions.addMessage("Msg_515", "KMK003_90");
			}
		}

		// validate msg_515
		if (this.restTimezone.isFixRestTime()) {
			this.restTimezone.getFixedRestTimezone().checkOverlap("KMK003_21");
		}
	}

	/**
	 * Checks if is rest tz in hd wtz.
	 *
	 * @return true, if is rest tz in hd wtz
	 */
	private boolean isRestTzInHolidayTz() {
		if (this.restTimezone.getFixedRestTimezone().getTimezones().isEmpty()) {
			return true;
		}
		return this.restTimezone.getFixedRestTimezone().getTimezones().stream().allMatch(
				resTz -> this.lstWorkTimezone.stream().anyMatch(hdWtz -> resTz.isBetweenOrEqual(hdWtz.getTimezone())));
	}

	public void correctData(ScreenMode screenMode, FlexWorkSetting oldDomain) {
		if (!this.restTimezone.getFlowRestTimezone().isUseHereAfterRestSet()) {
			this.restTimezone.correctData(screenMode, oldDomain.getOffdayWorkTime().getRestTimezone());
		}
	}
	
	/**
	 * Sort work time of off day.
	 */
	public void sortWorkTimeOfOffDay()
	{
		this.lstWorkTimezone = this.lstWorkTimezone.stream()
				.sorted((obj1, obj2) -> obj1.getTimezone().getStart().compareTo(obj2.getTimezone().getStart()))
				.collect(Collectors.toList());
	}
	
	@Override
	public FlexOffdayWorkTime clone() {
		FlexOffdayWorkTime cloned = new FlexOffdayWorkTime();
		try {
			cloned.lstWorkTimezone = this.lstWorkTimezone.stream().map(c -> c.clone()).collect(Collectors.toList());
			cloned.restTimezone = this.restTimezone.clone();;
		}
		catch (Exception e){
			throw new RuntimeException("FlexOffdayWorkTime clone error.");
		}
		return cloned;
	}
}

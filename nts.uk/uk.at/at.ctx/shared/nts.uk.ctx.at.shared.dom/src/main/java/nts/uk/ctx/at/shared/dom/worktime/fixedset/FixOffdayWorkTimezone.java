/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class FixOffdayWorkTimezone.
 */
@Getter
@NoArgsConstructor
// 固定勤務の休日出勤用勤務時間帯
public class FixOffdayWorkTimezone extends WorkTimeDomainObject implements Cloneable{

	/** The rest timezone. */
	// 休憩時間帯
	private FixRestTimezoneSet restTimezone;

	/** The lst work timezone. */
	// 勤務時間帯
	private List<HDWorkTimeSheetSetting> lstWorkTimezone;

	/**
	 * Instantiates a new fix offday work timezone.
	 *
	 * @param memento the memento
	 */
	public FixOffdayWorkTimezone(FixOffdayWorkTimezoneGetMemento memento) {
		this.restTimezone = memento.getRestTimezone();
		this.lstWorkTimezone = memento.getLstWorkTimezone();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FixOffdayWorkTimezoneSetMemento memento) {
		memento.setRestTimezone(this.restTimezone);
		memento.setLstWorkTimezone(this.lstWorkTimezone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		// #Msg_515 - domain TimezoneOfFixedRestTimeSet - validate overlap
		this.validateOverlap();
		// #Msg_756
		this.validateRestTimezone();
		
		//validate 770 for work time
		this.lstWorkTimezone.stream().forEach(item -> {
			item.getTimezone().validateRange("KMK003_90");
		});
		
		// validate 770 for rest
		this.restTimezone.getLstTimezone().stream().forEach(item -> {
			item.validateRange("KMK003_21");
		});
		
		super.validate();
	}

	/**
	 * Valid overlap.
	 */
	private void validateOverlap() {
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
		
		//validate msg_515
		this.restTimezone.validOverlap("KMK003_21");
	}
	
	/**
	 * Check rest timezone.
	 */
	private void validateRestTimezone() {
		this.restTimezone.getLstTimezone().forEach((timezone) -> {
			// Is timezone in WorkTimezone -  休出時間帯.時間帯
			boolean isHasWorkTime = this.lstWorkTimezone.stream()
					.map(item -> item.getTimezone())
					.anyMatch((timeZoneRounding) -> {
						return timezone.getStart().greaterThanOrEqualTo(timeZoneRounding.getStart())
								&& timezone.getEnd().lessThanOrEqualTo(timeZoneRounding.getEnd());
					});

			if (!isHasWorkTime) {
				this.bundledBusinessExceptions.addMessage("Msg_756");
			}
		});
	}

	@Override
	public FixOffdayWorkTimezone clone() {
		FixOffdayWorkTimezone cloned = new FixOffdayWorkTimezone();
		try {
			cloned.restTimezone = this.restTimezone.clone();
			cloned.lstWorkTimezone = this.lstWorkTimezone.stream().map(c -> c.clone()).collect(Collectors.toList());
		}
		catch (Exception e){
			throw new RuntimeException("FixOffdayWorkTimezone clone error.");
		}
		return cloned;
	}
	
}

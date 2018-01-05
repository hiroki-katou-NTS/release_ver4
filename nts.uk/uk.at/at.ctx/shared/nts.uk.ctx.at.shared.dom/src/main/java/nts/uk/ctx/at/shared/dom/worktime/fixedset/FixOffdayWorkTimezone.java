/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;

/**
 * The Class FixOffdayWorkTimezone.
 */
@Getter
// 固定勤務の休日出勤用勤務時間帯
public class FixOffdayWorkTimezone extends DomainObject {

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
		super.validate();

		// #Msg_756
		this.checkRestTimezone();

		// #Msg_515 - domain TimezoneOfFixedRestTimeSet - validate overlap
		this.validOverlap();		
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
				throw new BusinessException("Msg_515");
			}
		}
	}
	
	/**
	 * Check rest timezone.
	 */
	private void checkRestTimezone() {
		this.restTimezone.getLstTimezone().forEach((timezone) -> {
			// Is timezone in WorkTimezone -  休出時間帯.時間帯
			boolean isHasWorkTime = this.lstWorkTimezone.stream()
					.map(item -> item.getTimezone())
					.anyMatch((timeZoneRounding) -> {
						return timezone.getStart().greaterThanOrEqualTo(timeZoneRounding.getStart())
								&& timezone.getEnd().lessThanOrEqualTo(timeZoneRounding.getEnd());
					});

			if (!isHasWorkTime) {
				throw new BusinessException("Msg_756");
			}
		});
	}

}

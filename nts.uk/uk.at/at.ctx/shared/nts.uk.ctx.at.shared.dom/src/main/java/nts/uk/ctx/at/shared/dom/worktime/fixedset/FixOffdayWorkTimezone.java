/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.DomainObject;

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
	 * @param memento
	 *            the memento
	 */
	public FixOffdayWorkTimezone(FixOffdayWorkTimezoneGetMemento memento) {
		this.restTimezone = memento.getRestTimezone();
		this.lstWorkTimezone = memento.getLstWorkTimezone();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
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

		List<String> lstWorkTime = this.lstWorkTimezone.stream().map(item -> item.getTimezone().toString())
				.collect(Collectors.toList());

		this.restTimezone.getLstTimezone().forEach((timezone) -> {
			// has in 休出時間帯.時間帯
			boolean isHasWorkTime = lstWorkTime.contains(timezone.toString());

			if (!isHasWorkTime) {
				throw new BusinessException("Msg_756");
			}
		});

		// validate overlap
		this.validOverlap();
	}

	/**
	 * Valid overlap.
	 */
	private void validOverlap() {
		// sort asc by start time
		Collections.sort(this.lstWorkTimezone, new Comparator<HDWorkTimeSheetSetting>() {
			@Override
			public int compare(HDWorkTimeSheetSetting obj1, HDWorkTimeSheetSetting obj2) {
				return obj1.getTimezone().getStart().compareTo(obj2.getTimezone().getStart());
			}
		});

		Iterator<HDWorkTimeSheetSetting> iterator = this.lstWorkTimezone.iterator();
		while (iterator.hasNext()) {
			TimeZoneRounding current = iterator.next().getTimezone();

			if (!iterator.hasNext()) {
				break;
			}
			TimeZoneRounding next = iterator.next().getTimezone();
			if (current.getEnd().greaterThanOrEqualTo(next.getStart())) {
				throw new BusinessException("Msg_515");
			}
		}
	}

}

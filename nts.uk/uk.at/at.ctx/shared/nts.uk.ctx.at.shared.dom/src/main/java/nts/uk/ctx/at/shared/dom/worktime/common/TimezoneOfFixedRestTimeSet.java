/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class TimezoneOfFixedRestTimeSet.
 */
// 固定休憩時間の時間帯設定
@Getter
public class TimezoneOfFixedRestTimeSet extends WorkTimeDomainObject implements Cloneable{

	/** The timezones. */
	// 時間帯
	private List<DeductionTime> timezones;

	/**
	 * Instantiates a new timezone of fixed rest time set.
	 */
	public TimezoneOfFixedRestTimeSet() {
		super();
		this.timezones = new ArrayList<>();
	}

	/**
	 * Instantiates a new timezone of fixed rest time set.
	 *
	 * @param timezones
	 *            the timezones
	 */
	public TimezoneOfFixedRestTimeSet(List<DeductionTime> timezones) {
		super();
		this.timezones = timezones;
	}

	/**
	 * Check overlap.
	 *
	 * @param param
	 *            the param
	 */
	public void checkOverlap(String param) {
		Collections.sort(this.timezones, Comparator.comparing(DeductionTime::getStart));

		for (int i = 0; i < this.timezones.size(); i++) {
			DeductionTime deduct1 = this.timezones.get(i);
			for (int j = i + 1; j < this.timezones.size(); j++) {
				DeductionTime deduct2 = this.timezones.get(j);
				// check overlap
				if (deduct1.isOverlap(deduct2)) {
					this.bundledBusinessExceptions.addMessage("Msg_515", param);
				}
			}
		}
	}

	/**
	 * Instantiates a new timezone of fixed rest time set.
	 *
	 * @param memento
	 *            the memento
	 */
	public TimezoneOfFixedRestTimeSet(TimezoneOfFixedRestTimeSetGetMemento memento) {
		this.timezones = memento.getTimezones();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(TimezoneOfFixedRestTimeSetSetMemento memento) {
		memento.setTimezones(this.timezones);
	}

	/**
	 * 休憩時間の合計時間を計算
	 * @return　休憩合計時間
	 */
	public AttendanceTime calcTotalTime() {
		return new AttendanceTime(this.timezones.stream()
							 					.map(tc -> tc.timeSpan().lengthAsMinutes())
							 					.collect(Collectors.summingInt(tc -> tc)));
	}
	
	@Override
	public TimezoneOfFixedRestTimeSet clone() {
		TimezoneOfFixedRestTimeSet cloned = new TimezoneOfFixedRestTimeSet();
		try {
			cloned.timezones = this.timezones.stream().map(c -> c.clone()).collect(Collectors.toList());
		}
		catch (Exception e){
			throw new RuntimeException("TimezoneOfFixedRestTimeSet clone error.");
		}
		return cloned;
	}
}

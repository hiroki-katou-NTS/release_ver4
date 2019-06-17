/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class FixRestTimezoneSet.
 */
// 固定勤務の休憩時間帯
@Getter
@NoArgsConstructor
public class FixRestTimezoneSet extends WorkTimeDomainObject implements Cloneable{

	/** The lst timezone. */
	// 時間帯
	private List<DeductionTime> lstTimezone;

	
	/**
 	* Constructors 
 	*@param lstTimezone
 	*/
	public FixRestTimezoneSet(List<DeductionTime> lstTimezone) {
		super();
		this.lstTimezone = lstTimezone;
	}
	
	/**
	 * Instantiates a new fix rest timezone set.
	 *
	 * @param memento the memento
	 */
	public FixRestTimezoneSet(FixRestTimezoneSetGetMemento memento) {
		this.lstTimezone = memento.getLstTimezone();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FixRestTimezoneSetSetMemento memento) {
		memento.setLstTimezone(this.lstTimezone);
	}

	/**
	 * Valid overlap.
	 *
	 * @param param the param
	 */
	public void validOverlap(String param) {
		// sort asc by start time
		Collections.sort(this.lstTimezone, Comparator.comparing(DeductionTime::getStart));

		Iterator<DeductionTime> iterator = this.lstTimezone.iterator();
		while (iterator.hasNext()) {
			DeductionTime current = iterator.next();

			if (!iterator.hasNext()) {
				break;
			}
			DeductionTime next = iterator.next();
			if (current.getEnd().greaterThan(next.getStart())) {
				this.bundledBusinessExceptions.addMessage("Msg_515", param);
			}
		}
	}

	/**
	 * Restore data.
	 *
	 * @param other the other
	 */
	public void restoreData(FixRestTimezoneSet other) {
		// restore
		Map<Entry<TimeWithDayAttr, TimeWithDayAttr>, DeductionTime> mapRestTimezone = other.getLstTimezone().stream()
				.collect(Collectors.toMap(
						item -> new ImmutablePair<TimeWithDayAttr, TimeWithDayAttr>(item.getStart(), item.getEnd()),
						Function.identity()));
		this.lstTimezone.forEach(restTimezoneOther -> {
			restTimezoneOther.restoreData(mapRestTimezone.get(new ImmutablePair<TimeWithDayAttr, TimeWithDayAttr>(
					restTimezoneOther.getStart(), restTimezoneOther.getEnd())));
		});
	}
	
	/**
	 * Restore default data.
	 */
	public void restoreDefaultData() {
		this.lstTimezone = new ArrayList<>();
	}
	
	/**
	 * 休憩時間の合計時間を計算
	 * @return　休憩合計時間
	 */
	public AttendanceTime calcTotalTime() {
		return new AttendanceTime(this.getLstTimezone().stream()
							 					.map(tc -> tc.timeSpan().lengthAsMinutes())
							 					.collect(Collectors.summingInt(tc -> tc)));
	}

	public AttendanceTime calcTotalTimeDuplicatedAttLeave(
			List<TimeSpanForCalc> timeLeavingWorks, List<TimeSpanForCalc> workSpans) {
		
		int returnValue = 0;
		for(TimeSpanForCalc timeSpan : timeLeavingWorks) {
			if(timeSpan.getStart() != null && timeSpan.getEnd() != null) {
				for(TimeSpanForCalc workSpan : workSpans) {
					if(workSpan.getStart() != null && workSpan.getEnd() != null) {
						returnValue += this.lstTimezone.stream()
								.filter(tc -> tc.getDuplicatedWith(timeSpan.getSpan()).isPresent())
								.filter(tc -> tc.getDuplicatedWith(workSpan.getSpan()).isPresent())
								.map(tc -> tc.getDuplicatedWith(timeSpan.getSpan()).get().lengthAsMinutes())
								.collect(Collectors.summingInt(tc -> tc));
					}
				}
			}
		}
		return new AttendanceTime(returnValue);
	}
	
	@Override
	public FixRestTimezoneSet clone() {
		FixRestTimezoneSet cloned = new FixRestTimezoneSet();
		try {
			cloned.lstTimezone = this.lstTimezone.stream().map(c -> c.clone()).collect(Collectors.toList());
		}
		catch (Exception e){
			throw new RuntimeException("FixRestTimezoneSet clone error.");
		}
		return cloned;
	}
}

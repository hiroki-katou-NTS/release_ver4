/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

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
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 固定休憩時間の時間帯設定
 * The Class TimezoneOfFixedRestTimeSet.
 *
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.就業規則.就業時間帯.共通設定.休憩設定.固定休憩時間の時間帯設定
 */
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


	/**
	 * 休憩時間帯を取得
	 * ※休憩時間帯を計算時間帯リストとして取得する
	 * @return 休憩時間帯リスト(計算時間帯)
	 */
	public List<TimeSpanForCalc> getRestTimezonesForCalc() {
		return this.timezones.stream().map( e -> e.timeSpan() ).collect(Collectors.toList());
	}

	/**
     * Restore data.
     *
     * @param other the other
     */
    public void restoreData(TimezoneOfFixedRestTimeSet other) {
        // restore
        Map<Entry<TimeWithDayAttr, TimeWithDayAttr>, DeductionTime> mapRestTimezone = other.getTimezones().stream()
                .collect(Collectors.toMap(
                        item -> new ImmutablePair<TimeWithDayAttr, TimeWithDayAttr>(item.getStart(), item.getEnd()),
                        Function.identity()));
        this.timezones.forEach(restTimezoneOther -> {
            restTimezoneOther.restoreData(mapRestTimezone.get(new ImmutablePair<TimeWithDayAttr, TimeWithDayAttr>(
                    restTimezoneOther.getStart(), restTimezoneOther.getEnd())));
        });
    }
    
    /**
     * Restore default data.
     */
    public void restoreDefaultData() {
        this.timezones = new ArrayList<>();
    }
    
    public AttendanceTime calcTotalTimeDuplicatedAttLeave(
            List<TimeSpanForCalc> timeLeavingWorks, List<TimeSpanForCalc> workSpans) {

        int returnValue = 0;
        for(TimeSpanForCalc timeSpan : timeLeavingWorks) {
            if(timeSpan.getStart() != null && timeSpan.getEnd() != null) {
                for(TimeSpanForCalc workSpan : workSpans) {
                    if(workSpan.getStart() != null && workSpan.getEnd() != null) {
                        returnValue += this.timezones.stream()
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
    
    /**
     * Valid overlap.
     *
     * @param param the param
     */
    public void validOverlap(String param) {
        // sort asc by start time
        Collections.sort(this.timezones, Comparator.comparing(DeductionTime::getStart));

        Iterator<DeductionTime> iterator = this.timezones.iterator();
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
}

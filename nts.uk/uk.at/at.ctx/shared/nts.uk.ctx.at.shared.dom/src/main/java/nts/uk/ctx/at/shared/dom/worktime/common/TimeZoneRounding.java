/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class TimeZoneRounding.
 */
// 時間帯(丸め付き)
@Getter
@NoArgsConstructor
public class TimeZoneRounding extends TimeZone implements Cloneable{

	/** The rounding. */
	// 丸め
	private TimeRoundingSetting rounding;

	/**
	 * Instantiates a new time zone rounding.
	 *
	 * @param start the start
	 * @param end the end
	 * @param rounding the rounding
	 */
	public TimeZoneRounding(TimeWithDayAttr start, TimeWithDayAttr end,
			TimeRoundingSetting rounding) {
		super(start, end);
		this.rounding = rounding != null ? rounding : new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN);
	}

	/**
	 * Instantiates a new time zone rounding.
	 *
	 * @param memento
	 *            the memento
	 */
	public TimeZoneRounding(TimeZoneRoundingGetMemento memento) {
		super(memento.getStart(), memento.getEnd());
		this.rounding = memento.getRounding();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(TimeZoneRoundingSetMemento memento) {
		memento.setRounding(this.rounding);
		memento.setStart(this.start);
		memento.setEnd(this.end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.start.v() + "," + this.end.v();
	}

	/**
	 * 計算時間帯の取得 
	 */
	public TimeSpanForCalc getTimeSpan() {
		return new TimeSpanForCalc(this.start,this.end);
	}
	
	/**
	 * 丸め設定の初期化(1分/切り捨て　に変更)
	 */
	public void roudingReset() {
		this.rounding = new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN);
	}
	
	@Override
	public TimeZoneRounding clone() {
		TimeZoneRounding cloned = new TimeZoneRounding();
		try {
			cloned.start = new TimeWithDayAttr(this.start.valueAsMinutes());
			cloned.end = new TimeWithDayAttr(this.end.valueAsMinutes());
			cloned.rounding = this.rounding.clone();
		}
		catch (Exception e){
			throw new RuntimeException("AggregateTotalTimeSpentAtWork clone error.");
		}
		return cloned;
	}
}

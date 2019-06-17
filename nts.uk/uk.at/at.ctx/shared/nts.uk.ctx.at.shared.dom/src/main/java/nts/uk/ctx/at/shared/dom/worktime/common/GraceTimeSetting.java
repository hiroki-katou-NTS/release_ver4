/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class GraceTimeSetting.
 */
// 猶予時間設定
@Getter
@NoArgsConstructor
public class GraceTimeSetting extends WorkTimeDomainObject implements Cloneable{

	/** The include working hour. */
	// 就業時間に含める
	private boolean includeWorkingHour;

	/** The grace time. */
	// 猶予時間
	private LateEarlyGraceTime graceTime;

	/**
	 * Instantiates a new grace time setting.
	 *
	 * @param includeWorkingHour
	 *            the include working hour
	 * @param graceTime
	 *            the grace time
	 */
	public GraceTimeSetting(boolean includeWorkingHour, LateEarlyGraceTime graceTime) {
		super();
		this.includeWorkingHour = includeWorkingHour;
		this.graceTime = graceTime;
	}

	/**
	 * Instantiates a new grace time setting.
	 *
	 * @param memento
	 *            the memento
	 */
	public GraceTimeSetting(GraceTimeSettingGetMemento memento) {
		this.includeWorkingHour = memento.getIncludeWorkingHour();
		this.graceTime = memento.getGraceTime();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(GraceTimeSettingSetMemento memento) {
		memento.setIncludeWorkingHour(this.includeWorkingHour);
		memento.setGraceTime(this.graceTime);
	}
	
	/**
	 * 猶予時間が0：00かどうか判断する
	 * @return　0：00の場合：true　0：00でない場合：false
	 */
	public boolean isZero() {
		return this.graceTime.v() == 0;
	}
	
	/**
	 * 遅刻猶予時間帯を作成する
	 * @param baseTimeSheet
	 * @return
	 */
	public TimeSpanForCalc createLateGraceTimeSheet(TimezoneUse baseTimeSheet) {
		//猶予時間帯の終了時刻の作成
		val correctedEndTime = baseTimeSheet.getStart().forwardByMinutes(this.graceTime.minute());
		//猶予時間帯の作成
		return new TimeSpanForCalc(baseTimeSheet.getStart(), correctedEndTime);
	}
	
	/**
	 * 早退猶予時間帯を作成する
	 * @param baseTimeSheet
	 * @return
	 */
	public TimeSpanForCalc createLeaveEarlyGraceTimeSheet(TimezoneUse baseTimeSheet) {
		//猶予時間帯の開始時刻の作成
		val correctedStartTime = baseTimeSheet.getEnd().backByMinutes(this.graceTime.minute());
		//猶予時間帯の作成
		return new TimeSpanForCalc(correctedStartTime, baseTimeSheet.getEnd());
	}
	
	@Override
	public GraceTimeSetting clone() {
		GraceTimeSetting cloned = new GraceTimeSetting();
		try {
			cloned.includeWorkingHour = this.includeWorkingHour ? true : false ;
			cloned.graceTime = new LateEarlyGraceTime(this.graceTime.valueAsMinutes());
		}
		catch (Exception e){
			throw new RuntimeException("GraceTimeSetting clone error.");
		}
		return cloned;
	}

}

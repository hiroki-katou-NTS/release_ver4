/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class HDWorkTimeSheetSetting.
 */
// 休出時間の時間帯設定
@Getter
public class HDWorkTimeSheetSetting extends DomainObject {
	
	/** The work time no. */
	//就業時間帯NO
	private Integer workTimeNo;
	
	/** The timezone. */
	//時間帯
	private TimeZoneRounding timezone;

	/** The is legal holiday constraint time. */
	// 法定内休出を拘束時間として扱う
	private boolean isLegalHolidayConstraintTime;

	/** The in legal break frame no. */
	// 法定内休出枠NO
	private BreakFrameNo inLegalBreakFrameNo;

	/** The is non statutory dayoff constraint time. */
	// 法定外休出を拘束時間として扱う
	private boolean isNonStatutoryDayoffConstraintTime;

	/** The out legal break frame no. */
	// 法定外休出枠NO
	private BreakFrameNo outLegalBreakFrameNo;

	/** The is non statutory holiday constraint time. */
	// 法定外祝日を拘束時間として扱う
	private boolean isNonStatutoryHolidayConstraintTime;

	/** The out legal pub HD frame no. */
	// 法定外祝日枠NO
	private BreakFrameNo outLegalPubHDFrameNo;
	
	/**
	 * Instantiates a new HD work time sheet setting.
	 *
	 * @param memento the memento
	 */
	public HDWorkTimeSheetSetting(HDWorkTimeSheetSettingGetMemento memento) {
		this.workTimeNo = memento.getWorkTimeNo();
		this.timezone = memento.getTimezone();
		this.isLegalHolidayConstraintTime = memento.getIsLegalHolidayConstraintTime();
		this.inLegalBreakFrameNo = memento.getInLegalBreakFrameNo();
		this.isNonStatutoryDayoffConstraintTime = memento.getIsNonStatutoryDayoffConstraintTime();
		this.outLegalBreakFrameNo = memento.getInLegalBreakFrameNo();
		this.isNonStatutoryHolidayConstraintTime = memento.getIsNonStatutoryHolidayConstraintTime();
		this.outLegalPubHDFrameNo = memento.getOutLegalPubHDFrameNo();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(HDWorkTimeSheetSettingSetMemento memento){
		memento.setWorkTimeNo(this.workTimeNo);
		memento.setTimezone(this.timezone);
		memento.setIsLegalHolidayConstraintTime(this.isLegalHolidayConstraintTime);
		memento.setInLegalBreakFrameNo(this.inLegalBreakFrameNo);
		memento.setIsNonStatutoryDayoffConstraintTime(this.isNonStatutoryDayoffConstraintTime);
		memento.setOutLegalBreakFrameNo(this.outLegalBreakFrameNo);
		memento.setIsNonStatutoryHolidayConstraintTime(this.isNonStatutoryHolidayConstraintTime);
		memento.setOutLegalPubHDFrameNo(this.outLegalPubHDFrameNo);
	}
}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktype.HolidayAtr;

/**
 * The Class HDWorkTimeSheetSetting.
 */
// 休出時間の時間帯設定
@Getter
@NoArgsConstructor
public class HDWorkTimeSheetSetting extends WorkTimeDomainObject implements Cloneable{
	
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
		this.outLegalBreakFrameNo = memento.getOutLegalBreakFrameNo();
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

	/**
	 * 休日区分に従って、休出枠Ｎｏを返す
	 * @param atr
	 * @return 休出枠Ｎｏ
	 */
	public BreakFrameNo decisionBreakFrameNoByHolidayAtr(HolidayAtr atr) {
		switch(atr) {
			case STATUTORY_HOLIDAYS:
				return this.inLegalBreakFrameNo;
			case NON_STATUTORY_HOLIDAYS:			
				return this.outLegalBreakFrameNo;
			case PUBLIC_HOLIDAY:
				return this.outLegalPubHDFrameNo;
			default:
				throw new RuntimeException("unknown holidayAtr:"+atr); 
		}
	}
		
	
	@Override
	public HDWorkTimeSheetSetting clone() {
		HDWorkTimeSheetSetting cloned = new HDWorkTimeSheetSetting();
		try {
			cloned.workTimeNo = new Integer(this.workTimeNo.intValue());
			cloned.timezone = this.timezone.clone();
			cloned.isLegalHolidayConstraintTime = this.isLegalHolidayConstraintTime ? true : false ;
			cloned.inLegalBreakFrameNo = new BreakFrameNo(this.inLegalBreakFrameNo.v());
			cloned.isNonStatutoryDayoffConstraintTime = this.isNonStatutoryDayoffConstraintTime ? true : false ;
			cloned.outLegalBreakFrameNo = new BreakFrameNo(this.outLegalBreakFrameNo.v());
			cloned.isNonStatutoryHolidayConstraintTime = this.isNonStatutoryHolidayConstraintTime ? true : false ;
			cloned.outLegalPubHDFrameNo = new BreakFrameNo(this.outLegalPubHDFrameNo.v());
		}
		catch (Exception e){
			throw new RuntimeException("HDWorkTimeSheetSetting clone error.");
		}
		return cloned;	
	}
}

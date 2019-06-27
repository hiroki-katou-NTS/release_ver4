/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.predset;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class PrescribedTimezoneSetting.
 */
// 所定時間帯設定
@Getter
@NoArgsConstructor
public class PrescribedTimezoneSetting extends WorkTimeDomainObject implements Cloneable{

	/** The morning end time. */
	// 午前終了時刻
	private TimeWithDayAttr morningEndTime;

	/** The afternoon start time. */
	// 午後開始時刻
	private TimeWithDayAttr afternoonStartTime;

	/** The lst timezone. */
	// 時間帯
	private List<TimezoneUse> lstTimezone;

	/** The size one. */
	public static Integer SIZE_ONE = 1;

	/**
	 * Instantiates a new prescribed timezone setting.
	 *
	 * @param memento
	 *            the memento
	 */
	public PrescribedTimezoneSetting(PrescribedTimezoneSettingGetMemento memento) {
		this.morningEndTime = memento.getMorningEndTime();
		this.afternoonStartTime = memento.getAfternoonStartTime();
		this.lstTimezone = memento.getLstTimezone();
	}

	/**
	 * Instantiates a new prescribed timezone setting.
	 *
	 * @param morningEndTime
	 *            the morning end time
	 * @param afternoonStartTime
	 *            the afternoon start time
	 * @param lstTimezone
	 *            the lst timezone
	 */
	public PrescribedTimezoneSetting(TimeWithDayAttr morningEndTime, TimeWithDayAttr afternoonStartTime,
			List<TimezoneUse> lstTimezone) {
		super();
		this.morningEndTime = morningEndTime;
		this.afternoonStartTime = afternoonStartTime;
		this.lstTimezone = lstTimezone;
	}

	/**
	 * Checks if is use shift two.
	 *
	 * @return true, if is use shift two
	 */
	public boolean isUseShiftTwo() {
		return this.getTimezoneShiftTwo().isUsed();
	}

	/**
	 * Sets the morning work.
	 */
	public void setMorningWork() {
		if (this.isUseShiftTwo()) {
			// work time set update morning
			if (this.isMorningEndTimeLessThanOrEqualToShift2StartTime()) {

				// update end time shift 1 and remove shift 2
				this.setMorningEndTimeShiftOne();
				this.getTimezoneShiftTwo().resetTime();
			} else {

				// update end time shift 2
				this.setMorningEndTimeShiftTwo();
			}
		} else {
			// update time shift 1 to end time morning
			this.setMorningEndTimeShiftOne();
			this.getTimezoneShiftTwo().resetTime();
		}
	}

	/**
	 * Sets the afternoon work.
	 */
	public void setAfternoonWork() {
		if (this.isUseShiftTwo()) {
			if (this.isAfternoonStartTimeLessThanOrEqualToShift1EndTime()) {

				// update start time shift 1
				this.setAfternoonStartTimeShiftOne();
			} else {

				// update start time shift 2 and remove shift 1
				this.setAfternoonStartTimeShiftTwo();
				this.getTimezoneShiftOne().resetTime();
			}
		} else {
			// update time shift 1 and remove shift 2
			this.setAfternoonStartTimeShiftOne();
			this.getTimezoneShiftTwo().resetTime();
		}
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(PrescribedTimezoneSettingSetMemento memento) {
		memento.setMorningEndTime(this.morningEndTime);
		memento.setAfternoonStartTime(this.afternoonStartTime);
		memento.setLstTimezone(this.lstTimezone);
	}

	/**
	 * Gets the timezone shift one.
	 *
	 * @return the timezone shift one
	 */
	public TimezoneUse getTimezoneShiftOne() {
		return this.getTimezone(TimezoneUse.SHIFT_ONE);
	}

	/**
	 * Gets the timezone shift two.
	 *
	 * @return the timezone shift two
	 */
	public TimezoneUse getTimezoneShiftTwo() {
		return this.getTimezone(TimezoneUse.SHIFT_TWO);
	}

	/**
	 * Update start time shift.
	 *
	 * @param newTime
	 *            the new time
	 * @param workNo
	 *            the work no
	 */
	public void updateStartTimeShift(TimeWithDayAttr newTime, int workNo) {
		TimezoneUse tz = this.getTimezone(workNo);
		tz.updateStartTime(newTime);
	}

	/**
	 * Update end time shift.
	 *
	 * @param newTime
	 *            the new time
	 * @param workNo
	 *            the work no
	 */
	public void updateEndTimeShift(TimeWithDayAttr newTime, int workNo) {
		TimezoneUse tz = this.getTimezone(workNo);
		tz.updateEndTime(newTime);
	}

	/**
	 * Update timezone shift.
	 *
	 * @param workNo
	 *            the work no
	 * @param newStrTime
	 *            the new str time
	 * @param newEndTime
	 *            the new end time
	 */
	public void updateTimezoneShift(int workNo, TimeWithDayAttr newStrTime, TimeWithDayAttr newEndTime) {
		this.updateStartTimeShift(newStrTime, workNo);
		this.updateEndTimeShift(newEndTime, workNo);
	}

	/**
	 * Gets the timezone.
	 *
	 * @param workNo
	 *            the work no
	 * @return the timezone
	 */
	private TimezoneUse getTimezone(int workNo) {
		return this.lstTimezone.stream().filter(timezone -> timezone.getWorkNo() == workNo).findFirst().get();
	}

	/**
	 * Disable shift two.
	 */
	public void disableShiftTwo() {
		this.getTimezoneShiftTwo().disable();
	}

	/**
	 * Disable shift one.
	 */
	public void disableShiftOne() {
		this.getTimezoneShiftTwo().disable();
	}

	/**
	 * Checks if is morning end time less than or equal to shift 2 start time.
	 *
	 * @return true, if is morning end time less than or equal to shift 2 start
	 *         time
	 */
	private boolean isMorningEndTimeLessThanOrEqualToShift2StartTime() {
		return this.morningEndTime.lessThanOrEqualTo(this.getTimezoneShiftTwo().getStart());
	}

	/**
	 * Checks if is afternoon start time less than or equal to shift 1 end time.
	 *
	 * @return true, if is afternoon start time less than or equal to shift 1
	 *         end time
	 */
	private boolean isAfternoonStartTimeLessThanOrEqualToShift1EndTime() {
		return this.afternoonStartTime.lessThanOrEqualTo(this.getTimezoneShiftOne().getEnd());
	}

	/**
	 * Sets the morning end time shift two.
	 */
	private void setMorningEndTimeShiftTwo() {
		this.getTimezoneShiftTwo().updateEndTime(this.morningEndTime);
	}

	/**
	 * Sets the morning end time shift one.
	 */
	private void setMorningEndTimeShiftOne() {
		this.getTimezoneShiftOne().updateEndTime(this.morningEndTime);
	}

	/**
	 * Sets the afternoon start time shift one.
	 */
	private void setAfternoonStartTimeShiftOne() {
		this.getTimezoneShiftOne().updateStartTime(this.afternoonStartTime);
	}

	/**
	 * Sets the afternoon start time shift two.
	 */
	private void setAfternoonStartTimeShiftTwo() {
		this.getTimezoneShiftTwo().updateStartTime(this.afternoonStartTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		// Valid timezone
		this.validTimeDay();
		if (this.lstTimezone.size() > SIZE_ONE) {
			this.validTimeDayShiftTwo();
			this.validTimeDayShiftOneAndTwo();
		}

		super.validate();
	}

	/**
	 * Valid time day.
	 */
	private void validTimeDay() {
		// 使用しない
		if (!this.getTimezoneShiftTwo().isUsed()) {
			TimezoneUse tzWorkNo1 = this.getTimezoneShiftOne();
			if (!tzWorkNo1.consistOf(this.getAfternoonStartTime())) {
				this.bundledBusinessExceptions.addMessage("Msg_773", "KMK003_40");
			}
			if (!tzWorkNo1.consistOf(this.getMorningEndTime())) {
				this.bundledBusinessExceptions.addMessage("Msg_773", "KMK003_39");
			}
		}
	}

	/**
	 * Valid time day shift two.
	 */
	private void validTimeDayShiftTwo() {
		if (this.getTimezoneShiftTwo().isUsed()) {
			TimezoneUse tzWorkNo1 = this.getTimezoneShiftOne();
			TimezoneUse tzWorkNo2 = this.getTimezoneShiftTwo();
			if (!tzWorkNo1.consistOf(this.getMorningEndTime()) && !tzWorkNo2.consistOf(this.getMorningEndTime())) {
				this.bundledBusinessExceptions.addMessage("Msg_774", "KMK003_39");
			}
			if (!tzWorkNo1.consistOf(this.getAfternoonStartTime())
					&& !tzWorkNo2.consistOf(this.getAfternoonStartTime())) {
				this.bundledBusinessExceptions.addMessage("Msg_774", "KMK003_40");
			}
		}
	}

	/**
	 * Valid time day shift one and two.
	 */
	private void validTimeDayShiftOneAndTwo() {
		if (this.getTimezoneShiftTwo().isUsed()) {
			TimezoneUse tzWorkNo1 = this.getTimezoneShiftOne();
			TimezoneUse tzWorkNo2 = this.getTimezoneShiftTwo();
			if (tzWorkNo2.getStart().lessThan(tzWorkNo1.getEnd())) {
				this.bundledBusinessExceptions.addMessage("Msg_772");
			}
		}
	}

	/**
	 * Restore disabled data from.
	 *
	 * @param domain
	 *            the domain
	 */
	public void restoreDisabledDataFrom(PrescribedTimezoneSetting domain) {
		int indexOfShift2 = this.lstTimezone.indexOf(this.getTimezoneShiftTwo());
		this.lstTimezone.set(indexOfShift2, domain.getTimezoneShiftTwo());
	}

	/**
	 * Gets the match work no time sheet.
	 *
	 * @param workNo
	 *            the work no
	 * @return the match work no time sheet
	 */
	public Optional<TimezoneUse> getMatchWorkNoTimeSheet(int workNo) {
		return this.lstTimezone.stream().filter(tc -> tc.getWorkNo() == workNo)
				.findFirst();
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param workTimeType
	 *            the work time type
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, WorkTimeDivision workTimeType, PrescribedTimezoneSetting oldDomain) {
		if (screenMode == ScreenMode.SIMPLE) {
			// Simple mode
			this.getTimezoneShiftTwo().correctDefaultData();
		}

		if (screenMode == ScreenMode.DETAIL) {
			// Detail mode
			if ((workTimeType.getWorkTimeDailyAtr().equals(WorkTimeDailyAtr.REGULAR_WORK)
					&& workTimeType.getWorkTimeMethodSet().equals(WorkTimeMethodSet.DIFFTIME_WORK))
					|| workTimeType.getWorkTimeDailyAtr().equals(WorkTimeDailyAtr.FLEX_WORK)) {
				this.getTimezoneShiftTwo().correctData(oldDomain.getTimezoneShiftTwo());
			} else {
				TimezoneUse timeZone2 = this.getTimezoneShiftTwo();
				if (!timeZone2.isUsed()) {
					this.getTimezoneShiftTwo().restoreTime(oldDomain.getTimezoneShiftTwo());
				}
			}
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param workTimeType
	 *            the work time type
	 */
	public void correctDefaultData(ScreenMode screenMode, WorkTimeDivision workTimeType) {
		if (screenMode == ScreenMode.SIMPLE) {
			// Simple mode
			this.getTimezoneShiftTwo().correctDefaultData();
		}

		if (screenMode == ScreenMode.DETAIL) {
			// Detail mode
			if ((workTimeType.getWorkTimeDailyAtr().equals(WorkTimeDailyAtr.REGULAR_WORK)
					&& workTimeType.getWorkTimeMethodSet().equals(WorkTimeMethodSet.DIFFTIME_WORK))
					|| workTimeType.getWorkTimeDailyAtr().equals(WorkTimeDailyAtr.FLEX_WORK)) {
				this.getTimezoneShiftTwo().correctDefaultData();
			} else {
				TimezoneUse timeZone2 = this.getTimezoneShiftTwo();
				if (!timeZone2.isUsed()) {
					this.getTimezoneShiftTwo().correctDefaultData();
				}
			}
		}
	}

	/**
	 * Update morning end time.
	 *
	 * @param morningEndTime the morning end time
	 */
	public void updateMorningEndTime(TimeWithDayAttr morningEndTime) {
		this.morningEndTime = morningEndTime;
	}

	/**
	 * Update afternoon start time.
	 *
	 * @param afternoonStartTime the afternoon start time
	 */
	public void updateAfternoonStartTime(TimeWithDayAttr afternoonStartTime) {
		this.afternoonStartTime = afternoonStartTime;
	}
	
	@Override
	public PrescribedTimezoneSetting clone() {
		PrescribedTimezoneSetting cloned = new PrescribedTimezoneSetting();
		try {
			cloned.morningEndTime = new TimeWithDayAttr(this.morningEndTime.valueAsMinutes());
			cloned.afternoonStartTime = new TimeWithDayAttr(this.afternoonStartTime.valueAsMinutes());
			cloned.lstTimezone = this.lstTimezone.stream().map(c -> c.clone()).collect(Collectors.toList());
		}
		catch (Exception e){
			throw new RuntimeException("PrescribedTimezoneSetting clone error.");
		}
		return cloned;
	}
	
}

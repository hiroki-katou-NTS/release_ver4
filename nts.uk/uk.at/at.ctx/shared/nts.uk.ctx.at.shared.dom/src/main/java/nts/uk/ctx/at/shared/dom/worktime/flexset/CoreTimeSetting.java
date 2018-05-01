/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flexset;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.usecls.ApplyAtr;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class CoreTimeSetting.
 */
@Getter
// コアタイム時間帯設定
public class CoreTimeSetting extends WorkTimeDomainObject {

	/** The core time sheet. */
	// コアタイム時間帯
	private TimeSheet coreTimeSheet;

	/** The timesheet. */
	// 使用区分
	private ApplyAtr timesheet;

	/** The min work time. */
	// 最低勤務時間
	private AttendanceTime minWorkTime;

	/** The Constant ZERO_MINUTES. */
	// 00:00
	public static final int ZERO_MINUTES = 0;

	/**
	 * Instantiates a new core time setting.
	 *
	 * @param memento
	 *            the memento
	 */
	public CoreTimeSetting(CoreTimeSettingGetMemento memento) {
		this.coreTimeSheet = memento.getCoreTimeSheet();
		this.timesheet = memento.getTimesheet();
		this.minWorkTime = memento.getMinWorkTime();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(CoreTimeSettingSetMemento memento) {
		memento.setCoreTimeSheet(this.coreTimeSheet);
		memento.setTimesheet(this.timesheet);
		memento.setMinWorkTime(this.minWorkTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		// コアタイム時間帯.開始時刻 >= コアタイム時間帯.終了時刻 => Msg_770
		if (this.isUseTimeSheet()
				&& this.coreTimeSheet.getStartTime().greaterThanOrEqualTo(this.coreTimeSheet.getEndTime())) {
			this.bundledBusinessExceptions.addMessage("Msg_770", "KMK003_157");
		}

		// 使用区分 = 使用しない AND 最低勤務時間 <= 0
		if (!this.isUseTimeSheet() && (this.minWorkTime == null || this.minWorkTime.valueAsMinutes() <= ZERO_MINUTES)) {
			this.bundledBusinessExceptions.addMessage("Msg_776");
		}

		super.validate();
	}

	/**
	 * Checks if is use time sheet.
	 *
	 * @return true, if is use time sheet
	 */
	public boolean isUseTimeSheet() {
		return this.timesheet == ApplyAtr.USE;
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(ScreenMode screenMode, CoreTimeSetting oldDomain) {
		if (ApplyAtr.NOT_USE.equals(this.timesheet)) {
			this.coreTimeSheet = oldDomain.getCoreTimeSheet();
		} else {
			this.minWorkTime = oldDomain.getMinWorkTime();
		}
	}

	/**
	 * Correct default data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctDefaultData(ScreenMode screenMode) {
		if (ApplyAtr.NOT_USE.equals(this.timesheet)) {
			this.coreTimeSheet = TimeSheet.createDefault();
		} else {
			this.minWorkTime = new AttendanceTime(ZERO_MINUTES);
		}
	}
}

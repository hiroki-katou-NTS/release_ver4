/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.ScreenMode;

/**
 * The Class WorkTimezoneShortTimeWorkSet.
 */
// 就業時間帯の短時間勤務設定
@Getter
@NoArgsConstructor
public class WorkTimezoneShortTimeWorkSet extends WorkTimeDomainObject implements Cloneable{

	/** The nurs timezone work use. */
	// 介護時間帯に勤務した場合に勤務として扱う
	private boolean nursTimezoneWorkUse;

	/** The employment time deduct. */
	// 就業時間から控除する
	private boolean employmentTimeDeduct;

	/** The child care work use. */
	// 育児時間帯に勤務した場合に勤務として扱う
	private boolean childCareWorkUse;

	/**
	 * Instantiates a new work timezone short time work set.
	 *
	 * @param nursTimezoneWorkUse
	 *            the nurs timezone work use
	 * @param employmentTimeDeduct
	 *            the employment time deduct
	 * @param childCareWorkUse
	 *            the child care work use
	 */
	public WorkTimezoneShortTimeWorkSet(boolean nursTimezoneWorkUse, boolean employmentTimeDeduct,
			boolean childCareWorkUse) {
		super();
		this.nursTimezoneWorkUse = nursTimezoneWorkUse;
		this.employmentTimeDeduct = employmentTimeDeduct;
		this.childCareWorkUse = childCareWorkUse;
	}

	/**
	 * Instantiates a new work timezone short time work set.
	 *
	 * @param memento
	 *            the memento
	 */
	public WorkTimezoneShortTimeWorkSet(WorkTimezoneShortTimeWorkSetGetMemento memento) {
		this.nursTimezoneWorkUse = memento.getNursTimezoneWorkUse();
		this.employmentTimeDeduct = memento.getEmploymentTimeDeduct();
		this.childCareWorkUse = memento.getChildCareWorkUse();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WorkTimezoneShortTimeWorkSetSetMemento memento) {
		memento.setNursTimezoneWorkUse(this.nursTimezoneWorkUse);
		memento.setEmploymentTimeDeduct(this.employmentTimeDeduct);
		memento.setChildCareWorkUse(this.childCareWorkUse);
	}

	/**
	 * Correct data.
	 *
	 * @param screenMode
	 *            the screen mode
	 */
	public void correctData(ScreenMode screenMode) {
		if (ScreenMode.SIMPLE.equals(screenMode)) {
			this.nursTimezoneWorkUse = false;
			this.childCareWorkUse = false;
		}
	}
	
	@Override
	public WorkTimezoneShortTimeWorkSet clone() {
		WorkTimezoneShortTimeWorkSet cloned = new WorkTimezoneShortTimeWorkSet();
		try {
			cloned.nursTimezoneWorkUse = this.nursTimezoneWorkUse ? true : false ;
			cloned.employmentTimeDeduct = this.employmentTimeDeduct ? true : false ;
			cloned.childCareWorkUse = this.childCareWorkUse ? true : false ;
		}
		catch (Exception e){
			throw new RuntimeException("WorkTimezoneShortTimeWorkSet clone error.");
		}
		return cloned;
	}
}

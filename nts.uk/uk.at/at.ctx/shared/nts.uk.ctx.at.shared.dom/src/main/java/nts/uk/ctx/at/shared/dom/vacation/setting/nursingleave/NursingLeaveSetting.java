/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;

/**
 * The class 介護看護休暇設定.
 */
@Getter
public class NursingLeaveSetting extends AggregateRoot {

	/** 会社ID */
	private String companyId;

	/** 管理区分 */
	private ManageDistinct manageType;

	/** 介護看護区分 */
	private NursingCategory nursingCategory;

	/** 起算日 */
	private Integer startMonthDay;

	/** 上限人数設定 */
	private MaxPersonSetting maxPersonSetting;

	/** 勤務種類コード */
	private List<String> workTypeCodes;

	/**
	 * Checks if is managed.
	 *
	 * @return true, if is managed
	 */
	public boolean isManaged() {
		return this.manageType.equals(ManageDistinct.YES);
	}

	/**
	 * Instantiates a new nursing vacation setting.
	 *
	 * @param memento
	 *            the memento
	 */
	public NursingLeaveSetting(NursingLeaveSettingGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.manageType = memento.getManageType();
		this.nursingCategory = memento.getNursingCategory();
		this.startMonthDay = memento.getStartMonthDay();
		this.maxPersonSetting = memento.getMaxPersonSetting();
		this.workTypeCodes = memento.getWorkTypeCodes();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(NursingLeaveSettingSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setManageType(this.manageType);
		memento.setNursingCategory(this.nursingCategory);
		memento.setStartMonthDay(this.startMonthDay);
		memento.setMaxPersonSetting(this.maxPersonSetting);
		memento.setWorkTypeCodes(this.workTypeCodes);
	}
}

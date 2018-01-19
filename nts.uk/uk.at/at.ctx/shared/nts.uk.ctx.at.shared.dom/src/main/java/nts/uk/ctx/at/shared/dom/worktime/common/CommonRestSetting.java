/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class CommonRestSetting.
 */
// 共通の休憩設定
@Getter
public class CommonRestSetting extends DomainObject {

	/** The calculate method. */
	// 休憩時間中に退勤した場合の計算方法
	private RestTimeOfficeWorkCalcMethod calculateMethod;

	/**
	 * Instantiates a new common rest setting.
	 *
	 * @param calculateMethod the calculate method
	 */
	public CommonRestSetting(RestTimeOfficeWorkCalcMethod calculateMethod) {
		super();
		this.calculateMethod = calculateMethod;
	}

	/**
	 * Instantiates a new common rest setting.
	 *
	 * @param memento
	 *            the memento
	 */
	public CommonRestSetting(CommonRestSettingGetmemento memento) {
		this.calculateMethod = memento.getCalculateMethod();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(CommonRestSettingSetmemento memento) {
		memento.setCalculateMethod(this.calculateMethod);
	}

	/*
	 * : 休憩時間中に退勤した場合の計算方法を変更する
	 */
	public void changeCalcMethodToRecordUntilLeaveWork() {
		this.calculateMethod = RestTimeOfficeWorkCalcMethod.OFFICE_WORK_APPROP_ALL;
	}

}

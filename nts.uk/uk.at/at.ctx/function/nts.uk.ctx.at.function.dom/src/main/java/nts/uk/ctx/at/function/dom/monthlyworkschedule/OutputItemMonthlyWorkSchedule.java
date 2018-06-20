/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.dom.monthlyworkschedule;

import java.util.List;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class OutputItemMonthlyWorkSchedule.
 */
// 月別勤務表の出力項目
@Getter
public class OutputItemMonthlyWorkSchedule extends AggregateRoot {

	/** The company ID. */
	// 会社ID
	private String companyID;

	/** The item code. */
	// コード
	private MonthlyOutputItemSettingCode itemCode;

	/** The item name. */
	// 名称
	private MonthlyOutputItemSettingName itemName;

	/** The lst displayed attendance. */
	// 表示する勤怠項目
	private List<MonthlyAttendanceItemsDisplay> lstDisplayedAttendance;

	/** The is print. */
	// 備考欄の印字設定
	private PrintSettingRemarksColumn printSettingRemarksColumn;

	/**
	 * Instantiates a new output item monthly work schedule.
	 *
	 * @param memento
	 *            the memento
	 */
	public OutputItemMonthlyWorkSchedule(OutputItemMonthlyWorkScheduleGetMemento memento) {
		if (memento.getCompanyID() == null) {
			this.companyID = AppContexts.user().companyId();
		} else {
			this.companyID = memento.getCompanyID();
		}
		this.itemCode = memento.getItemCode();
		this.itemName = memento.getItemName();
		this.lstDisplayedAttendance = memento.getLstDisplayedAttendance();
		this.printSettingRemarksColumn = memento.getPrintSettingRemarksColumn();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(OutputItemMonthlyWorkScheduleSetMemento memento) {
		if (this.companyID == null) {
			this.companyID = AppContexts.user().companyId();
		}
		memento.setCompanyID(this.companyID);
		memento.setItemCode(this.itemCode);
		memento.setItemName(this.itemName);
		memento.setLstDisplayedAttendance(this.lstDisplayedAttendance);
		memento.setPrintRemarksColumn(this.printSettingRemarksColumn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		// execute algorithm アルゴリズム「登録チェック処理」を実行する to check C7_8 exist element?
		if (this.lstDisplayedAttendance.isEmpty() || this.lstDisplayedAttendance == null) {
			throw new BusinessException("Msg_880");
		}
	}

	/**
	 * Instantiates a new output item monthly work schedule.
	 */
	public OutputItemMonthlyWorkSchedule() {
		super();
	}
}

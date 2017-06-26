/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.insurance;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * The Class ChecklistPrintSetting.
 */
@Getter
public class ChecklistPrintSetting extends AggregateRoot {

	/** The company code. */
	private String companyCode;

	/** The show category insurance item. */
	private Boolean showCategoryInsuranceItem;

	/** The show delivery notice amount. */
	private Boolean showDeliveryNoticeAmount;

	/** The show detail. */
	private Boolean showDetail;

	/** The show office. */
	private Boolean showOffice;
	
	/** The show total. */
    private Boolean showTotal;

	/**
	 * Instantiates a new checklist print setting.
	 *
	 * @param memento the memento
	 */
	public ChecklistPrintSetting(ChecklistPrintSettingGetMemento memento) {
		super();
		this.companyCode = memento.getCompanyCode();
		this.showCategoryInsuranceItem = memento.getShowCategoryInsuranceItem();
		this.showDeliveryNoticeAmount = memento.getShowDeliveryNoticeAmount();
		this.showDetail = memento.getShowDetail();
		this.showOffice = memento.getShowOffice();
		this.showTotal = memento.getShowTotal();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(ChecklistPrintSettingSetMemento memento) {
		memento.setCompanyCode(this.companyCode);
		memento.setShowCategoryInsuranceItem(this.showCategoryInsuranceItem);
		memento.setShowDeliveryNoticeAmount(this.showDeliveryNoticeAmount);
		memento.setShowDetail(this.showDetail);
		memento.setShowOffice(this.showOffice);
		memento.setShowTotal(this.showTotal);
	}

}

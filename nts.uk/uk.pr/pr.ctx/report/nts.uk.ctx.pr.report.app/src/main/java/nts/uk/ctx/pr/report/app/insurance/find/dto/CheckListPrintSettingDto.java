/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.insurance.find.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento;

/**
 * The Class CheckListPrintSettingFindOutDto.
 */
@Setter
@Getter
public class CheckListPrintSettingDto implements ChecklistPrintSettingSetMemento {

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
	 * Creates the default setting.
	 *
	 * @return the check list print setting dto
	 */
	public static CheckListPrintSettingDto createDefaultSetting( ) {
		CheckListPrintSettingDto dto = new CheckListPrintSettingDto();
		dto.showCategoryInsuranceItem = true;
		dto.showDeliveryNoticeAmount = true;
		dto.showDetail = true;
		dto.showOffice = true;
		dto.showTotal = true;
		return dto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setCompanyCode(nts.uk.ctx.pr.report.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowCategoryInsuranceItem(java.lang.Boolean)
	 */
	@Override
	public void setShowCategoryInsuranceItem(Boolean showCategoryInsuranceItem) {
		this.showCategoryInsuranceItem = showCategoryInsuranceItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowDeliveryNoticeAmount(java.lang.Boolean)
	 */
	@Override
	public void setShowDeliveryNoticeAmount(Boolean showDeliveryNoticeAmount) {
		this.showDeliveryNoticeAmount = showDeliveryNoticeAmount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowDetail(java.lang.Boolean)
	 */
	@Override
	public void setShowDetail(Boolean showDetail) {
		this.showDetail = showDetail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowOffice(java.lang.Boolean)
	 */
	@Override
	public void setShowOffice(Boolean showOffice) {
		this.showOffice = showOffice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingSetMemento#
	 * setShowTotal(java.lang.Boolean)
	 */
	@Override
	public void setShowTotal(Boolean showTotal) {
		this.showTotal = showTotal;
	}

}

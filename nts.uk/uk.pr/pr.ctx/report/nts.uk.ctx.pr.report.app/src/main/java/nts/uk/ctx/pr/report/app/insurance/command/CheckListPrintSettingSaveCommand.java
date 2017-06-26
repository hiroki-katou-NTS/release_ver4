/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.insurance.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSetting;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento;

/**
 * The Class CheckListPrintSettingSaveCommand.
 */
@Getter
@Setter
public class CheckListPrintSettingSaveCommand {

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
	 * To domain.
	 *
	 * @param companyCode the company code
	 * @return the checklist print setting
	 */
	public ChecklistPrintSetting toDomain(String companyCode) {
		return new ChecklistPrintSetting(new PrintSettingGetMementoImpl(this, companyCode));
	}

	/**
	 * The Class ChecklistPrintSettingGetMementoImpl.
	 */
	public class PrintSettingGetMementoImpl implements ChecklistPrintSettingGetMemento {

		/** The command. */
		private CheckListPrintSettingSaveCommand command;

		/** The company code. */
		private String companyCode;

		/**
		 * Instantiates a new checklist print setting get memento impl.
		 *
		 * @param command the command
		 * @param companyCode the company code
		 */
		public PrintSettingGetMementoImpl(CheckListPrintSettingSaveCommand command, String companyCode) {
			super();
			this.command = command;
			this.companyCode = companyCode;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
		 * getShowOffice()
		 */
		@Override
		public Boolean getShowOffice() {
			return this.command.showOffice;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
		 * getShowDetail()
		 */
		@Override
		public Boolean getShowDetail() {
			return this.command.showDetail;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
		 * getShowDeliveryNoticeAmount()
		 */
		@Override
		public Boolean getShowDeliveryNoticeAmount() {
			return this.command.showDeliveryNoticeAmount;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
		 * getShowCategoryInsuranceItem()
		 */
		@Override
		public Boolean getShowCategoryInsuranceItem() {
			return this.command.showCategoryInsuranceItem;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
		 * getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return this.companyCode;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingGetMemento#
		 * getShowTotal()
		 */
		@Override
		public Boolean getShowTotal() {
			return this.command.showTotal;
		}

	}
}

/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import java.util.ArrayList;
import java.util.List;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySetting;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingName;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerFormHead;

/**
 * The Class JpaWLOutputSettingGetMemento.
 */
public class JpaWLOutputSettingGetMemento implements WLOutputSettingGetMemento {
	
	/** The entity. */
	private QlsptLedgerFormHead entity;
	
	/**
	 * Instantiates a new jpa WL output setting get memento.
	 *
	 * @param entity the entity
	 */
	public JpaWLOutputSettingGetMemento(QlsptLedgerFormHead entity) {
		super();
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
	 * #getCode()
	 */
	@Override
	public WLOutputSettingCode getCode() {
		return new WLOutputSettingCode(
				this.entity.getQlsptLedgerFormHeadPK().getFormCd());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
	 * #getName()
	 */
	@Override
	public WLOutputSettingName getName() {
		return new WLOutputSettingName(this.entity.getFormName());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
	 * #getOnceSheetPerPerson()
	 */
	@Override
	public Boolean getOnceSheetPerPerson() {
		// 1 => true.
		// 0 => false.
		return this.entity.getPrint1pageByPersonSet() == 1;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
	 * #getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.entity.getQlsptLedgerFormHeadPK().getCcd();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
	 * #getCategorySettings()
	 */
	@Override
	public List<WLCategorySetting> getCategorySettings() {
		List<WLCategorySetting> categorySettings = new ArrayList<>();
		
		// Add Salary Payment category.
		categorySettings.add(new WLCategorySetting(
				new JpaWLCategorySettingGetMemento(this.entity, WLCategory.Payment, PaymentType.Salary)));

		// Add Salary Deduction category.
		categorySettings.add(new WLCategorySetting(
				new JpaWLCategorySettingGetMemento(this.entity, WLCategory.Deduction, PaymentType.Salary)));

		// Add Salary Attendance category.
		categorySettings.add(new WLCategorySetting(
				new JpaWLCategorySettingGetMemento(this.entity, WLCategory.Attendance, PaymentType.Salary)));

		// Add Bonus Payment category.
		categorySettings.add(new WLCategorySetting(
				new JpaWLCategorySettingGetMemento(this.entity, WLCategory.Payment, PaymentType.Bonus)));

		// Add Bonus Deduction category.
		categorySettings.add(new WLCategorySetting(
				new JpaWLCategorySettingGetMemento(this.entity, WLCategory.Deduction, PaymentType.Bonus)));

		// Add Bonus Attendance category.
		categorySettings.add(new WLCategorySetting(
				new JpaWLCategorySettingGetMemento(this.entity, WLCategory.Attendance, PaymentType.Bonus)));

		return categorySettings;
	}

}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import java.util.ArrayList;
import java.util.List;

import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingCode;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingGetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingName;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstFormHead;

/**
 * The Class JpaSalaryOutputSettingGetMemento.
 */
public class JpaSalaryOutputSettingGetMemento implements SalaryOutputSettingGetMemento {

	/** The entity. */
	private QlsptPaylstFormHead entity;

	/**
	 * Instantiates a new jpa salary output setting get memento.
	 *
	 * @param entity the entity
	 */
	public JpaSalaryOutputSettingGetMemento(QlsptPaylstFormHead entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.entity.getQlsptPaylstFormHeadPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingGetMemento#getCode()
	 */
	@Override
	public SalaryOutputSettingCode getCode() {
		return new SalaryOutputSettingCode(this.entity.getQlsptPaylstFormHeadPK().getFormCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingGetMemento#getName()
	 */
	@Override
	public SalaryOutputSettingName getName() {
		return new SalaryOutputSettingName(this.entity.getFormName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingGetMemento#getCategorySettings()
	 */
	@Override
	public List<SalaryCategorySetting> getCategorySettings() {
		List<SalaryCategorySetting> categorySettings = new ArrayList<>();

		// Add Salary Payment category.
		categorySettings.add(
				new SalaryCategorySetting(new JpaSalaryCategorySettingGetMemento(this.entity, SalaryCategory.Payment)));

		// Add Salary Deduction category.
		categorySettings.add(new SalaryCategorySetting(
				new JpaSalaryCategorySettingGetMemento(this.entity, SalaryCategory.Deduction)));

		// Add Salary Attendance category.
		categorySettings.add(new SalaryCategorySetting(
				new JpaSalaryCategorySettingGetMemento(this.entity, SalaryCategory.Attendance)));

		// Add Salary ArticleOthers category.
		categorySettings.add(new SalaryCategorySetting(
				new JpaSalaryCategorySettingGetMemento(this.entity, SalaryCategory.ArticleOthers)));

		return categorySettings;
	}

}

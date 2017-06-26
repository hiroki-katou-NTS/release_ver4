/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingCode;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingName;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingSetMemento;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstFormHead;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstFormHeadPK;

/**
 * The Class JpaSalaryOutputSettingSetMemento.
 */
public class JpaSalaryOutputSettingSetMemento implements SalaryOutputSettingSetMemento {

	/** The company code. */
	private String companyCode;

	/** The code. */
	private SalaryOutputSettingCode code;

	/** The entity. */
	private QlsptPaylstFormHead entity;

	/**
	 * Instantiates a new jpa salary output setting set memento.
	 *
	 * @param entity the entity
	 */
	public JpaSalaryOutputSettingSetMemento(QlsptPaylstFormHead entity) {
		super();
		this.entity = entity;
		if (this.entity.getQlsptPaylstFormHeadPK() == null) {
			this.entity.setQlsptPaylstFormHeadPK(new QlsptPaylstFormHeadPK());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingSetMemento#setCompanyCode(nts.uk.ctx.pr.report.dom.
	 * company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.entity.getQlsptPaylstFormHeadPK().setCcd(companyCode);
		this.companyCode = companyCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingSetMemento#setCode(nts.uk.ctx.pr.report.dom.
	 * salarydetail.outputsetting.SalaryOutputSettingCode)
	 */
	@Override
	public void setCode(SalaryOutputSettingCode salaryOutputSettingCode) {
		this.entity.getQlsptPaylstFormHeadPK().setFormCd(salaryOutputSettingCode.v());
		this.code = salaryOutputSettingCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingSetMemento#setName(nts.uk.ctx.pr.report.dom.
	 * salarydetail.outputsetting.SalaryOutputSettingName)
	 */
	@Override
	public void setName(SalaryOutputSettingName salaryOutputSettingName) {
		this.entity.setFormName(salaryOutputSettingName.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingSetMemento#setCategorySettings(java.util.List)
	 */
	@Override
	public void setCategorySettings(List<SalaryCategorySetting> listSalaryCategorySetting) {
		this.entity.setQlsptPaylstFormDetailList(listSalaryCategorySetting.stream().map(category -> {
			JpaSalaryCategorySettingSetMemento setMemento = new JpaSalaryCategorySettingSetMemento(this.companyCode,
					this.code);
			category.saveToMemento(setMemento);
			return setMemento.getCategoryEntities();
		}).flatMap(category -> category.stream()).collect(Collectors.toList()));
	}

}

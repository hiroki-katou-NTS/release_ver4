/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySettingSetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItem;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingCode;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstFormDetail;

/**
 * The Class JpaSalaryCategorySettingSetMemento.
 */
public class JpaSalaryCategorySettingSetMemento implements SalaryCategorySettingSetMemento {

	/** The category entities. */
	@Getter
	private List<QlsptPaylstFormDetail> categoryEntities;

	/** The company code. */
	private String companyCode;

	/** The code. */
	private SalaryOutputSettingCode code;

	/**
	 * Instantiates a new jpa salary category setting set memento.
	 *
	 * @param companyCode the company code
	 * @param code the code
	 */
	public JpaSalaryCategorySettingSetMemento(String companyCode, SalaryOutputSettingCode code) {
		this.companyCode = companyCode;
		this.code = code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryCategorySettingSetMemento#setSalaryCategory(nts.uk.ctx.pr.report.
	 * dom.salarydetail.SalaryCategory)
	 */
	@Override
	public void setSalaryCategory(SalaryCategory salaryCategory) {
		categoryEntities.forEach(category -> category.getQlsptPaylstFormDetailPK().setCtgAtr(salaryCategory.value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryCategorySettingSetMemento#setSalaryOutputItems(java.util.List)
	 */
	@Override
	public void setSalaryOutputItems(List<SalaryOutputItem> listSalaryOutputItem) {
		this.categoryEntities = listSalaryOutputItem.stream().map(item -> {
			QlsptPaylstFormDetail detail = new QlsptPaylstFormDetail();

			// Convert setting items to entities.
			item.saveToMemento(new JpaSalaryOutputItemSetMemento(detail, this.companyCode, this.code));
			return detail;
		}).collect(Collectors.toList());
	}

}

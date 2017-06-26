/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.LazyList;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySettingGetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItem;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstFormHead;

/**
 * The Class JpaSalaryCategorySettingGetMemento.
 */
public class JpaSalaryCategorySettingGetMemento implements SalaryCategorySettingGetMemento {

	/** The entity. */
	private QlsptPaylstFormHead entity;

	/** The category. */
	private SalaryCategory category;

	/**
	 * Instantiates a new jpa salary category setting get memento.
	 *
	 * @param entity the entity
	 * @param category the category
	 */
	public JpaSalaryCategorySettingGetMemento(QlsptPaylstFormHead entity, SalaryCategory category) {
		super();
		this.entity = entity;
		this.category = category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryCategorySettingGetMemento#getSalaryCategory()
	 */
	@Override
	public SalaryCategory getSalaryCategory() {
		return this.category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryCategorySettingGetMemento#getSalaryOutputItems()
	 */
	@Override
	public List<SalaryOutputItem> getSalaryOutputItems() {
		return LazyList.withMap(
				() -> this.entity.getQlsptPaylstFormDetailList().stream()
						.filter(item -> item.getQlsptPaylstFormDetailPK().getCtgAtr() == this.category.value)
						.collect(Collectors.toList()),
				detail -> new SalaryOutputItem(new JpaSalaryOutputItemGetMemento(detail)));
	}

}

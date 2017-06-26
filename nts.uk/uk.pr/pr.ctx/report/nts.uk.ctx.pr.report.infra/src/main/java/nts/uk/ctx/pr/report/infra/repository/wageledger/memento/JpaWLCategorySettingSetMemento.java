/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingSetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItem;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerFormDetail;

/**
 * The Class JpaWLCategorySettingSetMemento.
 */
public class JpaWLCategorySettingSetMemento implements WLCategorySettingSetMemento {
	
	/** The category entitys. */
	@Getter
	private List<QlsptLedgerFormDetail> categoryEntities;
	
	/** The company code. */
	private String companyCode;
	
	/** The code. */
	private WLOutputSettingCode code;
	
	/**
	 * Instantiates a new jpa WL category setting set memento.
	 *
	 * @param entity the entity
	 */
	public JpaWLCategorySettingSetMemento(String companyCode,
			WLOutputSettingCode code) {
		this.companyCode = companyCode;
		this.code = code;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingSetMemento
	 * #setCategory(nts.uk.ctx.pr.report.dom.wageledger.WLCategory)
	 */
	@Override
	public void setCategory(WLCategory category) {
		this.categoryEntities.forEach(
				cate -> cate.getQlsptLedgerFormDetailPK().setCtgAtr(category.value));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingSetMemento
	 * #setPaymentType(nts.uk.ctx.pr.report.dom.wageledger.PaymentType)
	 */
	@Override
	public void setPaymentType(PaymentType paymentType) {
		this.categoryEntities.forEach(
				cate -> cate.getQlsptLedgerFormDetailPK().setPayBonusAtr(paymentType.value));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingSetMemento
	 * #setOutputItems(java.util.List)
	 */
	@Override
	public void setOutputItems(List<WLSettingItem> outputItems) {
		this.categoryEntities = outputItems.stream().map(item -> {
			QlsptLedgerFormDetail detail = new QlsptLedgerFormDetail();
			
			// Convert setting items to entities.
			item.saveToMemento(new JpaWLSettingItemSetMemento(detail, this.companyCode, this.code));
			return detail;
		}).collect(Collectors.toList());
	}

}

/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLItemType;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemSetMemento;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerFormDetail;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerFormDetailPK;

/**
 * The Class JpaWLSettingItemSetMemento.
 */
public class JpaWLSettingItemSetMemento implements WLSettingItemSetMemento {
	
	/** The entity. */
	private QlsptLedgerFormDetail entity;
	
	/**
	 * Instantiates a new jpa WL setting item set memento.
	 *
	 * @param entity the entity
	 */
	public JpaWLSettingItemSetMemento(QlsptLedgerFormDetail entity, String companyCode,
			WLOutputSettingCode code) {
		this.entity = entity;
		if (this.entity.getQlsptLedgerFormDetailPK() == null) {
			this.entity.setQlsptLedgerFormDetailPK(new QlsptLedgerFormDetailPK());
		}
		this.entity.getQlsptLedgerFormDetailPK().setCcd(companyCode);
		this.entity.getQlsptLedgerFormDetailPK().setFormCd(code.v());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemSetMemento
	 * #setLinkageCode(java.lang.String)
	 */
	@Override
	public void setLinkageCode(String linkageCode) {
		this.entity.getQlsptLedgerFormDetailPK().setItemAgreCd(linkageCode);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemSetMemento
	 * #setType(nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLItemType)
	 */
	@Override
	public void setType(WLItemType type) {
		this.entity.getQlsptLedgerFormDetailPK().setAggregateAtr(type.value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemSetMemento
	 * #setOrderNumber(int)
	 */
	@Override
	public void setOrderNumber(int orderNumber) {
		this.entity.setDispOrder(orderNumber);
	}

	

}

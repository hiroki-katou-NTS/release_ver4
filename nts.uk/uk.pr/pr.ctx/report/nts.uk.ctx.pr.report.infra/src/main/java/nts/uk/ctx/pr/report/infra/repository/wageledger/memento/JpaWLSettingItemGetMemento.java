/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLItemType;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerFormDetail;

/**
 * The Class JpaWLSettingItemGetMemento.
 */
public class JpaWLSettingItemGetMemento implements WLSettingItemGetMemento {
	
	/** The entity. */
	private QlsptLedgerFormDetail entity;
	
	/**
	 * Instantiates a new jpa WL setting item get memento.
	 *
	 * @param entity the entity
	 */
	public JpaWLSettingItemGetMemento(QlsptLedgerFormDetail entity) {
		super();
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento#getLinkageCode()
	 */
	@Override
	public String getLinkageCode() {
		return this.entity.getQlsptLedgerFormDetailPK().getItemAgreCd();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento#getType()
	 */
	@Override
	public WLItemType getType() {
		return WLItemType.valueOf(
				this.entity.getQlsptLedgerFormDetailPK().getAggregateAtr());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento#getOrderNumber()
	 */
	@Override
	public int getOrderNumber() {
		return this.entity.getDispOrder();
	}

}

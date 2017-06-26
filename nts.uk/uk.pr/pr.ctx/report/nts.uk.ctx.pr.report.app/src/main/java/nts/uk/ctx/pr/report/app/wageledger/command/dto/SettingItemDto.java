/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command.dto;

import lombok.Setter;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLItemType;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItem;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento;

/**
 * The Class SettingItemDto.
 */
@Setter
public class SettingItemDto {
	
	/** The code. */
	private String code;
	
	/** The is aggregate. */
	private Boolean isAggregateItem;
	
	/** The order number. */
	private int orderNumber;
	
	/**
	 * To domain.
	 *
	 * @return the WL setting item
	 */
	public WLSettingItem toDomain() {
		return new WLSettingItem(new WLSettingItemGetMementoImpl(this));
	}
	
	/**
	 * The Class WLSettingItemGetMementoImpl.
	 */
	public class WLSettingItemGetMementoImpl implements WLSettingItemGetMemento {
		
		/** The dto. */
		private SettingItemDto dto;
		
		/**
		 * Instantiates a new WL setting item get memento impl.
		 *
		 * @param dto the dto
		 */
		public WLSettingItemGetMementoImpl(SettingItemDto dto) {
			super();
			this.dto = dto;
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento
		 * #getLinkageCode()
		 */
		@Override
		public String getLinkageCode() {
			return this.dto.code;
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento
		 * #getType()
		 */
		@Override
		public WLItemType getType() {
			return dto.isAggregateItem ? WLItemType.Aggregate : WLItemType.Master;
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento
		 * #getOrderNumber()
		 */
		@Override
		public int getOrderNumber() {
			return this.dto.orderNumber;
		}
		
	}
}

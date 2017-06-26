/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySetting;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingGetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItem;

/**
 * The Class CategorySettingDto.
 */
@Setter
public class CategorySettingDto {

	/** The category. */
	private WLCategory category;

	/** The payment type. */
	private PaymentType paymentType;

	/** The output items. */
	private List<SettingItemDto> outputItems;

	/**
	 * To domain.
	 *
	 * @return the WL category setting
	 */
	public WLCategorySetting toDomain() {
		return new WLCategorySetting(new WLCategorySettingGetMementoImpl(this));
	}

	/**
	 * The Class WLCategorySettingGetMementoImpl.
	 */
	public class WLCategorySettingGetMementoImpl implements WLCategorySettingGetMemento {

		/** The dto. */
		private CategorySettingDto dto;

		/**
		 * Instantiates a new WL category setting get memento impl.
		 *
		 * @param dto the dto
		 */
		public WLCategorySettingGetMementoImpl(CategorySettingDto dto) {
			super();
			this.dto = dto;
		}

		/*
		 * (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.
		 * WLCategorySettingGetMemento#getPaymentType()
		 */
		@Override
		public PaymentType getPaymentType() {
			return dto.paymentType;
		}

		/*
		 * (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.
		 * WLCategorySettingGetMemento#getOutputItems()
		 */
		@Override
		public List<WLSettingItem> getOutputItems() {
			return dto.outputItems.stream().map(SettingItemDto::toDomain).collect(Collectors.toList());
		}

		/*
		 * (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.
		 * WLCategorySettingGetMemento#getCategory()
		 */
		@Override
		public WLCategory getCategory() {
			return dto.category;
		}

	}
}

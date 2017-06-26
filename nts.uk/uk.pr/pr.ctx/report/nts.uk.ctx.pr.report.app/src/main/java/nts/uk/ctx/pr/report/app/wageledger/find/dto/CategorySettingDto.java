/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.find.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingSetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItem;

/**
 * The Class CategorySettingDto.
 */
@Builder
public class CategorySettingDto implements WLCategorySettingSetMemento {
	
	/** The category. */
	public WLCategory category;
	
	/** The payment type. */
	public PaymentType paymentType;
	
	/** The output items. */
	public List<SettingItemDto> outputItems;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingSetMemento
	 * #setCategory(nts.uk.ctx.pr.report.dom.wageledger.WLCategory)
	 */
	@Override
	public void setCategory(WLCategory category) {
		this.category = category;
		
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingSetMemento
	 * #setPaymentType(nts.uk.ctx.pr.report.dom.wageledger.PaymentType)
	 */
	@Override
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingSetMemento
	 * #setOutputItems(java.util.List)
	 */
	@Override
	public void setOutputItems(List<WLSettingItem> outputItems) {
		this.outputItems = outputItems.stream().map(item -> {
			SettingItemDto itemDto = SettingItemDto.builder().build();
			item.saveToMemento(itemDto);
			return itemDto;
		}).sorted((a, b) -> Integer.compare(a.orderNumber, b.orderNumber)).collect(Collectors.toList());
	}
}

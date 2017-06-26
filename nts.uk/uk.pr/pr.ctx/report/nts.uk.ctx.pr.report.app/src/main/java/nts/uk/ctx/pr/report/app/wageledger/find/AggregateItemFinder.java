/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.find;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.pr.report.app.itemmaster.query.ItemMaterRepository;
import nts.uk.ctx.pr.report.app.itemmaster.query.MasterItemDto;
import nts.uk.ctx.pr.report.app.wageledger.command.dto.ItemSubjectDto;
import nts.uk.ctx.pr.report.app.wageledger.find.dto.AggregateItemDto;
import nts.uk.ctx.pr.report.app.wageledger.find.dto.HeaderSettingDto;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class AggregateItemFinder.
 */
@Stateless
public class AggregateItemFinder {

	/** The repository. */
	@Inject
	private WLAggregateItemRepository repository;

	/** The item mater repo. */
	@Inject
	private ItemMaterRepository itemMaterRepo;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<HeaderSettingDto> findAll() {
		String companyCode = AppContexts.user().companyCode();
		return this.repository.findAll(companyCode).stream().map(item -> {
			return HeaderSettingDto.builder()
					.category(item.getSubject().getCategory())
					.paymentType(item.getSubject().getPaymentType())
					.code(item.getSubject().getCode().v())
					.name(item.getName().v())
					.build();
		}).collect(Collectors.toList());
	}

	/**
	 * Find by category and payment type.
	 *
	 * @param category the category
	 * @param paymentType the payment type
	 * @return the list
	 */
	public List<HeaderSettingDto> findByCategoryAndPaymentType(WLCategory category, PaymentType paymentType) {
		String companyCode = AppContexts.user().companyCode();
		return this.repository.findByCategoryAndPaymentType(companyCode, category, paymentType).stream().map(item -> {
			return HeaderSettingDto.builder()
					.category(item.getSubject().getCategory())
					.paymentType(item.getSubject().getPaymentType())
					.code(item.getSubject().getCode().v())
					.name(item.getName().v()).build();
		}).collect(Collectors.toList());
	}

	/**
	 * Find detail.
	 *
	 * @param subject the subject
	 * @return the aggregate item dto
	 */
	public AggregateItemDto findDetail(ItemSubjectDto subject) {
		String companyCode = AppContexts.user().companyCode();

		// Query data.
		val aggregateItem = this.repository.findByCode(subject.toDomain(companyCode));

		// Return dto.
		val dto = AggregateItemDto.builder().build();
		aggregateItem.saveToMemento(dto);

		// Find master item name.
		Map<String, MasterItemDto> itemMap = this.itemMaterRepo
				.findByCodes(companyCode, new ArrayList<>(aggregateItem.getSubItems())).stream()
				.filter(item -> item.category.value == subject.getCategory().value)
				.collect(Collectors.toMap(item -> item.code, Function.identity()));
		dto.subItems.forEach(item -> {
			item.name = itemMap.get(item.code).name;
		});
		return dto;
	}
}

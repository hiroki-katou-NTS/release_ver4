/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.find;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.pr.report.app.itemmaster.query.ItemMaterRepository;
import nts.uk.ctx.pr.report.app.itemmaster.query.MasterItemDto;
import nts.uk.ctx.pr.report.app.wageledger.find.dto.HeaderSettingDto;
import nts.uk.ctx.pr.report.app.wageledger.find.dto.OutputSettingDto;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItem;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemRepository;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSetting;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class OutputSettingFinder.
 */
@Stateless
public class OutputSettingFinder {

	/** The repository. */
	@Inject
	private WLOutputSettingRepository repository;

	/** The finder. */
	@Inject
	private ItemMaterRepository finder;

	/** The aggregate item repository. */
	@Inject
	private WLAggregateItemRepository aggregateItemRepository;

	/**
	 * Find.
	 *
	 * @param code the code
	 * @return the output setting dto
	 */
	public OutputSettingDto find(String code) {
		String companyCode = AppContexts.user().companyCode();
		Optional<WLOutputSetting> optOutputSetting = this.repository.findByCode(companyCode,
				new WLOutputSettingCode(code));

		OutputSettingDto dto = OutputSettingDto.builder().build();
		if (optOutputSetting.isPresent()) {
			optOutputSetting.get().saveToMemento(dto);
		} else {
			throw new BusinessException("entity not found.");
		}

		// Find master item name.
		List<String> masterItemCode = dto.categorySettings.stream()
				.map(category -> category.outputItems.stream()
						.filter(item -> !item.isAggregateItem)
						.map(item -> item.code)
						.collect(Collectors.toList()))
				.flatMap(item -> item.stream())
				.collect(Collectors.toList());
		List<MasterItemDto> masterItemMap = this.finder.findByCodes(companyCode, masterItemCode);

		// Find aggregate item name.
		List<String> aggregateItemCode = dto.categorySettings.stream()
				.map(category -> category.outputItems.stream()
						.filter(item -> item.isAggregateItem)
						.map(item -> item.code)
						.collect(Collectors.toList()))
				.flatMap(item -> item.stream())
				.collect(Collectors.toList());
		List<WLAggregateItem> aggregateItems = this.aggregateItemRepository
				.findByCodes(companyCode, aggregateItemCode);
		
		// Add master item name to output setting. 
		dto.categorySettings.forEach(cate -> {
			cate.outputItems.forEach(item -> {

				// Master item.
				if (!item.isAggregateItem) {
					MasterItemDto masterItem = masterItemMap.stream()
							.filter(mi -> mi.code.equals(item.code) 
									&& mi.category.value == cate.category.value)
							.findFirst().get();
					item.name = masterItem.name;
					return;
				}

				// Aggregate item.
				WLAggregateItem aggregateItem = aggregateItems.stream()
						.filter(ai -> ai.getSubject().getCode().v().equals(item.code)
								&& ai.getSubject().getCategory() == cate.category)
						.findFirst().get();
				item.name = aggregateItem.getName().v();
			});
		});
		return dto;
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<HeaderSettingDto> findAll() {
		String companyCode = AppContexts.user().companyCode();
		return this.repository.findAll(companyCode).stream().map(setting -> {
			return HeaderSettingDto.builder()
					.code(setting.getCode().v())
					.name(setting.getName().v())
					.build();
		}).collect(Collectors.toList());
	}
}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElementRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRefItem;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRefRepository;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtReferenceRepository;

/**
 * The Class CodeRefItemGenerator.
 */
@Stateless
public class CodeRefItemGenerator implements ItemGenerator {

	/** The wt element repo. */
	@Inject
	private WtElementRepository wtElementRepo;

	/** The wt code ref repo. */
	@Inject
	private WtCodeRefRepository wtCodeRefRepo;

	/** The wt reference repo. */
	@Inject
	private WtReferenceRepository wtReferenceRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator.
	 * ItemGenerator#generate(nts.uk.ctx.pr.core.dom.wagetable.history.element.
	 * ElementSetting)
	 */
	@Override
	public List<? extends Item> generate(String companyCode, String historyId,
			ElementSetting elementSetting) {
		// Get the element.
		Optional<WtElement> optWtElement = this.wtElementRepo.findByHistoryId(historyId);

		// Check element is existed.
		if (!optWtElement.isPresent()) {
			return Collections.emptyList();
		}

		// Get the code ref.
		Optional<WtCodeRef> optCodeRef = this.wtCodeRefRepo.findByCode(companyCode,
				optWtElement.get().getReferenceCode());

		// Check code ref table is existed.
		if (!optCodeRef.isPresent()) {
			return Collections.emptyList();
		}

		// Get ref items.
		List<WtCodeRefItem> wtRefItems = this.wtReferenceRepo.getCodeRefItem(optCodeRef.get());

		// Check has items.
		if (CollectionUtil.isEmpty(wtRefItems)) {
			throw new BusinessException(new RawErrorMessage(
					"Have not any items on demension  " + elementSetting.getDemensionNo().value
							+ ": " + optCodeRef.get().getRefName()));
		}

		// Create map: unique code - old uuid.
		@SuppressWarnings("unchecked")
		List<CodeItem> codeItems = (List<CodeItem>) elementSetting.getItemList();
		Map<String, ElementId> mapCodeItems = codeItems.stream()
				.collect(Collectors.toMap(CodeItem::getReferenceCode, CodeItem::getUuid));

		// Generate uuid of code items.
		return wtRefItems.stream().map(item -> {
			// Create code item
			CodeItem codeItem = new CodeItem(item.getReferenceCode(), mapCodeItems.getOrDefault(
					item.getReferenceCode(), new ElementId(IdentifierUtil.randomUniqueId())));
			codeItem.setDisplayName(item.getDisplayName());

			// Return
			return codeItem;
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator.
	 * ItemGenerator#canHandle(nts.uk.ctx.pr.core.dom.wagetable.ElementType)
	 */
	@Override
	public boolean canHandle(ElementType type) {
		return type == ElementType.CODE_REF;
	}
}

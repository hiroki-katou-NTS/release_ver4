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

import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElementRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRefItem;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRefRepository;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtReferenceRepository;

/**
 * The Class MasterRefItemGenerator.
 */
@Stateless
public class MasterRefItemGenerator implements ItemGenerator {

	/** The wt element repo. */
	@Inject
	private WtElementRepository wtElementRepo;

	/** The wt code ref repo. */
	@Inject
	private WtMasterRefRepository wtMasterRefRepo;

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

		// Get the master ref.
		Optional<WtMasterRef> optMasterRef = this.wtMasterRefRepo.findByCode(companyCode,
				optWtElement.get().getReferenceCode());

		// Check master ref is existed.
		if (!optMasterRef.isPresent()) {
			return Collections.emptyList();
		}

		// Get ref items.
		List<WtCodeRefItem> wtRefItems = this.wtReferenceRepo.getMasterRefItem(optMasterRef.get());

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
		return type == ElementType.MASTER_REF;
	}
}

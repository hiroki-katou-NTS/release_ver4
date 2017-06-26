/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.EmployeeLevel;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;

/**
 * The Class StepItemGenerator.
 */
@Stateless
public class LevelItemGenerator implements ItemGenerator {

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

		// Create map: unique code - old uuid.
		@SuppressWarnings("unchecked")
		List<CodeItem> codeItems = (List<CodeItem>) elementSetting.getItemList();
		Map<String, ElementId> mapCodeItems = codeItems.stream()
				.collect(Collectors.toMap(CodeItem::getReferenceCode, CodeItem::getUuid));

		// Generate uuid of code items.
		return Arrays.asList(EmployeeLevel.values()).stream().map(item -> {
			// Create code item
			CodeItem codeItem = new CodeItem(item.value, mapCodeItems.getOrDefault(item.value,
					new ElementId(IdentifierUtil.randomUniqueId())));
			codeItem.setDisplayName(item.displayName);
			
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
		return type == ElementType.LEVEL;
	}
}

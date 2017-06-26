/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;

/**
 * A factory for creating ElementItem objects.
 */
@Stateless
public class ElementItemFactoryImpl implements ElementItemFactory {

	/** The generators. */
	@Inject
	private Instance<ItemGenerator> generators;

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator.
	 * ElementItemFactory#generate(java.util.List)
	 */
	@Override
	public List<ElementSetting> generate(String companyCode, String historyId, List<ElementSetting> elementSetting) {
		// Execute generate items method.
		for (ElementSetting element : elementSetting) {
			for (ItemGenerator instance : generators) {
				if (instance.canHandle(element.getType())) {
					List<? extends Item> itemList = instance.generate(companyCode, historyId, element);
					element.setItemList(itemList);
				}
			}
		}
		
		// Return
		return elementSetting;
	}
}

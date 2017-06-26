package nts.uk.ctx.pr.core.app.find.item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.itemmaster.ItemAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1Repository;
import nts.uk.shr.com.context.AppContexts;

/**
 * Version 1, month 12
 * item finder
 *
 */
@Stateless
public class ItemFinder {
	@Inject
	private ItemMasterV1Repository repostirory;

	/**
	 * finder all items by company code and category type
	 * 
	 * @param companyCode
	 * @param categoryAtr
	 * @return
	 */
	public List<ItemDto> getAllItems(int categoryAtr) {
		return this.repostirory.findAllByCategory(AppContexts.user().companyCode(), categoryAtr).stream()
				.map(item -> ItemDto.fromDomain(item)).collect(Collectors.toList());
	}

	/**
	 * finder item by company code, category type, item code
	 * 
	 * @param companyCode
	 * @param categoryAtr
	 * @param itemCode
	 * @return
	 */
	public Optional<ItemDto> getItem(int categoryAtr, String itemCode) {
		return this.repostirory.getItemMaster(AppContexts.user().companyCode(), categoryAtr, itemCode)
				.map(item -> ItemDto.fromDomain(item));
	}
	
	/**
	 * Find all item master by ave Pay Attribute
	 * @param avePayAtr
	 * @return
	 */
	public List<ItemDto> findAllByItemAtr(ItemAtr itemAtr) {
		return this.repostirory.findAllByCategory(AppContexts.user().companyCode(), itemAtr.value).stream()
				.map(item -> ItemDto.fromDomain(item)).collect(Collectors.toList());
	}
}

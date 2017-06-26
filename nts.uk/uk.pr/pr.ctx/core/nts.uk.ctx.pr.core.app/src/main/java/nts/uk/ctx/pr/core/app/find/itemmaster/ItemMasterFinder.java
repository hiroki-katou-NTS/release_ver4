package nts.uk.ctx.pr.core.app.find.itemmaster;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.itemmaster.dto.ItemMasterDto;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ItemMasterFinder {
	@Inject
	private ItemMasterRepository itemMasterRepo;

	/**
	 * Find all item master by ave Pay Attribute
	 * 
	 * @param avePayAtr
	 * @return list of item master
	 */
	public List<ItemMasterDto> findAllByItemAtr(ItemAtr itemAtr) {
		return this.itemMasterRepo.findAllByCategory(AppContexts.user().companyCode(), itemAtr.value).stream()
				.map(item -> ItemMasterDto.fromDomain(item)).collect(Collectors.toList());
	}

	/**
	 * finder all items by company code and category type
	 * 
	 * @param companyCode
	 * @param categoryAtr
	 * @return list of item master
	 */
	public List<ItemMasterDto> findBy(int categoryAtr) {
		return this.itemMasterRepo.findAllByCategory(AppContexts.user().companyCode(), categoryAtr).stream()
				.map(item -> ItemMasterDto.fromDomain(item)).collect(Collectors.toList());
	}

	/**
	 * finder item by company code, category type, item code
	 * 
	 * @param categoryAtr
	 *            category attribute
	 * @param itemCode
	 *            item code
	 * @return item master
	 */
	public ItemMasterDto find(int categoryAtr, String itemCode) {
		Optional<ItemMasterDto> itemOp = this.itemMasterRepo
				.find(AppContexts.user().companyCode(), categoryAtr, itemCode)
				.map(item -> ItemMasterDto.fromDomain(item));

		return !itemOp.isPresent() ? null : itemOp.get();
	}

	/**
	 * finder item by company code, category type, item code
	 * 
	 * @param categoryAtr
	 *            category attribute
	 * @param dispSet
	 *            display set
	 * @return list of item master
	 */
	public List<ItemMasterDto> findAllItemMasterByCtgAtrAndDispSet(int ctgAtr, int dispSet) {

		List<ItemMasterDto> ItemList;
		if (ctgAtr == -1 && dispSet == -1)
			ItemList = this.itemMasterRepo.findAll(AppContexts.user().companyCode()).stream()
					.map(item -> ItemMasterDto.fromDomain(item)).collect(Collectors.toList());
		else if (dispSet == -1)
			ItemList = this.itemMasterRepo.findAllByCategory(AppContexts.user().companyCode(), ctgAtr).stream()
					.map(item -> ItemMasterDto.fromDomain(item)).collect(Collectors.toList());
		else if (ctgAtr == -1)
			ItemList = this.itemMasterRepo.findAllByDispSet(AppContexts.user().companyCode(), dispSet).stream()
					.map(item -> ItemMasterDto.fromDomain(item)).collect(Collectors.toList());
		else
			ItemList = this.itemMasterRepo.findAllByDispSetAndCtgAtr(AppContexts.user().companyCode(), ctgAtr, dispSet)
					.stream().map(item -> ItemMasterDto.fromDomain(item)).collect(Collectors.toList());
		return ItemList;

	}

	/**
	 * Find all item master by category and list of item code
	 * 
	 * @param categoryAtr
	 *            category attribute
	 * @param itemCodes
	 *            list of item code
	 * @return list of item master
	 */
	public List<ItemMasterDto> findBy(int categoryAtr, List<String> itemCodes) {
		String companyCode = AppContexts.user().companyCode();
		return this.itemMasterRepo.findAll(companyCode, categoryAtr, itemCodes).stream()
				.map(item -> ItemMasterDto.fromDomain(item)).collect(Collectors.toList());
	}

}

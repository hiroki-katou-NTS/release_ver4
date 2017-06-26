package nts.uk.ctx.pr.core.finder.itemmaster;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterRepository;
import nts.uk.ctx.pr.core.finder.itemmaster.ItemMasterPub;
import nts.uk.ctx.pr.core.finder.itemmaster.ItemMasterSEL_3_Dto;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ItemMasterPubImp implements ItemMasterPub {
	@Inject
	private ItemMasterRepository itemMasterRepo;

	@Override
	public List<ItemMasterSEL_3_Dto> find_SEL_3(int categoryAtr) {
		
		return this.itemMasterRepo.findAllByCategory(AppContexts.user().companyCode(), categoryAtr).stream()
				.map(item -> {
					return new ItemMasterSEL_3_Dto(item.getItemCode().v(), item.getItemAbName().v());
				}).collect(Collectors.toList());

	}
	
	@Override
	public List<ItemMasterDto> findAll(String companyCode) {
		return itemMasterRepo.findAll(companyCode).stream()
				.map(item -> new ItemMasterDto(item.getItemCode().v(), item.getItemName().v(), item.getCategoryAtr().value, item.getDisplaySet().value))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ItemMasterDto> findBy(String companyCode, List<String> itemCodes) {
		return itemMasterRepo.findAll(companyCode, itemCodes).stream()
				.map(item -> new ItemMasterDto(item.getItemCode().v(), item.getItemName().v(), item.getCategoryAtr().value, item.getDisplaySet().value))
				.collect(Collectors.toList());
	}
	
}

package nts.uk.ctx.pr.core.app.find.itemmaster.itemperiod;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemperiod.ItemPeriodDto;
import nts.uk.ctx.pr.core.dom.itemmaster.itemperiod.ItemPeriod;
import nts.uk.ctx.pr.core.dom.itemmaster.itemperiod.ItemPeriodRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ItemPeriodFinder {
	@Inject
	private ItemPeriodRepository itemPeriodRepo;

	public ItemPeriodDto find(int categoryAtr, String itemCode) {

		Optional<ItemPeriod> itemOpt = this.itemPeriodRepo.find(AppContexts.user().companyCode(), categoryAtr,
				itemCode);
		if (!itemOpt.isPresent())
			return null;
		
		return ItemPeriodDto.fromDomain(itemOpt.get());
	}
}

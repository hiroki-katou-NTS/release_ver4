package nts.uk.ctx.pr.core.app.find.itemmaster.itemdeductperiod;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemdeductperiod.ItemDeductPeriodDto;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductperiod.ItemDeductPeriod;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductperiod.ItemDeductPeriodRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ItemDeductPeriodFinder {

	@Inject
	private ItemDeductPeriodRepository itemDeductPeriodRepo;

	public ItemDeductPeriodDto find(String itemCode) {
		Optional<ItemDeductPeriod> itemOpt = this.itemDeductPeriodRepo.find(AppContexts.user().companyCode(),itemCode);
		if (!itemOpt.isPresent())
			return null;
		
		return ItemDeductPeriodDto.fromDomain(itemOpt.get());

	}

}

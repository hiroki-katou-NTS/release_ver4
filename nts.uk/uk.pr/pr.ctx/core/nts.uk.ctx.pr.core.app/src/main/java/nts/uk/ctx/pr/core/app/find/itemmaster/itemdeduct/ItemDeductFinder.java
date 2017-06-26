package nts.uk.ctx.pr.core.app.find.itemmaster.itemdeduct;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemdeduct.ItemDeductDto;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeduct.ItemDeduct;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeduct.ItemDeductRespository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ItemDeductFinder {
	@Inject
	ItemDeductRespository itemDeductRepo;

	public ItemDeductDto find(String itemCode) {
		Optional<ItemDeduct> itemOpt = this.itemDeductRepo.find(AppContexts.user().companyCode(), itemCode);
		if (!itemOpt.isPresent()) 
			return null;
		return ItemDeductDto.fromDomain(itemOpt.get());
	}
}

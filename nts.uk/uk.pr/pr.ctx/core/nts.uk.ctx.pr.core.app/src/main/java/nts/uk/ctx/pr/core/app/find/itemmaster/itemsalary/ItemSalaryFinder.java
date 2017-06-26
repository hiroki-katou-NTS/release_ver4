package nts.uk.ctx.pr.core.app.find.itemmaster.itemsalary;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemsalary.ItemSalaryDto;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalary;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalaryRespository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ItemSalaryFinder {
	@Inject
	ItemSalaryRespository itemSalaryRespo;

	public ItemSalaryDto find(String itemCode) {
		Optional<ItemSalary> itemOpt = this.itemSalaryRespo.find(AppContexts.user().companyCode(), itemCode);
		if (!itemOpt.isPresent())
			return null;
		return ItemSalaryDto.fromDomain(itemOpt.get());
	}

}

package nts.uk.ctx.pr.core.app.find.itemmaster.itemsalarybd;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemsalarybd.ItemSalaryBDDto;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd.ItemSalaryBDRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ItemSalaryBDFinder {

	@Inject
	private ItemSalaryBDRepository itemSalaryBDRepo;

	public List<ItemSalaryBDDto> findAll(String itemCode) {
		return this.itemSalaryBDRepo.findAll(AppContexts.user().companyCode(),itemCode).stream().map(item -> ItemSalaryBDDto.fromDomain(item))
				.collect(Collectors.toList());

	}

}

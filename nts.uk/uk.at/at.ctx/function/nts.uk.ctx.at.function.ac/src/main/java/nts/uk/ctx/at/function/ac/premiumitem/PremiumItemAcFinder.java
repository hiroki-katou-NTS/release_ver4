package nts.uk.ctx.at.function.ac.premiumitem;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.PremiumItemFuncAdapter;
import nts.uk.ctx.at.function.dom.adapter.PremiumItemFuncAdapterDto;
import nts.uk.ctx.at.schedule.pub.budget.premium.PremiumItemPub;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PremiumItemAcFinder implements PremiumItemFuncAdapter {
	
	@Inject
	private PremiumItemPub premiumItemPub;

	@Override
	public List<PremiumItemFuncAdapterDto> getPremiumItemName(String companyID, List<Integer> displayNumbers) {
		return this.premiumItemPub.findByCompanyIDAndDisplayNumber(companyID, displayNumbers).stream().map(f -> {
			return new PremiumItemFuncAdapterDto(f.getDisplayNumber(), f.getName());
		}).collect(Collectors.toList());
	}

}

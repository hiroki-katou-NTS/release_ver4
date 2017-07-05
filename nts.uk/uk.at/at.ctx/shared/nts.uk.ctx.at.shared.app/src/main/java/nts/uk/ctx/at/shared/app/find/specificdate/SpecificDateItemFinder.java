package nts.uk.ctx.at.shared.app.find.specificdate;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.uk.ctx.at.shared.dom.specificdate.item.SpecificDateItem;
import nts.uk.ctx.at.shared.dom.specificdate.repository.SpecificDateItemRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class SpecificDateItemFinder {

	@Inject
	private SpecificDateItemRepository specificDateItemRepository;

	public List<SpecificDateItemDto> getAllByCompany() {
		String companyId = AppContexts.user().companyId();
		return specificDateItemRepository.getAll(companyId).stream().map(c -> toSpecificDateItemDto(c))
				.collect(Collectors.toList());
	}

	private SpecificDateItemDto toSpecificDateItemDto(SpecificDateItem specificDateItem) {
		return new SpecificDateItemDto(specificDateItem.getTimeItemId(), specificDateItem.getUseAtr().value,
				specificDateItem.getSpecificDateItemNo().v(), specificDateItem.getSpecificName().v());
	}

}

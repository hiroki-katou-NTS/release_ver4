package nts.uk.ctx.at.function.ac.optionalItem;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.OptionalItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.OptionalItemImport;
import optitem.OptionalItemExport;
import optitem.OptionalItemPub;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class OptionalItemAcFinder implements OptionalItemAdapter{
	
	@Inject
	private OptionalItemPub optionalItemPub;

	@Override
	public List<OptionalItemImport> findOptionalItem(String companyId, List<Integer> optionalItemNos) {
		List<OptionalItemExport> optionalItemExports = this.optionalItemPub.getOptionalItems(companyId, optionalItemNos);
		List<OptionalItemImport> optionalItemImports = optionalItemExports.stream().map(item -> {
			return new OptionalItemImport(item.getOptionalItemNo(), item.getOptionalItemName(), item.getOptionalItemUnit());
		}).collect(Collectors.toList());
		return optionalItemImports;
	}

}

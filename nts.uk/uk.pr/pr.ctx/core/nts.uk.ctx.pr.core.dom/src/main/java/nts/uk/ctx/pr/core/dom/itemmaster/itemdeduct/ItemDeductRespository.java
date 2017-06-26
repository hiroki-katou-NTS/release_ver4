package nts.uk.ctx.pr.core.dom.itemmaster.itemdeduct;

import java.util.Optional;

public interface ItemDeductRespository {

	Optional<ItemDeduct> find(String companyCode, String itemCode);

	void add(String companyCode, ItemDeduct itemDeduct);

	void delete(String companyCode, String itemCode);

	void update(String companyCode, ItemDeduct domain);

}

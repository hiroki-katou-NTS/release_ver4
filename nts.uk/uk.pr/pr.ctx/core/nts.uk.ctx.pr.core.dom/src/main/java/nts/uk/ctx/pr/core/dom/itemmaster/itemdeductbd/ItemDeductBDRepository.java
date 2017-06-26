package nts.uk.ctx.pr.core.dom.itemmaster.itemdeductbd;

import java.util.List;
import java.util.Optional;

public interface ItemDeductBDRepository {

	List<ItemDeductBD> findAll(String companyCode, String itemCode);

	void add(String companyCode, ItemDeductBD itemDeductBD);

	void delete(String companyCode, String itemCode, String itemBreakdownCd);

	Optional<ItemDeductBD> find(String companyCode, String itemCode, String itemBreakdownCd);

	void update(String companyCode, ItemDeductBD itemDeductBD);

}

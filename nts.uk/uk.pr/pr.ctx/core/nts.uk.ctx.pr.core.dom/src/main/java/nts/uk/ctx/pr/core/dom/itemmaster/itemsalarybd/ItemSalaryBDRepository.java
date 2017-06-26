package nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd;

import java.util.List;
import java.util.Optional;

public interface ItemSalaryBDRepository {
	List<ItemSalaryBD> findAll(String companyCode, String itemCode);

	void add(String companyCode, ItemSalaryBD itemSalaryBD);

	void delete(String companyCode, String itemCode, String itemBreakdownCode);

	Optional<ItemSalaryBD> find(String companyCode, String itemCode, String itemBreakdownCode);

	void update(String companyCode, ItemSalaryBD domain);

}

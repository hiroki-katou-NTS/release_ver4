package nts.uk.ctx.pr.core.dom.wageprovision.statementitem;

import java.util.Optional;
import java.util.List;

/**
 * 明細書項目
 */
public interface StatementItemRepository {

	List<StatementItem> getAllStatementItem();
	
	List<StatementItem> getAllItemByCid(String cid);

	Optional<StatementItem> getStatementItemById(String cid, int categoryAtr, int itemNameCd, String salaryItemId);

	void add(StatementItem domain);

	void update(StatementItem domain);

	void remove(String cid, int categoryAtr, int itemNameCd, String salaryItemId);

}

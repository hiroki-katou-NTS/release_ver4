package nts.uk.ctx.pr.core.dom.wageprovision.statementitem;

import java.util.Optional;
import java.util.List;

/**
 * 明細書項目
 */
public interface StatementItemRepository {

	List<StatementItem> getAllStatementItem();
	
	List<StatementItem> getAllItemByCid(String cid);
	
	List<StatementItem> getByCategory(String cid, int categoryAtr);
	
	List<StatementItem> getByItemNameCd(String cid, int categoryAtr, String itemNameCd);

	Optional<StatementItem> getStatementItemById(String cid, int categoryAtr, String itemNameCd, String salaryItemId);

	void add(StatementItem domain);

	void update(StatementItem domain);

	void remove(String cid, int categoryAtr, String itemNameCd, String salaryItemId);

}

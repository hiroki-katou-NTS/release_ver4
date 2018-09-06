package nts.uk.ctx.pr.core.dom.wageprovision.statementitem.deductionitemset;

import java.util.Optional;
import java.util.List;

/**
 * 
 * @author thanh.tq 控除項目設定
 *
 */
public interface DeductionItemStRepository {

	List<DeductionItemSet> getAllDeductionItemSt();

	Optional<DeductionItemSet> getDeductionItemStById(String cid, String salaryItemId);

	void add(DeductionItemSet domain);

	void update(DeductionItemSet domain);

	void remove(String cid, String salaryItemId);

}

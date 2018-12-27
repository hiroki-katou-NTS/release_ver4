package nts.uk.ctx.pr.core.dom.wageprovision.statementitem.timeitemset;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author thanh.tq 勤怠項目設定
 *
 */
public interface TimeItemSetRepository {

	List<TimeItemSet> getAllTimeItemSt();
	Optional<TimeItemSet> getTimeItemStById(String cid, int categoryAtr, String itemNameCode);
	List<TimeItemSet> getTimeItemStByCategoryAndCodes(String cid, int timeCountAtr, List<String> itemNameCodes);
	void add(TimeItemSet domain);
	void update(TimeItemSet domain);
	void remove(String cid, int categoryAtr, String itemNameCode);
	void updateAll(List<String> lstCode);

}

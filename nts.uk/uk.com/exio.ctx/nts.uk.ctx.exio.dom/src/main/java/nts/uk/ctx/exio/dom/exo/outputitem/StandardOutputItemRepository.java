package nts.uk.ctx.exio.dom.exo.outputitem;

import java.util.List;
import java.util.Optional;

/**
 * 出力項目(定型)
 */
public interface StandardOutputItemRepository {

	List<StandardOutputItem> getAllStdOutItem();

	List<StandardOutputItem> getStdOutItemByCidAndSetCd(String cid, String conditionSettingCode);

	Optional<StandardOutputItem> getStdOutItemById(String cid, String outputItemCode, String conditionSettingCode);

	void add(StandardOutputItem domain);

	void update(StandardOutputItem domain);

	void remove(String cid, String outputItemCode, String conditionSettingCode);
	
	void remove(String cid, String condSetCd);

}

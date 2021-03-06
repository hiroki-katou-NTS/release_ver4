package nts.uk.ctx.pr.core.dom.wageprovision.statementitem.validityperiodset;

import java.util.Optional;
import java.util.List;

/**
 * 
 * @author thanh.tq 有効期間とサイクルの設定
 *
 */
public interface SetPeriodCycleRepository {

	List<SetValidityPeriodCycle> getAllSetPeriodCycle();

	Optional<SetValidityPeriodCycle> getSetPeriodCycleById(String cid, int categoryAtr, String itemNameCd);

	void register(SetValidityPeriodCycle domain);

	void remove(String cid, int categoryAtr, String itemNameCd);

}

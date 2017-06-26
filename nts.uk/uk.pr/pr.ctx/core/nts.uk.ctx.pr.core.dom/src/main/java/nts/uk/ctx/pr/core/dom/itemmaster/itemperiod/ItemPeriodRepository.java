package nts.uk.ctx.pr.core.dom.itemmaster.itemperiod;

import java.util.Optional;

public interface ItemPeriodRepository {

	Optional<ItemPeriod> find(String companyCode, int ctgAtr, String itemCd);

}

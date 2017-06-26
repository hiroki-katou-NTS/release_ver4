package nts.uk.ctx.at.schedule.dom.budget.premium;

import java.util.List;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface PremiumItemRepository {
	
	public void update(PremiumItem premiumItem);
	
	public List<PremiumItem> findByCompanyID(String companyID);

}

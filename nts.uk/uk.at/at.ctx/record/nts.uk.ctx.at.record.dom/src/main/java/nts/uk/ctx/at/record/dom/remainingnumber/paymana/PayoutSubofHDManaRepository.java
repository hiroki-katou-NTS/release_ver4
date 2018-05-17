package nts.uk.ctx.at.record.dom.remainingnumber.paymana;

import java.util.List;

public interface PayoutSubofHDManaRepository {
	
	void add(PayoutSubofHDManagement domain);
	
	void update(PayoutSubofHDManagement domain);
	
	void delete(String payoutId, String subOfHDID);
	
	List<PayoutSubofHDManagement> getByPayoutId(String payoutId);
}

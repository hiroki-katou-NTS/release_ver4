package nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata;

import java.util.List;
import java.util.Optional;

public interface AnnLeaMaxDataRepository {
	
	Optional<AnnualLeaveMaxData> get(String employeeId);
	
	void add(AnnualLeaveMaxData maxData);
	
	void update(AnnualLeaveMaxData maxData);
	
	void delete(String employeeId);
	
	/**
	 * getAll
	 * @param cid
	 * @param sids
	 * @return
	 */
	List<AnnualLeaveMaxData> getAll(String cid, List<String> sids);

}

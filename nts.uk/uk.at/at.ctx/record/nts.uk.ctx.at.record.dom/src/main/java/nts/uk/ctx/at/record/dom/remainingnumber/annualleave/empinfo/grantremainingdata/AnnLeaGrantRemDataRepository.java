package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface AnnLeaGrantRemDataRepository {
	
	List<AnnualLeaveGrantRemainingData> find(String employeeId);
	
	Optional<AnnualLeaveGrantRemainingData> getLast(String employeeId);
	
	Optional<AnnualLeaveGrantRemainingData> findByID(String id);
	
	List<AnnualLeaveGrantRemainingData> findByCheckState(String employeeId, int checkState);
	
	List<AnnualLeaveGrantRemainingData> findNotExp(String employeeId);
	
	void add(AnnualLeaveGrantRemainingData data);
	
	void update(AnnualLeaveGrantRemainingData data);
	
	void delete(String employeeID, GeneralDate grantDate);
	
	void delete(String annaLeavID);

}

package nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo;

import java.util.List;
import java.util.Optional;

public interface AnnLeaEmpBasicInfoRepository {
	
	Optional<AnnualLeaveEmpBasicInfo> get(String employeeId);
	
	List<AnnualLeaveEmpBasicInfo> getList(List<String> employeeIds);
	
	void add(AnnualLeaveEmpBasicInfo baicInfo);
	
	void update(AnnualLeaveEmpBasicInfo basicInfo);
	
	void delete(String employeeId);

}

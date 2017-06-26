package nts.uk.ctx.at.shared.dom.attendance;

import java.util.List;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface AttendanceTypeRepository {
	
	public List<AttendanceType> getItemByType(String companyID, int type);
	
}

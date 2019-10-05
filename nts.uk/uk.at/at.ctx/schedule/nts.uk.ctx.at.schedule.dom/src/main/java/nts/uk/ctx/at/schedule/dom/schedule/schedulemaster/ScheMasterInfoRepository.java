package nts.uk.ctx.at.schedule.dom.schedule.schedulemaster;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author TanLV
 *
 */
public interface ScheMasterInfoRepository {
	Optional<ScheMasterInfo> getScheMasterInfo(String sId, GeneralDate generalDate);
	
	List<ScheMasterInfo> getScheMasterInfoByPeriod(String sId, DatePeriod period);
	
	void addScheMasterInfo(ScheMasterInfo scheMasterInfo);
	
	void updateScheMasterInfo(ScheMasterInfo scheMasterInfo);
	
	void deleteScheMasterInfo(String sId, GeneralDate generalDate);
}

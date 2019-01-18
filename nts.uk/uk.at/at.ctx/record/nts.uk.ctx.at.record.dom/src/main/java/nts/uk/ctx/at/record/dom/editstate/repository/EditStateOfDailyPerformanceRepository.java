package nts.uk.ctx.at.record.dom.editstate.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface EditStateOfDailyPerformanceRepository {

	void delete(String employeeId, GeneralDate ymd);
	
	void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> processingYmds);
	
	void add(List<EditStateOfDailyPerformance> editStates);
	
	List<EditStateOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd);
	
	List<EditStateOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd);
	
	List<EditStateOfDailyPerformance> finds(Map<String, List<GeneralDate>> param);
	
	void updateByKey(List<EditStateOfDailyPerformance> editStates);
	
	void addAndUpdate(List<EditStateOfDailyPerformance> editStates);
	
	Optional<EditStateOfDailyPerformance> findByKeyId(String employeeId, GeneralDate ymd, Integer id);
	
	List<EditStateOfDailyPerformance> findByItems(String employeeId, GeneralDate ymd, List<Integer> ids);
	
	void updateByKeyFlush(List<EditStateOfDailyPerformance> editStates);

	void deleteByListItemId(String employeeId, GeneralDate ymd, List<Integer> itemIdList);
	
	List<EditStateOfDailyPerformance> findByEditState(String sid, GeneralDate ymd, List<Integer> ids, EditStateSetting editState);
}

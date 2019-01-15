package nts.uk.ctx.at.record.dom.workinformation.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface WorkInformationRepository {
	
	/** Find an Employee WorkInformation of Daily Performance */
	Optional<WorkInfoOfDailyPerformance> find(String employeeId, GeneralDate ymd);
	
	List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
	
	List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmdAndEmps(List<String> employeeIds, DatePeriod datePeriod);
	
	List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmdDesc(String employeeId, DatePeriod datePeriod);
	
	List<WorkInfoOfDailyPerformance> finds(Map<String, List<GeneralDate>> param);
	
	List<WorkInfoOfDailyPerformance> findByListEmployeeId(List<String> employeeIds, DatePeriod ymds);
	
	List<WorkInfoOfDailyPerformance> findByEmployeeId(String employeeId);
	
	void delete(String employeeId, GeneralDate ymd);
	
	void updateByKey(WorkInfoOfDailyPerformance workInfoOfDailyPerformance);
	
	void update(List<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance);
	
	void updateV2(List<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance);
	
	void insert(WorkInfoOfDailyPerformance workInfoOfDailyPerformance);
	
	void updateByKeyFlush(WorkInfoOfDailyPerformance workInfoOfDailyPerformance);
	/**
	 * 
	 * @param employeeId 社員ID
	 * @param workTypeCode 勤務実績の勤務情報．勤務種類コード
	 * @param period 開始日<=年月日<=終了日
	 * @return
	 */
	List<GeneralDate> getByWorkTypeAndDatePeriod(String employeeId, String workTypeCode, DatePeriod period);
	/**
	 * 
	 * @param employeeId
	 * @param dates list
	 * @return
	 */
	List<WorkInfoOfDailyPerformance> findByListDate(String employeeId, List<GeneralDate> dates);
}
package nts.uk.ctx.at.record.dom.monthly;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface TimeOfMonthlyRepository {

	/**
	 * 検索
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @return 該当するTimeOfMonthly
	 */
	Optional<TimeOfMonthly> find(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate);

	/**
	 * 検索　（年月）
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @return TimeOfMonthly　（開始日順）
	 */
	List<TimeOfMonthly> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth);

	/**
	 * 検索　（年月と締めID）
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @return TimeOfMonthly　（開始日順）
	 */
	List<TimeOfMonthly> findByYMAndClosureIdOrderByStartYmd(
			String employeeId, YearMonth yearMonth, ClosureId closureId);
	
	/**
	 * 検索　（社員IDリスト）
	 * @param employeeIds 社員IDリスト
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @return 該当するTimeOfMonthly
	 */
	List<TimeOfMonthly> findByEmployees(List<String> employeeIds, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate);

	/**
	 * 検索　（社員IDリストと年月リスト）
	 * @param employeeIds 社員IDリスト
	 * @param yearMonths 年月リスト
	 * @return TimeOfMonthly　（開始日順）
	 */
	List<TimeOfMonthly> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths);

	/**
	 * 検索　（基準日）
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @return TimeOfMonthly間リスト
	 */
	List<TimeOfMonthly> findByDate(String employeeId, GeneralDate criteriaDate);

	/**
	 * 検索　（終了日を含む期間）
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @return TimeOfMonthlyリスト
	 */
	List<TimeOfMonthly> findByPeriodIntoEndYmd(String employeeId, DatePeriod period);
	
	/**
	 * 登録および更新
	 * 
	 */
	void persistAndUpdate(TimeOfMonthly domain) ;
	
	/**
	 * 削除
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 */
	void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	/**
	 * 削除
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 */
	void removeAffiliation(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	void removeAttendanceTime(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	/**
	 * 削除　（年月）
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 */
	void removeByYearMonth(String employeeId, YearMonth yearMonth);

	/**
	 * 削除　（年月）
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 */
	void removeAffiliation(String employeeId, YearMonth yearMonth);
	void removeAttendanceTime(String employeeId, YearMonth yearMonth);
}

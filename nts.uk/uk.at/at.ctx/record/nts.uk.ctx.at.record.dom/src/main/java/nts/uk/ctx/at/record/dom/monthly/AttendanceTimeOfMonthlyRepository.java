package nts.uk.ctx.at.record.dom.monthly;

import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/*
 * リポジトリ：月別実績の勤怠時間
 * @author shuichi_ishida
 */
public interface AttendanceTimeOfMonthlyRepository {

	/**
	 * 検索
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @return 該当する月別実績の勤怠時間
	 */
	Optional<AttendanceTimeOfMonthly> find(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate);

	/**
	 * 検索　（年月）
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @return 月別実績の勤怠時間　（開始日順）
	 */
	List<AttendanceTimeOfMonthly> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth);

	/**
	 * 検索　（年月と締めID）
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @return 月別実績の勤怠時間　（開始日順）
	 */
	List<AttendanceTimeOfMonthly> findByYMAndClosureIdOrderByStartYmd(
			String employeeId, YearMonth yearMonth, ClosureId closureId);
	
	/**
	 * 登録および更新
	 * @param attendanceTimeOfMonthly 月別実績の勤怠時間
	 */
	void persistAndUpdate(AttendanceTimeOfMonthly attendanceTimeOfMonthly);
	
	/**
	 * 削除
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 */
	void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	/**
	 * 削除　（年月）
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 */
	void removeByYearMonth(String employeeId, YearMonth yearMonth);
}

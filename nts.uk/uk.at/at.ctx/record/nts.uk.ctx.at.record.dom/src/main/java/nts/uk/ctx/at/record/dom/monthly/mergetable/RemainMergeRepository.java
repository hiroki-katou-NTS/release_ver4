package nts.uk.ctx.at.record.dom.monthly.mergetable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdRemain;
import nts.uk.ctx.at.record.dom.monthly.information.childnursing.MonChildHdRemain;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * リポジトリ：残数系
 * @author shuichi_ishida
 */
public interface RemainMergeRepository {

	/**
	 * 検索
	 * @param key 月別実績プライマリキー
	 * @return 残数系データ
	 */
	Optional<RemainMerge> find(MonthMergeKey key);
	
	Optional<RemainMerge> find(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	/**
	 * 検索
	 * @param employeeIds list employee ids
	 * @param yearMonths list yearMonths
	 * @return 残数系データ
	 */
	List<RemainMerge> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths);
	
	List<RemainMerge> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth);
	
	List<RemainMerge> findByYMAndClosureIdOrderByStartYmd(String employeeId, YearMonth yearMonth,
			ClosureId closureId);
	
	List<RemainMerge> findByEmployees(List<String> employeeIds, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate);
	
	List<RemainMerge> getByYmStatus(String sid, YearMonth ym, ClosureStatus status);
	
	List<RemainMerge> findByClosurePeriod(String employeeId, DatePeriod closurePeriod);
	
	/**
	 * 登録および更新
	 * @param key 月別実績プライマリキー
	 * @param domains 残数系データ
	 */
	void persistAndUpdate(MonthMergeKey key, RemainMerge domains);
	
	void persistAndUpdate(RemainMerge domain);
	
	void persistAndUpdate(MonCareHdRemain domain);
	
	void persistAndUpdate(MonChildHdRemain domain);
	
	void persistAndUpdate(SpecialHolidayRemainData domain);
	
	void persistAndUpdate(List<SpecialHolidayRemainData> domain);
	
	void persistAndUpdate(AbsenceLeaveRemainData domain);
	
	void persistAndUpdate(AnnLeaRemNumEachMonth domain);
	
	void persistAndUpdate(MonthlyDayoffRemainData domain);
	
	void persistAndUpdate(RsvLeaRemNumEachMonth domain);
	
	/**
	 * 削除
	 * @param key 月別実績プライマリキー
	 */
	void remove(MonthMergeKey key);

	void removeRsvLea(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	void removeRsvLea(String employeeId, YearMonth yearMonth);
	
	void removeDayOff(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	void removeAbsenceLeave(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	void removeMonCareHd(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	void removeMonCareHd(String employeeId, YearMonth yearMonth);

	void removeAnnLea(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	void removeAnnLea(String employeeId, YearMonth yearMonth);
	
	void removeMonChildHd(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	void removeMonChildHd(String employeeId, YearMonth yearMonth);
	
	void removeSpecHoliday(String employeeId, YearMonth yearMonth);
	
	void removeSpecHoliday(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
	
	void removeSpecHoliday(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate, int no);
	
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ260
	 * @param employeeId
	 * @param yearMonth
	 * @return
	 */
	public List<AbsenceLeaveRemainData> findByYearMonthRQ260(String employeeId, YearMonth yearMonth);
	
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ263
	 * @param sid
	 * @param month
	 * @return
	 */
	public List<SpecialHolidayRemainData> findByYearMonthRQ263(String sid, YearMonth month);
	
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ259
	 * @param employeeId
	 * @param yearMonth
	 * @return
	 */
	public List<MonthlyDayoffRemainData> findByYearMonthRQ259(String employeeId, YearMonth yearMonth);
	
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ258
	 * @param lstSID
	 * @param lstYearMonth
	 * @return
	 */
	public List<RsvLeaRemNumEachMonth> findByYearMonthRQ258(List<String> lstSID, List<YearMonth> lstYearMonth);
	
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ255
	 * @param lstSID
	 * @param lstYearMonth
	 * @return
	 */
	public List<AnnLeaRemNumEachMonth> findByYearMonthRQ255(List<String> lstSID, List<YearMonth> lstYearMonth);
	
	/**
	 * 
	 * @param employeeId
	 * @param lstYrMon
	 * @return
	 */
	public Map<YearMonth, List<RemainMerge>> findBySidsAndYrMons(String employeeId, List<YearMonth> lstYrMon);
}

package nts.uk.ctx.at.record.infra.repository.monthly.specialholiday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainDataRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class JpaSpecialHolidayRemainDataRepo extends JpaRepository implements SpecialHolidayRemainDataRepository {

	@Inject
	private RemainMergeRepository remainRepo;
	
	/** 取得 */
	@Override
	public List<SpecialHolidayRemainData> getByYmStatus(String sid, YearMonth ym, ClosureStatus status) {
		
		return this.remainRepo.getByYmStatus(sid, ym, status).stream()
								.map(c -> c.getSpecialHolidayRemainList())
								.flatMap(List::stream)
								.collect(Collectors.toList());
	}

	@Override
	public List<SpecialHolidayRemainData> getByYmStatus(String sid, YearMonth ym, ClosureStatus status, int speCode) {
		
		return this.remainRepo.getByYmStatus(sid, ym, status).stream()
				.map(c -> c.getSpecialHolidayRemainList().stream()
								.filter(sh -> sh.getSpecialHolidayCd() == speCode).findFirst().orElse(null))
				.filter(c -> c != null)
				.collect(Collectors.toList());
	}
	
	/** 検索 */
	// add 2018.9.13 shuichi_ishida
	@Override
	public List<SpecialHolidayRemainData> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.remainRepo.find(employeeId, yearMonth, closureId, closureDate)
								.map(c -> c.getSpecialHolidayRemainList())
								.orElse(new ArrayList<>());
	}
	
	/** 検索 */
	// add 2018.8.24 shuichi_ishida
	@Override
	public List<SpecialHolidayRemainData> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.remainRepo.findByYearMonthOrderByStartYmd(employeeId, yearMonth)
								.stream().map(c -> c.getSpecialHolidayRemainList())
								.flatMap(List::stream)
								.collect(Collectors.toList());
	}

	@Override
	public List<SpecialHolidayRemainData> getByYmCode(String sid, YearMonth ym, int speCode) {
		return this.remainRepo.findByYearMonthOrderByStartYmd(sid, ym)
								.stream().map(c -> c.getSpecialHolidayRemainList().stream()
										.filter(sh -> sh.getSpecialHolidayCd() == speCode).findFirst().orElse(null))
								.filter(c -> c != null)
								.collect(Collectors.toList());
	}
	
	/** 検索 */
	// add 2018.8.30 shuichi_ishida
	@Override
	public List<SpecialHolidayRemainData> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		return this.remainRepo.findBySidsAndYearMonths(employeeIds, yearMonths)
								.stream().map(c -> c.getSpecialHolidayRemainList())
								.flatMap(List::stream)
								.collect(Collectors.toList());
	}

	/** 登録および更新 */
	// add 2018.8.22 shuichi_ishida
	@Override
	public void persistAndUpdate(SpecialHolidayRemainData domain) {
		
		this.remainRepo.persistAndUpdate(domain);
	}
	
	/** 登録および更新 */
	// add 2018.9.30 shuichi_ishida
	@Override
	public void persistAndUpdate(List<SpecialHolidayRemainData> domains) {
		
		this.remainRepo.persistAndUpdate(domains);
	}
	
	/** 削除 */
	// add 2018.8.22 shuichi_ishida
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate,
			int specialHolidayCode) {
		
		this.remainRepo.removeSpecHoliday(employeeId, yearMonth, closureId, closureDate, specialHolidayCode);
	}
	
	/** 削除 */
	// add 2018.8.24 shuichi_ishida
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		this.remainRepo.removeSpecHoliday(employeeId, yearMonth, closureId, closureDate);
	}
	
	/** 削除　（年月） */
	// add 2018.8.22 shuichi_ishida
	@Override
	public void removeByYearMonth(String employeeId, YearMonth yearMonth) {
		
		this.remainRepo.removeSpecHoliday(employeeId, yearMonth);
	}
}

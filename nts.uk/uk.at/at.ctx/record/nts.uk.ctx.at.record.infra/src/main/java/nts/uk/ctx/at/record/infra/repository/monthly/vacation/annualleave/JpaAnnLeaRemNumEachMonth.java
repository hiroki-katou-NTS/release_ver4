package nts.uk.ctx.at.record.infra.repository.monthly.vacation.annualleave;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * リポジトリ実装：年休月別残数データ
 * @author shuichu_ishida
 */
@Stateless
public class JpaAnnLeaRemNumEachMonth extends JpaRepository implements AnnLeaRemNumEachMonthRepository {

	@Inject
	private RemainMergeRepository remainRepo;

	/** 検索 */
	@Override
	public Optional<AnnLeaRemNumEachMonth> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.remainRepo.find(employeeId, yearMonth, closureId, closureDate)
								.map(c -> c.getAnnLeaRemNumEachMonth());
	}
	
	/** 検索　（年月） */
	@Override
	public List<AnnLeaRemNumEachMonth> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.remainRepo.findByYearMonthOrderByStartYmd(employeeId, yearMonth)
				.stream().map(c -> c.getAnnLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 検索　（年月と締めID） */
	@Override
	public List<AnnLeaRemNumEachMonth> findByYMAndClosureIdOrderByStartYmd(String employeeId, YearMonth yearMonth,
			ClosureId closureId) {
		
		return this.remainRepo.findByYMAndClosureIdOrderByStartYmd(employeeId, yearMonth, closureId)
				.stream().map(c -> c.getAnnLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 検索　（社員IDリスト） */
	@Override
	public List<AnnLeaRemNumEachMonth> findbyEmployees(List<String> employeeIds, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate) {
		
		return this.remainRepo.findByEmployees(employeeIds, yearMonth, closureId, closureDate)
				.stream().map(c -> c.getAnnLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 検索　（社員IDリストと年月リスト） */
	@Override
	public List<AnnLeaRemNumEachMonth> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		return this.remainRepo.findBySidsAndYearMonths(employeeIds, yearMonths)
				.stream().map(c -> c.getAnnLeaRemNumEachMonth()).collect(Collectors.toList());
	}

	/** 検索　（社員IDと締め期間、条件＝締め済み） */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AnnLeaRemNumEachMonth> findByClosurePeriod(String employeeId, DatePeriod closurePeriod) {
		
		return this.remainRepo.findByClosurePeriod(employeeId, closurePeriod)
				.stream().map(c -> c.getAnnLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(AnnLeaRemNumEachMonth domain) {
		
		this.remainRepo.persistAndUpdate(domain);
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		this.remainRepo.removeAnnLea(employeeId, yearMonth, closureId, closureDate);
	}
	
	/** 削除　（年月） */
	@Override
	public void removeByYearMonth(String employeeId, YearMonth yearMonth) {
		this.remainRepo.removeAnnLea(employeeId, yearMonth);
	}
}

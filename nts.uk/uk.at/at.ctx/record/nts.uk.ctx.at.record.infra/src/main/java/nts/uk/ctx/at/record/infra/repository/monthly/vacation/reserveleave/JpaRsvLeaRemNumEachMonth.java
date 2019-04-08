package nts.uk.ctx.at.record.infra.repository.monthly.vacation.reserveleave;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * リポジトリ実装：積立年休月別残数データ
 * @author shuichu_ishida
 */
@Stateless
public class JpaRsvLeaRemNumEachMonth extends JpaRepository implements RsvLeaRemNumEachMonthRepository {

	@Inject
	private RemainMergeRepository remainRepo;

	/** 検索 */
	@Override
	public Optional<RsvLeaRemNumEachMonth> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.remainRepo.find(employeeId, yearMonth, closureId, closureDate)
				.map(c -> c.getRsvLeaRemNumEachMonth());
	}
	
	/** 検索　（年月） */
	@Override
	public List<RsvLeaRemNumEachMonth> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.remainRepo.findByYearMonthOrderByStartYmd(employeeId, yearMonth)
				.stream().map(c -> c.getRsvLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 検索　（年月と締めID） */
	@Override
	public List<RsvLeaRemNumEachMonth> findByYMAndClosureIdOrderByStartYmd(String employeeId, YearMonth yearMonth,
			ClosureId closureId) {
		
		return this.remainRepo.findByYMAndClosureIdOrderByStartYmd(employeeId, yearMonth, closureId)
				.stream().map(c -> c.getRsvLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 検索　（社員IDリスト） */
	@Override
	public List<RsvLeaRemNumEachMonth> findByEmployees(List<String> employeeIds, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate) {
		
		return this.remainRepo.findByEmployees(employeeIds, yearMonth, closureId, closureDate)
				.stream().map(c -> c.getRsvLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 検索　（社員IDリストと年月リスト） */
	@Override
	public List<RsvLeaRemNumEachMonth> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		return this.remainRepo.findBySidsAndYearMonths(employeeIds, yearMonths)
				.stream().map(c -> c.getRsvLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(RsvLeaRemNumEachMonth domain) {
		
		this.remainRepo.persistAndUpdate(domain);
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		this.remainRepo.removeRsvLea(employeeId, yearMonth, closureId, closureDate);
	}
	
	/** 削除　（年月） */
	@Override
	public void removeByYearMonth(String employeeId, YearMonth yearMonth) {
		this.remainRepo.removeRsvLea(employeeId, yearMonth);
	}
}

package nts.uk.ctx.at.record.infra.repository.monthly.information.care;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdRemain;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdRemainRepository;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 
 * @author phongtq
 */
@Stateless
public class JpaMonCareHdRemainRepository extends JpaRepository implements MonCareHdRemainRepository{
	
	@Inject
	private RemainMergeRepository remainRepo;
	
	@Override
	public Optional<MonCareHdRemain> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.remainRepo.find(employeeId, yearMonth, closureId, closureDate)
								.map(c -> c.getMonCareHdRemain());
	}

	@Override
	public List<MonCareHdRemain> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.remainRepo.findByYearMonthOrderByStartYmd(employeeId, yearMonth)
				.stream().map(c -> c.getMonCareHdRemain()).collect(Collectors.toList());
	}

	@Override
	public List<MonCareHdRemain> findByYMAndClosureIdOrderByStartYmd(String employeeId, YearMonth yearMonth,
			ClosureId closureId) {
		
		return this.remainRepo.findByYMAndClosureIdOrderByStartYmd(employeeId, yearMonth, closureId)
				.stream().map(c -> c.getMonCareHdRemain()).collect(Collectors.toList());
	}

	@Override
	public List<MonCareHdRemain> findByEmployees(List<String> employeeIds, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
	
		return this.remainRepo.findByEmployees(employeeIds, yearMonth, closureId, closureDate)
				.stream().map(c -> c.getMonCareHdRemain()).collect(Collectors.toList());
	}

	@Override
	public List<MonCareHdRemain> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		return this.remainRepo.findBySidsAndYearMonths(employeeIds, yearMonths)
				.stream().map(c -> c.getMonCareHdRemain()).collect(Collectors.toList());
	}

	@Override
	public void persistAndUpdate(MonCareHdRemain domain) {
		
		this.remainRepo.persistAndUpdate(domain);
	}

	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		
		this.remainRepo.removeMonCareHd(employeeId, yearMonth, closureId, closureDate);
	}

	@Override
	public void removeByYearMonth(String employeeId, YearMonth yearMonth) {

		this.remainRepo.removeMonCareHd(employeeId, yearMonth);
	}
}

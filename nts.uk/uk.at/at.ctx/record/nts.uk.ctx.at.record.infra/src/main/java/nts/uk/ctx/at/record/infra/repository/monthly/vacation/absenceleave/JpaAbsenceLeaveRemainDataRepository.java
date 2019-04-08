package nts.uk.ctx.at.record.infra.repository.monthly.vacation.absenceleave;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainDataRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class JpaAbsenceLeaveRemainDataRepository extends JpaRepository implements AbsenceLeaveRemainDataRepository{

	@Inject
	private RemainMergeRepository remainRepo;
	
	@Override
	public List<AbsenceLeaveRemainData> getDataBySidYmClosureStatus(String employeeId, YearMonth ym,
			ClosureStatus status) {
		return  this.remainRepo.getByYmStatus(employeeId, ym, status)
				.stream().map(c -> c.getAbsenceLeaveRemainData()).collect(Collectors.toList());
	}
	
	@Override
	public Optional<AbsenceLeaveRemainData> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.remainRepo.find(employeeId, yearMonth, closureId, closureDate).map(c -> c.getAbsenceLeaveRemainData());
	}
	
	@Override
	public List<AbsenceLeaveRemainData> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.remainRepo.findByYearMonthOrderByStartYmd(employeeId, yearMonth)
				.stream().map(c -> c.getAbsenceLeaveRemainData()).collect(Collectors.toList());
	}

	@Override
	public List<AbsenceLeaveRemainData> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		return this.remainRepo.findBySidsAndYearMonths(employeeIds, yearMonths)
				.stream().map(c -> c.getAbsenceLeaveRemainData()).collect(Collectors.toList());
	}

	@Override
	public void persistAndUpdate(AbsenceLeaveRemainData domain) {
		
		this.remainRepo.persistAndUpdate(domain);
	}
	
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		
		this.remainRepo.removeAbsenceLeave(employeeId, yearMonth, closureId, closureDate);
	}
}

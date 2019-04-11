package nts.uk.ctx.at.record.infra.repository.monthly;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.MonthlyRecordTransactionService;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthlyRepository;

@Stateless
public class MonthlyRecordTransactionServiceImp implements MonthlyRecordTransactionService {

	@Inject
	private TimeOfMonthlyRepository monthlyRepo;
	
	@Override
	public void updated(String employeeId, YearMonth yearMonth, int closureId, int closureDate, boolean lastOfMonth) {
		
		this.monthlyRepo.verShouldUp(employeeId, yearMonth, closureId, closureDate, lastOfMonth);
	}

	@Override
	public void updated(String employeeId, YearMonth yearMonth, int closureId, int closureDate, boolean lastOfMonth,
			long version) {
		this.monthlyRepo.verShouldUp(employeeId, yearMonth, closureId, closureDate, lastOfMonth, version);
	}

	
}

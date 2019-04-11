package nts.uk.ctx.at.record.infra.repository.daily;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.DailyRecordTransactionService;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;

@Stateless
public class JpaDailyRecordTransactionService implements DailyRecordTransactionService {

	@Inject
	private WorkInformationRepository workInfo;
	
	@Override
	public void updated(String empId, GeneralDate workingDate) {
		this.workInfo.dirtying(empId, workingDate);
	}

	@Override
	public void updated(List<String> empId, List<GeneralDate> workingDate) {
		this.workInfo.dirtying(empId, workingDate);
	}

	@Override
	public void updated(String empId, GeneralDate workingDate, long version) {
		this.workInfo.dirtying(empId, workingDate, version);
		
	}

}

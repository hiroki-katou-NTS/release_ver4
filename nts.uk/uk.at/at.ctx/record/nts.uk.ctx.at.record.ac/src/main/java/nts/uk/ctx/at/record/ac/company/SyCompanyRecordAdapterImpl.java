package nts.uk.ctx.at.record.ac.company;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.company.AffComHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.bs.employee.pub.company.AffCompanyHistExport;
import nts.uk.ctx.bs.employee.pub.company.SyCompanyPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class SyCompanyRecordAdapterImpl implements SyCompanyRecordAdapter {

	@Inject
	private SyCompanyPub syCompanyPub;

	@Override
	public List<AffCompanyHistImport> getAffCompanyHistByEmployee(List<String> sids, DatePeriod datePeriod) {
		List<AffCompanyHistImport> importList = this.syCompanyPub.GetAffCompanyHistByEmployee(sids, datePeriod).stream()
				.map(x -> convert(x)).collect(Collectors.toList());
		return importList;
	}

	private AffCompanyHistImport convert(AffCompanyHistExport dataExpprt) {
		List<AffComHistItemImport> subListImport = dataExpprt.getLstAffComHistItem().stream()
				.map(x -> new AffComHistItemImport(x.getHistoryId(), x.isDestinationData(), x.getDatePeriod()))
				.collect(Collectors.toList());
		return new AffCompanyHistImport(dataExpprt.getEmployeeId(), subListImport);
	}

}

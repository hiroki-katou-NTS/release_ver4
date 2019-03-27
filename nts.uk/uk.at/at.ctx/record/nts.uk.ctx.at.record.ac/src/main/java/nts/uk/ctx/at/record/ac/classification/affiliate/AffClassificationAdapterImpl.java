package nts.uk.ctx.at.record.ac.classification.affiliate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationAdapter;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationSidImport;
import nts.uk.ctx.bs.employee.pub.classification.SClsHistExport;
import nts.uk.ctx.bs.employee.pub.classification.SyClassificationPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AffClassificationAdapterImpl implements AffClassificationAdapter {

	@Inject
	private SyClassificationPub syClassificationPub;

	@Override
	public Optional<AffClassificationSidImport> findByEmployeeId(String companyId, String employeeId,
			GeneralDate baseDate) {
		Optional<SClsHistExport> hisExport = this.syClassificationPub.findSClsHistBySid(companyId, employeeId,
				baseDate);
		if(!hisExport.isPresent()){
			return Optional.empty();
		}
		AffClassificationSidImport affClassificationSidImport = new AffClassificationSidImport(
				hisExport.get().getEmployeeId(), hisExport.get().getClassificationCode(), hisExport.get().getPeriod());
		return Optional.of(affClassificationSidImport);
	}

	@Override
	public List<AffClassificationSidImport> finds(String companyId, List<String> employeeId, DatePeriod baseDate) {
		List<SClsHistExport> hisExport = this.syClassificationPub.findSClsHistBySid(companyId, employeeId,
				baseDate);
		return hisExport.stream().map(his -> new AffClassificationSidImport(his.getEmployeeId(), his.getClassificationCode(), his.getPeriod()))
									.collect(Collectors.toList());
	}

}

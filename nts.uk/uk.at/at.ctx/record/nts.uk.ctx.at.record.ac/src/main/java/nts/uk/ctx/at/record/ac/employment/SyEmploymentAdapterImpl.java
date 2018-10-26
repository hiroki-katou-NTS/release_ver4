package nts.uk.ctx.at.record.ac.employment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentAdapter;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentImport;
import nts.uk.ctx.bs.employee.pub.employment.SEmpHistExport;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;

@Stateless
public class SyEmploymentAdapterImpl implements SyEmploymentAdapter {

	@Inject
	private SyEmploymentPub syEmploymentPub;

	@Override
	public Optional<SyEmploymentImport> findByEmployeeId(String companyId, String employeeId, GeneralDate baseDate) {

		Optional<SEmpHistExport> empHist = this.syEmploymentPub.findSEmpHistBySid(companyId, employeeId, baseDate);
		
		if (!empHist.isPresent()) {
			return Optional.empty();
		}

		SyEmploymentImport syEmploymentImport = new SyEmploymentImport(empHist.get().getEmployeeId(),
				empHist.get().getEmploymentCode(), empHist.get().getEmploymentName(), empHist.get().getPeriod());

		return Optional.of(syEmploymentImport);
	}
	
	@Override
	public Map<String, String> getEmploymentMapCodeName(String companyId, List<String> empCodes) {
		return this.syEmploymentPub.getEmploymentMapCodeName(companyId, empCodes);
	}

}

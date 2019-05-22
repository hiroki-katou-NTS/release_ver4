package nts.uk.ctx.at.function.ac.dailyperformanceformat.businesstype;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.dailyperformanceformat.businesstype.BusinessTypeEmpOfHistAdapter;
import nts.uk.ctx.at.function.dom.adapter.dailyperformanceformat.businesstype.BusinessTypeOfEmpHistImport;
import nts.uk.ctx.at.record.pub.dailyperformanceformat.businesstype.BusinessTypeEmpOfHistoryPub;
import nts.uk.ctx.at.record.pub.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistoryEx;

@Stateless
public class BusinessTypeEmpOfHistAcFinder implements BusinessTypeEmpOfHistAdapter {

	@Inject
	private BusinessTypeEmpOfHistoryPub businessTypeEmpOfHistoryPub;
	@Override
	public Optional<BusinessTypeOfEmpHistImport> findByEmployeeDesc(String cid, String sId) {
		Optional<BusinessTypeOfEmployeeHistoryEx> data = businessTypeEmpOfHistoryPub.findByEmployeeDesc(cid, sId);
		if(data.isPresent()) {
			return Optional.of(convertToEx(data.get()));
		}
		return Optional.empty();
	}
	
	private BusinessTypeOfEmpHistImport convertToEx(BusinessTypeOfEmployeeHistoryEx export) {
		return new BusinessTypeOfEmpHistImport(
				export.getCompanyId(),
				export.getHistory(),
				export.getEmployeeId()
				);
	}

}

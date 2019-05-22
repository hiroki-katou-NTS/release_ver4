package nts.uk.ctx.at.record.pubimp.dailyperformanceformat.businesstype;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.pub.dailyperformanceformat.businesstype.BusinessTypeEmpOfHistoryPub;
import nts.uk.ctx.at.record.pub.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistoryEx;
@Stateless
public class BusinessTypeEmpOfHistoryPubImpl implements BusinessTypeEmpOfHistoryPub {

	@Inject
	private BusinessTypeEmpOfHistoryRepository businessTypeEmpOfHistoryRepo;
	@Override
	public Optional<BusinessTypeOfEmployeeHistoryEx> findByEmployeeDesc(String cid, String sId) {
		Optional<BusinessTypeOfEmployeeHistory> data = businessTypeEmpOfHistoryRepo.findByEmployeeDesc(cid, sId);
		if(data.isPresent()) {
			return Optional.of(convertToDomain(data.get()));
		}
		return Optional.empty();
	}

	private BusinessTypeOfEmployeeHistoryEx convertToDomain (BusinessTypeOfEmployeeHistory domain) {
		return new BusinessTypeOfEmployeeHistoryEx(domain.getCompanyId(),domain.getHistory(),domain.getEmployeeId());
	}
}

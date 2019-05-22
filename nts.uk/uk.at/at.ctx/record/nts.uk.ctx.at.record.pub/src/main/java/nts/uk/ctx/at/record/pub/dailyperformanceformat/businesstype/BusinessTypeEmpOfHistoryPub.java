package nts.uk.ctx.at.record.pub.dailyperformanceformat.businesstype;

import java.util.Optional;

public interface BusinessTypeEmpOfHistoryPub {
	Optional<BusinessTypeOfEmployeeHistoryEx> findByEmployeeDesc(String cid, String sId);
}

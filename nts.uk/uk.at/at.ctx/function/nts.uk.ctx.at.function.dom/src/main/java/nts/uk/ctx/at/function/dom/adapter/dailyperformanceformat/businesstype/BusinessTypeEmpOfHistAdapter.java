package nts.uk.ctx.at.function.dom.adapter.dailyperformanceformat.businesstype;

import java.util.Optional;

public interface BusinessTypeEmpOfHistAdapter {
	Optional<BusinessTypeOfEmpHistImport> findByEmployeeDesc(String cid, String sId);
}

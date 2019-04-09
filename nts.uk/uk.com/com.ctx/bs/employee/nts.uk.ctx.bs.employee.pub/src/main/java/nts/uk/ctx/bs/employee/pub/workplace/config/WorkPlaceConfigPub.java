package nts.uk.ctx.bs.employee.pub.workplace.config;

import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface WorkPlaceConfigPub {
	/**
	 * findByBaseDate
	 * 
	 * @param companyId
	 * @param baseDate
	 * @return
	 */
	Optional<WorkPlaceConfigExport> findByBaseDate(String companyId, GeneralDate baseDate);
}

package nts.uk.ctx.at.record.dom.adapter.workplace;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffAtWorkplaceImport;
import nts.uk.ctx.bs.employee.pub.workplace.AffWorkplaceHistoryItemExport;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;

/**
 * 
 * @author sonnh1
 *
 */
public interface SyWorkplaceAdapter {

	Map<String, Pair<String, String>> getWorkplaceMapCodeBaseDateName(String companyId,
			List<String> wpkCodes, List<GeneralDate> baseDates);
	
	Optional<SWkpHistRcImported> findBySid(String employeeId, GeneralDate baseDate);
	
	List<SWkpHistRcImported> findBySid(List<String>employeeIds, GeneralDate baseDate);

	/**
	 * [No.597]職場の所属社員を取得する
	 */
	List<EmployeeInfoImported> getLstEmpByWorkplaceIdsAndPeriod(List<String> workplaceIds, DatePeriod period);

	/**
	 * 期間から職場情報を取得
	 * @param companyId
	 * @param datePeriod
	 * @return
	 */
	List<WorkplaceInformationImport> getByCidAndPeriod(String companyId, DatePeriod datePeriod);
	
	/**
	 * @name 所属職場履歴Adapter
	 * @param employeeID
	 * @param date
	 * @return
	 */
	AffAtWorkplaceImport getAffWkpHistItemByEmpDate(String employeeID, GeneralDate date);
}

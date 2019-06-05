package nts.uk.ctx.bs.employee.dom.workplace.export;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;

/**
 * 
 * @author sonnh1
 *
 */
public interface WorkplaceExport {
	/**
	 * [No.560]職場IDから職場の情報をすべて取得する
	 * 
	 * 
	 * RequestList 560
	 * 
	 * @param companyId
	 * @param listWkpId
	 * @param baseDate
	 */
	public List<WorkplaceExportDto> getAllWkpConfig(String companyId, List<String> listWkpId, GeneralDate baseDate);

	/**
	 * [No.561]過去の職場の情報を取得する
	 * 
	 * RequestList 561
	 * 
	 * @param companyId
	 * @param listWkpId
	 * @param histId
	 */
	public List<WorkplaceExportDto> getPastWkpInfo(String companyId, List<String> listWkpId, String histId);
}

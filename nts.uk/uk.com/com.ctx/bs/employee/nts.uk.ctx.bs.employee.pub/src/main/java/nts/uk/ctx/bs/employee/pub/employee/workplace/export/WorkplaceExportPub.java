package nts.uk.ctx.bs.employee.pub.employee.workplace.export;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * 
 * @author sonnh1
 *
 */
public interface WorkplaceExportPub {
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
	public List<WorkplaceExportPubDto> getAllWkpConfig(String companyId, List<String> listWkpId, GeneralDate baseDate);

	/**
	 * [No.561]過去の職場の情報を取得する
	 * 
	 * RequestList 561
	 * 
	 * @param companyId
	 * @param listWkpId
	 * @param histId
	 */
	public List<WorkplaceExportPubDto> getPastWkpInfo(String companyId, List<String> listWkpId, String histId);
}

package nts.uk.ctx.bs.employee.dom.workplace.export;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * 
 * @author sonnh1
 *
 */
public interface WorkplaceExport {
	/**
	 * 職場IDから職場の階層コードを取得する
	 * 
	 * tuong tu RQ560
	 * 
	 * @param companyId
	 * @param listWkpId
	 * @param baseDate
	 */
	public List<WkpInfoDto> getAllWkpConfig(String companyId, List<String> listWkpId, GeneralDate baseDate);

	/**
	 * 過去の職場の階層コードを取得する
	 * 
	 * tuong tu RQ561
	 * 
	 * @param companyId
	 * @param listWkpId
	 * @param histId
	 */
	public List<WkpInfoDto> getPastWkpInfo(String companyId, List<String> listWkpId, String histId);
}

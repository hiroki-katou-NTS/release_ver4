/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pub.workplace;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * The Interface SyWorkplacePub.
 */
public interface SyWorkplacePub {

	/**
	 * Find by sid.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	// RequestList30
	Optional<SWkpHistExport> findBySid(String employeeId, GeneralDate baseDate);
	
	/**
	 * fix respon kal001
	 * @param sids
	 * @return
	 */
	List<SWkpHistExport> findBySId(List<String> sids );

	/**
	 * find by sids and base date
	 * @param sids
	 * @param baseDate
	 * @return
	 */
	List<SWkpHistExport> findBySId(List<String> sids, GeneralDate baseDate);

	/**
	 * Find wpk ids by wkp code.
	 *
	 * @param companyId the company id
	 * @param wpkCode the wpk code
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList41
	List<String> findWpkIdsByWkpCode(String companyId, String wpkCode, GeneralDate baseDate);

	/**
	 * Find wpk ids by sid.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @param date the date
	 * @return the list
	 */
	// RequestList65
	List<String> findWpkIdsBySid(String companyId, String employeeId, GeneralDate date);
	
	Map<GeneralDate, Map<String, List<String>>> findWpkIdsBySids(String companyId, List<String> employeeId, DatePeriod date);

	/**
	 * Find by wkp id.
	 *
	 * @param workplaceId the workplace id
	 * @param baseDate the base date
	 * @return the optional
	 */
	// RequestList66
	Optional<WkpCdNameExport> findByWkpId(String workplaceId, GeneralDate baseDate);
	
	/**
	 * Find parent wpk ids by wkp id.
	 *
	 * @param companyId the company id
	 * @param workplaceId the workplace id
	 * @param date the date
	 * @return the list
	 */
	// RequestList83
	// ??????ID?????????????????????????????????????????????
	List<String> findParentWpkIdsByWkpId(String companyId, String workplaceId, GeneralDate date);
	
	// RequestList83-3
	// ??????ID?????????????????????????????????????????????
	List<String> findParentWpkIdsByWkpIdDesc(String companyId, String workplaceId, GeneralDate date);

	/**
	 * Gets the workplace id.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the workplace id
	 */
	String getWorkplaceId(String companyId, String employeeId, GeneralDate baseDate);
		
	/**
	 * Find list workplace id by base date.
	 *
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList157
	List<String> findListWorkplaceIdByBaseDate(GeneralDate baseDate);
	
	/**
	 * Find list workplace id by cid and wkp id and base date.
	 *
	 * @param companyId the company id
	 * @param workplaceId the workplace id
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList154
	List<String> findListWorkplaceIdByCidAndWkpIdAndBaseDate(String companyId, String workplaceId, GeneralDate baseDate);
	
	/**
	 * Find list S id by cid and wkp id and period.
	 *
	 * @param workplaceId the workplace id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return the list
	 */
	// RequestList120
	List<AffWorkplaceExport> findListSIdByCidAndWkpIdAndPeriod(String workplaceId, GeneralDate startDate,GeneralDate endDate);
	
	/**
	 * Gets the wpl by list sid and period.
	 * RequestList189
	 * @param sid the sid
	 * @param datePeriod the date period
	 * @return the list
	 */
	List<WorkPlaceHistExport> GetWplByListSidAndPeriod(List<String> sid, DatePeriod datePeriod);
	
	/**
	 * Find by wkp ids at time.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @param wkpIds the wkp ids
	 * @return the list
	 */
	// RequestList312
	List<WkpConfigAtTimeExport> findByWkpIdsAtTime(String companyId, GeneralDate baseDate, List<String> wkpIds);
	
	/**
	 * Find by S id and base date.
	 *
	 * @param sids the sids
	 * @param baseDate the base date
	 * @return the list
	 */
	// RequestList 227
	List<AffAtWorkplaceExport> findBySIdAndBaseDate(List<String> sids, GeneralDate baseDate);
	
	List<AffAtWorkplaceExport> findBySIdAndBaseDateV2(List<String> sids, GeneralDate baseDate);
	
	
	
	/**
	 * Find wkp by wkp id.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @param wkpIds the wkp ids
	 * @return the list
	 */
	// RequestList324
	List<WorkPlaceInfoExport> findWkpByWkpId(String companyId, GeneralDate baseDate, List<String> wkpIds);
	
	/**
	 * Gets the wkp cd name.
	 *
	 * @param companyId the company id
	 * @param baseDate the base date
	 * @param wkpIds the wkp ids
	 * @return the wkp cd name
	 */
	// RequestList164
	List<WkpIdNameHierarchyCdExport> getWkpIdNameHierarchyCd(String companyId, GeneralDate baseDate, List<String> wkpIds);
	
	// RequestList120 version2
	List<AffWorkplaceExport> getByLstWkpIdAndPeriod(List<String> lstWkpId, GeneralDate startDate,GeneralDate endDate);
	
	/**
	 * ?????????????????????????????????????????????
	 * RequestList #168
	 * @param employeeID ??????ID
	 * @param startDate ?????????
	 * @param endDate ?????????
	 * @return
	 */
	WkpByEmpExport getLstHistByEmpAndPeriod(String employeeID, GeneralDate startDate, GeneralDate endDate);
	
	/**
	 * Gets the lst hist by emps and period.
	 *
	 * @param sIds the s ids
	 * @param period the period
	 * @return the lst hist by emps and period
	 */
	// RequestList422
	// ??????ID???List?????????????????????????????????????????????
	List<WkpHistWithPeriodExport> getLstHistByWkpsAndPeriod(List<String> wkpIds, DatePeriod period);
	
	
	/**
	 * Gets the lst period.
	 *
	 * @param companyId the company id
	 * @param period the period
	 * @return the lst period
	 */
	//RequestList485
	//??????ID??????????????????????????????????????????????????????
	List<DatePeriod> getLstPeriod(String companyId, DatePeriod period);
	
	/**
	 * Gets the workplace map code base date name.
	 *
	 * @param companyId the company id
	 * @param wpkCodes the wpk codes
	 * @param baseDates the base dates
	 * @return the workplace map code base date name
	 */
	Map<Pair<String, GeneralDate>, Pair<String,String>> getWorkplaceMapCodeBaseDateName(String companyId,
			List<String> wpkCodes, List<GeneralDate> baseDates);
	
	//RequestList590
	//???????????????????????????????????????????????????????????????
	// AffWorkplaceHistoryExport
	List<AffWorkplaceHistoryExport> getWorkplaceBySidsAndBaseDate(List<String> sids, GeneralDate baseDate);
	
	/**
	 * Request 597 ????????????????????????????????????
	 * @param sid
	 * @param period
	 * @return
	 */
	List<ResultRequest597Export> getLstEmpByWorkplaceIdsAndPeriod(List<String> workplaceIds, DatePeriod period);
	
	/**
	 * Request 598  ????????????????????????????????????????????????
	 * @param sid
	 * @param period
	 * @return
	 */
	List<String> getLstWorkplaceIdBySidAndPeriod(String sid, DatePeriod period);
	// RequestList324 ver2 CCG007??????????????????
	List<WorkPlaceInfoExport> findWkpByWkpIdRQ324Ver2(String companyId, GeneralDate baseDate, List<String> wkpIds);
	
	//RequestList30 NEW
	Optional<SWkpHistExport> findBySidNew(String companyId, String employeeId, GeneralDate baseDate);
	
	Optional<SWkpHistExport> findByWkpIdNEW(String companyId, String wkpId, GeneralDate baseDate);
}

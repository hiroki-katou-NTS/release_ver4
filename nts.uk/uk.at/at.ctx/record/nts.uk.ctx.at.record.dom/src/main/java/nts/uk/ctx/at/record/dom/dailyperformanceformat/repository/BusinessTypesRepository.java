package nts.uk.ctx.at.record.dom.dailyperformanceformat.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessType;

public interface BusinessTypesRepository {
	
	List<BusinessType> findAll(String companyId);
	/**
	 * author: HoangYen
	 * @param workTypeName
	 */
	void updateBusinessTypeName(BusinessType businessType);  
	/**
	 * author: HoangYen
	 * @param workTypeCode
	 * @param workTypeName
	 */
	void insertBusinessType(BusinessType businessType);
	/**
	 * author: HoangYen
	 * @param companyId
	 * @param businessTypeCode
	 * @return
	 */
	Optional<BusinessType> findBusinessType(String companyId, String businessTypeCode);
	/**
	 * author: HoangYen
	 * @param companyId
	 * @param businessTypeCode
	 */
	void deleteBusinessType(String companyId, String businessTypeCode);
}

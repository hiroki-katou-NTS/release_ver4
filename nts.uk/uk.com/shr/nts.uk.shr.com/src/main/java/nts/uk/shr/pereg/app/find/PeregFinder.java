package nts.uk.shr.pereg.app.find;

import java.util.List;

import nts.uk.shr.pereg.app.ComboBoxObject;
import nts.uk.shr.pereg.app.find.dto.DataClassification;
import nts.uk.shr.pereg.app.find.dto.GridPeregDto;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

public interface PeregFinder<T> {
	/**
	 * Returns ID of category that this handler can handle
	 * 
	 * @return category ID
	 */
	String targetCategoryCode();

	/**
	 * Returns class of command that is handled by this handler
	 * 
	 * @return class of command
	 */
	Class<T> dtoClass();

	/**
	 * PERSON - EMPLOYEE
	 * @return
	 */
	DataClassification dataType();

	PeregDomainDto getSingleData(PeregQuery query);
	
	List<PeregDomainDto> getListData(PeregQuery query);
	
	List<ComboBoxObject> getListFirstItems(PeregQuery query);
	/**
	 * lấy danh sách theo sids
	 * @param query
	 * @return
	 */
	List<GridPeregDto> getAllData(PeregQueryByListEmp query);

	default PeregDomainDto findSingle(PeregQuery query) {
		return this.getSingleData(query);
	}

	default List<PeregDomainDto> findList(PeregQuery query) {
		return this.getListData(query);
	}
	
	default List<ComboBoxObject> findListFirstItems(PeregQuery query) {
		return this.getListFirstItems(query);
	}
	
	default List<GridPeregDto> findAllData(PeregQueryByListEmp query) {
		return this.getAllData(query);
	}
}

package nts.uk.ctx.pereg.dom.person.additemdata.item;

import java.util.List;
import java.util.Optional;

/**
 * @author sonnlb
 *
 */
public interface EmpInfoItemDataRepository {

	List<EmpInfoItemData> getAllInfoItem(String categoryCd, String companyId, String employeeId);
	
	List<EmpInfoItemData> getAllInfoItemBySidCtgId(String ctgId, String employeeId);

	List<EmpInfoItemData> getAllInfoItemByRecordId(String recordId);
	
	Optional<EmpInfoItemData> getInfoItemByItemDefIdAndRecordId(String itemDefId, String recordId);
	
	List<EmpInfoItemData> getItemsData(String itemDefId, List<String> recordIds);

	void addItemData(EmpInfoItemData infoItemData);
	
	void registerEmpInfoItemData(EmpInfoItemData domain);
	
	void updateEmpInfoItemData(EmpInfoItemData domain);
	
	void deleteEmployInfoItemData(String recordID);
	
	boolean hasItemData(String itemCd, List<String> perInfoCtgId);
	
	/**
	 * getAllInfoItemByRecordId
	 * @author lanlt
	 * @param recordIds
	 * @return
	 */
	List<EmpInfoItemData> getAllInfoItemByRecordId(List<String>  recordIds);
	
	/**
	 * addAll EmpInfoItemData
	 * @author lanlt
	 * @param domains
	 */
	void addAll(List<EmpInfoItemData> domains);
	
	/**
	 * updateAll EmpInfoItemData
	 * @author lanlt
	 * @param domains
	 */
	void updateAll(List<EmpInfoItemData> domains);
	
}

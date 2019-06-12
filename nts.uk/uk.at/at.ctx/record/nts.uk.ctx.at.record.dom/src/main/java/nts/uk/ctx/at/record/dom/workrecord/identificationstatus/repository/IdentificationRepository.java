package nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;

public interface IdentificationRepository {
	
	List<Identification> findByEmployeeID(String employeeID,GeneralDate startDate,GeneralDate endDate);
	
	List<Identification> findByListEmployeeID(List<String> employeeIDs,GeneralDate startDate,GeneralDate endDate);
	
	List<Identification> findByListEmpDate(List<String> employeeIDs,GeneralDate dateRefer);
	
	List<Identification> findByEmployeeID(String employeeID, List<GeneralDate> dates);
	
	Optional<Identification> findByCode(String employeeID,GeneralDate processingYmd);
	
	void insert(Identification identification);
	
	void remove(String companyId, String employeeId, GeneralDate processingYmd);
	
	List<Identification> findByEmployeeIDSortDate(String employeeID,GeneralDate startDate,GeneralDate endDate);

	void removeByEmployeeIdAndDate(String employeeId, GeneralDate processingYmd);
	
	void removeByEmpListDate(String employeeId, List<GeneralDate> lstProcessingYmd);


}

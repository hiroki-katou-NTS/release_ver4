package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;
import java.util.Optional;

public interface TargetPersonRepository {
	/**
	 * get All target person
	 * @param employeeID
	 * @return
	 */
	List<TargetPerson> getAllTargetPerson(String employeeID);
	/**
	 * get target person by ID
	 * @param employeeID
	 * @param empCalAndSumExecLogId
	 * @return
	 */
	Optional<TargetPerson> getTargetPersonByID(String employeeID, String empCalAndSumExecLogId);
	
	/**
	 * get All target person
	 * @param empCalAndSumExecLogId
	 * @return
	 */
	List<TargetPerson> getTargetPersonById(String empCalAndSumExecLogId);

//	Optional<TargetPerson> getTargetPersonByID(String employeeID,String empCalAndSumExecLogId);
	
	List<TargetPerson> getByempCalAndSumExecLogID(String empCalAndSumExecLogID);
	
	void add(TargetPerson lstTargetPerson);
	
	void addAll(List<TargetPerson> lstTargetPerson);
	
	void update(String employeeID, String empCalAndSumExecLogId, int state);
	
}

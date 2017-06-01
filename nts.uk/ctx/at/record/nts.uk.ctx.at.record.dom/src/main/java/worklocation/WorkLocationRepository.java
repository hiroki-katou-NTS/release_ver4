package worklocation;

import java.util.List;
import java.util.Optional;
/**
 * 
 * @author hieult
 *
 */
public interface WorkLocationRepository {
	
	/**
	 * Find All
	 * @param companyID
	 * @return List Work Place
	 */
	List<WorkLocation> findAll (String companyID);
	/**
	 * 
	 * @param companyID
	 * @param workLocationCD
	 * @return Optional Work Place
	 */
	Optional<WorkLocation> findByCode (String companyID, String workLocationCD); 
}

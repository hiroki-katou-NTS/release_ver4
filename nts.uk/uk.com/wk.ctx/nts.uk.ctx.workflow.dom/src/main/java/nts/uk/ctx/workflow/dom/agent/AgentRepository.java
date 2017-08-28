package nts.uk.ctx.workflow.dom.agent;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface AgentRepository {
	/**
	 * find all Agent
	 * 
	 * @param companyCode
	 * @param employeeId
	 * @return
	 */
	List<Agent> findAllAgent(String companyId, String employeeId);
	
	/**
	 * get agent by agentSid
	 * @param companyId
	 * @param agentSid
	 * @return
	 */
	List<Agent> findByAgentSid(String companyId, String agentSid);
	
	
	/**
	 * add Agent
	 * 
	 * @param agent
	 */
	void add(Agent agent);
	

	/**
	 * update Agent
	 * 
	 * @param agent
	 */
	void update(Agent agent);
	

	/**
	 * delete Agent
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param requestId
	 */
	void delete(String companyId, String employeeId, String requestId);
	
	
	/**
	 * get agent by requestId
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param requestId
	 * @return
	 */
	Optional<Agent> find(String companyId, String employeeId, String requestId);
	
	

	/**
	 * get agent by Date
	 * @param companyId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Agent> findAll(String companyId, GeneralDate startDate, GeneralDate endDate);
	

	/**
	 * get agent by companyId
	 * @param companyId
	 * @return
	 */
	List<Agent> findByCid(String companyId);
	
	/**
	 * Find all gent by condition
	 * @param companyId company id
	 * @param employeeId employee id
	 * @param startDate start date
	 * @param endDate end date
	 * @return list of agent
	 */
	List<Agent> find(String companyId, String employeeId, GeneralDate startDate, GeneralDate endDate);
}

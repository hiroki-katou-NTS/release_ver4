package nts.uk.ctx.workflow.dom.agent;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.agent.output.AgentInfoOutput;

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
	 * Find agents by list employee
	 * @param companyId company id
	 * @param employeeId employee id
	 * @param startDate start date
	 * @param endDate end date
	 * @return list of agent
	 */
	List<Agent> find(String companyId, List<String> employeeIds, GeneralDate baseDate);
	/**
	 * 代行者、期間から承認代行情報を取得する
	 * @param companyId
	 * @param employee
	 * @param baseDate
	 * @return
	 */
	public List<Agent> findBySidDate(String companyId, String employeeId, GeneralDate startDate, GeneralDate endDate);
	/**
	 * パラメータの承認者が代行者に依頼されているか、パラメータの期間に依頼期間が１日でも重なるものを取得する
	 * @param companyId
	 * @param approverID
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Agent> findByApproverAndDate(String companyId, String approverID, GeneralDate startDate, GeneralDate endDate);
	
	public List<AgentInfoOutput> findAgentByPeriod(String companyID, List<String> listApprover, 
			GeneralDate startDate, GeneralDate endDate, Integer agentType);
	
	/**
	 * get agent by agentSid
	 * @param companyId
	 * @param agentSid
	 * @return
	 */
	List<Agent> getAgentBySidDate(String companyId, String employeeId, GeneralDate startDate, GeneralDate endDate);
	
	// only for SPR
	List<Agent> findAgentForSpr(String companyId, String approverID, GeneralDate startDate, GeneralDate endDate);
	//hoatt
	List<Agent> getListByAgentType1(String companyId, String agentId);
	//hoatt
	List<Agent> getListAgentBySidReqId(String companyId, String employeeId, String requestId);
}

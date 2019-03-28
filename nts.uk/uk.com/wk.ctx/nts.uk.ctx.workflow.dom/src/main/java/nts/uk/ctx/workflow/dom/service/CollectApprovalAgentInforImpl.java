package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.agent.Agent;
import nts.uk.ctx.workflow.dom.agent.AgentAppType;
import nts.uk.ctx.workflow.dom.agent.AgentRepository;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterInforOutput;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterOutput;
import nts.uk.ctx.workflow.dom.service.output.RepresenterInforOutput;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class CollectApprovalAgentInforImpl implements CollectApprovalAgentInforService {
	
	@Inject
	private AgentRepository agentRepository;

	@Override
	public ApprovalRepresenterOutput getApprovalAgentInfor(String companyID, List<String> listApprover) {
		List<ApprovalRepresenterInforOutput> listApprovalAgentInfor = new ArrayList<>();
		List<String> listAgent = new ArrayList<>();
		Boolean allPathSetFlag = true;
		if(CollectionUtil.isEmpty(listApprover)){
			return new ApprovalRepresenterOutput(listApprovalAgentInfor, listAgent, allPathSetFlag);
		}
		List<Agent> listAgentByDate = agentRepository.find(companyID, listApprover, GeneralDate.today());
		
		if(CollectionUtil.isEmpty(listAgentByDate)){
			listApprover.stream().forEach(approver -> {
				ApprovalRepresenterInforOutput obj = new ApprovalRepresenterInforOutput(approver, RepresenterInforOutput.noneInformation());
				listApprovalAgentInfor.add(obj);
			});
			allPathSetFlag = false;
			return new ApprovalRepresenterOutput(listApprovalAgentInfor, listAgent, allPathSetFlag);
		}
		for(String approver : listApprover){
			for(Agent agent : listAgentByDate){
				if (approver.equals(agent.getEmployeeId())) {
					// ktra xem AgentAppType = PATH hay k
					//if(agentAdapterDto.getAgentAppType1() != null
					if (agent.getAgentAppType1().equals(AgentAppType.PATH)) {
						ApprovalRepresenterInforOutput obj = new ApprovalRepresenterInforOutput(approver, RepresenterInforOutput.pathInformation());
						listApprovalAgentInfor.add(obj);
						continue;
					}
					
					if (Strings.isBlank(agent.getAgentSid1()) || agent.getAgentAppType1().equals(AgentAppType.NO_SETTINGS)) {
						ApprovalRepresenterInforOutput obj = new ApprovalRepresenterInforOutput(approver, RepresenterInforOutput.noneInformation());
						listApprovalAgentInfor.add(obj);
						allPathSetFlag = false;
						continue;
					}
					
					// ktra xem AgentAppType = SUBSTITUTE_DESIGNATION hay k
					//if(agentAdapterDto.getAgentAppType1() != null
					if (Strings.isNotBlank(agent.getAgentSid1()) && agent.getAgentAppType1().equals(AgentAppType.SUBSTITUTE_DESIGNATION)) {
						ApprovalRepresenterInforOutput obj = new ApprovalRepresenterInforOutput(approver,
								RepresenterInforOutput.representerInformation(agent.getAgentSid1()));
						listApprovalAgentInfor.add(obj);
						// add data in list representerSID
						listAgent.add(agent.getAgentSid1());
						allPathSetFlag = false;
						continue;
					}
				} else {
					ApprovalRepresenterInforOutput obj = new ApprovalRepresenterInforOutput(approver, RepresenterInforOutput.noneInformation());
					listApprovalAgentInfor.add(obj);
					allPathSetFlag = false;
					continue;
				}
			}
		}
		
		return new ApprovalRepresenterOutput(listApprovalAgentInfor, listAgent, allPathSetFlag);
	}

}

package nts.uk.ctx.workflow.app.command.agent;

import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.workflow.dom.agent.Agent;
import nts.uk.ctx.workflow.dom.agent.AgentAppType;
import nts.uk.ctx.workflow.dom.agent.AgentApprovalService;
import nts.uk.ctx.workflow.dom.agent.AgentRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author phongtq
 *
 */
@Stateless
@Transactional
public class UpdateAgentCommandHandler extends CommandHandler<AgentCommandBase> {
	@Inject
	private AgentRepository agentRepository;
	@Inject
	private AgentApprovalService agentApprSv;
	/**
	 * 8.代行者の登録_変更登録処理
	 */
	@Override
	protected void handle(CommandHandlerContext<AgentCommandBase> context) {
		
		AgentCommandBase agentCommandBase = context.getCommand();
		String employeeId = agentCommandBase.getEmployeeId();
		String companyId = AppContexts.user().companyId();
		String requestId = agentCommandBase.getRequestId();			
		Optional<Agent> upAgent = agentRepository.find(companyId, employeeId, requestId);
		if(!upAgent.isPresent()){
			throw new BusinessException("");
		}
		
		Agent agentInfor = new Agent(
				companyId, 
				employeeId,
				UUID.fromString(requestId),
				agentCommandBase.getStartDate(),
				agentCommandBase.getEndDate(), 
				(agentCommandBase.getAgentSid1()),
				EnumAdaptor.valueOf(agentCommandBase.getAgentAppType1(), AgentAppType.class),
				(agentCommandBase.getAgentSid2()),
				EnumAdaptor.valueOf(agentCommandBase.getAgentAppType2(), AgentAppType.class),
				(agentCommandBase.getAgentSid3()),
				EnumAdaptor.valueOf(agentCommandBase.getAgentAppType3(), AgentAppType.class),
				(agentCommandBase.getAgentSid4()),
				EnumAdaptor.valueOf(agentCommandBase.getAgentAppType4(), AgentAppType.class));
		//就業、人事、給与、経理の代行承認種類が「設定しない（待ってもらう）」かチェックする
		agentInfor.checkSameAgentRequest();
		agentInfor.checkAgentRequest();
		//期間のチェック
		agentApprSv.checkPeriodRegAgent(agentInfor, false);
		
		//update agent
		agentRepository.update(agentInfor);
	}
}
